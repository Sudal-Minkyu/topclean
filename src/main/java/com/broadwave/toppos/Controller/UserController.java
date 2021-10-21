package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-10-21
 * Time :
 * Remark : Toppos 가맹점주 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    // 가맹점주 메인페이지
    @RequestMapping("")
    public String userindex(@RequestHeader(value="Authorization") String accept){
        log.info("accept : "+accept);
        log.info("기맹점주 입니다");
        return "/index/userindex";
    }


}
