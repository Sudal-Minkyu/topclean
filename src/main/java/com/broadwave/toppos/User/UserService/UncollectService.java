package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerUncollectListDto;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.Customer.CustomerRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.Payment;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentUncollectSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailUncollectDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestCustomerUnCollectDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-01-19
 * Time :
 * Remark : Toppos 미수관리 서비스
 */
@Slf4j
@Service
public class UncollectService {

    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    private final ReceiptService receiptService;
    private final PaymentRepository paymentRepository;
    private final FranchiseRepository franchiseRepository;
    private final RequestRepository requestRepository;
    private final RequestRepositoryCustom requestRepositoryCustom;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    @Autowired
    public UncollectService(ModelMapper modelMapper, TokenProvider tokenProvider, ReceiptService receiptService,
                            RequestRepositoryCustom requestRepositoryCustom, FranchiseRepository franchiseRepository, CustomerRepository customerRepository,
                            PaymentRepository paymentRepository, RequestRepository requestRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom,
                            CustomerRepositoryCustom customerRepositoryCustom){
        this.modelMapper = modelMapper;
        this.requestRepositoryCustom = requestRepositoryCustom;
        this.customerRepository = customerRepository;
        this.franchiseRepository = franchiseRepository;
        this.receiptService = receiptService;
        this.tokenProvider = tokenProvider;
        this.requestRepository = requestRepository;
        this.paymentRepository = paymentRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    // 미수관리페이지 - 고객검색 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseUncollectCustomerList(String searchType, String searchText, HttpServletRequest request) {
        log.info("uncollectCustomerList 호출");

        log.info("searchType : "+searchType);
        log.info("searchText : "+searchText);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<Long> customerIdList = new ArrayList<>();
        List<HashMap<String,Object>> customerListData = new ArrayList<>();
        HashMap<String,Object> customerListInfo;
        List<CustomerUncollectListDto> customerListDtos = customerRepositoryCustom.findByCustomerUncollectList(frCode, searchType, searchText);
//        log.info("customerListDtos : "+customerListDtos);
        for (CustomerUncollectListDto customerUncollectListDto: customerListDtos) {
            customerListInfo = new HashMap<>();
            customerIdList.add(customerUncollectListDto.getBcId());
            customerListInfo.put("bcId", customerUncollectListDto.getBcId());
            customerListInfo.put("bcName", customerUncollectListDto.getBcName());
            customerListInfo.put("bcHp", customerUncollectListDto.getBcHp());
            customerListInfo.put("bcAddress", customerUncollectListDto.getBcAddress());
            customerListInfo.put("uncollectMoney", 0);
            customerListInfo.put("saveMoney", 0);
            customerListData.add(customerListInfo);
        }
        if(customerListData.size() != 0) {
            List<HashMap<String,Object>> customerListDataGet = receiptService.findByUnCollectAndSaveMoney(customerListData, customerIdList, "1");
            data.put("gridListData",customerListDataGet);
        }else{
            data.put("gridListData",customerListData);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 미수관리페이지 - 해당고객의 세탁접수 미수금 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseUncollectRequestList(Long bcId, HttpServletRequest request) {
        log.info("franchiseUncollectRequestList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<RequestCustomerUnCollectDto> requestCustomerUnCollectDtos = requestRepositoryCustom.findByRequestCustomerUnCollectList(bcId, frCode);
        List<HashMap<String,Object>> uncollectListData = new ArrayList<>();
        HashMap<String,Object> uncollectListInfo;
        for (RequestCustomerUnCollectDto requestCustomerUnCollectDto: requestCustomerUnCollectDtos) {
            uncollectListInfo = new HashMap<>();
            uncollectListInfo.put("frId", requestCustomerUnCollectDto.getFrId());
            uncollectListInfo.put("frYyyymmdd", requestCustomerUnCollectDto.getFrYyyymmdd());
            uncollectListInfo.put("requestDetailCount", requestCustomerUnCollectDto.getRequestDetailCount());
            uncollectListInfo.put("bgName", requestCustomerUnCollectDto.getBgName());
            uncollectListInfo.put("bsName", requestCustomerUnCollectDto.getBsName());
            uncollectListInfo.put("biName", requestCustomerUnCollectDto.getBiName());
            uncollectListInfo.put("frTotalAmount", requestCustomerUnCollectDto.getFrTotalAmount());
            uncollectListInfo.put("frPayAmount", requestCustomerUnCollectDto.getFrPayAmount());
            uncollectListInfo.put("uncollectMoney", requestCustomerUnCollectDto.getFrTotalAmount()-requestCustomerUnCollectDto.getFrPayAmount());
            uncollectListData.add(uncollectListInfo);
        }

        data.put("gridListData",uncollectListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 미수관리페이지 - 해당고객의 접수세부테이블 미수금 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseUncollectRequestDetailList(Long frId, HttpServletRequest request) {
        log.info("franchiseUncollectRequestDetailList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("frId : "+frId);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<RequestDetailUncollectDto> requestDetailUncollectDtos = requestDetailRepositoryCustom.findByRequestDetailUncollectList(frCode, frId);
        data.put("gridListData",requestDetailUncollectDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 미수관리페이지 - 선택한 미수금 결제할 접수 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseUncollectPayRequestList(List<Long> frIdList, Long bcId, HttpServletRequest request) {
        log.info("franchiseUncollectPayRequestList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("미수금결제할 고객ID : "+bcId);
        log.info("결제 리스트 : "+frIdList);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
//        log.info("소속된 지사 코드 : "+frbrCode);

        Optional<Customer> optionalCustomer = customerRepository.findByBcId(bcId);
        Optional<Franchise> optionalFranchise = franchiseRepository.findByFrCode(frCode);
        if(!optionalCustomer.isPresent() || !optionalFranchise.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), ResponseErrorCode.TP022.getDesc(),null, null ));
        }else{
            List<HashMap<String,Object>> payInfoData = new ArrayList<>();
            HashMap<String,Object> payInfo = new HashMap<>();
            payInfo.put("frCode", optionalFranchise.get().getFrCode());
            payInfo.put("frName", optionalFranchise.get().getFrName());
            payInfo.put("frBusinessNo", optionalFranchise.get().getFrBusinessNo());
            payInfo.put("frRpreName", optionalFranchise.get().getFrRpreName());
            payInfo.put("frTelNo", optionalFranchise.get().getFrTelNo());
            payInfo.put("bcName", optionalCustomer.get().getBcName());
            payInfo.put("bcHp", optionalCustomer.get().getBcHp());
            payInfoData.add(payInfo);

            List<RequestCustomerUnCollectDto> requestCustomerUnCollectDtos = requestRepositoryCustom.findByRequestUnCollectPayList(frIdList, frCode);
            List<HashMap<String,Object>> uncollectListData = new ArrayList<>();
            HashMap<String,Object> uncollectListInfo;
            for (RequestCustomerUnCollectDto requestCustomerUnCollectDto: requestCustomerUnCollectDtos) {
                uncollectListInfo = new HashMap<>();
                uncollectListInfo.put("frId", requestCustomerUnCollectDto.getFrId());
                uncollectListInfo.put("frYyyymmdd", requestCustomerUnCollectDto.getFrYyyymmdd());
                uncollectListInfo.put("requestDetailCount", requestCustomerUnCollectDto.getRequestDetailCount());
                uncollectListInfo.put("bgName", requestCustomerUnCollectDto.getBgName());
                uncollectListInfo.put("bsName", requestCustomerUnCollectDto.getBsName());
                uncollectListInfo.put("biName", requestCustomerUnCollectDto.getBiName());
                uncollectListInfo.put("frTotalAmount", requestCustomerUnCollectDto.getFrTotalAmount());
                uncollectListInfo.put("uncollectMoney", requestCustomerUnCollectDto.getFrTotalAmount()-requestCustomerUnCollectDto.getFrPayAmount());
                uncollectListData.add(uncollectListInfo);
            }

            data.put("gridListData",uncollectListData);
            data.put("payInfoData",payInfoData);

        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 미수관리페이지 - 선택한 미수금 접수테이블 결제
    public ResponseEntity<Map<String, Object>> franchiseUncollectPay(PaymentUncollectSet paymentUncollectMapperDto, HttpServletRequest request) {
        log.info("franchiseUncollectPay 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
//        log.info("소속된 지사 코드 : "+frbrCode);

        int realAmt = paymentUncollectMapperDto.getData().getFpRealAmt();
//        log.info("현재 미수금 : "+realAmt);

        List<Payment> paymentList = new ArrayList<>();
        List<Request> requestList = requestRepository.findByRequestList(paymentUncollectMapperDto.getFrIdList());
        for(Request requestData : requestList){
            Payment payment = modelMapper.map(paymentUncollectMapperDto.getData(),Payment.class);
            // 결제 기본데이터
            payment.setFpInType("02"); // 미수결제이기때문에 인타입 "02"
            payment.setFpCollectAmt(0);
            payment.setFpCancelYn("N");
            payment.setFpSavedMoneyYn("N");
            payment.setInsert_id(login_id);
            payment.setInsert_date(LocalDateTime.now());

            payment.setFrId(requestData);
            payment.setBcId(requestData.getBcId());
            int uncollectMoney = requestData.getFrTotalAmount()-requestData.getFrPayAmount();
//                log.info("결제금액 FpAmt : "+uncollectMoney);
            payment.setFpAmt(uncollectMoney); // 미수금 확인후 계산

            // 마스터테이블 업데이트
            requestData.setFrPayAmount(uncollectMoney+requestData.getFrPayAmount());
            requestData.setFrUncollectYn("N");
            requestData.setModify_id(login_id);
            requestData.setModify_date(LocalDateTime.now());

            realAmt = realAmt - uncollectMoney;

            paymentList.add(payment);
        }
        if(realAmt == 0){
//            log.info("저장시작");
            requestRepository.saveAll(requestList);
            paymentRepository.saveAll(paymentList);
        }else{
            return ResponseEntity.ok(res.fail("문자", realAmt+"원이 서버상의 미수금액과 일치하지 않습니다.", "문자", "관리자에게 문의해주시길 바랍니다."));
        }

        return ResponseEntity.ok(res.success());
    }




}
