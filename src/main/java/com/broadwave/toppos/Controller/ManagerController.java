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

    // 가맹점반품출고
    @RequestMapping("returnrelease")
    public String returnrelease(){
        return "manager/releasemanagement/returnrelease";
    }

    // 반송/검품
    // 가맹점반송
    @RequestMapping("returnproduct")
    public String returnproduct(){
        return "manager/return/returnproduct";
    }
    // 확인품등록
    @RequestMapping("checkregist")
    public String checkregist(){
        return "manager/return/checkregist";
    }
    // 확인품현황
    @RequestMapping("checkstate")
    public String checkstate(){
        return "manager/return/checkstate";
    }

    // 입출고 현황
    // TAG번호 조회
    @RequestMapping("tag")
    public String tag(){
        return "manager/inandout/tag";
    }
    // 지사입고현황
    @RequestMapping("instate")
    public String instate(){
        return "manager/inandout/instate";
    }
    // 지사출고현황
    @RequestMapping("outstate")
    public String outstate(){
        return "manager/inandout/outstate";
    }
    // 지사강제출고현황
    @RequestMapping("forceoutstate")
    public String forceoutstate(){
        return "manager/inandout/forceoutstate";
    }
    // 가맹반송현황
    @RequestMapping("returnstate")
    public String returnstate(){
        return "manager/inandout/returnstate";
    }
    // 체류세탁물현황
    @RequestMapping("staystate")
    public String staystate(){
        return "manager/inandout/staystate";
    }
    // 실시간접수현황
    @RequestMapping("receptionstate")
    public String receptionstate(){
        return "manager/inandout/receptionstate";
    }

    // 미납 관리
    // 미출고현황
    @RequestMapping("notrelease")
    public String notrelease(){
        return "manager/unpaid/notrelease";
    }

    // 설정
    // 마이페이지
    @RequestMapping("mypage")
    public String mypage(){
        return "manager/setting/mypage";
    }
    // 영업일관리 페이지
    @RequestMapping("businessday")
    public String businessday(){
        return "manager/setting/businessday";
    }
    // 물건찾기
    @RequestMapping("find")
    public String find(){
        return "manager/setting/find";
    }

    // 게시판
    // Tag분실 게시판 - 리스트
    @RequestMapping("tagboard")
    public String tagboard(){
        return "manager/setting/board/tagboard";
    }
    // 공지사항 게시판 - 리스트
    @RequestMapping("noticelist")
    public String noticelist(){
        return "manager/setting/board/list";
    }
    // 공지사항 게시판 - 보기
    @RequestMapping("noticeview")
    public String noticeview(){
        return "manager/setting/board/view";
    }
    // 공지사항 게시판 - 쓰기
    @RequestMapping("noticewrite")
    public String noticewrite(){
        return "manager/setting/board/write";
    }

    // 문자메세지
    @RequestMapping("message")
    public String message(){
        return "manager/setting/message";
    }

    // 외주관리가격설정
    @RequestMapping("ospricemanage")
    public String ospricemanage() {
        return "manager/outsource/ospricemanage";
    }
    // 외주출고처리
    @RequestMapping("ossend")
    public String ossend() {
        return "manager/outsource/ossend";
    }
    // 외주입고처리
    @RequestMapping("osreceive")
    public String osreceive() {
        return "manager/outsource/osreceive";
    }
    // 외주입출고현황
    @RequestMapping("osstatus")
    public String osstatus() {
        return "manager/outsource/osstatus";
    }

    // 입출고 현황 1 추가된 것
    // 강제 입고 현황(가맹)
    @RequestMapping("forcestorestat")
    public String forcestorestat(){
        return "manager/inandout/forcestorestat";
    }

    // 입출고 현황 2
    // 재세탁 입고 현황
    @RequestMapping("retrystat")
    public String retrystat(){
        return "manager/inandout2/retrystat";
    }

    // 반품 현황
    @RequestMapping("refundstat")
    public String refundstat(){ return "manager/inandout2/refundstat"; }

    // 초특급세탁 처리 현황
    @RequestMapping("highspeedsuperspecialstat")
    public String highspeedsuperspecialstat(){ return "manager/inandout2/highspeedsuperspecialstat"; }

    // 특급세탁 처리 현황
    @RequestMapping("highspeedspecialstat")
    public String highspeedspecialstat(){ return "manager/inandout2/highspeedspecialstat"; }

    // 급세탁 처리 현황
    @RequestMapping("highspeedstat")
    public String highspeedstat(){ return "manager/inandout2/highspeedstat"; }

    // 일반 처리 현황
    @RequestMapping("normalspeedstat")
    public String normalspeedstat(){ return "manager/inandout2/normalspeedstat"; }

    // 정산현황
    // 가맹점 일 정산 입금 등록
    @RequestMapping("frdaydeposit")
    public String frdaydeposit(){ return "manager/calculate/frdaydeposit"; }

    // 지사 월 정산 입금 등록
    @RequestMapping("brmonthbalance")
    public String brmonthbalance(){ return "manager/calculate/brmonthbalance"; }

    // 가맹점 일 정산 내역
    @RequestMapping("frdaybalance")
    public String frdaybalance(){ return "manager/calculate/frdaybalance"; }

    // 지사 월 정산 입금 현황
    @RequestMapping("brmonthdepositstatus")
    public String brmonthdepositstatus(){ return "manager/calculate/brmonthdepositstatus"; }

    // 가맹점 일 정산 입금 현황
    @RequestMapping("frdaydepositstatus")
    public String frdaydepositstatus(){ return "manager/calculate/frdaydepositstatus"; }
}
