package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.AccountRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Addprocess.Addprocess;
import com.broadwave.toppos.User.Addprocess.AddprocessDtos.AddprocessDto;
import com.broadwave.toppos.User.Addprocess.AddprocessRepositoryCustom;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerInfoDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerListDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerMessageListDto;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailAmtDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoney;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyRepository;
import com.broadwave.toppos.User.UserDtos.UserIndexDto;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLog;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLogRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-12-14
 * Time :
 * Remark : Toppos 가맹점 통합 서비스
 */
@Slf4j
@Service
public class UserService {
    private final TokenProvider tokenProvider;

    private final RequestDetailRepository requestDetailRepository;

    private final SaveMoneyRepository saveMoneyRepository;
    private final RequestRepository requestRepository;
    private final CustomerRepository customerRepository;
    private final UserLoginLogRepository userLoginLogRepository;

    private final AccountRepository accountRepository;
    private final AddprocessRepositoryCustom addProcessRepositoryCustom;

    @Autowired
    public UserService(TokenProvider tokenProvider, SaveMoneyRepository saveMoneyRepository, RequestRepository requestRepository, RequestDetailRepository requestDetailRepository,
                       CustomerRepository customerRepository, UserLoginLogRepository userLoginLogRepository,
                       AccountRepository accountRepository,
                       AddprocessRepositoryCustom addProcessRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.requestRepository = requestRepository;
        this.saveMoneyRepository = saveMoneyRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.customerRepository = customerRepository;
        this.userLoginLogRepository = userLoginLogRepository;
        this.accountRepository = accountRepository;
        this.addProcessRepositoryCustom = addProcessRepositoryCustom;
    }

    // 고객등록
    public Customer customerSave(Customer customer){
        customerRepository.save(customer);
        return customer;
    }

    // 핸드폰 번호로 고객 조회
    public Optional<Customer> findByBcHp(String bcHp, String frCode) {
        return customerRepository.findByBcHp(bcHp, frCode);
    }

    // 고유 ID값으로 고객 조회
    public Optional<Customer> findByBcId(Long bcId) {
        return customerRepository.findByBcId(bcId);
    }

    // 로그인한 가맹점의 대한 고객정보 조회
    public List<CustomerInfoDto> findByCustomerInfo(String frCode, String searchType, String searchString) {
        return customerRepository.findByCustomerInfo(frCode, searchType, searchString);
    }

    // 로그인한 가맹점의 고객리스트 호출
    public List<CustomerListDto> findByCustomerList(String frCode, String searchType, String searchString) {
        return customerRepository.findByCustomerList(frCode, searchType, searchString);
    }

    // 가맹점 메인페이지 전용 개인정보 호출
    public UserIndexDto findByUserInfo(String userid, String frCode) {
        return accountRepository.findByUserInfo(userid, frCode);
    }

    // 가맹점이 로그인하면 로그인기록을 남기는 서비스 : 하루에 최초 한번만 기록한다.
    public void userLoginLog(HttpServletRequest request){
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 소속된 가맹점 코드

        // 현재 날짜 받아오기
        LocalDateTime localDateTime = LocalDateTime.now();
        String blLoginDt = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Optional<UserLoginLog> optionalUserLoginLog = userLoginLogRepository.findByUserLoginLog(frCode,blLoginDt);
//        log.info("userLoginLog 함수실행 : 현재 로그인한 가맹점 : "+frCode+", 현재 날짜(yyyymmdd) : "+blLoginDt);
        if(!optionalUserLoginLog.isPresent()){
//            log.info("로그기록 합니다.");
            UserLoginLog userLoginLog = new UserLoginLog();
            userLoginLog.setFrCode(frCode);
            userLoginLog.setBlLoginDt(blLoginDt);
            userLoginLog.setInsert_id(login_id);
            userLoginLog.setInsertDateTime(localDateTime);
            userLoginLogRepository.save(userLoginLog);
        }
    }

    // 수선, 추가요금, 상용구 항목 리스트 데이터 호출
    public List<AddprocessDto> findByAddProcessDtoList(String frCode, String baType) {
        return addProcessRepositoryCustom.findByAddProcessDtoList(frCode, baType);
    }

    // 수선, 추가요금, 상용구 항목 리스트 데이터 호출
    public List<Addprocess> findByAddProcessList(String frCode, String baType) {
        return addProcessRepositoryCustom.findByAddProcessList(frCode, baType);
    }

    // 세부테이블 업데이트 후 마스터테이블 업데이트하는 공용함수 - 미수금 비교하여 처리 => frTotalAmount 와 frPayAmount 비교하여 frTotalAmount가 더 클 경우 frUncollectYn은 "Y"로 업데이트 쳐준다.
    public void requestDetailUpdateFromMasterUpdate(String frNo, String frCode) {
        log.info("requestDetailUpdateFromMasterUpdate 호출");

        log.info("frNo : "+frNo);
        //디테일의 fdTotAmt를 합산한 결과가 frTotalAmount가 되고 frTotalAmount와 가져온 frPayAmount의 데이터를 비교하여 업데이트를 한다.
        List<RequestDetailAmtDto> requestDetailAmtDtos =  requestDetailRepository.findByRequestDetailAmtList(frNo); // 세부테이블의 합계금액 리스트 호출
        int totalAmt = 0;
        int normalAmt = 0;
        for(RequestDetailAmtDto requestDetailAmtDto : requestDetailAmtDtos){
            normalAmt = normalAmt+requestDetailAmtDto.getFdNormalAmt();
            totalAmt = totalAmt+requestDetailAmtDto.getFdTotAmt();
        }

        // 마스터테이블 업데이트
        Optional<Request> optionalRequest = requestRepository.request(frNo,frCode);
        if(optionalRequest.isPresent()){
            optionalRequest.get().setFrNormalAmount(normalAmt);
            optionalRequest.get().setFrTotalAmount(totalAmt);
            optionalRequest.get().setFrUncollectYn("Y");
            requestRepository.save(optionalRequest.get());
        }
    }

    // 수선, 추가요금, 상용구 항목 리스트 데이터 호출
    public List<Addprocess> save(String frCode, String baType) {
        return addProcessRepositoryCustom.findByAddProcessList(frCode, baType);
    }

    // 적립금 저장
    public void saveMoneySave(SaveMoney saveMoney) {
        saveMoneyRepository.save(saveMoney);
    }

}
