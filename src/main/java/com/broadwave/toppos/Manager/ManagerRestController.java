package com.broadwave.toppos.Manager;

import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarDto;
import com.broadwave.toppos.Manager.ManagerService.CalendarService;
import com.broadwave.toppos.Manager.ManagerService.TagNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private final CalendarService calendarService; // 휴무일지정 서비스
    private final TagNoticeService tagNoticeService; // 택분실게시판 서비스

    @Autowired
    public ManagerRestController(CalendarService calendarService, TagNoticeService tagNoticeService) {
        this.calendarService = calendarService;
        this.tagNoticeService = tagNoticeService;
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
    //  택분실게시판 - 리스트호출 테이블
    @PostMapping("/lostNoticeList")
    public ResponseEntity<Map<String,Object>> lostNoticeList(@RequestParam("searchType")String searchType, @RequestParam("searchString")String searchString,
                                                             Pageable pageable, HttpServletRequest request) {
        return tagNoticeService.lostNoticeList(searchType, searchString, pageable, request, "1");
    }




















}
