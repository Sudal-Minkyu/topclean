package com.broadwave.toppos.Manager;

import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarDto;
import com.broadwave.toppos.Manager.ManagerService.CalendarService;
import com.broadwave.toppos.Manager.ManagerService.ManagerService;
import com.broadwave.toppos.Manager.ManagerService.ReceiptReleaseService;
import com.broadwave.toppos.Manager.ManagerService.TagNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 지사 RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/manager") //  ( 권한 : 지사일반, 지사장 )
public class ManagerRestController {

    private final ManagerService managerService; // 지사전용 서비스
    private final CalendarService calendarService; // 휴무일지정 서비스
    private final TagNoticeService tagNoticeService; // 택분실게시판 서비스

    private final ReceiptReleaseService receiptReleaseService; // 지사 출고 전용 서비스

    @Autowired
    public ManagerRestController(ManagerService managerService, CalendarService calendarService, TagNoticeService tagNoticeService,
                                 ReceiptReleaseService receiptReleaseService) {
        this.managerService = managerService;
        this.calendarService = calendarService;
        this.tagNoticeService = tagNoticeService;
        this.receiptReleaseService = receiptReleaseService;
    }



//@@@@@@@@@@@@@@@@@@@@@ 휴무일지정 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 휴무일 저장
    @PostMapping("calendarSave")
    public ResponseEntity<Map<String,Object>> calendarSave(HttpServletRequest request,@RequestBody List<BranchCalendarDto> branchCalendarDtoList){
        return calendarService.calendarSave(branchCalendarDtoList, request);
    }

    // 휴무일 데이터 받아오기
    @GetMapping("calendarInfo")
    public ResponseEntity<Map<String,Object>> calendarInfo(HttpServletRequest request, @RequestParam(value="targetYear", defaultValue="") String targetYear){
        return calendarService.calendarInfo(targetYear, request);
    }



//@@@@@@@@@@@@@@@@@@@@@ 택분실게시판 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  택분실게시판 - 리스트 호출
    @PostMapping("/lostNoticeList")
    public ResponseEntity<Map<String,Object>> lostNoticeList(@RequestParam("searchString")String searchString, @RequestParam("filterFromDt")String filterFromDt,
                                                             @RequestParam("filterToDt")String filterToDt,
                                                             Pageable pageable, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime fromDt = null;
        if(filterFromDt != null){
            filterFromDt = filterFromDt+" 00:00:00.000";
            fromDt = LocalDateTime.parse(filterFromDt, formatter);
//            log.info("fromDt :"+fromDt);
        }

        LocalDateTime toDt = null;
        if(filterToDt != null){
            filterToDt = filterToDt+" 23:59:59.999";
            toDt = LocalDateTime.parse(filterToDt, formatter);
//            log.info("toDt :"+toDt);
        }

        return tagNoticeService.lostNoticeList(searchString, fromDt, toDt, pageable, request, "1");
    }

    //  택분실게시판 - 글보기
    @GetMapping("/lostNoticeView")
    public ResponseEntity<Map<String,Object>> lostNoticeView(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeView(htId, request, "1");
    }

    //  택분실게시판 - 댓글 리스트 호출
    @GetMapping("/lostNoticeCommentList")
    public ResponseEntity<Map<String,Object>> lostNoticeCommentList(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentList(htId, request);
    }

    //  택분실게시판 - 댓글 작성 and 수정
    @PostMapping("/lostNoticeCommentSave")
    public ResponseEntity<Map<String,Object>> lostNoticeCommentSave(@RequestParam("hcId") Long hcId, @RequestParam("htId") Long htId, @RequestParam("type") String type,
                                                                    @RequestParam("comment") String comment, @RequestParam("preId") Long preId,
                                                                    HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentSave(hcId, htId, type, comment, preId, request);
    }

//@@@@@@@@@@@@@@@@@@@@@ 지사출고, 지사출고 취소, 가맹점강제출고, 가맹점반송 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  현 지사의 소속된 가맹점명 리스트 호출(앞으로 공용으로 쓰일 것)
    @GetMapping("branchBelongList")
    public ResponseEntity<Map<String,Object>> branchBelongList(HttpServletRequest request){
        return managerService.branchBelongList(request);
    }

    //  접수테이블의 상태 변화 API - 지사출고 실행함수
    @PostMapping("branchStateChange")
    public ResponseEntity<Map<String,Object>> branchStateChange(@RequestParam(value="fdIdList", defaultValue="") List<List<Long>> fdIdList, @RequestParam(value="miDegree", defaultValue="") Integer miDegree,
                                                                   HttpServletRequest request){
         return receiptReleaseService.branchStateChange(fdIdList, miDegree, request);
    }

    //  지사출고 - 세부테이블 지사입고상태 리스트
    @GetMapping("branchReceiptBranchInList")
    public ResponseEntity<Map<String,Object>> branchReceiptBranchInList(@RequestParam("frId")Long frId, @RequestParam("filterFromDt")String filterFromDt,
                                                                           @RequestParam("filterToDt")String filterToDt, @RequestParam("isUrgent")String isUrgent, HttpServletRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime fromDt = null;
        if(filterFromDt != null){
            filterFromDt = filterFromDt+" 00:00:00.000";
            fromDt = LocalDateTime.parse(filterFromDt, formatter);
//            log.info("fromDt :"+fromDt);
        }

        LocalDateTime toDt = null;
        if(filterToDt != null){
            filterToDt = filterToDt+" 23:59:59.999";
            toDt = LocalDateTime.parse(filterToDt, formatter);
//            log.info("toDt :"+toDt);
        }

        return receiptReleaseService.branchReceiptBranchInList(frId, fromDt, toDt, isUrgent, request);
    }

    //  지사출고 취소 - 세부테이블 지사출고 상태 리스트
    @GetMapping("branchReceiptBranchInCancelList")
    public ResponseEntity<Map<String,Object>> branchReceiptBranchInCancelList(@RequestParam("frId")Long frId, @RequestParam("filterFromDt")String filterFromDt,
                                                                        @RequestParam("filterToDt")String filterToDt, @RequestParam("tagNo")String tagNo, HttpServletRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime fromDt = null;
        if(filterFromDt != null){
            filterFromDt = filterFromDt+" 00:00:00.000";
            fromDt = LocalDateTime.parse(filterFromDt, formatter);
    //            log.info("fromDt :"+fromDt);
        }

        LocalDateTime toDt = null;
        if(filterToDt != null){
            filterToDt = filterToDt+" 23:59:59.999";
            toDt = LocalDateTime.parse(filterToDt, formatter);
    //            log.info("toDt :"+toDt);
        }

        return receiptReleaseService.branchReceiptBranchInCancelList(frId, fromDt, toDt, tagNo, request);
    }

    //  접수테이블의 상태 변화 API - 지사출고취소, 지사반송, 가맹점입고 실행 함수
    @PostMapping("branchReleaseCancel")
    public ResponseEntity<Map<String,Object>> branchReleaseCancel(@RequestParam(value="fdIdList", defaultValue="") List<Long> fdIdList,
                                                                  @RequestParam("type")String type, HttpServletRequest request){
        return receiptReleaseService.branchReleaseCancel(fdIdList, type, request);
    }

//    //  접수테이블의 상태 변화 API - 가맹점반송, 가맹점강제출고 실행 함수
//    @PostMapping("branchProcess")
//    public ResponseEntity<Map<String,Object>> branchProcess(@RequestParam(value="fdIdList", defaultValue="") Long fdId, HttpServletRequest request){
//        return receiptReleaseService.branchProcess(fdId, request);
//    }






}
