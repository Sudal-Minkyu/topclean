package com.broadwave.toppos.User;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.Customer.CustomerRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    @Autowired
    public UserService(CustomerRepository customerRepository, CustomerRepositoryCustom customerRepositoryCustom){
        this.customerRepository = customerRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
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

}
