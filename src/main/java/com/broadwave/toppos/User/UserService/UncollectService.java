package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.CustomerRepositoryCustom;
import com.broadwave.toppos.User.Customer.CustomerUncollectListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.PhotoRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final UserService userService;
    private final TokenProvider tokenProvider;

    private final AWSS3Service awss3Service;
    private final ReceiptService receiptService;
    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryCustom paymentRepositoryCustom;
    private final PhotoRepository photoRepository;
    private final InspeotRepository inspeotRepository;
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    private final SaveMoneyRepository saveMoneyRepository;

    private final CustomerRepositoryCustom customerRepositoryCustom;

    @Autowired
    public UncollectService(ModelMapper modelMapper, TokenProvider tokenProvider, UserService userService, AWSS3Service awss3Service, PhotoRepository photoRepository, ReceiptService receiptService,
                            PaymentRepository paymentRepository, PaymentRepositoryCustom paymentRepositoryCustom, InspeotRepository inspeotRepository,
                            RequestRepository requestRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom, InspeotRepositoryCustom inspeotRepositoryCustom,
                            RequestDetailRepository requestDetailRepository, SaveMoneyRepository saveMoneyRepository, CustomerRepositoryCustom customerRepositoryCustom){
        this.modelMapper = modelMapper;
        this.inspeotRepository = inspeotRepository;
        this.awss3Service = awss3Service;
        this.receiptService = receiptService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.photoRepository = photoRepository;
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.paymentRepository = paymentRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
        this.paymentRepositoryCustom = paymentRepositoryCustom;
        this.saveMoneyRepository = saveMoneyRepository;
    }

    // 미수관리페이지 - 고객검색 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseUncollectCustomerList(String searchType, String searchString, HttpServletRequest request) {
        log.info("uncollectCustomerList 호출");

        log.info("searchType : "+searchType);
        log.info("searchString : "+searchString);

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
        List<CustomerUncollectListDto> customerListDtos = customerRepositoryCustom.findByCustomerUncollectList(frCode, searchType, searchString);
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








        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
