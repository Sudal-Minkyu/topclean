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
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestCustomerUnCollectDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final RequestDetailRepository requestDetailRepository;

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    @Autowired
    public UncollectService(ModelMapper modelMapper, TokenProvider tokenProvider, ReceiptService receiptService,
                            RequestRepository requestRepository, FranchiseRepository franchiseRepository, CustomerRepository customerRepository,
                            PaymentRepository paymentRepository, RequestDetailRepository requestDetailRepository,
                            CustomerRepositoryCustom customerRepositoryCustom){
        this.modelMapper = modelMapper;
        this.requestRepository = requestRepository;
        this.customerRepository = customerRepository;
        this.franchiseRepository = franchiseRepository;
        this.receiptService = receiptService;
        this.tokenProvider = tokenProvider;
        this.paymentRepository = paymentRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
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

        List<RequestCustomerUnCollectDto> requestCustomerUnCollectDtos = requestRepository.findByRequestCustomerUnCollectList(bcId, frCode);
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

        List<RequestDetailUncollectDto> requestDetailUncollectDtos = requestDetailRepository.findByRequestDetailUncollectList(frCode, frId);
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

            List<RequestCustomerUnCollectDto> requestCustomerUnCollectDtos = requestRepository.findByRequestUnCollectPayList(frIdList, frCode);
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
    @Transactional
    public ResponseEntity<Map<String, Object>> franchiseUncollectPay(PaymentUncollectSet paymentUncollectMapperDto, HttpServletRequest request) {
        log.info("franchiseUncollectPay 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        int realAmt = paymentUncollectMapperDto.getData().getFpRealAmt();
        log.info("현재 결제할 전체 미수금 realAmt : "+realAmt);

        List<Request> requestList = requestRepository.findByRequestList(paymentUncollectMapperDto.getFrIdList());

        Payment payment = modelMapper.map(paymentUncollectMapperDto.getData(),Payment.class);
        if(requestList.size() != 0){
            int fpAmt = requestList.get(0).getFrTotalAmount()-requestList.get(0).getFrPayAmount();
            log.info("결제금액 FpAmt : "+fpAmt);

            int fpCollectAmt = realAmt - fpAmt;
            log.info("미수완납금액 fpCollectAmt : "+fpCollectAmt);

            // 결제 기본데이터
            payment.setFpInType("02"); // 미수결제이기때문에 인타입 "02"
            payment.setFpCancelYn("N");
            payment.setFpSavedMoneyYn("N");
            payment.setInsert_id(login_id);
            payment.setInsert_date(LocalDateTime.now());
            payment.setFpYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

            payment.setFrId(requestList.get(0));
            payment.setBcId(requestList.get(0).getBcId());
            payment.setFpCollectAmt(fpCollectAmt);

            payment.setFpAmt(fpAmt); // 미수금 확인후 계산

            int frPayAmt = fpAmt+requestList.get(0).getFrPayAmount();
            log.info("첫번째 미수 fr_pay_amount frPayAmt : "+frPayAmt);
            // 마스터테이블 업데이트
            requestList.get(0).setFrPayAmount(fpAmt+requestList.get(0).getFrPayAmount());
            if(frPayAmt == requestList.get(0).getFrTotalAmount()){
                requestList.get(0).setFrUncollectYn("N");
            }else{
                requestList.get(0).setFrUncollectYn("Y");
            }
            requestList.get(0).setModify_id(login_id);
            requestList.get(0).setModify_date(LocalDateTime.now());
//            log.info("payment : "+payment);

            log.info("저장시작");
            Payment savePayment = paymentRepository.save(payment);
            if(requestList.size() > 1){
                for(int i = 1; i<requestList.size(); i++){
                    requestList.get(i).setFrUncollectYn("N");
                    requestList.get(i).setFpId(savePayment);
                    requestList.get(i).setModify_id(login_id);
                    requestList.get(i).setModify_date(LocalDateTime.now());
                }
            }
            requestRepository.saveAll(requestList);
            data.put("frId",savePayment.getFrId());
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }




}
