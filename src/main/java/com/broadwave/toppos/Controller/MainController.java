package com.broadwave.toppos.Controller;

import com.broadwave.toppos.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping("/")
    public String index(HttpServletRequest request){
        String login_ip = CommonUtils.getIp(request);
        System.out.println("디벨롭테스트");
        return "index";
    }

}
