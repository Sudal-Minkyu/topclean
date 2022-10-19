package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-10-21
 * Time :
 * Remark : Toppos Error컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/error")
public class ErrorController {

    // 권한없는 페이지
    @RequestMapping("403")
    public String error1(){
        log.info("권한없는 페이지 입니다");
        return "error/403";
    }

    // 존재하지 않은 페이지
    @RequestMapping("401")
    public String error2(){
        log.info("존재하지 않은 페이지 입니다");
        return "error/403";
    }

}
