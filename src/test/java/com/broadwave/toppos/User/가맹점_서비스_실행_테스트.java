package com.broadwave.toppos.User;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountRepository;
import com.broadwave.toppos.Account.AcountDtos.AccountRole;
import com.broadwave.toppos.Head.Branch.Branch;
import com.broadwave.toppos.Head.Branch.BranchRepository;
import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerListDto;
import com.broadwave.toppos.User.UserService.ReceiptService;
import com.broadwave.toppos.User.UserService.UserService;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Minkyu
 * Date :
 * Remark : 본사 서비스 테스트코드
 */
@Slf4j
@DisplayName("╯°□°）╯가맹점 서비스 테스트 실행")
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
public class 가맹점_서비스_실행_테스트 {

    // 지사코드 : tc, 가맹점코드 : tct
    // 지사계정 : test_manager, 가맹점코드 : test_user

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiptService receiptService;

    @Test
    public void Test1_가맹점_고객생성_테스트() {

        String bcHp = "01020450716";

        Customer customer = new Customer();

        Optional<Customer> optionalCustomerByHp= userService.findByBcHp(bcHp,"tct");
        if(!optionalCustomerByHp.isPresent()){
            // given
            customer.setBcHp(bcHp);
            customer.setBcName("테스형");
            customer.setBcSex("1");
            customer.setBcAddress("테스트코드 주소");
            customer.setBcBirthday("19940716");
            customer.setBcAge("20");
            customer.setBcWeddingAnniversary("");
            customer.setBcValuation("01");
            customer.setBcMessageAgree("Y");
            customer.setBcAgreeType("2");
            customer.setBcRemark("테스트코드로 작성된 고객");

            customer.setFrCode("tct");
            customer.setBcQuitYn("N");
            customer.setInsert_id("testcode_system");
            customer.setInsertDateTime(LocalDateTime.now());
            customer.setBcMessageAgreeDt(LocalDateTime.now());

            // when
            Customer customerSave =  userService.customerSave(customer);

            // then
            assertEquals(bcHp, customerSave.getBcHp());

        }else{
            log.info("해당 고객이 존재함.");
        }
    }

    @Test
    public void Test2_고객_리스트_테스트() {

        // when
        List<CustomerListDto> customerListDtos = userService.findByCustomerList("tct", "", "");
        log.info("customerListDtos.size() : "+customerListDtos.size());

        // then
        assertEquals(1, customerListDtos.size());
    }

    @Test
    public void Test3_임시저장_세탁접수_테스트() {



    }

    @Test
    public void Test4_세탁임시접수_테스트() {



    }










}