package com.broadwave.toppos.Controller;

import com.broadwave.toppos.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping("/")
    public String index(HttpServletRequest request){
        String login_ip = CommonUtils.getIp(request);
        log.info("현재 사용자 아이피 : "+login_ip);
        return "index";
    }
    
    // grid test
    @RequestMapping("gridtest")
    public String gridtest(){
        return "gridtest";
    }

}
