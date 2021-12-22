package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.EtcDataDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.*;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.*;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import com.broadwave.toppos.keygenerate.KeyGenerateService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-12-14
 * Time :
 * Remark : Toppos 가맹점 접수 전용 서비스
 */
@Slf4j
@Service
public class ReceiptService {

    // 현재 날짜 받아오기
    LocalDateTime localDateTime = LocalDateTime.now();
    private final  String nowDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final KeyGenerateService keyGenerateService;
    private final ModelMapper modelMapper;

    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final PaymentRepository paymentRepository;
    private final SaveMoneyRepository saveMoneyRepository;

    private final RequestRepositoryCustom requestRepositoryCustom;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    private final SaveMoneyRepositoryCustom saveMoneyRepositoryCustom;

    private final HeadService headService;
    private final CustomerRepository customerRepository;
    private final BranchCalendarRepositoryCustom branchCalendarRepositoryCustom;

    @Autowired
    public ReceiptService(UserService userService, KeyGenerateService keyGenerateService, TokenProvider tokenProvider, ModelMapper modelMapper,
                          RequestRepository requestRepository, RequestDetailRepository requestDetailRepository, PaymentRepository paymentRepository, SaveMoneyRepository saveMoneyRepository,
                          RequestRepositoryCustom requestRepositoryCustom, RequestDetailRepositoryCustom requestDetailRepositoryCustom, SaveMoneyRepositoryCustom saveMoneyRepositoryCustom,
                          HeadService headService, CustomerRepository customerRepository,BranchCalendarRepositoryCustom branchCalendarRepositoryCustom){
        this.userService = userService;
        this.headService = headService;
        this.requestRepository = requestRepository;
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.customerRepository = customerRepository;
        this.saveMoneyRepository = saveMoneyRepository;
        this.branchCalendarRepositoryCustom = branchCalendarRepositoryCustom;
        this.keyGenerateService = keyGenerateService;
        this.requestRepositoryCustom = requestRepositoryCustom;
        this.saveMoneyRepositoryCustom = saveMoneyRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    // 태그번호, 출고예정일 데이터
    public List<EtcDataDto> findByEtc(Long frEstimateDuration, String frCode, String nowDate) {
        return branchCalendarRepositoryCustom.findByEtc(frEstimateDuration, frCode, nowDate);
    }

    // 접수코드를 통한 접수마스터 테이블 조회
    public Optional<Request> findByRequest(String frNo, String frConfirmYn, String frCode){
        return requestRepository.findByRequest(frNo, frConfirmYn, frCode);
    }

    // 접수코드를 통한 접수마스터 테이블 조회
    public List<RequestInfoDto> findByRequestList(String frCode, String nowDate, Customer customer){
        return requestRepositoryCustom.findByRequestList(frCode, nowDate, customer);
    }

    // 접수코드와 태그번호를 통한 접수세부 테이블 조회
    public Optional<RequestDetail> findByRequestDetail(String frNo, String fdTag){
        return requestDetailRepository.findByRequestDetail(frNo, fdTag);
    }

    // 접수 세부테이블 삭제
    public void findByRequestDetailDelete(RequestDetail requestDetail) {
        requestDetailRepository.delete(requestDetail);
    }

    // 접수 마스터테이블 임시저장 리스트 호출
    public List<RequestListDto> findByRequestTempList(String frCode){
        return requestRepositoryCustom.findByRequestTempList(frCode);
    }

    // 삭제를 위한 세부테이블 임시저장 리스트 호출
    public List<RequestDetail> findByRequestTempDetail(String frNo) {
        return requestDetailRepository.findByRequestTempDetail(frNo);
    }

    // 접수 세부테이블 임시저장 리스트 호출
    public List<RequestDetailDto> findByRequestTempDetailList(String frNo) {
        return requestDetailRepositoryCustom.findByRequestTempDetailList(frNo);
    }

    // 접수 세부테이블 가격관련 리스트 호출
    public List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo) {
        return requestDetailRepositoryCustom.findByRequestDetailAmtList(frNo);
    }

    // 결제 마스터테이블 미수금액 리스트 호출
    public List<RequestCollectDto> findByRequestCollectList(Customer customer, String nowDate) {
        return requestRepositoryCustom.findByRequestCollectList(customer, nowDate);
    }

    // 현재 고객의 적립금 리스트 호출
    public Integer findBySaveMoney(Customer customer) {
        List<SaveMoneyDto> saveMoneyDtoList = saveMoneyRepositoryCustom.findBySaveMoney(customer);
        int plusSaveMoney = 0;
        int minusSaveMoney = 0;
        if(saveMoneyDtoList.size() != 0) {
            for (SaveMoneyDto saveMoneyDto : saveMoneyDtoList) {
                if(saveMoneyDto.getFsType().equals("1")){
                    plusSaveMoney = plusSaveMoney + saveMoneyDto.getFsAmt();
                }else {
                    minusSaveMoney = minusSaveMoney + saveMoneyDto.getFsAmt();
                }
            }
            return plusSaveMoney-minusSaveMoney;
        }else{
            return 0;
        }
    }

    // 접수페이지 가맹점 임시저장 및 결제하기 세탁접수 API
    public ResponseEntity<Map<String,Object>> requestSave(RequestDetailSet requestDetailSet, HttpServletRequest request) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드

        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        RequestMapperDto etcData = requestDetailSet.getEtc(); // etc 데이터 얻기

        ArrayList<RequestDetailDto> addList = requestDetailSet.getAdd(); // 추가 리스트 얻기
        ArrayList<RequestDetailDto> updateList = requestDetailSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<RequestDetailDto> deleteList = requestDetailSet.getDelete(); // 제거 리스트 얻기

        log.info("ECT 데이터 : "+etcData);
        log.info("추가 리스트 : "+addList);
        log.info("수정 리스트 : "+updateList);
        log.info("삭제 리스트 : "+deleteList);

        // 현재 고객을 받아오기
        Optional<Customer> optionalCustomer = userService.findByBcId(etcData.getBcId());
        if(!optionalCustomer.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP018.getCode(), ResponseErrorCode.TP018.getDesc(),null, null));
        }else{

            Request requestSave;
            log.info("etcData.getFrNo() : "+etcData.getFrNo());
            if(etcData.getFrNo() != null){
                log.info("접수마스터 테이블 수정합니다. 접수코드 : "+etcData.getFrNo());
                Optional<Request> optionalRequest = findByRequest(etcData.getFrNo(), "N", frCode);
                if(!optionalRequest.isPresent()){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "접수 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "접수코드 : "+etcData.getFrNo()));
                }else{
                    optionalRequest.get().setFrTotalAmount(etcData.getFrTotalAmount());
                    optionalRequest.get().setFrDiscountAmount(etcData.getFrDiscountAmount());
                    optionalRequest.get().setFrNormalAmount(etcData.getFrNormalAmount());
                    optionalRequest.get().setFrQty(etcData.getFrQty());
                    optionalRequest.get().setFrYyyymmdd(nowDate);
                    optionalRequest.get().setModity_id(login_id);
                    optionalRequest.get().setModity_date(LocalDateTime.now());
                    requestSave = optionalRequest.get();
                }
            }else{
                log.info("접수마스터 테이블 신규 저장합니다.");

                requestSave = modelMapper.map(etcData, Request.class);

                requestSave.setBcId(optionalCustomer.get());
                requestSave.setBcCode(frbrCode);
                requestSave.setFrCode(frCode);
                requestSave.setFrYyyymmdd(nowDate);
                requestSave.setFrPayAmount(0);
                requestSave.setFrRefBoxCode(null); // 무인보관함 연계시 무인보관함 접수번호 : 일단 무조건 NULL
                requestSave.setFr_insert_id(login_id);
                requestSave.setFr_insert_date(LocalDateTime.now());
                requestSave.setFrUncollectYn("Y");

                log.info("접수마스터 테이블 저장 or 수정 : "+requestSave);
            }

            log.info("etcData.getCheckNum() : "+etcData.getCheckNum());
            // 임시저장인지, 결제하기저장인지 여부 : 임시저장이면 Y, 아니면 N로 저장
            if(etcData.getCheckNum().equals("1")){
                requestSave.setFrConfirmYn("N");
            }else{
                requestSave.setFrConfirmYn("Y");

                // 결제일 경우, 현재 고객의 적립금과 미수금액을 보내준다.
                // 미수금액 리스트를 호출한다. 조건 : 미수여부는 Y, 임시저장확정여부는 N, 고객아이디 eq, 가맹점코드 = frCode eq, 현재날짜의 전날들만 인것들만 조회하기
                List<RequestCollectDto>  requestCollectDtoList = findByRequestCollectList(optionalCustomer.get(), null); // nowDate 현재날짜
                List<Integer> uncollectMoneyList = findByBeforeAndTodayUnCollect(requestCollectDtoList, nowDate);
                int beforeUncollectMoney = 0;
                int todayUncollectMoney = 0;
                if(requestCollectDtoList.size() != 0){
                    beforeUncollectMoney = uncollectMoneyList.get(0);
                    todayUncollectMoney = uncollectMoneyList.get(1);
                    data.put("beforeUncollectMoney",beforeUncollectMoney);
                    data.put("todayUncollectMoney",todayUncollectMoney);
                }else{
                    data.put("beforeUncollectMoney",0);
                    data.put("todayUncollectMoney",0);
                }
//                log.info("합계금액 : "+totalAmount);
//                log.info("결제금액 : "+payAmount);
                log.info("전일 미수금액 : "+ beforeUncollectMoney);
                log.info("당일 미수금액 : "+ todayUncollectMoney);

                // 적립금 리스트를 호출한다. 조건 : 고객 ID, 적립유형 1 or 2, 마감여부 : N,
                Integer saveMoney = findBySaveMoney(optionalCustomer.get());
                data.put("saveMoney",saveMoney);
                log.info("적립금액 : "+ (saveMoney));
            }

            String lastTagNo = null; // 마지막 태그번호
            List<RequestDetail> requestDetailList = new ArrayList<>(); // 세부테이블 객체 리스트
            // 접수 세부 테이블 저장
            if(addList.size()!=0){
                for (RequestDetailDto requestDetailDto : addList) {
                    log.info("RequestDetailDto : "+requestDetailDto);
                    RequestDetail requestDetail = modelMapper.map(requestDetailDto, RequestDetail.class);

                    requestDetail.setBiItemcode(requestDetailDto.getBiItemcode());
                    requestDetail.setFdState("S1");
                    requestDetail.setFdStateDt(LocalDateTime.now());
                    requestDetail.setFdCancel("N");
                    requestDetail.setFdTotAmt(requestDetailDto.getFdRequestAmt());
                    requestDetail.setFdEstimateDt(requestDetailDto.getFrEstimateDate());
                    requestDetail.setInsert_id(login_id);
                    requestDetail.setInsert_date(LocalDateTime.now());
                    lastTagNo = requestDetailDto.getFdTag();
                    requestDetailList.add(requestDetail);
                }
            }
            // 접수 세부 테이블 업데이트
            if(updateList.size()!=0){
                for (RequestDetailDto requestDetailDto : updateList) {
                    log.info("수정로직 FrNo : "+etcData.getFrNo());
                    log.info("수정로직 FdTag : "+requestDetailDto.getFdTag());
                    Optional<RequestDetail> optionalRequestDetail = findByRequestDetail(etcData.getFrNo(), requestDetailDto.getFdTag());
                    if(!optionalRequestDetail.isPresent()){
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "택번호 : "+requestDetailDto.getFdTag()));
                    }else{
                        optionalRequestDetail.get().setBiItemcode(requestDetailDto.getBiItemcode());
                        optionalRequestDetail.get().setFdColor(requestDetailDto.getFdColor());
                        optionalRequestDetail.get().setFdPattern(requestDetailDto.getFdPattern());
                        optionalRequestDetail.get().setFdPriceGrade(requestDetailDto.getFdPriceGrade());

                        optionalRequestDetail.get().setFdOriginAmt(requestDetailDto.getFdOriginAmt());
                        optionalRequestDetail.get().setFdNormalAmt(requestDetailDto.getFdNormalAmt());
                        optionalRequestDetail.get().setFdRepairRemark(requestDetailDto.getFdRepairRemark());
                        optionalRequestDetail.get().setFdRepairAmt(requestDetailDto.getFdRepairAmt());

                        optionalRequestDetail.get().setFdAdd1Remark(requestDetailDto.getFdAdd1Remark());
                        optionalRequestDetail.get().setFdSpecialYn(requestDetailDto.getFdSpecialYn());
                        optionalRequestDetail.get().setFdAdd1Amt(requestDetailDto.getFdAdd1Amt());

                        optionalRequestDetail.get().setFdPressed(requestDetailDto.getFdPressed());
                        optionalRequestDetail.get().setFdWhitening(requestDetailDto.getFdWhitening());
                        optionalRequestDetail.get().setFdPollution(requestDetailDto.getFdPollution());
                        optionalRequestDetail.get().setFdPollutionLevel(requestDetailDto.getFdPollutionLevel());
                        optionalRequestDetail.get().setFdStarch(requestDetailDto.getFdStarch());
                        optionalRequestDetail.get().setFdWaterRepellent(requestDetailDto.getFdWaterRepellent());

                        optionalRequestDetail.get().setFdDiscountGrade(requestDetailDto.getFdDiscountGrade());
                        optionalRequestDetail.get().setFdDiscountAmt(requestDetailDto.getFdDiscountAmt());
                        optionalRequestDetail.get().setFdQty(requestDetailDto.getFdQty());

                        optionalRequestDetail.get().setFdRequestAmt(requestDetailDto.getFdRequestAmt());
                        optionalRequestDetail.get().setFdRetryYn(requestDetailDto.getFdRetryYn());

                        optionalRequestDetail.get().setFdRemark(requestDetailDto.getFdRemark());
                        optionalRequestDetail.get().setFdEstimateDt(requestDetailDto.getFrEstimateDate());

                        optionalRequestDetail.get().setModity_id(login_id);
                        optionalRequestDetail.get().setModity_date(LocalDateTime.now());
//                        RequestDetail requestDetail = optionalRequestDetail.get();
                        requestDetailList.add(optionalRequestDetail.get());
                    }
                }
            }
            log.info("requestDetailList : "+requestDetailList);
            // 접수 세부 테이블 삭제
            if(deleteList.size()!=0){
                for (RequestDetailDto requestDetailDto : deleteList) {
                    log.info("삭제로직 FrNo : "+etcData.getFrNo());
                    log.info("삭제로직 FdTag : "+requestDetailDto.getFdTag());
                    Optional<RequestDetail> optionalRequestDetail = findByRequestDetail(etcData.getFrNo(), requestDetailDto.getFdTag());
                    if(!optionalRequestDetail.isPresent()){
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "택번호 : "+requestDetailDto.getFdTag()));
                    }else{
                        findByRequestDetailDelete(optionalRequestDetail.get());
                    }
                }
            }

            // 현재 접수한 고객의 대한 마지막방문일자 업데이트
            optionalCustomer.get().setBcLastRequestDt(nowDate);
            Customer customer = optionalCustomer.get();

            Request requestSaveO = requestAndDetailSave(requestSave, requestDetailList, customer);


            // 모두 저장되면 최종 택번호 업데이트
            Optional<Franchise> optionalFranchise = headService.findByFrCode(frCode); // 가맹점
            if(optionalFranchise.isPresent()){
                if(addList.size()==0){
                    lastTagNo = optionalFranchise.get().getFrLastTagno();
                }
                log.info("마지막 택번호 : "+lastTagNo);
                optionalFranchise.get().setFrLastTagno(lastTagNo);

                headService.franchiseSave(optionalFranchise.get());
                log.info(optionalFranchise.get().getFrName()+" 가맹점 택번호 업데이트 완료 : "+lastTagNo);
            }

            data.put("frNo",requestSaveO.getFrNo());

            return ResponseEntity.ok(res.dataSendSuccess(data));

        }
    }

    // 문의 접수 API : 임시저장 또는 결제할시 저장한다. 마스터테이블, 세부테이블 저장
    @Transactional(rollbackFor = SQLException.class)
    public Request requestAndDetailSave(Request request, List<RequestDetail> requestDetailList, Customer customer){
        try{
            String frNo;
            if (request.getFrNo() == null || request.getFrNo().isEmpty()){
                frNo = keyGenerateService.keyGenerate("fs_request", request.getFrCode()+request.getFrYyyymmdd(), request.getFr_insert_id());
                request.setFrNo(frNo);
            }else{
                frNo = request.getFrNo();
            }
            log.info("frNo : "+frNo);
            Request requestSave = requestRepository.save(request);

            for (RequestDetail requestDetail : requestDetailList) {
                if (requestDetail.getFrNo() == null) {
                    requestDetail.setFrNo(frNo);
                    requestDetail.setFrId(requestSave);
                }
            }
            customerRepository.save(customer);
            requestDetailRepository.saveAll(requestDetailList);

            return requestSave;
        }catch (Exception e){
            log.info("에러발생 트랜젝션실행 : "+e);
            return null;
        }
    }

    // 임시저장 글 삭제로직
    public ResponseEntity<Map<String, Object>> requestDelete(HttpServletRequest request, String frNo) {
        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        log.info("frNo2 : "+frNo);
        Optional<Request> optionalRequest = findByRequest(frNo, "N", frCode);
//        log.info("optionalRequest : "+optionalRequest);
        if(!optionalRequest.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "접수코드 : "+frNo));
        }else{
            List<RequestDetail> requestDetailList = findByRequestTempDetail(optionalRequest.get().getFrNo());
//            log.info("requestDetailList : "+requestDetailList);
//            log.info("requestDetailList.size() : "+requestDetailList.size());
            requestDeleteStart(optionalRequest.get(), requestDetailList);
        }

        return ResponseEntity.ok(res.success());
    }

    // 삭제 실행
    @Transactional(rollbackFor = SQLException.class)
    public void requestDeleteStart(Request optionalRequest, List<RequestDetail> requestDetailList) {
        try{
            requestDetailRepository.deleteAll(requestDetailList);
            requestRepository.delete(optionalRequest);
        }catch (Exception e){
            log.info("에러발생 트랜젝션실행1 : "+e);
        }
    }




    //  접수페이지 가맹점 세탁접수 결제 API
    public ResponseEntity<Map<String, Object>> requestPayment(PaymentSet paymentSet, HttpServletRequest request) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        PaymentMapperDto etcData = paymentSet.getEtc(); // etc 데이터 얻기
        ArrayList<PaymentDto> paymentDtos = paymentSet.getPayment(); // 결제 정보 데이터 리스트 얻기

        log.info("ECT 데이터 : "+etcData);
        log.info("paymentDtos 리스트 : "+paymentDtos);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드

        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        // 현재 고객을 받아오기 -> 고객정보가 존재해야 결제를 진행할 수 있다.
        Optional<Customer> optionalCustomer = userService.findByBcId(etcData.getBcId());
        if(!optionalCustomer.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP018.getCode(), ResponseErrorCode.TP018.getDesc(),null, null));
        }else{
            log.info("결제 정보있음 고객명 : "+optionalCustomer.get().getBcName());
            log.info("접수코드 데이터 : "+etcData.getFrNo());
            Optional<Request> optionalRequest = findByRequest(etcData.getFrNo(), "Y" ,frCode);
            if(!optionalRequest.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "결제 할 접수"+ResponseErrorCode.TP009.getDesc(), "문자", "접수코드 : "+etcData.getFrNo()));
            }else{

                List<Payment> paymentList = new ArrayList<>();
                Integer frPayAmount = 0;
                int collectAmt = 0;

                String collectYn = "N";
                SaveMoney saveMoney = null;
                // 결제 데이터가 존재할시 저장 시작
                if(paymentDtos.size() != 0){
                    List<PaymentEtcDto> paymentEtcDtos = new ArrayList<>();  // 결제완료시 보낼 Etc 데이터 리스트
                    for(PaymentDto paymentDto : paymentDtos){
                        if(paymentDto.getFpAmt()>0){
                            PaymentEtcDto paymentEtcDto = new PaymentEtcDto();
                            frPayAmount = frPayAmount + paymentDto.getFpAmt();
                            Payment payment = modelMapper.map(paymentDto,Payment.class);
                            payment.setBcId(optionalCustomer.get());
                            payment.setFrId(optionalRequest.get());
                            payment.setInsert_id(login_id);
                            payment.setInsert_date(LocalDateTime.now());

                            collectAmt = collectAmt+payment.getFpCollectAmt(); // 미수상환금액 계산

                            log.info("payment.getFpCollectAmt() : "+payment.getFpCollectAmt());
                            // 미수금완납시 처리
                            if(payment.getFpCollectAmt()>0){
                                collectYn = "Y";
                            }

                            // 결제완료시 보낼 Etc 데이터 리스트
                            paymentEtcDto.setFpType(payment.getFpType());
                            paymentEtcDto.setFpAmt(payment.getFpAmt());
                            paymentEtcDto.setFpCatIssuername(payment.getFpCatIssuername());
                            paymentEtcDtos.add(paymentEtcDto);

                            if(payment.getFpType().equals("03")){
                                saveMoney = new SaveMoney();
                                saveMoney.setBcId(optionalCustomer.get());
                                saveMoney.setFsType("2");
                                saveMoney.setFsClose("N");
                                saveMoney.setFsAmt(payment.getFpAmt());
                                saveMoney.setInsert_id(login_id);
                                saveMoney.setInsert_date(LocalDateTime.now());
                            }

                            // 저장할 결제데이터 리스트
                            paymentList.add(payment);
                        }
                    }

                    log.info("미수금완납 결제여부 : "+collectYn);
                    log.info("결제 금액 : "+frPayAmount);
                    // 마스터테이블에 결제금액 업데이트
                    optionalRequest.get().setFrPayAmount(frPayAmount);
                    optionalRequest.get().setModity_id(login_id);
                    optionalRequest.get().setModity_date(LocalDateTime.now());

                    // 미수여부 기능작업 조건 : 합계금액보다 결제금액이 같거나 크면 N
                    if(optionalRequest.get().getFrTotalAmount() <= frPayAmount){
                        optionalRequest.get().setFrUncollectYn("N");
                    }

                    Payment result = requestAndPaymentSave(optionalRequest.get(), paymentList, saveMoney);
                    if(result != null){
                        log.info("다시 업데이트 할 접수코드 : "+result.getFrId().getFrNo());

                        log.info("현재 날짜 yyyymmdd : "+nowDate);

                        // 결제가 성공적으로 저장이 됬을때 타는 로직
                        // -> 세부테이블의 total 금액을 마스터테이블의 합계금액에 업데이트 쳐준다.
                        List<RequestDetailAmtDto> requestDetailAmtDtos = findByRequestDetailAmtList(result.getFrId().getFrNo()); // 세부테이블의 합계금액 리스트 호출
                        int totalAmt = optionalRequest.get().getFrTotalAmount();
                        if(requestDetailAmtDtos.size() != 0){
                            totalAmt = 0;
                            for(RequestDetailAmtDto requestDetailAmtDto : requestDetailAmtDtos){
                                totalAmt = totalAmt+requestDetailAmtDto.getFdTotAmt();
                            }
                        }
                        optionalRequest.get().setFrTotalAmount(totalAmt);
                        // 마스터테이블을 다시 업데이트 쳐준다.
                        requestRepository.save(optionalRequest.get());

                        // 미수완납시 마스터테이블 업데이트
                        List<Request> updateRequestList = new ArrayList<>();
                        if(collectYn.equals("Y")){
                            Request requestUpdate;
                            List<RequestInfoDto> updateRequestLists = findByRequestList(frCode, nowDate, optionalCustomer.get());
                            log.info("updateRequestLists : "+updateRequestLists.size());
                            for(RequestInfoDto updateRequest : updateRequestLists){
                                requestUpdate = modelMapper.map(updateRequest, Request.class);
                                requestUpdate.setFpId(result);
                                requestUpdate.setFrUncollectYn("N");
                                requestUpdate.setModity_id(login_id);
                                requestUpdate.setModity_date(LocalDateTime.now());
                                updateRequestList.add(requestUpdate);
                            }
                            requestRepository.saveAll(updateRequestList); // 미수금완납시 마스터테이블 처리
                        }

                        // 결제완료 후 미수상환금액 보낸다.
                        data.put("collectAmt",collectAmt);
                        log.info("결제후 미수상환금액 : "+ collectAmt);

                        // 전일미수금을 보낸다. 전일미수금에서 미수상환금액을 뺀 금액
                        List<RequestCollectDto>  requestCollectDtoList = findByRequestCollectList(optionalCustomer.get(), null); // nowDate 현재날짜
                        List<Integer> uncollectMoneyList = findByBeforeAndTodayUnCollect(requestCollectDtoList, nowDate);
                        log.info("uncollectMoneyList : "+ uncollectMoneyList);
                        int beforeUncollectMoney = 0;
                        int todayUncollectMoney = 0;
                        if(requestCollectDtoList.size() != 0){
                            beforeUncollectMoney = uncollectMoneyList.get(0);
                            todayUncollectMoney = uncollectMoneyList.get(1);
                            data.put("beforeUncollectMoney",beforeUncollectMoney);
                            data.put("todayUncollectMoney",todayUncollectMoney);
                        }else{
                            data.put("beforeUncollectMoney",0);
                            data.put("todayUncollectMoney",0);
                        }
                        log.info("전일미수금액 : "+ beforeUncollectMoney);
                        log.info("당일미수금액 : "+ todayUncollectMoney);

                        // 적립금을 보낸다. 만약 적립금을 사용하면 사용금액의 따른 적립금을 뺀 가격을 보낸다.
                        Integer resultSaveMoney = findBySaveMoney(optionalCustomer.get());
                        data.put("saveMoney",resultSaveMoney);
                        log.info("결제후 적립금액 : "+ resultSaveMoney);

                        // 옆에 결제내역에서 보여줄 데이터 전송
                        data.put("paymentEtcDtos",paymentEtcDtos);

                    }else{
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP019.getCode(), ResponseErrorCode.TP019.getDesc(), "문자", "다시 시도해주세요."));
                    }

                }

            }
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 결제 Save : 마스터테이블업데이트 및 결제정보 저장
    @Transactional(rollbackFor = SQLException.class)
    public Payment requestAndPaymentSave(Request request, List<Payment> paymentList, SaveMoney saveMoney){
        try{
            log.info("결제성공");
            requestRepository.save(request);
            paymentRepository.saveAll(paymentList);
            if(saveMoney!=null){
                saveMoneyRepository.save(saveMoney);
            }
            return paymentList.get(0);
        }catch (Exception e){
            log.info("에러발생 트랜젝션실행 : "+e);
            return null;
        }
    }


    public List<RequestUnCollectDto> findByUnCollectList(List<Long> customerIdList, String nowDate) {
        return requestRepositoryCustom.findByUnCollectList(customerIdList, nowDate);
    }

    public List<SaveMoneyListDto> findBySaveMoneyList(List<Long> customerIdList, String fsType) {
        return saveMoneyRepositoryCustom.findBySaveMoneyList(customerIdList, fsType);
    }

    // 고객정보 조회용 적립금, 미수금 호출 함수
    public List<HashMap<String,Object>> findByUnCollectAndSaveMoney(List<HashMap<String, Object>> customerListData, List<Long> customerIdList){

        log.info("미수금 리스트를 받아옵니다.");
        List<RequestUnCollectDto> requestUnCollectDtoList = findByUnCollectList(customerIdList, nowDate);
        for (HashMap<String, Object> listDatum : customerListData) {
            for (int j = 0; j < requestUnCollectDtoList.size(); j++) {
                if (listDatum.get("bcId").equals(requestUnCollectDtoList.get(j).getBcId())) {
                    listDatum.put("beforeUncollectMoney", requestUnCollectDtoList.get(j).getUnCollect());
                    requestUnCollectDtoList.remove(j);
                    break;
                }
            }
        }

        log.info("적립금 리스트를 받아옵니다.");
        List<SaveMoneyListDto> saveMoneyListDtoListType1 = findBySaveMoneyList(customerIdList, "1");
        List<SaveMoneyListDto> saveMoneyListDtoListType2 = findBySaveMoneyList(customerIdList, "2");
        int plusSaveMoney;
        int minusSaveMoney;
        int saveMoney;
        for (HashMap<String, Object> customerListDatum : customerListData) {
            plusSaveMoney = 0;
            minusSaveMoney = 0;
            for (SaveMoneyListDto saveMoneyListDto : saveMoneyListDtoListType1) {
                if (customerListDatum.get("bcId").equals(saveMoneyListDto.getBcId())) {
                    plusSaveMoney = plusSaveMoney + saveMoneyListDto.getFsAmt();
                    for (int x = 0; x < saveMoneyListDtoListType2.size(); x++) {
                        if (saveMoneyListDto.getBcId().equals(saveMoneyListDtoListType2.get(x).getBcId())) {
                            minusSaveMoney = minusSaveMoney + saveMoneyListDtoListType2.get(x).getFsAmt();
                            saveMoneyListDtoListType2.remove(x);
                            break;
                        }
                    }
                }
            }
            saveMoney = plusSaveMoney - minusSaveMoney;
            customerListDatum.put("saveMoney", saveMoney);
        }

        return customerListData;
    }

    // 접속완료 또는 결제완료 후 전일미수금과, 당일 미수금 호출
    public List<Integer> findByBeforeAndTodayUnCollect(List<RequestCollectDto> requestCollectDtoList, String nowDate){
        List<Integer> uncollectMoneyList = new ArrayList<>();
        int todayTotalAmount = 0;
        int todayPayAmount = 0;
        int beforeTotalAmount = 0;
        int beforePayAmount = 0;
        int beforeUncollectMoney = 0;
        int todayUncollectMoney = 0;
        if(requestCollectDtoList.size() != 0) {
            for (RequestCollectDto requestCollectDto : requestCollectDtoList) {
                if (!requestCollectDto.getFrYyyymmdd().equals(nowDate)) {
                    beforeTotalAmount = beforeTotalAmount + requestCollectDto.getFrTotalAmount();
                    beforePayAmount = beforePayAmount + requestCollectDto.getFrPayAmount();
                } else {
                    todayTotalAmount = todayTotalAmount + requestCollectDto.getFrTotalAmount();
                    todayPayAmount = todayPayAmount + requestCollectDto.getFrPayAmount();
                }
            }
            beforeUncollectMoney = beforeTotalAmount - beforePayAmount;
            todayUncollectMoney = todayTotalAmount - todayPayAmount;
            uncollectMoneyList.add(beforeUncollectMoney);
            uncollectMoneyList.add(todayUncollectMoney);
        }else{
            uncollectMoneyList.add(beforeUncollectMoney);
            uncollectMoneyList.add(todayUncollectMoney);
        }
        return uncollectMoneyList;
    }

    // 가맹점코드로 조회하여 해당 가맹점이 한번이라도 접수나 임시저장을 했는지 조회하는 함수
    public List<RequestSearchDto> findByRequestFrCode(String frCode) {
        return requestRepositoryCustom.findByRequestFrCode(frCode);
    }














}






















