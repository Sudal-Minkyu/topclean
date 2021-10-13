package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Minkyu
 * Date : 2021-10-13
 * Time :
 * Remark : Toppos Account컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("accountlist")
    public String account_List(HttpServletRequest request){
        return "admin/accountlist";
    }

}
