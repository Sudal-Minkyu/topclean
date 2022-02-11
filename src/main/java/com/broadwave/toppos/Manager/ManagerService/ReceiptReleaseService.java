package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Process.Issue.Issue;
import com.broadwave.toppos.Manager.Process.Issue.IssueRepository;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.Photo;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.RequestDetailReleaseListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import com.broadwave.toppos.keygenerate.KeyGenerateService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    private final IssueRepository issueRepository;

    @Autowired
    public ReceiptReleaseService(TokenProvider tokenProvider, KeyGenerateService keyGenerateService, IssueRepository issueRepository,
                                 RequestDetailRepository requestDetailRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.keyGenerateService = keyGenerateService;
        this.tokenProvider = tokenProvider;
        this.requestDetailRepository = requestDetailRepository;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
        this.issueRepository = issueRepository;
    }

    //  접수테이블의 상태 변화 API - 지사출고 실행함수
    public ResponseEntity<Map<String, Object>> branchStateChange(List<List<Long>> fdIdList, Integer miDegree, HttpServletRequest request) {
        log.info("branchStateChange 호출");

        log.info("출고처리할 ID : "+fdIdList);
        log.info("출고차수 : "+miDegree);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        // stateType 상태값
        // "S2"이면 지사출고 페이지 버튼 "S2" -> "S4"
        log.info("지사출고 처리");
        for (int i = 1; i < fdIdList.size(); i++) {
            List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS2List(fdIdList.get(i));
//                log.info("requestDetailList : " + requestDetailList);

            String frCode = requestDetailList.get(0).getFrId().getFrCode();
            String miNo = keyGenerateService.keyGenerate("mr_issue", brCode+frCode+nowDate, login_id); // 지사출고 miNo 채번

            Issue newIssue = new Issue();
            newIssue.setBrCode(brCode);
            newIssue.setFrCode(frCode);
            newIssue.setMiNo(miNo);
            newIssue.setMiDegree(miDegree);
            newIssue.setMiDt(nowDate);
            newIssue.setMiTime(LocalDateTime.now());
            newIssue.setInsert_id(login_id);
            Issue issue = issueRepository.save(newIssue);

            for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : " + requestDetail.getFrId());
                requestDetail.setFdPreState("S2"); // 이전상태 값
                requestDetail.setFdPreStateDt(LocalDateTime.now());

                requestDetail.setFdState("S4");
                requestDetail.setFdStateDt(LocalDateTime.now());

                requestDetail.setFdS4Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                requestDetail.setFdS4Time(LocalDateTime.now());
                requestDetail.setFdS4Id(login_id);
                requestDetail.setMiId(issue);

                requestDetail.setModify_id(login_id);
                requestDetail.setModify_date(LocalDateTime.now());
            }
            requestDetailRepository.saveAll(requestDetailList);
        }

        return ResponseEntity.ok(res.success());
    }

    //  지사출고 - 세부테이블 지사입고 상태 리스트
    public ResponseEntity<Map<String, Object>> branchReceiptBranchInList(Long frId, LocalDateTime fromDt, LocalDateTime toDt, String isUrgent, HttpServletRequest request) {
        log.info("franchiseReceiptBranchInList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        // 지사출고 페이지에 보여줄 리스트 호출
        List<RequestDetailReleaseListDto> requestDetailReleaseListDtos = requestDetailRepositoryCustom.findByRequestDetailReleaseList(brCode, frId, fromDt, toDt, isUrgent);
        data.put("gridListData",requestDetailReleaseListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    public ResponseEntity<Map<String, Object>> branchReceiptBranchInCancelList(Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo, HttpServletRequest request) {
        log.info("branchReceiptBranchInCancelList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

//        // 지사출고 페이지에 보여줄 리스트 호출
//        List<RequestDetailReleaseListDto> requestDetailReleaseListDtos = requestDetailRepositoryCustom.findByRequestDetailReleaseList(brCode, frId, fromDt, toDt, tagNo);
//        data.put("gridListData",requestDetailReleaseListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
}
