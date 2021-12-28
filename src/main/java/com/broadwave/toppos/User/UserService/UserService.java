package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.AccountRepositoryCustom;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.*;
import com.broadwave.toppos.User.GroupSort.GroupSort;
import com.broadwave.toppos.User.GroupSort.GroupSortRepository;
import com.broadwave.toppos.User.GroupSort.GroupSortRepositoryCustom;
import com.broadwave.toppos.User.GroupSort.GroupSortUpdateDto;
import com.broadwave.toppos.User.UserIndexDto;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLog;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLogRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    private final CustomerRepository customerRepository;
    private final UserLoginLogRepository userLoginLogRepository;
    private final GroupSortRepository groupSortRepository;

    private final CustomerRepositoryCustom customerRepositoryCustom;
    private final AccountRepositoryCustom accountRepositoryCustom;
    private final GroupSortRepositoryCustom groupSortRepositoryCustom;

    @Autowired
    public UserService(TokenProvider tokenProvider,
                       CustomerRepository customerRepository, UserLoginLogRepository userLoginLogRepository,
                       CustomerRepositoryCustom customerRepositoryCustom, AccountRepositoryCustom accountRepositoryCustom,
                       GroupSortRepository groupSortRepository, GroupSortRepositoryCustom groupSortRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
        this.userLoginLogRepository = userLoginLogRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.accountRepositoryCustom = accountRepositoryCustom;
        this.groupSortRepository = groupSortRepository;
        this.groupSortRepositoryCustom = groupSortRepositoryCustom;
    }

    // 고객등록
    public Customer customerSave(Customer customer){
        customerRepository.save(customer);
        return customer;
    }

    // 핸드폰 번호로 고객 조회
    public Optional<Customer> findByBcHp(String bcHp) {
        return customerRepository.findByBcHp(bcHp);
    }

    // 고유 ID값으로 고객 조회
    public Optional<Customer> findByBcId(Long bcId) {
        return customerRepository.findByBcId(bcId);
    }

    // 로그인한 가맹점의 대한 고객정보 조회
    public List<CustomerInfoDto> findByCustomerInfo(String frCode, String searchType, String searchString) {
        return customerRepositoryCustom.findByCustomerInfo(frCode, searchType, searchString);
    }

    // 로그인한 가맹점의 고객리스트 호출
    public List<CustomerListDto> findByCustomerList(String frCode, String searchType, String searchString) {
        return customerRepositoryCustom.findByCustomerList(frCode, searchType, searchString);
    }

    // 가맹점 메인페이지 전용 개인정보 호출
    public UserIndexDto findByUserInfo(String userid, String frCode) {
        return accountRepositoryCustom.findByUserInfo(userid, frCode);
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

}
