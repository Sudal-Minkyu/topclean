package com.broadwave.toppos.User.ReuqestMoney.UserService;

import com.broadwave.toppos.Account.AccountRepositoryCustom;
import com.broadwave.toppos.User.Customer.*;
import com.broadwave.toppos.User.UserIndexDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    private final AccountRepositoryCustom accountRepositoryCustom;

    @Autowired
    public UserService(CustomerRepository customerRepository, CustomerRepositoryCustom customerRepositoryCustom, AccountRepositoryCustom accountRepositoryCustom){
        this.customerRepository = customerRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.accountRepositoryCustom = accountRepositoryCustom;
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

}
