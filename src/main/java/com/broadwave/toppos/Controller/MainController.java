package com.broadwave.toppos.Controller;

import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : Toppos 메인컨트롤러
 */
@Slf4j
@Controller
public class MainController {

    @Autowired
    AccountService accountService;

    // 시스템관리자 페이지
    @RequestMapping("/")
    public String index(HttpServletRequest request){

        String login_userid = CommonUtils.getCurrentuser(request);
        log.info("login_userid : "+login_userid);

//        Optional<Account> accountOptional = accountService.findByUserid(currentuserid);
//        if(accountOptional.isPresent()){
//            if(accountOptional.get().getRole().getDesc().equals("가맹점주")) {
//                log.info("가맹점주 입니다");
//                return "/index/userindex";
//            }else if(accountOptional.get().getRole().getDesc().equals("지사장") || accountOptional.get().getRole().getDesc().equals("지사일반")){
//                log.info("지사장 또는 지사일반 입니다");
//                return "/index/managerindex";
//            }else if(accountOptional.get().getRole().getDesc().equals("회계관리") || accountOptional.get().getRole().getDesc().equals("본사일반")){
//                log.info("회계관리 또는 본사일반 입니다");
//                return "/index/headindex";
//            }else{
//                log.info("시스템관라지 입니다");
                return "/index/index";
//            }
//        }else{
//            return "/error/403";
//        }

    }

    @RequestMapping("login")
    public String login(){
        return "login";
    }


}
