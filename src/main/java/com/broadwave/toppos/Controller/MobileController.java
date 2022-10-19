package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2022-03-11
 * Time :
 * Remark : Toppos 모바일 페이지 /mobile/unAuth/** -> 인증없이 접근가능, /mobile/auth/** -> 인증있어야 접근가능
 */
@Slf4j
@Controller
@RequestMapping("/mobile")
public class MobileController {

    @Autowired
    public MobileController() {

    }

    // QR 모바일 마감페이지
    @RequestMapping("/unAuth/qrpickup")
    public String qrpickup(){
        return "mobile/unAuth/qrpickup";
    }

    // 모바일 고객용 접수증(영수증)
    @RequestMapping("/unAuth/receipt")
    public String receipt() {
        return "mobile/unAuth/receipt";
    }

    // 모바일 고객용 검품응답
    @RequestMapping("/unAuth/inspectresponse")
    public String inspectresponse() {
        return "mobile/unAuth/inspectresponse";
    }
}
