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
        return "index/managerindex";
    }

    // 출고관리
    // 지사출고
    @RequestMapping("release")
    public String release(){
        return "manager/releasemanagement/release";
    }
    // 지사출고취소
    @RequestMapping("releasecancel")
    public String releasecancel(){
        return "manager/releasemanagement/releasecancel";
    }
    // 가맹점강제출고
    @RequestMapping("forcerelease")
    public String forcerelease(){
        return "manager/releasemanagement/forcerelease";
    }

    // 반송/검품
    // 가맹점반송
    @RequestMapping("retturnit")
    public String retturnit(){
        return "manager/return/retturnit";
    }

    // 영업일관리 페이지
    @RequestMapping("businessday")
    public String businessday(){
        return "manager/businessday";
    }

}
