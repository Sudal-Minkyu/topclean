package com.broadwave.toppos.Head;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountRepository;
import com.broadwave.toppos.Account.AcountDtos.AccountListDto;
import com.broadwave.toppos.Account.AcountDtos.AccountRole;
import com.broadwave.toppos.Head.Branch.Branch;
import com.broadwave.toppos.Head.Branch.BranchRepository;
import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Minkyu
 * Date :
 * Remark : 본사 서비스 테스트코드
 */
@Slf4j
@DisplayName("╯°□°）╯본사 서비스 실행")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class HeadServiceUnitTest {

    // 지사코드 : tc, 가맹점코드 : tct

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private FranchiseRepository franchRepository;

    @DisplayName("1. 테스트 지사 생성")
    @Order(1)
    @Test
    public void branchSetUpA() {

        String brCode = "tc"; // 지사코드
        String brName = "테스트코드_지사"; // 지사명

        // 테스트 "tc" 지사코드 있는지 확인
        // given
        Optional<Branch> optionalBranch = branchRepository.findByBrCode(brCode);

        if(!optionalBranch.isPresent()){
            // 테스트 지사생성
            // when
            Branch branch = new Branch();
            branch.setBrCode(brCode);
            branch.setBrName(brName);
            branch.setBrTelNo("01020450716");
            branch.setBrContractDt("20220101");
            branch.setBrContractFromDt("20220101");
            branch.setBrContractToDt("20990101");
            branch.setBrContractState("02");
            branch.setBrRemark("테스트 코드 지사입니다.");
            branch.setBrCarculateRateBr(20.0);
            branch.setBrCarculateRateFr(80.0);
            branch.setBrRoyaltyRateBr(60.0);
            branch.setBrRoyaltyRateFr(60.0);
            branch.setInsert_id("system");
            branch.setInsertDateTime(LocalDateTime.now());
            Branch branchSave = branchRepository.save(branch);

            // than
            assertThat("테스트코드_지사", is(branchSave.getBrName()));
        }else{
            log.info("해당 지사가 존재함.");
        }
    }

    @DisplayName("2. 테스트 가맹점 생성")
    @Order(2)
    @Test
    public void franchSetUpB(){

        String frCode = "tct"; // 가맹점코드
        String frName = "테스트코드_가맹점"; // 가맹점명

        // 테스트 "tct" 가맹점코드 있는지 확인
        // given
        Optional<Franchise> optionalFranchise = franchRepository.findByFrCode(frCode);

        if(!optionalFranchise.isPresent()){
            // 테스트 지사생성
            // when
            Franchise franchise = new Franchise();
            franchise.setFrCode(frCode);
            franchise.setFrName(frName);
            franchise.setFrRefCode("testCode_fr");
            franchise.setFrTagNo("ttt");
            franchise.setFrTagType("3");
            franchise.setFrLastTagno("ttt0000");
            franchise.setFrContractDt("20220101");
            franchise.setFrContractFromDt("20220101");
            franchise.setFrContractToDt("20990101");
            franchise.setFrContractState("02");
            franchise.setFrRemark("테스트 코드 가맹점입니다.");
            franchise.setFrPriceGrade("E");
            franchise.setFrBusinessNo("123456789");
            franchise.setFrRpreName("시스템_테스트가맹점");
            franchise.setFrTelNo("01020450716");
            franchise.setFrEstimateDuration(2);
            franchise.setFrUrgentDayYn("Y");
            franchise.setInsert_id("system");
            franchise.setInsertDateTime(LocalDateTime.now());
            Franchise franchiseSave = franchRepository.save(franchise);

            // than
            assertThat("테스트코드_가맹점", is(franchiseSave.getFrName()));
        }else{
            log.info("해당 가맹점이 존재함.");
        }
    }

    @DisplayName("3. 테스트 지사계정 생성")
    @Order(3)
    @Test
    public void managerAccountSetUpC(){

        String testManager = "test_manager";
        Optional<Account> optionalManagerAccount = accountRepository.findByUserid(testManager);

        if(!optionalManagerAccount.isPresent()){
            // given
            Account managerAccount = new Account();
            managerAccount.setUserid(testManager);
            managerAccount.setPassword(passwordEncoder.encode("123789"));
            managerAccount.setRole(AccountRole.ROLE_MANAGER);
            managerAccount.setUsername("테스트코드_지사계정");
            managerAccount.setUsertel("01099990000");
            managerAccount.setUseremail("maanger_test@mail.com");
            managerAccount.setFrCode("not");
            managerAccount.setBrCode("tc");
            managerAccount.setUserremark("테스트코드용 지사계정 입니다.");
            managerAccount.setInsert_id("system");
            managerAccount.setInsertDateTime(LocalDateTime.now());
            // when
            Account accountSave = accountRepository.save(managerAccount);
            // then
            assertThat("test_manager", is(accountSave.getUserid()));
        }else{
            log.info("해당 지사 계정이 존재함.");
        }
    }

    @DisplayName("4. 테스트 가맹점계정 생성")
    @Order(4)
    @Test
    public void userAccountSetUpD(){

        String testUser = "test_user";
        Optional<Account> optionalUserAccount = accountRepository.findByUserid(testUser);

        if(!optionalUserAccount.isPresent()){
            // given
            Account userAccount = new Account();
            userAccount.setUserid(testUser);
            userAccount.setPassword(passwordEncoder.encode("123789"));
            userAccount.setRole(AccountRole.ROLE_USER);
            userAccount.setUsername("테스트코드_가맹점계정");
            userAccount.setUsertel("01099990000");
            userAccount.setUseremail("user_test@mail.com");
            userAccount.setFrCode("tct");
            userAccount.setBrCode("no");
            userAccount.setUserremark("테스트코드용 가맹점계정 입니다.");
            userAccount.setInsert_id("system");
            userAccount.setInsertDateTime(LocalDateTime.now());
            // when
            Account accountSave = accountRepository.save(userAccount);
            // then
            assertThat("test_user", is(accountSave.getUserid()));
        }else{
            log.info("해당 가맹점 계정이 존재함.");
        }
    }

    @DisplayName("5. 테스트계정 리스트 조회 테스트")
    @Order(5)
    @Test
    public void accountListTestE(){

        // 생성된 테스트 계정 리스트 조회
        // when
        List<AccountListDto> accountListDtos = accountRepository.findByAccountList("test_", "", null, "", "");
        log.info("accountListDtos.size() : "+accountListDtos.size());

        // then
        assertEquals(2, accountListDtos.size());

    }

    @DisplayName("6. 테스트계정 삭제 테스트")
    @Order(6)
    @Test
    public void accountDeleteTestF(){

        String testManager = "test_manager";
        String testUser = "test_user";

        // 삭제할 테스트 계정 조회
        // given
        Optional<Account> optionalManagerAccount = accountRepository.findByUserid(testManager);
        Optional<Account> optionalUserAccount = accountRepository.findByUserid(testUser);

        // when
        optionalManagerAccount.ifPresent(account -> accountRepository.delete(account));
        optionalUserAccount.ifPresent(account -> accountRepository.delete(account));

        // 삭제된 테스트 계정 조회
        Optional<Account> optionalDeleteManagerAccount = accountRepository.findByUserid(testManager);
        Optional<Account> optionalDeleteUserAccount = accountRepository.findByUserid(testUser);

        // then
        if(!optionalDeleteManagerAccount.isPresent()){
            log.info("지사 계정 삭제완료");
        }
        if(!optionalDeleteUserAccount.isPresent()){
            log.info("가맹점 계정 삭제완료");
        }

    }

}