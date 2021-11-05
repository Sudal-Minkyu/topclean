package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-11-05
 * Time :
 * Remark : Toppos 본사 또는 회계관리 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/head")
public class HeadController {

    // 본사일반&회계관리 메인페이지
    @RequestMapping("")
    public String head_index(){
        return "/index/headindex";
    }

    // 본사일반 사용자등록 페이지
    @RequestMapping("accountreg")
    public String accountreg(){
        return "/head/accountreg";
    }

    // 본사일반 계약관리 페이지
    @RequestMapping("contract")
    public String contract(){
        return "/head/contract";
    }

}
