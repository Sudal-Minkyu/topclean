package com.broadwave.toppos.Manager;

import com.broadwave.toppos.Head.HeadService.NoticeService;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarDto;
import com.broadwave.toppos.Manager.ManagerService.*;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryMapperDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeMapperDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMapperDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotSet;
import com.broadwave.toppos.User.UserService.InspectService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Value("${toppos.aws.s3.bucket.url}")
    private String AWSBUCKETURL;

    private final ManagerService managerService; // 지사전용 서비스
    private final CalendarService calendarService; // 휴무일지정 서비스
    private final TagNoticeService tagNoticeService; // 택분실게시판 서비스
    private final TagGalleryService tagGalleryService; // NEW 택분실게시판 서비스
    private final InspectService inspectService; // 검품등록게시판 서비스
    private final CurrentService currentService; // 현황페이지 서비스
    private final NoticeService noticeService; // 공지사항페이지 서비스

    private final ReceiptReleaseService receiptReleaseService; // 지사 출고 전용 서비스

    @Autowired
    public ManagerRestController(ManagerService managerService, CalendarService calendarService, TagNoticeService tagNoticeService, TagGalleryService tagGalleryService,
                                 ReceiptReleaseService receiptReleaseService, InspectService inspectService, CurrentService currentService, NoticeService noticeService) {
        this.managerService = managerService;
        this.calendarService = calendarService;
        this.tagNoticeService = tagNoticeService;
        this.tagGalleryService = tagGalleryService;
        this.inspectService = inspectService;
        this.currentService = currentService;
        this.noticeService = noticeService;
        this.receiptReleaseService = receiptReleaseService;
    }

    //@@@@@@@@@@@@@@@@@@@@@ 가맹점 메인화면 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 현재 로그인한 지사 정보 가져오기
    @GetMapping("branchInfo")
    @ApiOperation(value = "지사 점보조회" , notes = "현재 로그인한 지사 정보를 가져온다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> branchInfo(HttpServletRequest request){
        return managerService.branchInfo(request);
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
    //  택분실게시판 - 등록&수정
    @PostMapping("lostNoticeSave")
    public ResponseEntity<Map<String,Object>> lostNoticeSave(@ModelAttribute TagNoticeMapperDto tagNoticeMapperDto, HttpServletRequest request) throws IOException {
        return tagNoticeService.lostNoticeSave(tagNoticeMapperDto, request);
    }

    //  택분실게시판 - 글삭제
    @PostMapping("lostNoticeDelete")
    public ResponseEntity<Map<String,Object>> lostNoticeDelete(@RequestParam("htId") Long htId) {
        return tagNoticeService.lostNoticeDelete(htId);
    }

    //  택분실게시판 - 리스트 호출
    @PostMapping("lostNoticeList")
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
    @GetMapping("lostNoticeView")
    public ResponseEntity<Map<String,Object>> lostNoticeView(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeView(htId, request, "1");
    }

    //  택분실게시판 - 댓글 리스트 호출
    @GetMapping("lostNoticeCommentList")
    public ResponseEntity<Map<String,Object>> lostNoticeCommentList(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentList(htId, request);
    }

    //  택분실게시판 - 댓글 작성 and 수정
    @PostMapping("lostNoticeCommentSave")
    public ResponseEntity<Map<String,Object>> lostNoticeCommentSave(@RequestParam("hcId") Long hcId, @RequestParam("htId") Long htId, @RequestParam("type") String type,
                                                                    @RequestParam("comment") String comment, @RequestParam("preId") Long preId,
                                                                    HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentSave(hcId, htId, type, comment, preId, request);
    }


//@@@@@@@@@@@@@@@@@@@@@ NEW 택분실게시판 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  NEW 택분실게시판 - 등록&수정
    @ApiOperation(value = "택분실 등록" , notes = "지사가 택분실을 등록한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @PostMapping("tagGallerySave")
    public ResponseEntity<Map<String,Object>> tagGallerySave(@ModelAttribute TagGalleryMapperDto tagGalleryMapperDto, HttpServletRequest request) throws IOException {
        return tagGalleryService.tagGallerySave(tagGalleryMapperDto, request);
    }

    //  NEW 택분실게시판 - 리스트 호출
    @ApiOperation(value = "택분실 조회" , notes = "지사가 택분실 리스트 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @GetMapping("tagGalleryList")
    public ResponseEntity<Map<String,Object>> tagGalleryList(@RequestParam("searchString")String searchString, @RequestParam("filterFromDt")String filterFromDt,
                                                             @RequestParam("filterToDt")String filterToDt, HttpServletRequest request) {
        return tagGalleryService.tagGalleryList(searchString, filterFromDt, filterToDt, request);
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

    // 출고증인쇄 함수
    @GetMapping("branchDispatchPrint")
    public ResponseEntity<Map<String,Object>> branchDispatchPrint(@RequestParam(value="miNoList", defaultValue="") List<String> miNoList) {
        return receiptReleaseService.branchDispatchPrint(miNoList);
    }

    //  지사출고 - 세부테이블 지사입고상태, 지사강제출고 리스트
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

//    //  지사반송 - 세부테이블 반송 처리 할 리스트
//    @GetMapping("branchReceiptReturnList")
//    public ResponseEntity<Map<String,Object>> branchReceiptReturnList(@RequestParam("frId")Long frId, @RequestParam("filterFromDt")String filterFromDt,
//                                                                              @RequestParam("filterToDt")String filterToDt, @RequestParam("tagNo")String tagNo, HttpServletRequest request){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//        LocalDateTime fromDt = null;
//        if(filterFromDt != null){
//            filterFromDt = filterFromDt+" 00:00:00.000";
//            fromDt = LocalDateTime.parse(filterFromDt, formatter);
//            //            log.info("fromDt :"+fromDt);
//        }
//
//        LocalDateTime toDt = null;
//        if(filterToDt != null){
//            filterToDt = filterToDt+" 23:59:59.999";
//            toDt = LocalDateTime.parse(filterToDt, formatter);
//            //            log.info("toDt :"+toDt);
//        }
//
//        return receiptReleaseService.branchReceiptReturnList(frId, fromDt, toDt, tagNo, request);
//    }

    //  가맹점 강제츨고 - 세부테이블 강제출고 처리 할 리스트
    @GetMapping("branchReceiptForceReleaseList")
    public ResponseEntity<Map<String,Object>> branchReceiptForceReleaseList(@RequestParam("frId")Long frId, @RequestParam("filterFromDt")String filterFromDt,
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

        return receiptReleaseService.branchReceiptForceReleaseList(frId, fromDt, toDt, tagNo, request);
    }

    //  접수테이블의 상태 변화 API - 지사출고취소, 지사반송, 가맹점강제출고 실행 함수
    @PostMapping("branchRelease")
    @ApiOperation(value = "접수테이블의 상태 변화" , notes = "지사출고취소(type : 1), 지사반송(type : 2), 가맹점강제출고(type : 3)를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> branchReleaseCancel(@RequestParam(value="fdIdList", defaultValue="") List<Long> fdIdList,
                                                                  @RequestParam("type")String type, HttpServletRequest request){
        return receiptReleaseService.branchRelease(fdIdList, type, request);
    }

//@@@@@@@@@@@@@@@@@@@@@ 확인품등록, 확인품현황 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  확인품 등록할 리스트 호출API
    @GetMapping("branchInspection")
    public ResponseEntity<Map<String,Object>> branchInspection(@RequestParam("franchiseId")Long franchiseId, @RequestParam("filterFromDt")String filterFromDt,
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

        return inspectService.branchInspection(franchiseId, fromDt, toDt, tagNo, request);
    }

    // 확인품 검품 리스트 요청
    @GetMapping("branchInspectionList")
    @ApiOperation(value = "확인품 검품 리스트" , notes = "확인품리스트를 요청한다.(type : '1'은 가맹검품만, type : '2'는 확인품만, type : '0'은 모든항목 )")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> branchInspectionList(@RequestParam(value="fdId", defaultValue="") Long fdId,
                                                                      @RequestParam(value="type", defaultValue="") String type){
        return inspectService.branchInspectionList(fdId, type);
    }

    //  확인품 등록 API
    @PostMapping("branchInspectionSave")
    public ResponseEntity<Map<String,Object>> branchInspectionSave(@ModelAttribute InspeotMapperDto inspeotMapperDto, MultipartHttpServletRequest multi) throws IOException {
        return inspectService.InspectionSave(inspeotMapperDto, multi, AWSBUCKETURL);
    }

    //  등록 확인품 삭제
    @PostMapping("branchInspectionDelete")
    public ResponseEntity<Map<String,Object>> branchInspectionDelete(@RequestBody InspeotSet inspeotSet){
        return inspectService.InspectionDelete(inspeotSet);
    }

    //  확인품현황 리스트 호출API
    @GetMapping("branchInspectionCurrentList")
    public ResponseEntity<Map<String,Object>> branchInspectionCurrentList(@RequestParam("franchiseId")Long franchiseId, @RequestParam("filterFromDt")String filterFromDt,
                                                               @RequestParam("filterToDt")String filterToDt, @RequestParam("tagNo")String tagNo, HttpServletRequest request){
        return inspectService.branchInspectionCurrentList(franchiseId, filterFromDt, filterToDt, tagNo, request);
    }

//@@@@@@@@@@@@@@@@@@@@@ TAG번호조회 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  TAG번호조회 리스트 호출API
    @GetMapping("branchTagSearchList")
    public ResponseEntity<Map<String,Object>> branchTagSearchList(@RequestParam("franchiseId")Long franchiseId,  @RequestParam("tagNo")String tagNo, HttpServletRequest request){
        return managerService.branchTagSearchList(franchiseId, tagNo, request);
    }

//@@@@@@@@@@@@@@@@@@@@@ 지사입고현황, 체류세탁물현황, 출고현황, 강제출고현황, 미출고현황, 가맹반송현황 관련 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  지사입고현황, 체류세탁물현황 - 왼쪽 리스트 호출API
    @GetMapping("branchStoreCurrentList")
    @ApiOperation(value = "지사입고현황, 체류세탁물현황 리스트" , notes = "지사입고현황, 체류세탁물현황 리스트를 요청한다.(type : '2'가 아닌 모든 값은 지사입고현황 , type : '2'는 체류세탁물현황)")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> branchStoreCurrentList(@RequestParam("franchiseId")Long franchiseId, @RequestParam("filterFromDt")String filterFromDt,
                                                                     @RequestParam("filterToDt")String filterToDt, @RequestParam("type")String type, HttpServletRequest request){
        return currentService.branchStoreCurrentList(franchiseId, filterFromDt, filterToDt, type, request);
    }

    //  지사입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchStoreInputList")
    public ResponseEntity<Map<String,Object>> branchStoreInputList(@RequestParam("frCode")String frCode, @RequestParam("fdS2Dt")String fdS2Dt, HttpServletRequest request){
        return currentService.branchStoreInputList(frCode, fdS2Dt, request);
    }

    //  체류세탁물현황 - 오른쪽 리스트 호출API
    @GetMapping("branchStoreRemainList")
    public ResponseEntity<Map<String,Object>> branchStoreRemainList(@RequestParam("frCode")String frCode, @RequestParam("fdS2Dt")String fdS2Dt, HttpServletRequest request){
        return currentService.branchStoreRemainList(frCode, fdS2Dt, request);
    }

    // 지사출고현황, 지사강제출고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchReleaseCurrentList")
    @ApiOperation(value = "지사출고현황, 지사강제출고현황 리스트" , notes = "지사입고현황, 체류세탁물현황 리스트를 요청한다.(type : '1'은 지사출고현황, type : '1'이 아닌 모든 값은 지사강제출고현황)")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> branchReleaseCurrentList(@RequestParam("franchiseId")Long franchiseId, @RequestParam("filterFromDt")String filterFromDt,
                                                                     @RequestParam("filterToDt")String filterToDt, @RequestParam("type")String type, HttpServletRequest request){
        return currentService.branchReleaseCurrentList(franchiseId, filterFromDt, filterToDt, type, request);
    }

    //  지사출고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReleaseInputList")
    public ResponseEntity<Map<String,Object>> branchReleaseInputList(@RequestParam("frCode")String frCode, @RequestParam("fdS4Dt")String fdS4Dt, HttpServletRequest request){
        return currentService.branchReleaseInputList(frCode, fdS4Dt, request);
    }

    //  지사강제출고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReleaseForceList")
    public ResponseEntity<Map<String,Object>> branchReleaseForceList(@RequestParam("frCode")String frCode, @RequestParam("fdS7Dt")String fdS7Dt, HttpServletRequest request){
        return currentService.branchReleaseForceList(frCode, fdS7Dt, request);
    }

    // 미출고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchUnReleaseCurrentList")
    @ApiOperation(value = "미출고현황" , notes = "미출고현황의 왼쪽리스트 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> branchUnReleaseCurrentList(@RequestParam("franchiseId")Long franchiseId, @RequestParam("filterFromDt")String filterFromDt,
                                                                       @RequestParam("filterToDt")String filterToDt, @RequestParam("type")String type, HttpServletRequest request){
        return currentService.branchUnReleaseCurrentList(franchiseId, filterFromDt, filterToDt, type, request);
    }

    // 미출고현황 - 오른쪽 리스트 호출API
    @ApiOperation(value = "미출고현황" , notes = "오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @GetMapping("branchUnReleaseCurrentInputList")
    public ResponseEntity<Map<String,Object>> branchUnReleaseCurrentInputList(@RequestParam("frCode")String frCode, @RequestParam("filterFromDt")String filterFromDt,
                                                                         @RequestParam("filterToDt")String filterToDt, @RequestParam("type")String type, HttpServletRequest request){
        return currentService.branchUnReleaseCurrentInputList(frCode, filterFromDt, filterToDt, type, request);
    }

    // 가맹반송현황 - 왼쪽 리스트 호출API
    @GetMapping("branchReturnCurrentList")
    public ResponseEntity<Map<String,Object>> branchReturnCurrentList(@RequestParam("franchiseId")Long franchiseId, @RequestParam("filterFromDt")String filterFromDt,
                                                                       @RequestParam("filterToDt")String filterToDt, HttpServletRequest request){
        return currentService.branchReturnCurrentList(franchiseId, filterFromDt, filterToDt, request);
    }

    // 가맹반송현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReturnCurrentInputList")
    public ResponseEntity<Map<String,Object>> branchReturnCurrentInputList(@RequestParam("frCode")String frCode, @RequestParam("fdS3Dt")String fdS3Dt, HttpServletRequest request){
        return currentService.branchReturnCurrentInputList(frCode,fdS3Dt, request);
    }

// @@@@@@@@@@@@@@@@@@@ 공지사항 게시판 API @@@@@@@@@@@@@@@@@@@@@@@@@@
    // 공지사항 게시판 - 리스트 호출
    @PostMapping("/noticeList")
    public ResponseEntity<Map<String,Object>> noticeList(@RequestParam("searchString")String searchString, @RequestParam("filterFromDt")String filterFromDt,
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

        return noticeService.noticeList(searchString, fromDt, toDt, pageable, request, "2");
    }

    //  공지사항 게시판 - 글보기
    @GetMapping("/noticeView")
    public ResponseEntity<Map<String,Object>> noticeView(@RequestParam("hnId") Long hnId) {
        return noticeService.noticeView(hnId, "2");
    }





}
