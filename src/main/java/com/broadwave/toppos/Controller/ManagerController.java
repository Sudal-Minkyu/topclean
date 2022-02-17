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

    // 미납 관리
    // 미출고현황
    @RequestMapping("notrelease")
    public String notrelease(){
        return "manager/unpaid/notrelease";
    }

    // 설정
    // 영업일관리 페이지
    @RequestMapping("businessday")
    public String businessday(){
        return "manager/setting/businessday";
    }
    // 게시판
    // Tag분실 게시판 - 리스트
    @RequestMapping("tagboard")
    public String tagboard(){
        return "manager/setting/board/tagboard";
    }
    // Tag분실 게시판 - 보기
    @RequestMapping("tagview")
    public String tagview(){
        return "manager/setting/board/view";
    }
    // Tag분실 게시판 - 작성
    @RequestMapping("boardwrite")
    public String boardwrite(){
        return "manager/setting/board/boardwrite";
    }
}
