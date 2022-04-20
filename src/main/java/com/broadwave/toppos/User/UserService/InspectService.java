package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.Payment;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentCencelDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.Inspeot;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotInfoDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMainListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMapperDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory.MessageHistory;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory.MessageHistoryRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.Photo;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.PhotoDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.PhotoRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailBranchInspectListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailBranchInspectionCurrentListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailBranchInspeotDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailInspectDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailSearchDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailUpdateDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestFdTagDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoney;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-01-05
 * Time :
 * Remark : Toppos 가맹점 통합조회 서비스
 */
@Slf4j
@Service
public class InspectService {

    @Value("${toppos.templatecode.number}")
    private String templatecodeNumber;

    @Value("${toppos.templatecode.check}")
    private String templatecodeCheck;

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    private final AWSS3Service awss3Service;
    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryCustom paymentRepositoryCustom;
    private final PhotoRepository photoRepository;
    private final InspeotRepository inspeotRepository;
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final SaveMoneyRepository saveMoneyRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public InspectService(ModelMapper modelMapper, TokenProvider tokenProvider, UserService userService, AWSS3Service awss3Service, PhotoRepository photoRepository,
                          PaymentRepository paymentRepository, PaymentRepositoryCustom paymentRepositoryCustom, InspeotRepository inspeotRepository,
                          RequestRepository requestRepository, InspeotRepositoryCustom inspeotRepositoryCustom,
                          RequestDetailRepository requestDetailRepository, SaveMoneyRepository saveMoneyRepository, MessageHistoryRepository messageHistoryRepository,
                          CustomerRepository customerRepository){
        this.modelMapper = modelMapper;
        this.inspeotRepository = inspeotRepository;
        this.awss3Service = awss3Service;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.photoRepository = photoRepository;
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.paymentRepository = paymentRepository;
        this.paymentRepositoryCustom = paymentRepositoryCustom;
        this.saveMoneyRepository = saveMoneyRepository;
        this.messageHistoryRepository = messageHistoryRepository;
        this.customerRepository = customerRepository;
    }

    //  통합조회용 - 접수세부 테이블
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailSearch(Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("franchiseRequestDetailSearch 호출");

        log.info("고객ID bcId : "+bcId);
        log.info("택번호 searchTag : "+searchTag);
        log.info("조회타입 filterCondition : "+filterCondition);
        log.info("시작 접수일자 filterFromDt : "+filterFromDt);
        log.info("종료 접수일자 filterToDt : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frTagNo = (String) claims.get("frTagNo"); // 현재 가맹점의 택번호(2자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("현재 접속한 가맹점 택번호 : "+frTagNo);

        List<HashMap<String,Object>> requestDetailSearchDtoData = new ArrayList<>();
        HashMap<String,Object> requestDetailInfo;

        List<RequestDetailSearchDto> requestDetailSearchDtoList = requestDetailRepository.requestDetailSearch(frCode, bcId, frTagNo+searchTag, filterCondition, filterFromDt, filterToDt); //  통합조회용 - 접수세부 호출
        log.info("requestDetailSearchDtoList 크기 : "+requestDetailSearchDtoList.size());


        for(RequestDetailSearchDto requestDetailDto : requestDetailSearchDtoList){
            requestDetailInfo = new HashMap<>();

            requestDetailInfo.put("bcName", requestDetailDto.getBcName());
            requestDetailInfo.put("frYyyymmdd", requestDetailDto.getFrYyyymmdd());
            requestDetailInfo.put("frInsertDt", requestDetailDto.getFrInsertDt());

            requestDetailInfo.put("fdId", requestDetailDto.getFdId());
            requestDetailInfo.put("frId", requestDetailDto.getFrId());
            requestDetailInfo.put("frNo", requestDetailDto.getFrNo());

            requestDetailInfo.put("fdTag", requestDetailDto.getFdTag());
            requestDetailInfo.put("biItemcode", requestDetailDto.getBiItemcode());
            requestDetailInfo.put("fdState", requestDetailDto.getFdState());
            requestDetailInfo.put("fdPreState", requestDetailDto.getFdPreState());

            requestDetailInfo.put("fdS2Dt", requestDetailDto.getFdS2Dt());
            requestDetailInfo.put("fdS3Dt", requestDetailDto.getFdS3Dt());
            requestDetailInfo.put("fdS4Dt", requestDetailDto.getFdS4Dt());
            requestDetailInfo.put("fdS5Dt", requestDetailDto.getFdS5Dt());
            requestDetailInfo.put("fdS6Dt", requestDetailDto.getFdS6Dt());
            requestDetailInfo.put("fdS6Time", requestDetailDto.getFdS6Time());
            requestDetailInfo.put("fdS6CancelYn", requestDetailDto.getFdS6CancelYn());
            requestDetailInfo.put("fdS6CancelTime", requestDetailDto.getFdS6CancelTime());

            requestDetailInfo.put("fdCancel", requestDetailDto.getFdCancel());
            requestDetailInfo.put("fdCacelDt", requestDetailDto.getFdCacelDt());

            requestDetailInfo.put("fdColor", requestDetailDto.getFdColor());
            requestDetailInfo.put("fdPattern", requestDetailDto.getFdPattern());
            requestDetailInfo.put("fdPriceGrade", requestDetailDto.getFdPriceGrade());

            requestDetailInfo.put("fdOriginAmt", requestDetailDto.getFdOriginAmt());
            requestDetailInfo.put("fdNormalAmt", requestDetailDto.getFdNormalAmt());

            requestDetailInfo.put("fdAdd2Amt", requestDetailDto.getFdAdd2Amt());
            requestDetailInfo.put("fdAdd2Remark", requestDetailDto.getFdAdd2Remark());

            requestDetailInfo.put("fdPollution", requestDetailDto.getFdPollution());
            requestDetailInfo.put("fdDiscountGrade", requestDetailDto.getFdDiscountGrade());
            requestDetailInfo.put("fdDiscountAmt", requestDetailDto.getFdDiscountAmt());
            requestDetailInfo.put("fdQty", requestDetailDto.getFdQty());

            requestDetailInfo.put("fdRequestAmt", requestDetailDto.getFdRequestAmt());
            requestDetailInfo.put("fdSpecialYn", requestDetailDto.getFdSpecialYn());
            requestDetailInfo.put("fdTotAmt", requestDetailDto.getFdTotAmt());
            requestDetailInfo.put("fdRemark", requestDetailDto.getFdRemark());
            requestDetailInfo.put("fdEstimateDt", requestDetailDto.getFdEstimateDt());

            requestDetailInfo.put("fdRetryYn", requestDetailDto.getFdRetryYn());
            requestDetailInfo.put("fdUrgentYn", requestDetailDto.getFdUrgentYn());

            requestDetailInfo.put("fdPressed", requestDetailDto.getFdPressed());
            requestDetailInfo.put("fdAdd1Amt", requestDetailDto.getFdAdd1Amt());
            requestDetailInfo.put("fdAdd1Remark", requestDetailDto.getFdAdd1Remark());
            requestDetailInfo.put("fdRepairAmt", requestDetailDto.getFdRepairAmt());
            requestDetailInfo.put("fdRepairRemark", requestDetailDto.getFdRepairRemark());
            requestDetailInfo.put("fdWhitening", requestDetailDto.getFdWhitening());
            requestDetailInfo.put("fdPollutionLevel", requestDetailDto.getFdPollutionLevel());
            requestDetailInfo.put("fdWaterRepellent", requestDetailDto.getFdWaterRepellent());
            requestDetailInfo.put("fdStarch", requestDetailDto.getFdStarch());

            requestDetailInfo.put("fdPollutionLocFcn", requestDetailDto.getFdPollutionLocFcn());
            requestDetailInfo.put("fdPollutionLocFcs", requestDetailDto.getFdPollutionLocFcs());
            requestDetailInfo.put("fdPollutionLocFcb", requestDetailDto.getFdPollutionLocFcb());
            requestDetailInfo.put("fdPollutionLocFlh", requestDetailDto.getFdPollutionLocFlh());
            requestDetailInfo.put("fdPollutionLocFrh", requestDetailDto.getFdPollutionLocFrh());
            requestDetailInfo.put("fdPollutionLocFlf", requestDetailDto.getFdPollutionLocFlf());
            requestDetailInfo.put("fdPollutionLocFrf", requestDetailDto.getFdPollutionLocFrf());

            requestDetailInfo.put("fdPollutionLocBcn", requestDetailDto.getFdPollutionLocBcn());
            requestDetailInfo.put("fdPollutionLocBcs", requestDetailDto.getFdPollutionLocBcs());
            requestDetailInfo.put("fdPollutionLocBcb", requestDetailDto.getFdPollutionLocBcb());
            requestDetailInfo.put("fdPollutionLocBrh", requestDetailDto.getFdPollutionLocBrh());
            requestDetailInfo.put("fdPollutionLocBlh", requestDetailDto.getFdPollutionLocBlh());
            requestDetailInfo.put("fdPollutionLocBrf", requestDetailDto.getFdPollutionLocBrf());
            requestDetailInfo.put("fdPollutionLocBlf", requestDetailDto.getFdPollutionLocBlf());

            requestDetailInfo.put("frRefType", requestDetailDto.getFrRefType());

            requestDetailInfo.put("fdAgreeType", requestDetailDto.getFdAgreeType());
            requestDetailInfo.put("fdSignImage", requestDetailDto.getFdSignImage());

            requestDetailInfo.put("frFiId", requestDetailDto.getFrFiId());
            requestDetailInfo.put("frFiCustomerConfirm", requestDetailDto.getFrFiCustomerConfirm());
            requestDetailInfo.put("brFiId", requestDetailDto.getBrFiId());
            requestDetailInfo.put("brFiCustomerConfirm", requestDetailDto.getBrFiCustomerConfirm());

            requestDetailInfo.put("fpCancelYn", requestDetailDto.getFpCancelYn());

            requestDetailInfo.put("fdPollutionType", requestDetailDto.getFdPollutionType());
            requestDetailInfo.put("fdPollutionBack", requestDetailDto.getFdPollutionBack());

            List<PhotoDto> photoDtoList = photoRepository.findByPhotoDtoRequestDtlList(Long.parseLong(String.valueOf(requestDetailDto.getFdId())));
            requestDetailInfo.put("photoList", photoDtoList);

            requestDetailSearchDtoData.add(requestDetailInfo);
        }

        data.put("gridListData",requestDetailSearchDtoData);
        
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  통합조회용 - 접수 세부테이블 수정
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
            log.info("수정할 세부테이블 아이디 : "+optionalRequestDetail.get().getId());

            Integer nowFdTotAmt = optionalRequestDetail.get().getFdTotAmt();
            Integer updateFdTotAmt = requestDetailUpdateDto.getFdTotAmt();

            // RequestDetail 수정 시작
            optionalRequestDetail.get().setFdTotAmt(updateFdTotAmt);

            optionalRequestDetail.get().setBiItemcode(requestDetailUpdateDto.getBiItemcode());
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

            optionalRequestDetail.get().setFdPollutionLocFcn(requestDetailUpdateDto.getFdPollutionLocFcn());
            optionalRequestDetail.get().setFdPollutionLocFcs(requestDetailUpdateDto.getFdPollutionLocFcs());
            optionalRequestDetail.get().setFdPollutionLocFcb(requestDetailUpdateDto.getFdPollutionLocFcb());
            optionalRequestDetail.get().setFdPollutionLocFlh(requestDetailUpdateDto.getFdPollutionLocFlh());
            optionalRequestDetail.get().setFdPollutionLocFrh(requestDetailUpdateDto.getFdPollutionLocFrh());
            optionalRequestDetail.get().setFdPollutionLocFlf(requestDetailUpdateDto.getFdPollutionLocFlf());
            optionalRequestDetail.get().setFdPollutionLocFrf(requestDetailUpdateDto.getFdPollutionLocFrf());

            optionalRequestDetail.get().setFdPollutionLocBcn(requestDetailUpdateDto.getFdPollutionLocBcn());
            optionalRequestDetail.get().setFdPollutionLocBcs(requestDetailUpdateDto.getFdPollutionLocBcs());
            optionalRequestDetail.get().setFdPollutionLocBcb(requestDetailUpdateDto.getFdPollutionLocBcb());
            optionalRequestDetail.get().setFdPollutionLocBrh(requestDetailUpdateDto.getFdPollutionLocBrh());
            optionalRequestDetail.get().setFdPollutionLocBlh(requestDetailUpdateDto.getFdPollutionLocBlh());
            optionalRequestDetail.get().setFdPollutionLocBrf(requestDetailUpdateDto.getFdPollutionLocBrf());
            optionalRequestDetail.get().setFdPollutionLocBlf(requestDetailUpdateDto.getFdPollutionLocBlf());

            optionalRequestDetail.get().setFdStarch(requestDetailUpdateDto.getFdStarch());
            optionalRequestDetail.get().setFdWaterRepellent(requestDetailUpdateDto.getFdWaterRepellent());
            optionalRequestDetail.get().setFdDiscountGrade(requestDetailUpdateDto.getFdDiscountGrade());
            optionalRequestDetail.get().setFdDiscountAmt(requestDetailUpdateDto.getFdDiscountAmt());

            optionalRequestDetail.get().setFdQty(requestDetailUpdateDto.getFdQty());
            optionalRequestDetail.get().setFdRequestAmt(requestDetailUpdateDto.getFdRequestAmt());
            optionalRequestDetail.get().setFdRetryYn(requestDetailUpdateDto.getFdRetryYn());
            optionalRequestDetail.get().setFdUrgentYn(requestDetailUpdateDto.getFdUrgentYn());
            optionalRequestDetail.get().setFdRemark(requestDetailUpdateDto.getFdRemark());

            optionalRequestDetail.get().setFdAgreeType(requestDetailUpdateDto.getFdAgreeType());
            optionalRequestDetail.get().setFdSignImage(requestDetailUpdateDto.getFdSignImage());

            optionalRequestDetail.get().setModify_id(login_id);
            optionalRequestDetail.get().setModify_date(LocalDateTime.now());
            RequestDetail requestDetailSave = optionalRequestDetail.get();
//            log.info("requestDetailSave : "+requestDetailSave);

            if(nowFdTotAmt>updateFdTotAmt){
                List<PaymentCencelDto> paymentCencelDtoList = paymentRepositoryCustom.findByRequestDetailCencelDataList(frCode, optionalRequestDetail.get().getFrId().getId());
                if(paymentCencelDtoList.size() != 0){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP023.getCode(), ResponseErrorCode.TP023.getDesc(), "문자","결제 취소를 해주시길 바랍니다."));
                }else{
                    // 결제내용이없을때 저장 즉, 접수취소 할 수 있는 상태이면 가격이 내려가도 저장할 수 있다.
                    // 업데이트 실행 후 마스터테이블 수정 함수호출
                    requestDetailRepository.save(requestDetailSave);
                    // 만약 금액이 변동되었을 시 수정후, 마스터테이블도 업데이트하기 (결제취소됬을땐 예외)
                    userService.requestDetailUpdateFromMasterUpdate(requestDetailUpdateDto.getFrNo(), frCode);
                }
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
    @Transactional
    public ResponseEntity<Map<String, Object>> franchiseDetailCencelDataList(Long frId, HttpServletRequest request) {
        log.info("franchiseDetailCencelDataList 호출");

        log.info("접수마스터 ID frId : "+frId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<PaymentCencelDto> paymentCencelDtoList = paymentRepositoryCustom.findByRequestDetailCencelDataList(frCode, frId);
//        log.info("paymentCencelDtoList : "+paymentCencelDtoList);
        data.put("gridListData",paymentCencelDtoList);

        RequestFdTagDto requestDetailFdTagDto = requestRepository.findByRequestDetailFdTag(frCode, frId);
        log.info("requestDetailFdTagDto : "+requestDetailFdTagDto);
        if(!requestDetailFdTagDto.getFdTag().equals("")) {
            data.put("fdTag",requestDetailFdTagDto.getFdTag());
        }else {
            data.put("fdTag",null);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  통합조회용 - 결제취소 요청
    @Transactional
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailCencel(Long fpId, String type, HttpServletRequest request) {
        log.info("franchiseRequestDetailCencel 호출");

//        log.info("결제 ID fpId : "+fpId);
//        log.info("적립급/취소 타입 : "+type);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        Optional<Payment> optionalPayment = paymentRepository.findById(fpId);
        if(!optionalPayment.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "적립금 전환 " + ResponseErrorCode.TP022.getDesc(), null, null));
        }else{
            Optional<Request> optionalRequest = requestRepository.findById(optionalPayment.get().getFrId().getId());

            if(!optionalRequest.isPresent()){
                if(type.equals("1")){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "결제 취소 할 " + ResponseErrorCode.TP022.getDesc(), null, null));
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "적립금 전환 할 " + ResponseErrorCode.TP022.getDesc(), null, null));
                }
            }else{
                if(optionalRequest.get().getFpId() != null){
                    RequestFdTagDto requestFdTagDto = requestRepository.findByRequestDetailFdTag(frCode, optionalRequest.get().getId());
                    if(!requestFdTagDto.getFdTag().equals("")) {
                        log.info("결제 취소 조건에 부적합니다.");
                        StringBuilder tagNo = new StringBuilder(requestFdTagDto.getFdTag());
                        tagNo.insert(3,'-');
                        return ResponseEntity.ok(res.fail("문자", "조회하신 결제 건은 택번호: " + tagNo + " 로 미수완납이 처리되어있습니다.", "문자",
                                "택번호: " + tagNo + " 로 조회하셔서 먼저 결제취소하신 후 시도해주세요."));
                    }
                }else{
                    if(type.equals("1")){
                        log.info("결제 취소합니다.");

                        log.info("결제 취소 조건에 적합니다.");
//                        log.info("requestList : "+requestList);

                        optionalPayment.get().setFpCancelYn("Y");

                        // 마스터테이블의 계산가격을 업데이트한다.
                        optionalRequest.get().setFrPayAmount(optionalRequest.get().getFrPayAmount()-optionalPayment.get().getFpAmt());
                        optionalRequest.get().setFrUncollectYn("Y");
                        optionalRequest.get().setModify_id(login_id);
                        optionalRequest.get().setModify_date(LocalDateTime.now());

                        paymentRepository.save(optionalPayment.get());
                        requestRepository.save(optionalRequest.get());
                    }else{
                        log.info("적립금으로 전환합니다.");
                        optionalPayment.get().setFpCancelYn("Y");
                        optionalPayment.get().setFpSavedMoneyYn("Y");
                        paymentRepository.save(optionalPayment.get());

                        // 마스터테이블의 계산가격을 업데이트한다.
                        optionalRequest.get().setFrPayAmount(optionalRequest.get().getFrPayAmount() - optionalPayment.get().getFpAmt());
                        optionalRequest.get().setFrUncollectYn("Y");
                        optionalRequest.get().setModify_id(login_id);
                        optionalRequest.get().setModify_date(LocalDateTime.now());
                        requestRepository.save(optionalRequest.get());

                        SaveMoney saveMoney = new SaveMoney();
                        saveMoney.setBcId(optionalPayment.get().getBcId());
                        saveMoney.setFpId(optionalPayment.get());
                        saveMoney.setFsYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                        saveMoney.setFsType("1");
                        saveMoney.setFsClose("N");
                        saveMoney.setFsAmt(optionalPayment.get().getFpRealAmt());
                        saveMoney.setInsert_id(login_id);
                        saveMoney.setInsert_date(LocalDateTime.now());
                        saveMoneyRepository.save(saveMoney);
                    }

                    List<Request> requestList = requestRepository.findByRequestPaymentList(fpId);
                    for (Request value : requestList) {
                        value.setFpId(null);
                        value.setFrUncollectYn("Y");
                        value.setModify_date(LocalDateTime.now());
                        value.setModify_id(login_id);
                    }
                    requestRepository.saveAll(requestList);

                }
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 검품등록 API(가맹점, 지사)
    @Transactional
    public ResponseEntity<Map<String, Object>> InspectionSave(InspeotMapperDto inspeotMapperDto, HttpServletRequest request, String AWSBUCKETURL, String type) throws IOException {
        log.info("InspectionSave 호출");

        log.info("inspeotMapperDto : "+inspeotMapperDto);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        MessageHistory messageHistory = new MessageHistory();

        String strMessage = null;
        String message = null;
        String nextmessage = null;

        RequestDetailBranchInspeotDto requestDetailBranchInspeotDto = requestDetailRepository.findByBranchInspeotDto(inspeotMapperDto.getFdId());
        if(requestDetailBranchInspeotDto == null){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"검풍등록 할 "+ResponseErrorCode.TP024.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
        }else{

            if(inspeotMapperDto.getDeletePhotoList() != null) {
                // AWS 파일 삭제
                List<Photo> photoList = photoRepository.findByPhotoDeleteList(inspeotMapperDto.getDeletePhotoList());
                for(Photo photo : photoList){
                    String insertDate =photo.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    String path;
                    if(type.equals("1")){
                        path = "/toppos-franchise-inspection/"+insertDate;
                    }else{
                        path = "/toppos-manager-inspection/"+insertDate;
                    }
                    log.info("path : "+path);
                    String filename = photo.getFfFilename();
//                        log.info("filename : "+filename);
                    awss3Service.deleteObject(path,filename);
                }
                photoRepository.findByInspectPhotoDeleteList(inspeotMapperDto.getDeletePhotoList());
            }

            Inspeot inspeot;
            Long fiId;
            if(inspeotMapperDto.getFiId() == null){
                fiId = Long.parseLong("0");
            }else{
                fiId = inspeotMapperDto.getFiId();
            }
            Optional<Inspeot> optionalInspeot = inspeotRepository.findById(fiId);

            if(optionalInspeot.isPresent()){
                log.info("검품 수정입니다.");
                inspeot = modelMapper.map(optionalInspeot.get(), Inspeot.class);
                inspeot.setFiAddAmt(inspeotMapperDto.getFiAddAmt());
                inspeot.setFiComment(inspeotMapperDto.getFiComment());
                List<Photo> photo = photoRepository.findByPhotoList(optionalInspeot.get().getId());
                if(photo.size() != 0 || inspeotMapperDto.getAddPhotoList() != null){
                    inspeot.setFiPhotoYn("Y");
                }else{
                    inspeot.setFiPhotoYn("N");
                }
                inspeot.setModify_id(login_id);
                inspeot.setModify_date(LocalDateTime.now());

                List<String> msg = inspectPhotoSave(inspeot, null, inspeotMapperDto.getAddPhotoList(), inspeotMapperDto.getFiType(), AWSBUCKETURL, login_id, "1");
                log.info("msg : "+msg);

            }else{
                inspeot = new Inspeot();
                inspeot.setFdId(requestDetailBranchInspeotDto.getRequestDetail());
                inspeot.setFrCode(requestDetailBranchInspeotDto.getRequestDetail().getFrId().getFrCode());
                inspeot.setBrCode(requestDetailBranchInspeotDto.getRequestDetail().getFrId().getBrCode());
                inspeot.setFiType(inspeotMapperDto.getFiType());
                inspeot.setFiComment(inspeotMapperDto.getFiComment());
                inspeot.setFiAddAmt(inspeotMapperDto.getFiAddAmt());
                inspeot.setFiPhotoYn("N");
                inspeot.setFiSendMsgYn("N");
                inspeot.setFiCustomerConfirm("1");
                inspeot.setInsert_id(login_id);
                inspeot.setInsertDateTime(LocalDateTime.now());

                if(type.equals("2")) {
                    message = requestDetailBranchInspeotDto.getRequestDetail().getFrId().getBcId().getBcName() + "님의 확인품이 등록되었습니다.";
                    nextmessage = "확인품 등록알림";
                    strMessage =
                            "\n\n택번호 : " + requestDetailBranchInspeotDto.getRequestDetail().getFdTag().charAt(3) + "-" + requestDetailBranchInspeotDto.getRequestDetail().getFdTag().substring(4, 7) +
                                    "\n상품명 : " + requestDetailBranchInspeotDto.getBgName() + " " + requestDetailBranchInspeotDto.getBiName() +
                                    "\n검품내용 : " + inspeotMapperDto.getFiComment();
                }

                List<String> msg = inspectPhotoSave(inspeot, strMessage, inspeotMapperDto.getAddPhotoList(), inspeotMapperDto.getFiType(), AWSBUCKETURL, login_id, "2");
                log.info("msg : "+msg);

                if(type.equals("2")){
                    message = message+msg.get(0)+"\n통합조회 페이지를 통해 확인해 주세요.";
                    nextmessage = nextmessage+msg.get(0)+"\n통합조회 페이지를 통해 확인해 주세요.";

                    Optional<Inspeot> saveInspeot = inspeotRepository.findById(Long.parseLong(msg.get(1)));
                    saveInspeot.ifPresent(messageHistory::setFiId);

                    // 송신이력 저장
                    messageHistory.setBcId(requestDetailBranchInspeotDto.getRequestDetail().getFrId().getBcId());
                    messageHistory.setFmType("01");
                    messageHistory.setFrCode(requestDetailBranchInspeotDto.getRequestDetail().getFrId().getFrCode());
                    messageHistory.setBrCode(requestDetailBranchInspeotDto.getRequestDetail().getFrId().getBrCode());
                    messageHistory.setFmMessage(message);
                    messageHistory.setInsertYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    messageHistory.setInsert_id(login_id);
                    messageHistory.setInsertDateTime(LocalDateTime.now());
                    messageHistoryRepository.save(messageHistory);

                    boolean successBoolean = requestRepository.kakaoMessage(message, nextmessage,"", templatecodeCheck,
                            "fs_request_inspect", Long.parseLong(msg.get(1)), requestDetailBranchInspeotDto.getFrTelNo(), templatecodeNumber, "K");
                    log.info("successBoolean : "+successBoolean);

                    if(successBoolean) {
                        log.info("메세지 인서트 성공");
                    }else{
                        log.info("메세지 인서트 실패");
                    }

                }

            }

        }

        return ResponseEntity.ok(res.success());

    }

    // 검품 사진파일 등록 함수
    public List<String> inspectPhotoSave(Inspeot inspeot, String strMessage, List<MultipartFile> addPhotoList, String fiType, String AWSBUCKETURL, String login_id, String type) throws IOException {

        List<String> objects = new ArrayList<>();

        Inspeot saveInspeot;

        if(addPhotoList != null){

            inspeot.setFiPhotoYn("Y");
            saveInspeot = inspeotRepository.save(inspeot);

            Photo photo;
            log.info("addPhotoList.size() : "+addPhotoList.size());

            List<Photo> photoList = new ArrayList<>();
            for(int i=0; i<addPhotoList.size(); i++){
                photo = new Photo();

                // 파일 중복명 처리
                String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
                log.info("fileName : "+fileName);

                // S3에 저장 할 파일주소
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                String filePath;
                if(fiType.equals(("F"))){
                    photo.setFfType("02");
                    filePath = "/toppos-franchise-inspection/" + date.format(new Date());
                }else{
                    photo.setFfType("03");
                    filePath = "/toppos-manager-inspection/" + date.format(new Date());
                }

                log.info("filePath : "+AWSBUCKETURL+filePath+"/");

                if(i == 0){
                    strMessage = strMessage + "\n"+AWSBUCKETURL+filePath+"/"+"s_"+fileName; // 메세지 썸네일 이미지넣음
                    log.info("strMessage : "+strMessage);
                }

                awss3Service.imageFileUpload(addPhotoList.get(i), fileName, filePath);

                photo.setFiId(saveInspeot);
                photo.setFfPath(AWSBUCKETURL+filePath+"/");
                photo.setFfFilename(fileName);
                photo.setInsert_id(login_id);
                photo.setInsertDateTime(LocalDateTime.now());
                photoList.add(photo);

            }
            log.info("photoList : "+photoList);
            photoRepository.saveAll(photoList);
            objects.add(strMessage);

        }else{
            log.info("첨부파일이 존재하지 않습니다");
            if(type.equals("2")){
                inspeot.setFiPhotoYn("N");
            }
            saveInspeot = inspeotRepository.save(inspeot);
            objects.add("");
        }
        objects.add(String.valueOf(saveInspeot.getId()));

        return objects;
    }

    //  통합조회용 - 등록 검품 삭제
    public ResponseEntity<Map<String, Object>> InspectionDelete(Long fiId, String type, HttpServletRequest request) {
        log.info("InspectionDelete 호출");
        log.info("fiId : "+fiId);
        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        Optional<Inspeot> inspeot = inspeotRepository.findById(fiId);
        // AWS 파일 삭제
        List<Photo> photoList = photoRepository.findByInspectPhotoDeleteListData(fiId);
        for(Photo photo : photoList){
            String insertDate =photo.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String path;
            if(type.equals("1")){
                path = "/toppos-franchise-inspection/"+insertDate;
            }else{
                path = "/toppos-manager-inspection/"+insertDate;
            }
            log.info("path : "+path);
            String filename = photo.getFfFilename();
            log.info("filename : "+filename);
            awss3Service.deleteObject(path,filename);
        }

        if(inspeot.isPresent()){
            photoRepository.findByInspectPhotoDelete(inspeot.get().getId());
            inspeotRepository.delete(inspeot.get());
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "삭제 할 검품이 "+ResponseErrorCode.TP030.getDesc(), null, null));
        }

        return ResponseEntity.ok(res.success());
    }

//    //  통합조회용 - 등록 검품 삭제
//    @Transactional
//    public ResponseEntity<Map<String, Object>> InspectionDelete(Long InspectionDelete) {
//        log.info("franchiseInspectionDelete 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        ArrayList<InspeotDto> list = inspeotSet.getList();
//
//        if(list.size() != 0){
//            List<Long> inspeotDeleteList = new ArrayList<>();
//            List<Long> photoDeleteList = new ArrayList<>();
//            for(InspeotDto inspeotDto : list){
//                inspeotDeleteList.add(inspeotDto.getFiId());
//            }
//
//            List<InspeotDto> inspeotDtos = inspeotRepositoryCustom.findByInspeotDtoList(inspeotDeleteList);
//            for(InspeotDto inspeotDto : inspeotDtos){
//                if(inspeotDto.getFiPhotoYn().equals("Y")){
//                    photoDeleteList.add(inspeotDto.getFiId());
//                }
//            }
//
//            try {
//                if(inspeotDeleteList.size() != 0){
//                    inspeotRepository.findByInspectDelete(inspeotDeleteList);
//                }
//                if(photoDeleteList.size() != 0){
//                    photoRepository.findByInspectPhotoDelete(photoDeleteList);
//                }
//            }catch (Exception e){
//                log.info("예외 발생 : "+e);
//            }
//        }
//
//        return ResponseEntity.ok(res.success());
//    }

    //  통합조회 - 검품 리스트 요청
    public ResponseEntity<Map<String, Object>> franchiseInspectionList(Long fdId, String type) {
        log.info("franchiseInspectionList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("fdId : "+fdId);
        log.info("type : "+type);
        List<InspeotListDto> inspeotList = inspeotRepositoryCustom.findByInspeotList(fdId, type);
        data.put("gridListData",inspeotList);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  통합조회용 - 접수 취소
    public ResponseEntity<Map<String, Object>> franchiseReceiptCancel(Long fdId, HttpServletRequest request) {
        log.info("franchiseReceiptCancel 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(fdId);
        if(!optionalRequestDetail.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"접수취소 할 "+ResponseErrorCode.TP024.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
        }else{
            Optional<Request> optionalRequest = requestRepository.findById(optionalRequestDetail.get().getFrId().getId());
            if(!optionalRequest.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"접수취소 할 "+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
            }else{
                Integer fdTotAmt =  optionalRequestDetail.get().getFdTotAmt();
                Integer frTotalAmount =  optionalRequest.get().getFrTotalAmount()-fdTotAmt;
                Integer frPayAmount =  optionalRequest.get().getFrPayAmount();
                if(frTotalAmount.equals(frPayAmount)){
                    optionalRequest.get().setFrUncollectYn("N");
                }
                optionalRequest.get().setFrQty(optionalRequest.get().getFrQty()-1);
                optionalRequest.get().setFrTotalAmount(frTotalAmount);
                optionalRequest.get().setModify_id(login_id);
                optionalRequest.get().setModify_date(LocalDateTime.now());

                optionalRequestDetail.get().setFdCancel("Y");
                optionalRequestDetail.get().setFdCacelDt(LocalDateTime.now());
                optionalRequestDetail.get().setModify_id(login_id);
                optionalRequestDetail.get().setModify_date(LocalDateTime.now());
                requestDetailRepository.save(optionalRequestDetail.get());
                requestRepository.save(optionalRequest.get());
            }
        }

        return ResponseEntity.ok(res.success());
    }

    //  통합조회용 - 인도 취소
    public ResponseEntity<Map<String, Object>> franchiseLeadCancel(Long fdId, HttpServletRequest request) {
        log.info("franchiseLeadCancel 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        String fdState;
        String fdPreState;
        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(fdId);
        if(!optionalRequestDetail.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"인도취소 할 "+ResponseErrorCode.TP024.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
        }else{
            fdState =  optionalRequestDetail.get().getFdState(); // 바뀌기 전 현재상태
            if(fdState.equals("S6")){
                fdPreState =  optionalRequestDetail.get().getFdPreState(); // 바뀌기 전 이전상태
                optionalRequestDetail.get().setFdState(fdPreState);
                optionalRequestDetail.get().setFdStateDt(LocalDateTime.now());
                optionalRequestDetail.get().setFdPreState(fdState);
                optionalRequestDetail.get().setFdPreStateDt(LocalDateTime.now());
                optionalRequestDetail.get().setFdS6Type(null);
                optionalRequestDetail.get().setFdS6CancelYn("Y");
                optionalRequestDetail.get().setFdS6CancelTime(LocalDateTime.now());
                optionalRequestDetail.get().setModify_id(login_id);
                optionalRequestDetail.get().setModify_date(LocalDateTime.now());
                requestDetailRepository.save(optionalRequestDetail.get());
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"해당상품은 인도취소한 상품입니다.", "문자", "재조회 후 다시 시도해주세요."));
            }
        }

        return ResponseEntity.ok(res.success());
    }

    //  통합조회용 - 검품 고객 수락/거부
    @Transactional
    public ResponseEntity<Map<String, Object>> franchiseInspectionYn(Long fiId, String type, Integer fiAddAmt, HttpServletRequest request) {
        log.info("franchiseInspectionYn 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        Optional<Inspeot> optionalInspeot = inspeotRepository.findById(fiId);
        if(!optionalInspeot.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"검품 수락및거부 할"+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
        }else{
            optionalInspeot.get().setFiProgressStateDt(LocalDateTime.now());
            optionalInspeot.get().setModify_id(login_id);
            optionalInspeot.get().setModify_date(LocalDateTime.now());

            if(type.equals("2")){
                log.info("검품 수락 ID : "+fiId);
                optionalInspeot.get().setFiCustomerConfirm("2");

                Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(optionalInspeot.get().getFdId().getId());
                if(!optionalRequestDetail.isPresent()){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"업데이트 할 "+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
                }else{
                    optionalRequestDetail.get().setFdAdd2Amt(optionalRequestDetail.get().getFdAdd2Amt()+fiAddAmt);
                    optionalRequestDetail.get().setFdTotAmt(optionalRequestDetail.get().getFdTotAmt()+fiAddAmt);
                    optionalRequestDetail.get().setModify_id(login_id);
                    optionalRequestDetail.get().setModify_date(LocalDateTime.now());

                    Optional<Request> optionalRequest = requestRepository.request(optionalRequestDetail.get().getFrNo(), frCode);
                    if(!optionalRequest.isPresent()){
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"업데이트 할 "+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
                    }else {
                        Integer frTotalAmount = optionalRequest.get().getFrTotalAmount()+fiAddAmt;
                        optionalRequest.get().setFrTotalAmount(frTotalAmount);
                        if(frTotalAmount <= optionalRequest.get().getFrPayAmount()){
                            optionalRequest.get().setFrUncollectYn("N");
                        }else{
                            optionalRequest.get().setFrUncollectYn("Y");
                        }
                        optionalRequest.get().setModify_id(login_id);
                        optionalRequest.get().setModify_date(LocalDateTime.now());

                        inspeotRepository.save(optionalInspeot.get());
                        requestDetailRepository.save(optionalRequestDetail.get());
                        requestRepository.save(optionalRequest.get());
                    }
                }
            }else{
                log.info("검품 거부 ID : "+fiId);
                optionalInspeot.get().setFiCustomerConfirm("3");
                inspeotRepository.save(optionalInspeot.get());
            }
        }

        return ResponseEntity.ok(res.success());
    }

    //  검품이력 조회 및 메세지 - 리스트호출 테이블
    public ResponseEntity<Map<String, Object>> inspectList(Long bcId, String searchTag, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("inspectList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frTagNo = (String) claims.get("frTagNo"); // 현재 가맹점의 택번호(2자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("현재 접속한 가맹점 택번호 : "+frTagNo);

        List<RequestDetailInspectDto> requestDetailInspectDtos = requestDetailRepository.findByRequestDetailInspectList(frCode, bcId, frTagNo+searchTag, filterFromDt, filterToDt);
        data.put("gridListData",requestDetailInspectDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  통합조회용 - 카카오톡 메세지 보내기
    public ResponseEntity<Map<String, Object>> franchiseInspectionMessageSend(Long bcId, Long fiId, String fmMessage, String isIncludeImg, HttpServletRequest request) {
        log.info("franchiseInspectionMessageSend 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        log.info("이미지 여부 : "+isIncludeImg);
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setFmType("01");

        if(fiId != null){
            Optional<Inspeot> optionalInspeot = inspeotRepository.findById(fiId);
            if(optionalInspeot.isPresent()){
                optionalInspeot.get().setFiSendMsgYn("Y");
                optionalInspeot.get().setFiMessage(fmMessage);
                optionalInspeot.get().setFiMessageSendDt(LocalDateTime.now());
                optionalInspeot.get().setModify_id(login_id);
                optionalInspeot.get().setModify_date(LocalDateTime.now());
                optionalInspeot.ifPresent(messageHistory::setFiId);
                inspeotRepository.save(optionalInspeot.get());
            }
        }

        Optional<Customer> optionalCustomer = customerRepository.findByBcId(bcId);
        if (optionalCustomer.isPresent()) {
            messageHistory.setBcId(optionalCustomer.get());
        } else {
            messageHistory.setBcId(null);
        }
        messageHistory.setFrCode(frCode);
        messageHistory.setBrCode(frbrCode);
        messageHistory.setFmMessage(fmMessage);
        messageHistory.setInsert_id(login_id);
        messageHistory.setInsertDateTime(LocalDateTime.now());
        messageHistoryRepository.save(messageHistory);

        return ResponseEntity.ok(res.success());
    }



    // 지사검품등록용 - 확인품 등록할 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchInspection(Long franchiseId, String fromDt, String toDt, String tagNo, HttpServletRequest request) {
        log.info("branchInspection 호출");

        log.info("franchiseId : "+franchiseId);
        log.info("tagNo : "+tagNo);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<HashMap<String,Object>> RequestDetailBranchInspectListDtoData = new ArrayList<>();
        HashMap<String,Object> requestDetailInfo;

        // 확인품 등록 할 리스트 호출
        List<RequestDetailBranchInspectListDto> requestDetailBranchForceListDtos = requestDetailRepository.findByRequestDetailBranchInspectList(brCode, franchiseId, fromDt, toDt, tagNo);

        for(RequestDetailBranchInspectListDto requestDetailDto : requestDetailBranchForceListDtos){
            requestDetailInfo = new HashMap<>();

            requestDetailInfo.put("fdId", requestDetailDto.getFdId());

            requestDetailInfo.put("frName", requestDetailDto.getFrName());
            requestDetailInfo.put("insertDt", requestDetailDto.getInsertDt());
            requestDetailInfo.put("fdS2Time", requestDetailDto.getFdS2Time());

            requestDetailInfo.put("fdTag", requestDetailDto.getFdTag());
            requestDetailInfo.put("fdColor", requestDetailDto.getFdColor());

            requestDetailInfo.put("bgName", requestDetailDto.getBgName());
            requestDetailInfo.put("bsName", requestDetailDto.getBsName());
            requestDetailInfo.put("biName", requestDetailDto.getBiName());

            requestDetailInfo.put("fdPriceGrade", requestDetailDto.getFdPriceGrade());
            requestDetailInfo.put("fdRetryYn", requestDetailDto.getFdRetryYn());
            requestDetailInfo.put("fdPressed", requestDetailDto.getFdPressed());

            requestDetailInfo.put("fdAdd1Amt", requestDetailDto.getFdAdd1Amt());
            requestDetailInfo.put("fdAdd1Remark", requestDetailDto.getFdAdd1Remark());
            requestDetailInfo.put("fdRepairAmt", requestDetailDto.getFdRepairAmt());
            requestDetailInfo.put("fdRepairRemark", requestDetailDto.getFdRepairRemark());

            requestDetailInfo.put("fdWhitening", requestDetailDto.getFdWhitening());
            requestDetailInfo.put("fdPollution", requestDetailDto.getFdPollution());
            requestDetailInfo.put("fdWaterRepellent", requestDetailDto.getFdWaterRepellent());
            requestDetailInfo.put("fdStarch", requestDetailDto.getFdStarch());
            requestDetailInfo.put("fdUrgentYn", requestDetailDto.getFdUrgentYn());

            requestDetailInfo.put("bcName", requestDetailDto.getBcName());
            requestDetailInfo.put("fdTotAmt", requestDetailDto.getFdTotAmt());
            requestDetailInfo.put("fdState", requestDetailDto.getFdState());
            requestDetailInfo.put("fdPreState", requestDetailDto.getFdPreState());

            requestDetailInfo.put("frFiId", requestDetailDto.getFrFiId());
            requestDetailInfo.put("frFiCustomerConfirm", requestDetailDto.getFrFiCustomerConfirm());
            requestDetailInfo.put("brFiId", requestDetailDto.getBrFiId());
            requestDetailInfo.put("brFiCustomerConfirm", requestDetailDto.getBrFiCustomerConfirm());

            requestDetailInfo.put("fdPollutionType", requestDetailDto.getFdPollutionType());
            requestDetailInfo.put("fdPollutionBack", requestDetailDto.getFdPollutionBack());

            List<PhotoDto> photoDtoList = photoRepository.findByPhotoDtoRequestDtlList(Long.parseLong(String.valueOf(requestDetailDto.getFdId())));
            requestDetailInfo.put("photoList", photoDtoList);

            RequestDetailBranchInspectListDtoData.add(requestDetailInfo);
        }

        data.put("gridListData",RequestDetailBranchInspectListDtoData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  검품 조회 - 검품 상세보기 정보 요청
    public ResponseEntity<Map<String, Object>> inspectionInfo(Long fiId, String type, HttpServletRequest request) {
        log.info("inspectionInfo 호출");

        log.info("fiId : "+fiId);
        log.info("type : "+type);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String code;
        if(type.equals("1")){
            code = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        }else{
            code = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        }

        log.info("code : "+code);
        InspeotInfoDto inspeotInfoDto = inspeotRepositoryCustom.findByInspeotInfo(fiId, code, type);
        data.put("inspeotInfoDto",inspeotInfoDto);

        List<PhotoDto> photoList = new ArrayList<>();
        if(inspeotInfoDto.getFiPhotoYn().equals("Y")){
            photoList = photoRepository.findByPhotoDtoInspeotList(inspeotInfoDto.getFiId());
        }
        data.put("photoList",photoList);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    //  지사검품등록용 - 검품 리스트 요청
    public ResponseEntity<Map<String, Object>> branchInspectionList(Long fdId, String type) {
        log.info("branchInspectionList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("fdId : "+fdId);
        log.info("type : "+type);
        List<InspeotListDto> inspeotList = inspeotRepositoryCustom.findByInspeotList(fdId, type);
        data.put("gridListData",inspeotList);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  확인품현황 리스트 호출 API
    public ResponseEntity<Map<String, Object>> branchInspectionCurrentList(Long franchiseId, String fromDt, String toDt, String tagNo, HttpServletRequest request) {
        log.info("branchInspectionCurrentList 호출");

//        log.info("franchiseId : "+franchiseId);
//        log.info("tagNo : "+tagNo);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchInspectionCurrentListDto> requestDetailBranchInspectionCurrentListDtos = requestDetailRepository.findByRequestDetailBranchInspectionCurrentList
                (brCode, franchiseId, fromDt, toDt, tagNo);

        data.put("gridListData",requestDetailBranchInspectionCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 메인페이지용 검품 리스트 호출 API
    public List<InspeotMainListDto> findByInspeotB1(String frbrCode, int limit, String frCode) {
        return inspeotRepositoryCustom.findByInspeotB1(frbrCode, limit, frCode);
    }


}
