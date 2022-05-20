package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Process.Issue.Issue;
import com.broadwave.toppos.Manager.Process.Issue.IssueDispatchDto;
import com.broadwave.toppos.Manager.Process.Issue.IssueRepository;
import com.broadwave.toppos.Manager.Process.IssueForce.IssueForce;
import com.broadwave.toppos.Manager.Process.IssueForce.IssueForceRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import com.broadwave.toppos.keygenerate.KeyGenerateService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-02-08
 * Time :
 * Remark : Toppos 지사 - 출고 전용서비스
 */
@Slf4j
@Service
public class ReceiptReleaseService {

    // 현재 날짜 받아오기
    LocalDateTime localDateTime = LocalDateTime.now();
    private final  String nowDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    private final TokenProvider tokenProvider;
    private final KeyGenerateService keyGenerateService;

    private final RequestDetailRepository requestDetailRepository;

    private final IssueRepository issueRepository;
    private final IssueForceRepository issueForceRepository;

    @Autowired
    public ReceiptReleaseService(TokenProvider tokenProvider, KeyGenerateService keyGenerateService, IssueRepository issueRepository, IssueForceRepository issueForceRepository,
                                 RequestDetailRepository requestDetailRepository){
        this.keyGenerateService = keyGenerateService;
        this.tokenProvider = tokenProvider;
        this.requestDetailRepository = requestDetailRepository;
        this.issueRepository = issueRepository;
        this.issueForceRepository = issueForceRepository;
    }

    //  접수테이블의 상태 변화 API - 지사출고 실행함수
    @Transactional
    public ResponseEntity<Map<String, Object>> branchStateChange(List<List<Long>> fdIdList, List<List<String>> fdS4TypeList, Integer miDegree, HttpServletRequest request) {
        log.info("branchStateChange 호출");

        log.info("출고처리할 ID : "+fdIdList);
        log.info("출고차수 : "+miDegree);
        log.info("fdS4TypeList : "+fdS4TypeList);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<String> miNoList = new ArrayList<>();

        // stateType 상태값
        // "S2"이면 지사출고 페이지 버튼 "S2" -> "S4"
        log.info("지사출고 처리");
        for (int i = 1; i < fdIdList.size(); i++) {
            List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS2OrS7List(fdIdList.get(i));
//            log.info("requestDetailList : " + requestDetailList);

            String frCode = requestDetailList.get(0).getFrId().getFrCode();
            String miNo = keyGenerateService.keyGenerate("mr_issue", brCode+frCode+nowDate, login_id); // 지사출고 miNo 채번
            miNoList.add(miNo);
            Issue newIssue = new Issue();
            newIssue.setBrCode(brCode);
            newIssue.setFrCode(frCode);
            newIssue.setMiNo(miNo);
            newIssue.setMiDegree(miDegree);
            newIssue.setMiDt(nowDate);
            newIssue.setMiTime(LocalDateTime.now());
            newIssue.setInsert_id(login_id);
            Issue issue = issueRepository.save(newIssue);

            for (int j=0; j<requestDetailList.size(); j++) {
//                log.info("가져온 frID 값 : " + requestDetailList.get(j).getFrId());
                requestDetailList.get(j).setFdPreState(requestDetailList.get(j).getFdState()); // 이전상태 값
                requestDetailList.get(j).setFdPreStateDt(LocalDateTime.now());

                requestDetailList.get(j).setFdS4Type(fdS4TypeList.get(i).get(j));

                requestDetailList.get(j).setFdState("S4");
                requestDetailList.get(j).setFdStateDt(LocalDateTime.now());
                requestDetailList.get(j).setFdS4Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                requestDetailList.get(j).setFdS4Time(LocalDateTime.now());
                requestDetailList.get(j).setFdS4Id(login_id);

                requestDetailList.get(j).setFdBrState("S4");
                requestDetailList.get(j).setFdBrStateTime(LocalDateTime.now());

                requestDetailList.get(j).setMiId(issue);

                requestDetailList.get(j).setModify_id(login_id);
                requestDetailList.get(j).setModify_date(LocalDateTime.now());
            }

            requestDetailRepository.saveAll(requestDetailList);
        }

        data.put("miNoList",miNoList);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사출고 - 세부테이블 지사입고 상태 리스트
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInList(Long frId, String fromDt, String toDt, String isUrgent, HttpServletRequest request) {
        log.info("branchReceiptBranchInList 호출");

        log.info("fromDt : "+fromDt);
        log.info("toDt : "+toDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        log.info("frId : "+frId);
        log.info("isUrgent : "+isUrgent);
        // 지사출고 페이지에 보여줄 리스트 호출
        List<RequestDetailReleaseListDto> requestDetailReleaseListDtos = requestDetailRepository.findByRequestDetailReleaseList(brCode, frId, fromDt, toDt, isUrgent);
        data.put("gridListData",requestDetailReleaseListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사출고 - 세부테이블 강제출고 리스트
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInForceList(Long frId, String fromDt, String toDt, HttpServletRequest request) {
        log.info("branchReceiptBranchInForceList 호출");

//        log.info("fromDt : "+fromDt);
//        log.info("toDt : "+toDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        // 지사출고 페이지에 보여줄 리스트 호출
        List<RequestDetailReleaseListDto> requestDetailReleaseListDtos = requestDetailRepository.findByRequestDetailReleaseForceList(brCode, frId, fromDt, toDt);
        data.put("gridListData",requestDetailReleaseListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사출고 취소 - 세부테이블 지사출고 상태 리스트
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInCancelList(Long frId, String fromDt, String toDt, String tagNo, HttpServletRequest request) {
        log.info("branchReceiptBranchInCancelList 호출");

//        log.info("frId : "+frId);
        log.info("fromDt : "+fromDt);
        log.info("toDt : "+toDt);
//        log.info("tagNo : "+tagNo);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        // 지사출고취소 페이지에 보여줄 리스트 호출
        List<RequestDetailReleaseCancelListDto> requestDetailReleaseCancelListDtos = requestDetailRepository.findByRequestDetailReleaseCancelList(brCode, frId, fromDt, toDt, tagNo);
        data.put("gridListData",requestDetailReleaseCancelListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

//    //  지사반송 - 세부테이블 반송 처리 할 리스트
//    public ResponseEntity<Map<String, Object>> branchReceiptReturnList(Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo, HttpServletRequest request) {
//        log.info("branchReceiptReturnList 호출");
//
//        log.info("frId : "+frId);
//        log.info("tagNo : "+tagNo);
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
//        log.info("현재 접속한 지사 코드 : "+brCode);
//
//        // 반송 처리 할 리스트 호출
//        List<RequestDetailBranchReturnListDto> requestDetailBranchReturnListDtos = requestDetailRepository.findByRequestDetailBranchReturnList(brCode, frId, fromDt, toDt, tagNo);
//
//        List<Long> fdIdList = new ArrayList<>();
//        for (RequestDetailBranchReturnListDto requestDetailBranchReturnListDto : requestDetailBranchReturnListDtos) {
//            fdIdList.add(requestDetailBranchReturnListDto.getFdId());
//        }
//
//        List<InspeotYnDto> inspeotYnDtoBList = inspeotRepositoryCustom.findByInspeotYnBAndType3(fdIdList); // 지사검품(확인품) 여부
//
//        for (int i=0; i< requestDetailBranchReturnListDtos.size(); i++) {
//            for(InspeotYnDto inspeotYnDto : inspeotYnDtoBList){
//                if(!inspeotYnDto.getFdId().equals(requestDetailBranchReturnListDtos.get(i).getFdId())){
//                    requestDetailBranchReturnListDtos.set(i,null);
//                }
//            }
//        }
//
//        data.put("gridListData",requestDetailBranchReturnListDtos);
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }

    //  가맹점 강제츨고 - 세부테이블 강제출고 처리 할 리스트
    public ResponseEntity<Map<String, Object>> branchReceiptForceReleaseList(Long frId, String fromDt, String toDt, String tagNo, HttpServletRequest request) {
        log.info("branchReceiptForceReleaseList 호출");

//        log.info("frId : "+frId);
//        log.info("tagNo : "+tagNo);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        // 가맹점 강제츨고 처리 할 리스트 호출
        List<RequestDetailBranchForceListDto> requestDetailBranchForceListDtos = requestDetailRepository.findByRequestDetailBranchForceList(brCode, frId, fromDt, toDt, tagNo);
        data.put("gridListData",requestDetailBranchForceListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  가맹점 반품츨고 - 세부테이블 반품츨고 처리 할 리스트
    public ResponseEntity<Map<String, Object>> branchReceiptReturnReleaseList(Long frId, String fromDt, String toDt, String tagNo, HttpServletRequest request) {
        log.info("branchReceiptReturnReleaseList 호출");

//        log.info("frId : "+frId);
//        log.info("tagNo : "+tagNo);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        // 가맹점 반품츨고 처리 할 리스트 호출
        List<RequestDetailBranchReturnListDto> requestDetailBranchForceListDtos = requestDetailRepository.findByRequestDetailBranchReturnList(brCode, frId, fromDt, toDt, tagNo);
        data.put("gridListData",requestDetailBranchForceListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  접수테이블의 상태 변화 API - 지사출고취소, 지사반송, 가맹점입고 실행 함수
    @Transactional
    public ResponseEntity<Map<String, Object>> branchRelease(List<Long> fdIdList, String type, HttpServletRequest request) {
        log.info("branchRelease 호출");

        log.info("fdIdList : "+fdIdList);
        log.info("type : "+type);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        if(fdIdList.size() == 0) {
            switch (type) {
                case "1": {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP029.getCode(), "출고취소 할 "+ResponseErrorCode.TP029.getDesc(), null, null));
                }
                case "2": {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP029.getCode(), "반품 할 "+ResponseErrorCode.TP029.getDesc(), null, null));
                }
                case "3": {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP029.getCode(), "강제출고 할 "+ResponseErrorCode.TP029.getDesc(), null, null));
                }
                default:
                    return ResponseEntity.ok(res.fail("문자", "처리 타입이 존재하지 않습니다.", "문자", "관리자에게 문의해주세요."));
            }
        }else{
            switch (type) {
                case "1": { // 지사출고취소
                    log.info("지사출고취소 처리");
                    List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS4List(fdIdList);
//             log.info("requestDetailList : "+requestDetailList);
                    for (RequestDetail requestDetail : requestDetailList) {
                        if(requestDetail.getFdState().equals("S4")){
                            requestDetail.setFdPreState(requestDetail.getFdState()); // 이전상태 값
                            requestDetail.setFdPreStateDt(LocalDateTime.now());
                            if(requestDetail.getFdS4Type().equals("01")){
                                requestDetail.setFdState("S2");
                                requestDetail.setFdBrState("S2");
                            }else{
                                requestDetail.setFdState("S7");
                                requestDetail.setFdBrState("S7");
                            }
                            requestDetail.setFdStateDt(LocalDateTime.now());

                            // 출고처리취소시 아래 값 모두 null 처리
                            requestDetail.setMiId(null);
                            requestDetail.setFdS4Dt(null);
                            requestDetail.setFdS4Time(null);
                            requestDetail.setFdS4Id(null);
                            requestDetail.setFdS4Type(null);

                            requestDetail.setFdBrStateTime(LocalDateTime.now());


                            requestDetail.setModify_id(login_id);
                            requestDetail.setModify_date(LocalDateTime.now());
                        }else{
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP028.getCode(), "지사출고취소 "+ResponseErrorCode.TP028.getDesc(), "문자", "새로고침이후 다시 시도해주세요."));
                        }
                    }
                    requestDetailRepository.saveAll(requestDetailList);
                    break;
                }
                case "2": { // 지사반품
                    log.info("지사반품 처리");
                    List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS3List(fdIdList);
                    log.info("requestDetailList : "+requestDetailList);
                    for (RequestDetail requestDetail : requestDetailList) {
//                        log.info("가져온 frID 값 : "+requestDetail.getFrId());
                        if(requestDetail.getFdState().equals("S2")) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                            requestDetail.setFdPreState(requestDetail.getFdState()); // 이전상태 값
                            requestDetail.setFdPreStateDt(LocalDateTime.now());

                            requestDetail.setFdState("S7");
                            requestDetail.setFdStateDt(LocalDateTime.now());
                            requestDetail.setFdS7Type("02");
                            requestDetail.setFdS7Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                            requestDetail.setFdS7Time(LocalDateTime.now());
                            requestDetail.setFdS7Id(login_id);

                            requestDetail.setModify_id(login_id);
                            requestDetail.setModify_date(LocalDateTime.now());

                            requestDetail.setFdBrState("S7");
                            requestDetail.setFdBrStateTime(LocalDateTime.now());

                        }else{
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP028.getCode(), "반품 "+ResponseErrorCode.TP028.getDesc(), "문자", "새로고침이후 다시 시도해주세요."));
                        }
                    }
                    requestDetailRepository.saveAll(requestDetailList);
                    break;
                }
                case "3": { // 가맹점강제출고
                    log.info("가맹점강제출고 처리");
                    IssueForce issueForce;
                    List<IssueForce> issueForceList = new ArrayList<>();
                    List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS3List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                    for (RequestDetail requestDetail : requestDetailList) {
                        if(requestDetail.getFdState().equals("S2")) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                            requestDetail.setFdPreState(requestDetail.getFdState()); // 이전상태 값
                            requestDetail.setFdPreStateDt(LocalDateTime.now());

                            requestDetail.setFdState("S7");
                            requestDetail.setFdStateDt(LocalDateTime.now());
                            requestDetail.setFdS7Type("01");
                            requestDetail.setFdS7Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                            requestDetail.setFdS7Time(LocalDateTime.now());
                            requestDetail.setFdS7Id(login_id);

                            requestDetail.setModify_id(login_id);
                            requestDetail.setModify_date(LocalDateTime.now());

                            requestDetail.setFdBrState("S7");
                            requestDetail.setFdBrStateTime(LocalDateTime.now());

                            issueForce = issueForceRepository.findByFdId(requestDetail.getId());
                            // 가맹점 강제 출고처리
                            if (issueForce == null) {
                                issueForce = new IssueForce();
                                issueForce.setFdId(requestDetail.getId());
                                issueForce.setFrCode(requestDetail.getFrId().getFrCode());
                                issueForce.setBrCode(requestDetail.getFrId().getBrCode());
                            }
                            issueForce.setMrDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                            issueForce.setMrTime(LocalDateTime.now());
                            issueForce.setInsert_id(login_id);
                            issueForceList.add(issueForce);
                        }else{
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP028.getCode(), "강제출고 "+ResponseErrorCode.TP028.getDesc(), "문자", "새로고침이후 다시 시도해주세요."));
                        }
                    }
                    requestDetailRepository.saveAll(requestDetailList);
                    issueForceRepository.saveAll(issueForceList);
                    break;
                }
                default:
                    return ResponseEntity.ok(res.fail("문자", "처리 타입이 존재하지 않습니다.", "문자", "관리자에게 문의해주세요."));
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 출고증인쇄 함수
    public ResponseEntity<Map<String, Object>> branchDispatchPrint(List<String> miNoList) {
        log.info("branchDispatchPrint 호출");

        log.info("miNoList : "+miNoList);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<IssueDispatchDto> issueDispatchDtos = issueRepository.findByDispatchPrintData(miNoList);
        log.info("issueDispatchDtos : "+issueDispatchDtos);
        data.put("issueDispatchDtos",issueDispatchDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사 외주출고 - 세부테이블 외주 출고처리 할 리스트 호출
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInOutsouringList(Long frId, String filterFromDt, String filterToDt, String isOutsourceable, HttpServletRequest request) {
        log.info("branchReceiptBranchInOutsouringList 호출");

        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        log.info("frId : "+frId);
        log.info("isOutsourceable : "+isOutsourceable);

        List<RequestDetailOutsourcingDeliveryListDto> requestDetailOutsourcingDeliveryListDtos = requestDetailRepository.findByRequestDetailOutsourcingDeliveryList(brCode, frId, filterFromDt, filterToDt, isOutsourceable);
        data.put("gridListData",requestDetailOutsourcingDeliveryListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}


