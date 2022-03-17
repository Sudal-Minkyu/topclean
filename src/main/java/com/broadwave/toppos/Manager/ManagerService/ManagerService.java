package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseManagerListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.Head.HeadService.NoticeService;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeListDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Process.Issue.IssueRepository;
import com.broadwave.toppos.Manager.Process.Issue.IssueWeekAmountDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMainListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailTagSearchListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestWeekAmountDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLogDto;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLogRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos 지사 전용 - 서비스
 */
@Slf4j
@Service
public class ManagerService {

    private final TokenProvider tokenProvider;

    private final NoticeService noticeService;
    private final FranchiseRepository franchiseRepository;
    private final UserLoginLogRepository userLoginLogRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestRepository requestRepository;
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final IssueRepository issueRepository;

    @Autowired
    public ManagerService(TokenProvider tokenProvider, NoticeService noticeService, RequestRepository requestRepository,
                          UserLoginLogRepository userLoginLogRepository, FranchiseRepository franchiseRepository, RequestDetailRepository requestDetailRepository,
                          InspeotRepositoryCustom inspeotRepositoryCustom, IssueRepository issueRepository){
        this.tokenProvider = tokenProvider;
        this.noticeService = noticeService;
        this.userLoginLogRepository = userLoginLogRepository;
        this.franchiseRepository = franchiseRepository;
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.issueRepository = issueRepository;
    }

    //  현 지사의 소속된 가맹점명 리스트 호출(앞으로 공용으로 쓰일 것)
    public ResponseEntity<Map<String, Object>> branchBelongList(HttpServletRequest request) {
        log.info("managerBelongList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<FranchiseManagerListDto> franchiseManagerListDtos =  franchiseRepository.findByManagerInFranchise(brCode);
        data.put("franchiseList",franchiseManagerListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  TAG번호조회 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchTagSearchList(Long franchiseId, String tagNo, HttpServletRequest request) {
        log.info("branchTagSearchList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailTagSearchListDto> requestDetailTagSearchListDtos =  requestDetailRepository.findByRequestDetailTagSearchList(brCode, franchiseId, tagNo);
        data.put("gridListData",requestDetailTagSearchListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 현재 로그인한 지사 정보 가져오기
    public ResponseEntity<Map<String, Object>> branchInfo(HttpServletRequest request) {
        log.info("branchInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<NoticeListDto> noticeListDtos = noticeService.branchMainNoticeList();

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("금일날짜 : "+nowDate);
        List<FranchiseManagerListDto> franchiseManagerListDtos = franchiseRepository.findByManagerInFranchise(brCode);
        List<UserLoginLogDto> chartFranchOpenListDtos = userLoginLogRepository.findByFranchiseLog(brCode, nowDate);
        List<InspeotMainListDto> inspeotMainListDtos = inspeotRepositoryCustom.findByInspeotB1(brCode, 3, null);

        int franchiseAll = franchiseManagerListDtos.size();
        int franchiseLog = chartFranchOpenListDtos.size();
        List<HashMap<String,Object>> chartFranchOpenData = new ArrayList<>();
        HashMap<String,Object> chartFranchOpenInfo;
        for(int i=0; i<2; i++){
            chartFranchOpenInfo = new HashMap<>();
            if(i==0){
                chartFranchOpenInfo.put("category", "오픈");
                chartFranchOpenInfo.put("value", franchiseLog);
            }else{
                chartFranchOpenInfo.put("category", "미오픈");
                chartFranchOpenInfo.put("value", franchiseAll-franchiseLog);
            }
            chartFranchOpenData.add(chartFranchOpenInfo);
        }

        List<String> frNameList = new ArrayList<>();
        for (FranchiseManagerListDto franchiseManagerListDto : franchiseManagerListDtos) {
            frNameList.add(franchiseManagerListDto.getFrName());
        }

        // 일주일전 LocalDataTime
        LocalDateTime weekDays = LocalDateTime.now().minusDays(7);
        String formatWeekDays = weekDays.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        log.info("weekDays : "+weekDays);
//        log.info("formatWeekDays : "+formatWeekDays);
        List<RequestWeekAmountDto> requestWeekAmountDtos = requestRepository.findByRequestWeekAmount(brCode, frNameList, weekDays);
//        log.info("requestWeekAmountDtos : "+requestWeekAmountDtos);
        List<IssueWeekAmountDto> issueWeekAmountDtos = issueRepository.findByIssueWeekAmount(brCode, formatWeekDays);

        data.put("noticeData",noticeListDtos); // 공지사항 리스트(본사의 공지사항) - 최근3개
        data.put("inspeotList",inspeotMainListDtos);  // 검품리스트 3개호출
        data.put("requestWeekAmountData",requestWeekAmountDtos); // 1주일간의 가맹점 접수금액
        data.put("chartFranchOpenData",chartFranchOpenData); // 가맹점 오픈 현황
        data.put("issueWeekAmountDtos",issueWeekAmountDtos); // 1주일간의 지사 출고금액

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
