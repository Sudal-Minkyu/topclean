package com.broadwave.toppos.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2021-10-21
 * Time :
 * Remark : Toppos 가맹점주 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    // 가맹점주전용 사인창
    @RequestMapping("sign")
    public String sign(){
        return "user/sign";
    }

    // 가맹점주 메인페이지
    @RequestMapping("")
    public String user_index(){
        return "index/userindex";
    }

    // 가맹점주 고객등록 페이지
    @RequestMapping("customerreg")
    public String customer_reg(){
        return "user/customer/customerreg";
    }

    // 가맹점주 고객리스트 페이지
    @RequestMapping("customerlist")
    public String customer_list(){
        return "user/customer/customerlist";
    }

    // 가맹점주 접수등록 페이지
    @RequestMapping("receiptreg")
    public String receiptreg(){
        return "user/receipt/receiptreg";
    }
    
    // 나의 정보 관리
    @RequestMapping("mypage")
    public String mypage(){
        return "user/mypage";
    }
    
    // 외부연계
    @RequestMapping("externallink")
    public String externallink(){
        return "user/externallink";
    }
    
    // 상품 순서 관리 - 상품 분류
    @RequestMapping("classification")
    public String classification(){
        return "user/classification";
    }
    
    // 통합조회
    @RequestMapping("integrate")
    public String integrate(){
        return "user/integrate";
    }
    
    // 검품이력 조회 및 메세지
    @RequestMapping("inspect")
    public String inspect(){
        return "user/inspect";
    }

    // 상품 순서 관리 - 상품 분류
    @RequestMapping("itemsort")
    public String itemsort(){
        return "user/receipt/itemsort";
    }
    
    // 수기마감(일마감)
    @RequestMapping("closed")
    public String closed(){
        return "user/closed";
    }

}
