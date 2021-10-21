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
        return "/test/keyboard";
    }

    // 그리드 테스트 페이지
    @RequestMapping("grid")
    public String grid(){
        return "/test/gridtest";
    }

}
