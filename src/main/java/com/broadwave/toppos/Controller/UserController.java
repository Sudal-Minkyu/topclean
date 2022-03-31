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
        return "user/customer/sign";
    }

    // 운동화 세탁 접수 동의서
    @RequestMapping("consent")
    public String consent(){
        return "user/receipt/consent";
    }
    // 운동화 세탁 접수 동의서 - 사인
    @RequestMapping("consentsign")
    public String consentsign(){
        return "user/receipt/consentsign";
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

    // 미납 조회
    @RequestMapping("unpaid")
    public String unpaid(){
        return "user/customer/unpaid";
    }

    // 가맹점주 접수등록 페이지
    @RequestMapping("receiptreg")
    public String receiptreg(){
        return "user/receipt/receiptreg";
    }

    // 고객 인도
    @RequestMapping("delivery")
    public String delivery(){
        return "user/receipt/delivery";
    }
    
    // 나의 정보 관리
    @RequestMapping("mypage")
    public String mypage(){
        return "user/setting/mypage";
    }
    
    // 외부연계
    @RequestMapping("externallink")
    public String externallink(){
        return "user/receipt/externallink";
    }
    
    // 통합조회
    @RequestMapping("integrate")
    public String integrate(){
        return "user/receipt/integrate";
    }
    
    // 검품이력 조회 및 메세지
    @RequestMapping("inspect")
    public String inspect(){
        return "user/receipt/inspect";
    }

    // 상품 순서 관리 - 상품 분류
    @RequestMapping("itemsort")
    public String itemsort(){
        return "user/setting/itemsort";
    }
    
    // 일일 영업일보
    @RequestMapping("dailystat")
    public String businessday(){
        return "user/manage/dailystat";
    }

    // 수기마감(일마감)
    @RequestMapping("closed")
    public String closed(){
        return "user/manage/closed";
    }
    
    // 입출고
    // 가맹점 입고
    @RequestMapping("franchisein")
    public String franchisein(){
        return "user/inandout/franchisein";
    }

    // 가맹점 입고취소
    @RequestMapping("franchiseincancel")
    public String franchiseincancel(){
        return "user/inandout/franchiseincancel";
    }
    
    // 가맹점 강제 입고
    @RequestMapping("force")
    public String force(){
        return "user/inandout/force";
    }

    // 지사 반송건 전송
    @RequestMapping("returnit")
    public String returnit(){
        return "user/inandout/returnit";
    }

    // 택분실
    @RequestMapping("taglost")
    public String taglost() {
        return "user/board/taglost";
    }

    // 게시판 리스트
    //공지사항
    @RequestMapping("noticelist")
    public String noticelist() {
        return "user/board/list";
    }

    @RequestMapping("noticeview")
    public String noticeview() {
        return "user/board/view";
    }

    // 물건 찾기
    @RequestMapping("find")
    public String find() {
        return "user/board/find";
    }

    @RequestMapping("test")
    public String usertest() {
        return "jstest/test";
    }
}
