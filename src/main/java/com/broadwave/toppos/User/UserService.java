package com.broadwave.toppos.User;

import com.broadwave.toppos.Account.AccountRepositoryCustom;
import com.broadwave.toppos.User.Customer.*;
import com.broadwave.toppos.User.GroupSort.GroupSortDto;
import com.broadwave.toppos.User.GroupSort.GroupSortRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    private final AccountRepositoryCustom accountRepositoryCustom;
    private final GroupSortRepositoryCustom groupSortRepositoryCustom;
    @Autowired
    public UserService(CustomerRepository customerRepository, CustomerRepositoryCustom customerRepositoryCustom,
                       AccountRepositoryCustom accountRepositoryCustom, GroupSortRepositoryCustom groupSortRepositoryCustom){
        this.customerRepository = customerRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.accountRepositoryCustom = accountRepositoryCustom;
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

    public UserIndexDto findByUserInfo(String userid, String frCode) {
        return accountRepositoryCustom.findByUserInfo(userid, frCode);
    }

    public List<GroupSortDto> findByGroupSortList(String frCode) {
        return groupSortRepositoryCustom.findByGroupSortList(frCode);
    }
}
