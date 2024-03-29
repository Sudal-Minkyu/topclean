package com.broadwave.toppos.Manager;

import com.broadwave.toppos.Head.HeadService.NoticeService;
import com.broadwave.toppos.Head.HeadService.SummaryService;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeMapperDto;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarDto;
import com.broadwave.toppos.Manager.HmTemplate.HmTemplateDto;
import com.broadwave.toppos.Manager.ManagerService.*;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryMapperDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeMapperDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListInputDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMapperDto;
import com.broadwave.toppos.User.UserService.FindService;
import com.broadwave.toppos.User.UserService.InspectService;
import com.broadwave.toppos.User.UserService.QrService;
import com.broadwave.toppos.User.UserService.ReceiptService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final HmTemplateService hmTemplateService; // 문자메세지 서비스
    private final QrService qrService; // Qr관련 서비스

    private final ReceiptReleaseService receiptReleaseService; // 지사출고 전용 서비스
    private final ReceiptService receiptService; // 접수 서비스
    private final FindService findService; // 물건찾기 서비스

    private final OutsourcingPriceService outsourcingPriceService; // 외주가격 서비스
    private final SummaryService summaryService; // 정산페이지 서비스

    @Autowired
    public ManagerRestController(ManagerService managerService, CalendarService calendarService, TagNoticeService tagNoticeService, TagGalleryService tagGalleryService,
                                 ReceiptReleaseService receiptReleaseService, InspectService inspectService, CurrentService currentService, NoticeService noticeService, ReceiptService receiptService,
                                 FindService findService, HmTemplateService hmTemplateService, QrService qrService, OutsourcingPriceService outsourcingPriceService, SummaryService summaryService) {
        this.managerService = managerService;
        this.calendarService = calendarService;
        this.tagNoticeService = tagNoticeService;
        this.tagGalleryService = tagGalleryService;
        this.inspectService = inspectService;
        this.currentService = currentService;
        this.noticeService = noticeService;
        this.receiptReleaseService = receiptReleaseService;
        this.receiptService = receiptService;
        this.findService = findService;
        this.hmTemplateService = hmTemplateService;
        this.qrService = qrService;
        this.outsourcingPriceService = outsourcingPriceService;
        this.summaryService = summaryService;
    }

    // 현재 로그인한 지사 정보 가져오기
    @GetMapping("branchHeaderData")
    @ApiOperation(value = "해더 데이터", notes = "현재 로그인한 해더 데이터 정보를 가져온다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchHeaderData(HttpServletRequest request) {
        return managerService.branchHeaderData(request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 가맹점 메인화면 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 현재 로그인한 지사 정보 가져오기
    @GetMapping("branchInfo")
    @ApiOperation(value = "지사 점보조회", notes = "현재 로그인한 지사 정보를 가져온다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchInfo(HttpServletRequest request) {
        return managerService.branchInfo(request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 휴무일지정 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 휴무일 저장
    @PostMapping("calendarSave")
    public ResponseEntity<Map<String, Object>> calendarSave(HttpServletRequest request, @RequestBody List<BranchCalendarDto> branchCalendarDtoList) {
        return calendarService.calendarSave(branchCalendarDtoList, request);
    }

    // 휴무일 데이터 받아오기
    @GetMapping("calendarInfo")
    public ResponseEntity<Map<String, Object>> calendarInfo(HttpServletRequest request, @RequestParam(value = "targetYear", defaultValue = "") String targetYear) {
        return calendarService.calendarInfo(targetYear, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 택분실게시판 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  택분실게시판 - 등록&수정
    @PostMapping("lostNoticeSave")
    public ResponseEntity<Map<String, Object>> lostNoticeSave(@ModelAttribute TagNoticeMapperDto tagNoticeMapperDto, HttpServletRequest request) throws IOException {
        return tagNoticeService.lostNoticeSave(tagNoticeMapperDto, request);
    }

    //  택분실게시판 - 글삭제
    @PostMapping("lostNoticeDelete")
    public ResponseEntity<Map<String, Object>> lostNoticeDelete(@RequestParam("htId") Long htId) {
        return tagNoticeService.lostNoticeDelete(htId);
    }

    //  택분실게시판 - 리스트 호출
    @PostMapping("lostNoticeList")
    public ResponseEntity<Map<String, Object>> lostNoticeList(@RequestParam("searchString") String searchString, @RequestParam("filterFromDt") String filterFromDt,
                                                              @RequestParam("filterToDt") String filterToDt,
                                                              Pageable pageable, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime fromDt = null;
        if (filterFromDt != null) {
            filterFromDt = filterFromDt + " 00:00:00.000";
            fromDt = LocalDateTime.parse(filterFromDt, formatter);
//            log.info("fromDt :"+fromDt);
        }

        LocalDateTime toDt = null;
        if (filterToDt != null) {
            filterToDt = filterToDt + " 23:59:59.999";
            toDt = LocalDateTime.parse(filterToDt, formatter);
//            log.info("toDt :"+toDt);
        }

        return tagNoticeService.lostNoticeList(searchString, fromDt, toDt, pageable, request, "1");
    }

    //  택분실게시판 - 글보기
    @GetMapping("lostNoticeView")
    public ResponseEntity<Map<String, Object>> lostNoticeView(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeView(htId, request, "1");
    }

    //  택분실게시판 - 댓글 리스트 호출
    @GetMapping("lostNoticeCommentList")
    public ResponseEntity<Map<String, Object>> lostNoticeCommentList(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentList(htId, request);
    }

    //  택분실게시판 - 댓글 작성 and 수정
    @PostMapping("lostNoticeCommentSave")
    public ResponseEntity<Map<String, Object>> lostNoticeCommentSave(@RequestParam("hcId") Long hcId, @RequestParam("htId") Long htId, @RequestParam("type") String type,
                                                                     @RequestParam("comment") String comment, @RequestParam("preId") Long preId,
                                                                     HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentSave(hcId, htId, type, comment, preId, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ NEW 택분실게시판 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  NEW 택분실게시판 - 등록&수정
    @ApiOperation(value = "택분실 등록", notes = "지사가 택분실을 등록한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("tagGallerySave")
    public ResponseEntity<Map<String, Object>> tagGallerySave(@ModelAttribute TagGalleryMapperDto tagGalleryMapperDto, HttpServletRequest request) throws IOException {
        return tagGalleryService.tagGallerySave(tagGalleryMapperDto, request);
    }

    //  NEW 택분실게시판 - 리스트 호출
    @ApiOperation(value = "택분실 조회", notes = "지사가 택분실 리스트 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("tagGalleryList")
    public ResponseEntity<Map<String, Object>> tagGalleryList(@RequestParam("type") String searchString, @RequestParam("filterFromDt") String filterFromDt,
                                                              @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return tagGalleryService.tagGalleryList(searchString, filterFromDt, filterToDt, request, "1");
    }

    //  NEW 택분실게시판 - 상세보기 호출
    @ApiOperation(value = "택분실 상세보기", notes = "지사가 택분실 상세보기 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("tagGalleryDetail")
    public ResponseEntity<Map<String, Object>> tagGalleryDetail(@RequestParam("btId") Long btId, HttpServletRequest request) {
        return tagGalleryService.tagGalleryDetail(btId, request, "1");
    }

    //  NEW 택분실게시판 - 삭제
    @ApiOperation(value = "택분실 삭제", notes = "지사가 택분실 글을 삭제한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("tagGalleryDelete")
    public ResponseEntity<Map<String, Object>> tagGalleryDelete(@RequestParam("btId") Long btId, HttpServletRequest request) {
        return tagGalleryService.tagGalleryDelete(btId, request);
    }

    //  NEW 택분실게시판 - 해당 게시물을 종료하는 호출
    @ApiOperation(value = "택분실 게시종료", notes = "지사가 택분실 게시를 종료한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("tagGalleryEnd")
    public ResponseEntity<Map<String, Object>> tagGalleryEnd(@RequestParam("btId") Long btId, HttpServletRequest request) {
        return tagGalleryService.tagGalleryEnd(btId, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 지사출고, 지사출고 취소, 가맹점강제출고, 가맹점반송 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  현 지사의 소속된 가맹점명 리스트 호출(앞으로 공용으로 쓰일 것)
    @GetMapping("branchBelongList")
    public ResponseEntity<Map<String, Object>> branchBelongList(HttpServletRequest request) {
        return managerService.branchBelongList(request);
    }

    //  접수테이블의 상태 변화 API - 지사출고 실행함수
    @PostMapping("branchStateChange")
    public ResponseEntity<Map<String, Object>> branchStateChange(@RequestParam(value = "fdIdList", defaultValue = "") List<List<Long>> fdIdList,
                                                                 @RequestParam(value = "fdS4TypeList", defaultValue = "") List<List<String>> fdS4TypeList,
                                                                 @RequestParam(value = "miDegree", defaultValue = "") Integer miDegree,
                                                                 HttpServletRequest request) {
        return receiptReleaseService.branchStateChange(fdIdList, fdS4TypeList, miDegree, request);
    }

    // 출고증인쇄 함수
    @GetMapping("branchDispatchPrint")
    @ApiOperation(value = "출고증 인쇄", notes = "출고증인쇄를 신청한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchDispatchPrint(@RequestParam(value = "miNoList", defaultValue = "") List<String> miNoList) {
        return receiptReleaseService.branchDispatchPrint(miNoList);
    }

    //  지사출고 - 세부테이블 지사입고상태 지사출고 리스트
    @GetMapping("branchReceiptBranchInList")
    @ApiOperation(value = "지사 출고 리스트", notes = "지사출고 할 리스트를 호출한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                         @RequestParam("filterToDt") String filterToDt, @RequestParam("isUrgent") String isUrgent, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptBranchInList(frId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), isUrgent, request);
    }

    //  지사출고 - 세부테이블 지사강제출고 리스트
    @GetMapping("branchReceiptBranchInForceList")
    @ApiOperation(value = "지사 강제출고 리스트", notes = "지사강제출고 할 리스트를 호출한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInForceList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                              @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptBranchInForceList(frId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), request);
    }

    //  지사출고 취소 - 세부테이블 지사출고 상태 리스트
    @GetMapping("branchReceiptBranchInCancelList")
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInCancelList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                               @RequestParam("filterToDt") String filterToDt, @RequestParam("tagNo") String tagNo, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptBranchInCancelList(frId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), tagNo, request);
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

    //  지사 강제츨고 - 세부테이블 강제출고 처리 할 리스트
    @GetMapping("branchReceiptForceReleaseList")
    public ResponseEntity<Map<String, Object>> branchReceiptForceReleaseList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                             @RequestParam("filterToDt") String filterToDt, @RequestParam("tagNo") String tagNo, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptForceReleaseList(frId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), tagNo, request);
    }

    //  지사 반품출고 - 세부테이블 반품출고 처리 할 리스트
    @GetMapping("branchReceiptReturnReleaseList")
    public ResponseEntity<Map<String, Object>> branchReceiptReturnReleaseList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                              @RequestParam("filterToDt") String filterToDt, @RequestParam("tagNo") String tagNo, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptReturnReleaseList(frId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), tagNo, request);
    }

    //  접수테이블의 상태 변화 API - 지사출고취소, 지사반품, 가맹점강제출고 실행 함수
    @PostMapping("branchRelease")
    @ApiOperation(value = "접수테이블의 상태 변화", notes = "지사출고취소(type : 1), 지사반품(type : 2), 가맹점강제출고(type : 3)를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchRelease(@RequestParam(value = "fdIdList", defaultValue = "") List<Long> fdIdList,
                                                             @RequestParam("type") String type, HttpServletRequest request) {
        return receiptReleaseService.branchRelease(fdIdList, type, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 확인품등록, 확인품현황 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  확인품 등록할 리스트 호출API
    @GetMapping("branchInspection")
    @ApiOperation(value = "확인품 등록할 리스트", notes = "확인품 등록할 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchInspection(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                @RequestParam("filterToDt") String filterToDt, @RequestParam("tagNo") String tagNo, HttpServletRequest request) {
        return inspectService.branchInspection(franchiseId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), tagNo, request);
    }

    //  지사검품 조회 - 확인품 정보 요청
    @GetMapping("branchInspectionInfo")
    @ApiOperation(value = "확인품 검품 정보", notes = "해당 접수의 확인품 정보를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchInspectionInfo(@RequestParam(value = "fiId", defaultValue = "") Long fiId, HttpServletRequest request) {
        return inspectService.inspectionInfo(fiId, "1", request);
    }

//    // 확인품 검품 리스트 요청
//    @GetMapping("branchInspectionList")
//    @ApiOperation(value = "확인품 검품 리스트" , notes = "확인품리스트를 요청한다.(type : '1'은 가맹검품만, type : '2'는 확인품만, type : '0'은 모든항목 )")
//    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
//    public ResponseEntity<Map<String,Object>> branchInspectionList(@RequestParam(value="fdId", defaultValue="") Long fdId,
//                                                                      @RequestParam(value="type", defaultValue="") String type){
//        return inspectService.branchInspectionList(fdId, type);
//    }

    //  확인품 등록/수정 API - 22/03/30 수정추가
    @PostMapping("branchInspectionSave")
    public ResponseEntity<Map<String, Object>> branchInspectionSave(@ModelAttribute InspeotMapperDto inspeotMapperDto, HttpServletRequest request) throws IOException {
        return inspectService.InspectionSave(inspeotMapperDto, request, AWSBUCKETURL, "2");
    }

    //  등록 확인품 삭제
    @ApiOperation(value = "확인품 삭제", notes = "지사가 확인품 글을 삭제한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping("branchInspectionDelete")
    public ResponseEntity<Map<String, Object>> branchInspectionDelete(@RequestParam("fiId") Long fiId, HttpServletRequest request) {
        return inspectService.InspectionDelete(fiId, "2", request);
    }

    //  확인품현황 리스트 호출API
    @GetMapping("branchInspectionCurrentList")
    public ResponseEntity<Map<String, Object>> branchInspectionCurrentList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                           @RequestParam("filterToDt") String filterToDt, @RequestParam("tagNo") String tagNo, HttpServletRequest request) {
        return inspectService.branchInspectionCurrentList(franchiseId, filterFromDt, filterToDt, tagNo, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ TAG번호조회 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  TAG번호조회 리스트 호출API
    @GetMapping("branchTagSearchList")
    public ResponseEntity<Map<String, Object>> branchTagSearchList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("tagNo") String tagNo, HttpServletRequest request) {
        return managerService.branchTagSearchList(franchiseId, tagNo, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 지사입고현황, 체류세탁물현황, 출고현황, 강제출고현황, 미출고현황, 가맹반송현황 관련 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  지사입고현황, 체류세탁물현황 - 왼쪽 리스트 호출API
    @GetMapping("branchStoreCurrentList")
    @ApiOperation(value = "지사입고현황, 체류세탁물현황 리스트", notes = "지사입고현황, 체류세탁물현황 리스트를 요청한다.(type : '2'가 아닌 모든 값은 지사입고현황 , type : '2'는 체류세탁물현황)")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchStoreCurrentList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                      @RequestParam("filterToDt") String filterToDt, @RequestParam("type") String type, HttpServletRequest request) {
        return currentService.branchStoreCurrentList(franchiseId, filterFromDt, filterToDt, type, request);
    }

    //  지사입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchStoreInputList")
    public ResponseEntity<Map<String, Object>> branchStoreInputList(@RequestParam("frCode") String frCode, @RequestParam("fdS2Dt") String fdS2Dt, HttpServletRequest request) {
        return currentService.branchStoreInputList(frCode, fdS2Dt, request);
    }

    //  체류세탁물현황 - 오른쪽 리스트 호출API
    @GetMapping("branchStoreRemainList")
    public ResponseEntity<Map<String, Object>> branchStoreRemainList(@RequestParam("frCode") String frCode, @RequestParam("fdS2Dt") String fdS2Dt, HttpServletRequest request) {
        return currentService.branchStoreRemainList(frCode, fdS2Dt, request);
    }

    // 지사출고현황, 지사강제출고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchReleaseCurrentList")
    @ApiOperation(value = "지사출고현황, 지사강제출고현황 리스트", notes = "지사입고현황, 체류세탁물현황 리스트를 요청한다.(type : '1'은 지사출고현황, type : '1'이 아닌 모든 값은 지사강제출고현황)")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReleaseCurrentList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                        @RequestParam("filterToDt") String filterToDt, @RequestParam("type") String type, HttpServletRequest request) {
        return currentService.branchReleaseCurrentList(franchiseId, filterFromDt, filterToDt, type, request);
    }

    //  지사출고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReleaseInputList")
    public ResponseEntity<Map<String, Object>> branchReleaseInputList(@RequestParam("frCode") String frCode, @RequestParam("fdS4Dt") String fdS4Dt, HttpServletRequest request) {
        return currentService.branchReleaseInputList(frCode, fdS4Dt, request);
    }

    //  지사강제출고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReleaseForceList")
    public ResponseEntity<Map<String, Object>> branchReleaseForceList(@RequestParam("frCode") String frCode, @RequestParam("fdS7Dt") String fdS7Dt, HttpServletRequest request) {
        return currentService.branchReleaseForceList(frCode, fdS7Dt, request);
    }

    // 미출고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchUnReleaseCurrentList")
    @ApiOperation(value = "미출고현황", notes = "미출고현황의 왼쪽리스트 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchUnReleaseCurrentList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                          @RequestParam("filterToDt") String filterToDt, @RequestParam("type") String type, HttpServletRequest request) {
        return currentService.branchUnReleaseCurrentList(franchiseId, filterFromDt, filterToDt, type, request);
    }

    // 미출고현황 - 오른쪽 리스트 호출API
    @ApiOperation(value = "미출고현황", notes = "오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("branchUnReleaseCurrentInputList")
    public ResponseEntity<Map<String, Object>> branchUnReleaseCurrentInputList(@RequestParam("frCode") String frCode, @RequestParam("filterFromDt") String filterFromDt,
                                                                               @RequestParam("filterToDt") String filterToDt, @RequestParam("type") String type, HttpServletRequest request) {
        return currentService.branchUnReleaseCurrentInputList(frCode, filterFromDt, filterToDt, type, request);
    }

    // 가맹반송현황 - 왼쪽 리스트 호출API
    @GetMapping("branchReturnCurrentList")
    public ResponseEntity<Map<String, Object>> branchReturnCurrentList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                       @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.branchReturnCurrentList(franchiseId, filterFromDt, filterToDt, request);
    }

    // 가맹반송현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReturnCurrentInputList")
    public ResponseEntity<Map<String, Object>> branchReturnCurrentInputList(@RequestParam("frCode") String frCode, @RequestParam("fdS3Dt") String fdS3Dt, HttpServletRequest request) {
        return currentService.branchReturnCurrentInputList(frCode, fdS3Dt, request);
    }


    // @@@@@@@@@@@@@@@@@@@ 공지사항 게시판 API @@@@@@@@@@@@@@@@@@@@@@@@@@
    // 공지사항 게시판 - 리스트 호출
    @PostMapping("/noticeList")
    public ResponseEntity<Map<String, Object>> noticeList(@RequestParam("hnType") String hnType, @RequestParam("searchString") String searchString, @RequestParam("filterFromDt") String filterFromDt,
                                                          @RequestParam("filterToDt") String filterToDt,
                                                          Pageable pageable) {
        return noticeService.noticeList(hnType, searchString, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), pageable, "2");
    }

    //  공지사항 게시판 - 글보기
    @ApiOperation(value = "공지사항 글보기", notes = "지사에서 공지사항 글을 본다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("/noticeView")
    public ResponseEntity<Map<String, Object>> noticeView(@RequestParam("hnId") Long hnId) {
        return noticeService.noticeView(hnId, "2");
    }

    //  공지사항 게시판 - 등록&수정
    @PostMapping("noticeSave")
    @ApiOperation(value = "공지사항 등록및수정", notes = "지사에서 공지사항을 등록하거나 수정한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> noticeSave(@ModelAttribute NoticeMapperDto noticeMapperDto, HttpServletRequest request) throws IOException {
        return noticeService.noticeSave(noticeMapperDto, request, "2");
    }

    //  공지사항 게시판 - 글삭제
    @PostMapping("noticeDelete")
    @ApiOperation(value = "공지사항 글삭제", notes = "지사에서 공지사항 글을 삭제한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> noticeDelete(@RequestParam("hnId") Long hnId) {
        return noticeService.noticeDelete(hnId);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 실시간접수현황 관련 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 실시간접수현황 왼쪽 리스트 호출
    @GetMapping("branchRealTimeList")
    @ApiOperation(value = "실시간접수현황 왼쪽 리스트", notes = "실시간접수현황 왼쪽 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchRealTimeList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                  @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return receiptService.branchRealTimeList(franchiseId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), request);
    }

    // 지사 실시간접수현황 오른쪽 리스트 호출
    @GetMapping("branchRealTimeSubList")
    @ApiOperation(value = "실시간접수현황 오른쪽 리스트", notes = "실시간접수현황 오른쪽 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchRealTimeSubList(@RequestParam("frYyyymmdd") String frYyyymmdd, HttpServletRequest request) {
        return receiptService.branchRealTimeSubList(frYyyymmdd, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 물건찾기 관련 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 물건찾기 리스트 호출
    @GetMapping("branchObjectFind")
    @ApiOperation(value = "물건찾기 왼쪽 리스트", notes = "물건찾기 왼쪽 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchObjectFind(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                @RequestParam("filterToDt") String filterToDt, HttpServletRequest request, @RequestParam("ffState") String ffState) {
        return findService.objectFind(franchiseId, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), ffState, request);
    }

    // 지사 물건찾기 확인 업데이트
    @PostMapping("branchObjectFindCheck")
    @ApiOperation(value = "물건찾기 지사확인", notes = "물건찾기 요청(01) 항목을 지사확인중(02)으로 업데이트 또는 확인중(02) 항목을 찾기완료(03)로 업데이트 한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchObjectFindCheck(@RequestParam(value = "ffIdList", defaultValue = "") List<Long> ffIdList,
                                                                     @RequestParam("ffState") String ffState, HttpServletRequest request) {
        return findService.branchObjectFindCheck(ffIdList, ffState, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 나의 정보관리 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 현재 지사의 정보 호출하기
    @GetMapping("myInfo")
    public ResponseEntity<Map<String, Object>> myInfo(HttpServletRequest request) {
        return managerService.branchMyInfo(request);
    }

    // 지사정보관리 수정 API
    @PostMapping("branchInfoSave")
    public ResponseEntity<Map<String, Object>> branchInfoSave(@RequestParam(value = "brName", defaultValue = "") String brName,
                                                              @RequestParam(value = "brTelNo", defaultValue = "") String brTelNo, HttpServletRequest request) {
        return managerService.branchInfoSave(brName, brTelNo, request);
    }

    // 지사 나의정보관리 수정 API
    @PostMapping("branchMyInfoSave")
    public ResponseEntity<Map<String, Object>> branchMyInfoSave(@RequestParam(value = "userEmail", defaultValue = "") String userEmail,
                                                                @RequestParam(value = "userTel", defaultValue = "") String userTel,
                                                                @RequestParam(value = "nowPassword", defaultValue = "") String nowPassword,
                                                                @RequestParam(value = "newPassword", defaultValue = "") String newPassword,
                                                                @RequestParam(value = "checkPassword", defaultValue = "") String checkPassword,
                                                                HttpServletRequest request) {
        return managerService.branchMyInfoSave(userEmail, userTel, nowPassword, newPassword, checkPassword, request);
    }


    //@@@@@@@@@@@@@@@@@@@@@ 문자메세지 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 메세지 보낼 고객 리스트 호출
    @GetMapping("messageCustomerList")
    @ApiOperation(value = "지사의 문자 메시지 고객리스트", notes = "문자를 메시지 보낼 가맹점의 고객리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> messageCustomerList(
            @RequestParam(value = "visitDayRange", defaultValue = "") String visitDayRange,
            @RequestParam(value = "franchiseId", defaultValue = "") Long franchiseId,
            HttpServletRequest request) {
        return hmTemplateService.messageCustomerList(visitDayRange, franchiseId, null, request);
    }

    // 문자 메시지 보내기
    @PostMapping("messageSendCustomer")
    @ApiOperation(value = "문자 메시지 보내기", notes = "선택한 고객들에게 문자메세지를 보낸다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> messageSendCustomer(
            @RequestParam(value = "bcIdList", defaultValue = "") List<Long> bcIdList,
            @RequestParam(value = "hmMessage", defaultValue = "") String hmMessage,
            @RequestParam(value = "msgType", defaultValue = "") String msgType,
            @RequestParam(value = "hmSendreqtimeDt", defaultValue = "") String hmSendreqtimeDt,
            HttpServletRequest request) {
        return hmTemplateService.messageSendCustomer("2", bcIdList, hmMessage, hmSendreqtimeDt, msgType, request);
    }

    // 메세지 템플릿 6개 저장
    @PostMapping("templateSave")
    @ApiOperation(value = "지사 메세지 템플릿 6개 저장", notes = "문자 메세지 템플릿을 저장한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> templateSave(@RequestBody List<HmTemplateDto> hmTemplateDtos, HttpServletRequest request) {
        return hmTemplateService.hmTemplateSave("2", hmTemplateDtos, request);
    }

    // 메세지 템플릿 6개 호출
    @GetMapping("templateList")
    @ApiOperation(value = "지사  메세지 템플릿 호출", notes = "문자 메세지 템플릿을 호출한다")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> templateList(HttpServletRequest request) {
        return hmTemplateService.hmTemplateList("2", request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 외주가격 리스트 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 외주가격에 쓰일 대분류 이름과 코드 호출
    @GetMapping("itemGroupcodeAndNameList")
    @ApiOperation(value = "대분류 코드와 이름 리스트", notes = "사용여부가 Y인 대분류 코드와 이름을 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> ItemGroupcodeAndNameList(HttpServletRequest request) {
        return outsourcingPriceService.groupcodeAndNameList(request);
    }

    // 외주가격 리스트 호출
    @PostMapping("outsourcingPriceList")
    @ApiOperation(value = "외주가격 리스트", notes = "외주가격 리스트를 호출한다")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> outsourcingPriceList(@RequestBody OutsourcingPriceListInputDto outsourcingPriceListInputDto, HttpServletRequest request) {

        return outsourcingPriceService.outsourcingPriceList(outsourcingPriceListInputDto, request);
    }

    // 외주가격 리스트 저장
    @PostMapping("outsourcingPriceSave")
    @ApiOperation(value = "외주가격 저장", notes = "입력한 외주가격 리스트들을 저장한다")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> outsourcingPriceSave(@RequestBody List<OutsourcingPriceListDto> outsourcingPriceListDtos, HttpServletRequest request) {

        return outsourcingPriceService.outsourcingPriceSave(outsourcingPriceListDtos, request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 외주출고처리 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  지사 외주출고  - 세부테이블 외주 출고처리 할 리스트 호출
    @GetMapping("branchReceiptBranchInOutsouringList")
    @ApiOperation(value = "지사 외주 출고처리 리스트", notes = "지사 외주 출고처리 할 리스트를 호출한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInOutsouringList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                                   @RequestParam("filterToDt") String filterToDt, @RequestParam("isOutsourceable") String isOutsourceable, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptBranchInOutsouringList(frId, filterFromDt, filterToDt, isOutsourceable, request);
    }

    //  지사 외주출고  - 세부테이블 외주 출고처리 실행 호출
    @PostMapping("branchStateOutsouringChange")
    public ResponseEntity<Map<String, Object>> branchStateOutsouringChange(@RequestParam(value = "fdIdList", defaultValue = "") List<Long> fdIdList,
                                                                           HttpServletRequest request) {
        return receiptReleaseService.branchStateOutsouringChange(fdIdList, request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 외주입고처리 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  지사 외주입고  - 세부테이블 외주 입고처리 할 리스트 호출
    @GetMapping("branchReceiptBranchOutOutsouringList")
    @ApiOperation(value = "지사 외주 입고처리 리스트", notes = "지사 외주 입고처리 할 리스트를 호출한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptBranchOutOutsouringList(@RequestParam("frId") Long frId, @RequestParam("filterFromDt") String filterFromDt,
                                                                                    @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptBranchOutOutsouringList(frId, filterFromDt, filterToDt, request);
    }

    //  지사 외주입고  - 세부테이블 외주 입고처리 실행 호출
    @PostMapping("branchStateOutsouringOutChange")
    public ResponseEntity<Map<String, Object>> branchStateOutsouringOutChange(@RequestParam(value = "fdIdList", defaultValue = "") List<Long> fdIdList,
                                                                              HttpServletRequest request) {
        return receiptReleaseService.branchStateOutsouringOutChange(fdIdList, request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 외주입출고 현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  지사 외주 입출고 현황 리스트 왼쪽 호출
    @GetMapping("branchReceiptOutsouringList")
    @ApiOperation(value = "지사 외주 입출고 현황 왼쪽 리스트", notes = "지사 외주 입출고 현황 왼쪽 리스트를 호출한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptOutsouringList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                           @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptOutsouringList(franchiseId, filterFromDt, filterToDt, request);
    }

    //  지사 외주 입출고 현황 리스트 오른쪽 호출
    @GetMapping("branchReceiptOutsouringSubList")
    @ApiOperation(value = "지사 외주 입출고 현황 오른쪽 리스트", notes = "지사 외주 입출고 현황 오른쪽 리스트를 호출한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptOutsouringSubList(@RequestParam("fdO1Dt") String fdO1Dt, HttpServletRequest request) {
        return receiptReleaseService.branchReceiptOutsouringSubList(fdO1Dt, request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 지사 세탁현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 초특급세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchSpecialQuickReceiptList")
    @ApiOperation(value = "지사 초특급세탁 현황 리스트", notes = "지사가 초특급세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchSpecialQuickReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                             @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "1", "Y", "1");
    }

    // 지사 초특급세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchSpecialQuickReceiptSubList")
    @ApiOperation(value = "지사 초특급세탁 현황 리스트", notes = "지사가 초특급세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchSpecialQuickReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                                @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "1", "Y", "1");
    }

    // 지사 특급세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchSpecialReceiptList")
    @ApiOperation(value = "지사 특급세탁 현황 리스트", notes = "지사가 특급세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchSpecialReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                        @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "2", "Y", "2");
    }

    // 지사 특급세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchSpecialReceiptSubList")
    @ApiOperation(value = "지사 특급세탁 현황 리스트", notes = "지사가 특급세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchSpecialReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                           @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "2", "Y", "2");
    }

    // 지사 급세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchQuickReceiptList")
    @ApiOperation(value = "지사 급세탁 현황 리스트", notes = "지사가 급세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchQuickReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "3", "Y", "3");
    }

    // 지사 급세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchQuickReceiptSubList")
    @ApiOperation(value = "지사 급세탁 현황 리스트", notes = "지사가 급세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchQuickReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                         @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "3", "Y", "3");
    }

    // 지사 일반세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchNormalReceiptList")
    @ApiOperation(value = "지사 일반세탁 현황 리스트", notes = "지사가 일반세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchNormalReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                       @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "4", "N", "");
    }

    // 지사 일반세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchNormalReceiptSubList")
    @ApiOperation(value = "지사 일반세탁 현황 리스트", notes = "지사가 일반세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchNormalReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                       @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "4", "N", "");
    }

    //@@@@@@@@@@@@@@@@@@@@@ 반품현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 반품현황 - 왼쪽 리스트 호출API
    @GetMapping("branchReturnReceiptList")
    @ApiOperation(value = "지사 반품현황 리스트", notes = "지사가 반품현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReturnReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                       @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.returnReceiptList(branchId, franchiseId, filterFromDt, filterToDt);
    }

    // 지사 반품현황 - 오른쪽 리스트 호출API
    @GetMapping("branchReturnReceiptSubList")
    @ApiOperation(value = "지사 반품현황 리스트", notes = "지사가 반품현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReturnReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                          @RequestParam("fdS6Dt") String fdS6Dt) {
        return receiptService.returnReceiptSubList(branchId, franchiseId, fdS6Dt);
    }

//@@@@@@@@@@@@@@@@@@@@@ 재세탁입고 현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 재세탁입고 현황 - 왼쪽 리스트 호출API
    @GetMapping("branchRetryList")
    @ApiOperation(value = "지사 재세탁입고 현황 리스트", notes = "지사가 재세탁입고 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchRetryList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                               @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.retryList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 재세탁입고 현황 - 오른쪽 리스트 호출API
    @GetMapping("branchRetrySubList")
    @ApiOperation(value = "지사 재세탁입고 현황 리스트", notes = "지사가 재세탁입고 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchRetrySubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                  @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return currentService.retrySubList(branchId, franchiseId, frYyyymmdd);
    }

//@@@@@@@@@@@@@@@@@@@@@ 강제출고 현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 강제입고현황 - 왼쪽 리스트 호출API
    @GetMapping("branchForceStoreInputList")
    @ApiOperation(value = "지사 강제입고현황 리스트", notes = "지사가 강제입고현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchForceStoreInputList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                         @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.forceStoreInputList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 지사 강제입고현황 - 오른쪽 리스트 호출API
    @GetMapping("branchForceStoreInputSubList")
    @ApiOperation(value = "지사 강제출고현황 리스트", notes = "지사가 강제출고현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchForceStoreInputSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                            @RequestParam("fdS8Dt") String fdS8Dt) {
        return currentService.forceStoreInputSubList(branchId, franchiseId, fdS8Dt);
    }



//@@@@@@@@@@@@@@@@@@@@@ 정산 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사 월정산 입금 리스트 호출API
    @GetMapping("branchReceiptMonthlyList")
    @ApiOperation(value = "지사의 월정산입금 리스트", notes = "지사의 월정산입금 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptMonthlyList(@RequestParam("branchId") Long branchId, @RequestParam("filterFromYearMonth") String filterFromYearMonth,
                                                                            @RequestParam("filterToYearMonth") String filterToYearMonth) {
        return summaryService.receiptMonthlyList(branchId, filterFromYearMonth, filterToYearMonth);
    }

    // 본사 지사별 월정산 요역 리스트 호출API
    @GetMapping("branchMonthlySummaryList")
    @ApiOperation(value = "지사 월정산 리스트", notes = "지사 월정산 요약 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchMonthlySummaryList(@RequestParam("filterYearMonth") String filterYearMonth, HttpServletRequest request) {
        return summaryService.branchMonthlySummaryList(filterYearMonth, request);
    }

    // 지사 가맹점 일정산 요약 리스트 호출API
    @GetMapping("branchFranchiseDaliySummaryList")
    @ApiOperation(value = "지사 가맹점 일정산 리스트", notes = "지사가 가맹점 일일정산 요약 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchFranchiseDaliySummaryList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterYearMonth") String filterYearMonth) {
        return summaryService.daliySummaryList(franchiseId, filterYearMonth,null);
    }

    // 지사 가맹점별 일정산 입금현황 호출API
    @GetMapping("branchFranchiseDailyStatusList")
    @ApiOperation(value = "본사 가맹점별 일정산입금 리스트", notes = "지사가 가맹점별 일정산입금 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchFranchiseDailyStatusList(@RequestParam("filterYearMonth") String filterYearMonth, HttpServletRequest request) {
        return summaryService.franchiseDailyStatusList(filterYearMonth, request);
    }



//@@@@@@@@@@@@@@@@@@@@@ 가맹점 일정산 내역 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 일정산 입금 리스트 호출API
    @GetMapping("branchReceiptDailyList")
    @ApiOperation(value = "지사의 일정산 입금 리스트", notes = "지사의 일정산 입금 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchReceiptDailyList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterFromDt") String filterFromDt,
                                                                            @RequestParam("filterToDt") String filterToDt) {
        return summaryService.branchReceiptDailyList(franchiseId, filterFromDt, filterToDt);
    }

    // 지사 일정산 입금 저장 호출API
    @PostMapping("branchDailySummarySave")
    @ApiOperation(value = "지사 일정산 저장", notes = "지사 일정산을 저장합니다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchDailySummarySave(HttpServletRequest request, @RequestParam("hsYyyymmdd") String hsYyyymmdd, @RequestParam("frCode") String frCode,
                                                                            @RequestParam("hrReceiptYyyymmdd") String hrReceiptYyyymmdd, @RequestParam("hrReceiptSaleAmt") Integer hrReceiptSaleAmt,
                                                                            @RequestParam("hrReceiptRoyaltyAmt") Integer hrReceiptRoyaltyAmt) {
        return summaryService.branchDailySummarySave(request, hsYyyymmdd, frCode, hrReceiptYyyymmdd, hrReceiptSaleAmt, hrReceiptRoyaltyAmt);
    }


//@@@@@@@@@@@@@@@@@@@@@ QR스캔이력 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // QR 건수 조회 왼쪽리스트 호출 API
    @GetMapping("branchQrCloseCntList")
    @ApiOperation(value = "가맹점 Qr마감 건수 리스트 호출", notes = "가맹점의 Qr마감 건수 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchQrCloseCntList(@RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return qrService.branchQrCloseCntList(filterFromDt, filterToDt, request);
    }

    // QR 건수 조회 오른쪽리스트 호출 API
    @GetMapping("branchQrCloseCntSubList")
    @ApiOperation(value = "가맹점 Qr마감 날짜별 리스트 호출", notes = "가맹점의 Qr마감 해당 날짜별 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> branchQrCloseCntSubList(@RequestParam("insertYyyymmdd") String insertYyyymmdd, HttpServletRequest request) {
        return qrService.branchQrCloseCntSubList(insertYyyymmdd, request);
    }

}