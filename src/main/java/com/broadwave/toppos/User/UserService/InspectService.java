package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.Payment;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentCencelDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.*;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoney;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2021-01-05
 * Time :
 * Remark : Toppos 가맹점 통합조회 서비스
 */
@Slf4j
@Service
public class InspectService {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryCustom paymentRepositoryCustom;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    private final SaveMoneyRepository saveMoneyRepository;

    @Autowired
    public InspectService(TokenProvider tokenProvider,UserService userService, PaymentRepository paymentRepository, PaymentRepositoryCustom paymentRepositoryCustom,
                          RequestDetailRepositoryCustom requestDetailRepositoryCustom, RequestDetailRepository requestDetailRepository, SaveMoneyRepository saveMoneyRepository){
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.requestDetailRepository = requestDetailRepository;
        this.paymentRepository = paymentRepository;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
        this.paymentRepositoryCustom = paymentRepositoryCustom;
        this.saveMoneyRepository = saveMoneyRepository;
    }

    //  통합조회용 - 접수세부 테이블
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailSearch(Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("franchiseRequestDetailSearch 호출");
//        log.info("고객ID bcId : "+bcId);
//        log.info("택번호 searchTag : "+searchTag);
//        log.info("조회타입 filterCondition : "+filterCondition);
//        log.info("시작 접수일자 filterFromDt : "+filterFromDt);
//        log.info("종료 접수일자 filterToDt : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<RequestDetailSearchDto> requestDetailSearchDtoList = requestDetailSearch(frCode, bcId, searchTag, filterCondition, filterFromDt, filterToDt); //  통합조회용 - 접수세부 호출

        data.put("gridListData",requestDetailSearchDtoList);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
    //  통합조회용 - 접수세부 호출용 함수
    private List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt) {
        return requestDetailRepositoryCustom.requestDetailSearch(frCode, bcId, searchTag, filterCondition, filterFromDt, filterToDt);
    }

    //  통합조회용 - 접수 세부테이블 수정
    @Transactional
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailUpdate(RequestDetailUpdateDto requestDetailUpdateDto, HttpServletRequest request) {
        log.info("franchiseRequestDetailUpdate 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(requestDetailUpdateDto.getFdId());
        if(!optionalRequestDetail.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 "+ ResponseErrorCode.TP022.getDesc(), null,null));
        }else{
            Integer nowFdTotAmt = optionalRequestDetail.get().getFdTotAmt();
            Integer updateFdTotAmt = requestDetailUpdateDto.getFdTotAmt();

            // RequestDetail 수정 시작
            optionalRequestDetail.get().setFdTotAmt(updateFdTotAmt);

            optionalRequestDetail.get().setFdColor(requestDetailUpdateDto.getFdColor());
            optionalRequestDetail.get().setFdPattern(requestDetailUpdateDto.getFdPattern());
            optionalRequestDetail.get().setFdPriceGrade(requestDetailUpdateDto.getFdPriceGrade());
            optionalRequestDetail.get().setFdOriginAmt(requestDetailUpdateDto.getFdOriginAmt());
            optionalRequestDetail.get().setFdNormalAmt(requestDetailUpdateDto.getFdNormalAmt());

            optionalRequestDetail.get().setFdAdd2Amt(requestDetailUpdateDto.getFdAdd2Amt());
            optionalRequestDetail.get().setFdAdd2Remark(requestDetailUpdateDto.getFdAdd2Remark());
            optionalRequestDetail.get().setFdRepairRemark(requestDetailUpdateDto.getFdRepairRemark());
            optionalRequestDetail.get().setFdRepairAmt(requestDetailUpdateDto.getFdRepairAmt());
            optionalRequestDetail.get().setFdSpecialYn(requestDetailUpdateDto.getFdSpecialYn());

            optionalRequestDetail.get().setFdAdd1Amt(requestDetailUpdateDto.getFdAdd1Amt());
            optionalRequestDetail.get().setFdAdd1Remark(requestDetailUpdateDto.getFdAdd1Remark());
            optionalRequestDetail.get().setFdPressed(requestDetailUpdateDto.getFdPressed());
            optionalRequestDetail.get().setFdWhitening(requestDetailUpdateDto.getFdWhitening());
            optionalRequestDetail.get().setFdPollution(requestDetailUpdateDto.getFdPollution());

            optionalRequestDetail.get().setFdPollutionLevel(requestDetailUpdateDto.getFdPollutionLevel());
            optionalRequestDetail.get().setFdStarch(requestDetailUpdateDto.getFdStarch());
            optionalRequestDetail.get().setFdWaterRepellent(requestDetailUpdateDto.getFdWaterRepellent());
            optionalRequestDetail.get().setFdDiscountGrade(requestDetailUpdateDto.getFdDiscountGrade());
            optionalRequestDetail.get().setFdDiscountAmt(requestDetailUpdateDto.getFdDiscountAmt());

            optionalRequestDetail.get().setFdQty(requestDetailUpdateDto.getFdQty());
            optionalRequestDetail.get().setFdRequestAmt(requestDetailUpdateDto.getFdRequestAmt());
            optionalRequestDetail.get().setFdRetryYn(requestDetailUpdateDto.getFdRetryYn());
            optionalRequestDetail.get().setFdUrgentYn(requestDetailUpdateDto.getFdUrgentYn());
            optionalRequestDetail.get().setFdRemark(requestDetailUpdateDto.getFdRemark());

            optionalRequestDetail.get().setModity_id(login_id);
            optionalRequestDetail.get().setModity_date(LocalDateTime.now());
            RequestDetail requestDetailSave = optionalRequestDetail.get();
            log.info("requestDetailSave : "+requestDetailSave);

            if(nowFdTotAmt>updateFdTotAmt){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP023.getCode(), ResponseErrorCode.TP023.getDesc(), "문자","수정 금액(전/후) : "+nowFdTotAmt+" / "+updateFdTotAmt));
            }else if(nowFdTotAmt.equals(updateFdTotAmt)){
                // 업데이트만 실행
                requestDetailRepository.save(requestDetailSave);
            }else{
                // 업데이트 실행 후 마스터테이블 수정 함수호출
                requestDetailRepository.save(requestDetailSave);

                // 만약 금액이 변동되었을 시 수정후, 마스터테이블도 업데이트하기 (가격이 높아졌을때만 시행, 작아지면 리턴처리)
                userService.requestDetailUpdateFromMasterUpdate(requestDetailUpdateDto.getFrNo(), frCode);
            }

        }

        return ResponseEntity.ok(res.success());

    }

    //  통합조회용 - 결제취소 전 결제내역리스트 요청
    public ResponseEntity<Map<String, Object>> franchiseDetailCencelDataList(Long frId, HttpServletRequest request) {
        log.info("franchiseDetailCencelDataList 호출");

//        log.info("접수마스터 ID frId : "+frId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<PaymentCencelDto> paymentCencelDtoList = paymentRepositoryCustom.findByRequestDetailCencelDataList(frCode, frId);
//        log.info("paymentCencelDtoList : "+paymentCencelDtoList);
        data.put("gridListData",paymentCencelDtoList);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  통합조회용 - 결제취소 요청
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailCencel(Long fpId, String type, HttpServletRequest request) {
        log.info("franchiseRequestDetailCencel 호출");

        log.info("결제 ID fpId : "+fpId);
        log.info("적립급/취소 타입 : "+type);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        if(type.equals("1")){
            log.info("결제 취소합니다.");
            return ResponseEntity.ok(res.fail("문자 ", "결제취소는 작업중입니다.", null, null));
        }else{
            log.info("적립금으로 전환합니다.");
            Optional<Payment> optionalPayment = paymentRepository.findById(fpId);
            if(!optionalPayment.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "적립금 전환 "+ResponseErrorCode.TP022.getDesc(), null, null));
            }else{
                optionalPayment.get().setFpCancelYn("Y");
                optionalPayment.get().setFpSavedMoneyYn("Y");
                paymentRepository.save(optionalPayment.get());

                SaveMoney saveMoney = new SaveMoney();
                saveMoney.setBcId(optionalPayment.get().getBcId());
                saveMoney.setFpId(optionalPayment.get());
                saveMoney.setFsType("1");
                saveMoney.setFsClose("N");
                saveMoney.setFsAmt(optionalPayment.get().getFpAmt());
                saveMoney.setInsert_id(login_id);
                saveMoney.setInsert_date(LocalDateTime.now());
                saveMoneyRepository.save(saveMoney);
            }
        }

        return ResponseEntity.ok(res.success());
    }








}
