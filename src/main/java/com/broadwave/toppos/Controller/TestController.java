package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    // 가상키보드 테스트 페이지
    @RequestMapping("keyboard")
    public String keyboard(){
        return "test/keyboard";
    }

    // 그리드 테스트 페이지
    @RequestMapping("grid")
    public String grid(){
        return "test/gridtest";
    }

    // 낙원님테스트페이지
    @RequestMapping("snwgrid")
    public String snwgrid(){
        return "test/snwgrid";
    }

    // 결제테스트페이지
    @RequestMapping("paymenttest")
    public String paymenttest(){
        return "test/paymenttest";
    }

    // 지겸 테스트페이지
    @RequestMapping("jktest")
    public String jktest(){
        return "test/jktest";
    }
    
    // 지겸 테스트페이지2
    @RequestMapping("jktest2")
    public String jktest2(){
        return "test/jktest2";
    }

    // 민규 테스트페이지
    @RequestMapping("phototest")
    public String phototest(){
        return "test/phototest";
    }

}
