package com.broadwave.toppos.configs;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 최초 유저 생성
 */
@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        //팀저장
//        Team team1 = Team.builder()
//                .teamcode("T00001")
//                .teamname("시스템관리")
//                .remark("최초생성")
//                .insertDateTime(LocalDateTime.now())
//                .insert_id("system")
//                .build();
//        if(!teamService.findByTeamcode(team1.getTeamcode()).isPresent()) {
//            teamService.tesmSave(team1);
//        }else{
//            team1 = teamService.findByTeamcode(team1.getTeamcode()).get();
//        }
//
//        //사용자저장
//        Account account1 = Account.builder()
//                .userid("admin")
//                .username("관리자")
//                .email("admin@mail.com")
//                .password("1234")
//                .insertDateTime(LocalDateTime.now())
//                .insert_id("system")
//                .role(AccountRole.ROLE_ADMIN)
//                .build();
//        account1.setTeam(team1);
//        if(!accountService.findByUserid(account1.getUserid()).isPresent()){
//            accountService.saveAccount(account1);
//        }
//
//        //게스트아이디저장
//        Account account2 = Account.builder()
//                .userid("guest")
//                .username("게스트")
//                .email("guest@mail.com")
//                .password("1234")
//                .insertDateTime(LocalDateTime.now())
//                .insert_id("system")
//                .role(AccountRole.ROLE_ADMIN)
//                .build();
//        account2.setTeam(team1);
//        if(!accountService.findByUserid(account2.getUserid()).isPresent()) {
//            accountService.saveAccount(account2);
//        }



    }

}
