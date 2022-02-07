package com.broadwave.toppos.user;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark :
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

//    @Autowired
//    private CustomerRepository customerRepository;
//
////    private final UserLoginLogRepository userLoginLogRepository;
////    private final GroupSortRepository groupSortRepository;
////    private final AddprocessRepository addprocessRepository;
////
////    private final CustomerRepositoryCustom customerRepositoryCustom;
////    private final AccountRepositoryCustom accountRepositoryCustom;
////    private final GroupSortRepositoryCustom groupSortRepositoryCustom;
////    private final AddprocessRepositoryCustom addProcessRepositoryCustom;
////
////    @Autowired
////    public UserServiceTest(CustomerRepository customerRepository, UserLoginLogRepository userLoginLogRepository,
////                       CustomerRepositoryCustom customerRepositoryCustom, AccountRepositoryCustom accountRepositoryCustom,
////                       GroupSortRepository groupSortRepository, GroupSortRepositoryCustom groupSortRepositoryCustom,
////                       AddprocessRepository addprocessRepository, AddprocessRepositoryCustom addProcessRepositoryCustom){
////        this.customerRepository = customerRepository;
////        this.userLoginLogRepository = userLoginLogRepository;
////        this.customerRepositoryCustom = customerRepositoryCustom;
////        this.accountRepositoryCustom = accountRepositoryCustom;
////        this.groupSortRepository = groupSortRepository;
////        this.groupSortRepositoryCustom = groupSortRepositoryCustom;
////        this.addprocessRepository = addprocessRepository;
////        this.addProcessRepositoryCustom = addProcessRepositoryCustom;
////    }
//
//    @Test
//    public void customerTest(){
//        Customer customer1 = new Customer();
//        customer1.setFrCode("999");
//        customer1.setBcHp("01011112222");
//        customer1.setBcName("테스트이름");
//        customer1.setBcSex("0");
//        customer1.setBcAddress("테스트주소");
//        customer1.setBcBirthday(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
//        customer1.setBcAge("10");
//        customer1.setBcGrade("03");
//        customer1.setBcValuation("3");
//        customer1.setBcMessageAgree("Y");
//        customer1.setBcMessageAgreeDt(LocalDateTime.now());
//        customer1.setBcAgreeType("2");
//        customer1.setBcSignImage(null);
//        customer1.setBcRemark("01011112222");
//        customer1.setBcQuitYn("N");
//        customer1.setBcQuitDate(null);
//        customer1.setBcLastRequestDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
//        customer1.setInsert_id("system");
//        customer1.setInsertDateTime(LocalDateTime.now());
//        customer1.setModify_id(null);
//        customer1.setModifyDateTime(null);
//        Customer customer = customerSave(customer1);
//        log.info("테스트 고객명 : "+customer.getBcName());
//
//        // 고객 고유 ID값으로 고객 조회 테스트
//        findByBcId(customer.getBcId());
//
//    }
//
//    // 고객등록 테스트
//    @Test
//    public Customer customerSave(Customer customer){
//        customerRepository.save(customer);
//        return customer;
//    }
//
////    // 핸드폰 번호로 고객 조회 테스트
////    @Test
////    public Optional<Customer> findByBcHp(String bcHp) {
////        return customerRepository.findByBcHp(bcHp);
////    }
//
//    // 고유 ID값으로 고객 조회 테스트
//    @Test
//    public void findByBcId(Long bcId) {
//        Optional<Customer> optionalCustomer = customerRepository.findByBcId(bcId);
//        if(optionalCustomer.isPresent()){
//            log.info("삭제할 테스트 고객명 : "+optionalCustomer.get().getBcName());
//            customerRepository.delete(optionalCustomer.get());
//        }else{
//            log.info("테스트고객 생성실패");
//        }
//    }

}
