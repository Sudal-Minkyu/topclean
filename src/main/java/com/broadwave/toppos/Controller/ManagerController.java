package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-10-26
 * Time :
 * Remark : Toppos 지사장 또는 지사일반 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/manager")
public class ManagerController {

    // 지사장&지사일반 메인페이지
    @RequestMapping("")
    public String manager_index(){
        return "/index/managerindex";
    }


}
