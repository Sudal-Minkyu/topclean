package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-10-13
 * Time :
 * Remark : Toppos 관리자 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    // 관리자 페이지
    @RequestMapping("")
    public String admin_index(){
        log.info("관리자 입니다");
        return "index/adminindex";
    }

    @RequestMapping("accountlist")
    public String account_List(){
        return "admin/accountlist";
    }

}
