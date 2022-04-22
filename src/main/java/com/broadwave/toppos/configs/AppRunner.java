package com.broadwave.toppos.configs;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AcountDtos.AccountRole;
import com.broadwave.toppos.Account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 최초 유저 생성
 */
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //사용자저장
        Account account1 = Account.builder()
                .userid("admin")
                .username("관리자")
                .password("123789")
                .useremail("admin@mail.com")
                .frCode("not")
                .brCode("no")
                .insertDateTime(LocalDateTime.now())
                .insert_id("system")
                .role(AccountRole.ROLE_ADMIN)
                .build();
        if(!accountService.findByUserid(account1.getUserid()).isPresent()){
            accountService.save(account1);
        }

    }

}
