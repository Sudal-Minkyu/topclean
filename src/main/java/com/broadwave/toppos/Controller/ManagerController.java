package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-11-05
 * Time :
 * Remark : Toppos 지사 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/manager")
public class ManagerController {

    // 지사 & 지사장
    @RequestMapping("")
    public String head_index(){
        return "/index/managerindex";
    }

    // 영업일관리 페이지
    @RequestMapping("businessday")
    public String businessday(){
        return "/manager/businessday";
    }

}
