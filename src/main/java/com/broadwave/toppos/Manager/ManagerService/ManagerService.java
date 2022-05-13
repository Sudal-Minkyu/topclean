package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountRepository;
import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Account.AcountDtos.AccountBranchHeaderDto;
import com.broadwave.toppos.Head.Branch.Branch;
import com.broadwave.toppos.Head.Branch.BranchDtos.BranchInfoDto;
import com.broadwave.toppos.Head.Branch.BranchRepository;
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
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestWeekAmountDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLogDto;
import com.broadwave.toppos.User.UserLoginLog.UserLoginLogRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final IssueRepository issueRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ManagerService(TokenProvider tokenProvider, NoticeService noticeService, RequestRepository requestRepository, AccountService accountService, AccountRepository accountRepository,
                          UserLoginLogRepository userLoginLogRepository, FranchiseRepository franchiseRepository, RequestDetailRepository requestDetailRepository,
                          InspeotRepositoryCustom inspeotRepositoryCustom, IssueRepository issueRepository, BranchRepository branchRepository, PasswordEncoder passwordEncoder){
        this.tokenProvider = tokenProvider;
        this.noticeService = noticeService;
        this.userLoginLogRepository = userLoginLogRepository;
        this.franchiseRepository = franchiseRepository;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.issueRepository = issueRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //  현 지사의 소속된 지사명, 이름 호출
    public ResponseEntity<Map<String, Object>> branchHeaderData(HttpServletRequest request) {
        log.info("branchHeaderData 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        AccountBranchHeaderDto accountBranchHeaderDto =  accountRepository.findByBranchHeaderInfo(login_id);
        data.put("accountHeaderData", accountBranchHeaderDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
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

//        log.info("franchiseId : "+franchiseId);
//        log.info("tagNo : "+tagNo);
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

        // 공지사항(본사) + 공지사항(지사) 리스트 limit 5
        List<NoticeListDto> noticeListDtos = noticeService.branchMainNoticeList(brCode);

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("금일날짜 : "+nowDate);

        List<FranchiseManagerListDto> franchiseManagerListDtos = franchiseRepository.findByManagerInFranchise(brCode);
        List<UserLoginLogDto> chartFranchOpenListDtos = userLoginLogRepository.findByFranchiseLog(brCode, nowDate);
        List<InspeotMainListDto> inspeotMainListDtos = inspeotRepositoryCustom.findByInspeotB1(brCode, 5, null);

        int franchiseAll = franchiseManagerListDtos.size();
        int franchiseLog = chartFranchOpenListDtos.size();
        // 가맹점 오픈 현황
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

        // 일주일전 Local
//        LocalDateTime weekDays = LocalDateTime.now().minusDays(7);
//        String formatWeekDays = weekDays.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        log.info("weekDays : "+weekDays);
//        log.info("formatWeekDays : "+formatWeekDays);

        // 1주일간의 가맹점 접수금액
        List<RequestWeekAmountDto> requestWeekAmountDtos = requestRepository.findByRequestWeekAmount(brCode);
//        log.info("requestWeekAmountDtos : "+requestWeekAmountDtos);

        // 1주일간의 일자별 가맹점 접수금액
        List<RequestWeekAmountDto> requestWeekDaysAmountDtos = requestRepository.findByRequestWeekDaysAmount(brCode);
        log.info("receiptWeekDaysAmountDtos : "+requestWeekDaysAmountDtos);

        // 1주일간의 지사 출고금액
        List<IssueWeekAmountDto> issueWeekAmountDtos = issueRepository.findByIssueWeekAmount(brCode);
        log.info("issueWeekAmountDtos : "+issueWeekAmountDtos);

        data.put("noticeData",noticeListDtos); // 공지사항 리스트(본사의 공지사항) - 최근5개
        data.put("inspeotList",inspeotMainListDtos);  // 검품리스트 5개호출

        data.put("chartFranchOpenData",chartFranchOpenData); // 가맹점 오픈 현황

        data.put("requestWeekAmountData",requestWeekAmountDtos); // 1주일간의 가맹점 접수금액
        data.put("requestWeekDaysAmountDtos",requestWeekDaysAmountDtos); // 1주일간의 일자별 가맹점 접수금액
        data.put("issueWeekAmountDtos",issueWeekAmountDtos); // 1주일간의 지사 출고금액

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 현재 지사의 정보 호출하기
    public ResponseEntity<Map<String, Object>> branchMyInfo(HttpServletRequest request) {
        log.info("branchMyInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        BranchInfoDto branchInfoDto =  accountRepository.findByBranchInfo(login_id);
        data.put("branchInfoDto",branchInfoDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사정보관리 수정 API
    public ResponseEntity<Map<String, Object>> branchInfoSave(String brName, String brTelNo, HttpServletRequest request) {
        log.info("branchInfoSave 호출");

        log.info("brName : "+brName);
        log.info("brTelNo : "+brTelNo);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        Optional<Branch> branchOptional = branchRepository.findByBrCode(brCode);
        if(branchOptional.isPresent()){
            if(!brName.equals("")) {
                branchOptional.get().setBrName(brName);
            }
            if(!brTelNo.equals("")){
                branchOptional.get().setBrTelNo(brTelNo);
            }
            branchRepository.save(branchOptional.get());
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 지사의 "+ResponseErrorCode.TP005.getDesc(), null, null));
        }

        return ResponseEntity.ok(res.success());
    }

    // 지사 나의정보관리 수정 API
    @Transactional
    public ResponseEntity<Map<String, Object>> branchMyInfoSave(String userEmail, String userTel, String nowPassword, String newPassword, String checkPassword, HttpServletRequest request) {
        log.info("branchMyInfoSave 호출");

        log.info("userEmail : "+userEmail);
        log.info("userTel : "+userTel);
        log.info("nowPassword : "+nowPassword);
        log.info("newPassword : "+newPassword);
        log.info("checkPassword : "+checkPassword);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        Optional<Account> optionalAccount = accountRepository.findByUserid(login_id);

        //수정일때
        if(!optionalAccount.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "나의 "+ResponseErrorCode.TP005.getDesc(), "문자", "고객센터에 문의해주세요."));
        }else{
            if(!userEmail.equals("")) {
                optionalAccount.get().setUseremail(userEmail);
            }
            if(!userTel.equals("")){
                optionalAccount.get().setUsertel(userTel);
            }

            if(!nowPassword.equals("") && !newPassword.equals("") && !checkPassword.equals("") ) {

                //현재암호비교
                if (!passwordEncoder.matches(nowPassword,optionalAccount.get().getPassword())){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP020.getCode(), "현재 "+ResponseErrorCode.TP020.getDesc(), null, null));
                }

                if(!newPassword.equals(checkPassword)){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP021.getCode(), ResponseErrorCode.TP021.getDesc(), null, null));
                }
                optionalAccount.get().setPassword(passwordEncoder.encode(checkPassword));
            }

            Account accountSave =  accountService.saveUpdate(optionalAccount.get());
            log.info("사용자정보(패스워드)수정 성공 아이디 : " + accountSave.getUserid() +"'" );
        }


        return ResponseEntity.ok(res.success());
    }

}
