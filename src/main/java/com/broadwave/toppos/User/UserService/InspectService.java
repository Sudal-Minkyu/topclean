package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Addprocess.AddprocessRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.*;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final HeadService headService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final AddprocessRepository addprocessRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    @Autowired
    public InspectService(ModelMapper modelMapper, TokenProvider tokenProvider, AccountService accountService, HeadService headService, UserService userService,
                          RequestDetailRepositoryCustom requestDetailRepositoryCustom, PasswordEncoder passwordEncoder, AddprocessRepository addprocessRepository,
                          RequestDetailRepository requestDetailRepository){
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.accountService = accountService;
        this.headService = headService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.addprocessRepository = addprocessRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    //  통합조회용 - 접수세부 테이블
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailSearch(Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt, HttpServletRequest request) {

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
        HashMap<String, Object> data = new HashMap<>();

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


























}
