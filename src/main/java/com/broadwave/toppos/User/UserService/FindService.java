package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Find.Find;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Find.FindListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Find.FindRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestFindListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.common.AjaxResponse;
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
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 상품정렬 서비스
 */
@Slf4j
@Service
public class FindService {

    private final TokenProvider tokenProvider;
    private final FindRepository findRepository;
    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;

    @Autowired
    public FindService(TokenProvider tokenProvider, FindRepository findRepository, RequestRepository requestRepository, RequestDetailRepository requestDetailRepository){
        this.tokenProvider = tokenProvider;
        this.findRepository = findRepository;
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
    }

    // 지사 물건찾기 리스트 호출
    public ResponseEntity<Map<String, Object>> objectFind(Long franchiseId, String filterFromDt, String filterToDt, String ffState,HttpServletRequest request) {
        log.info("objectFind 호출");

        log.info("franchiseId : "+franchiseId);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        log.info("ffState : "+ffState);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String brCode = (String) claims.get("brCode"); // 현재 지사 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<FindListDto> findListDtos = findRepository.findByFindList(franchiseId, brCode, filterFromDt, filterToDt, ffState);
        log.info("findListDtos : "+findListDtos);

        data.put("gridListData",findListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사 물건찾기 확인 업데이트
    @Transactional
    public ResponseEntity<Map<String, Object>> branchObjectFindCheck(List<Long> ffIdList, HttpServletRequest request) {
        log.info("branchObjectFindCheck 호출");

        log.info("ffIdList : "+ffIdList);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        int successNum = findRepository.findByFindCheckUpdate(ffIdList,login_id);
        log.info("성공한 업데이트 수 : "+successNum);

        return ResponseEntity.ok(res.success());
    }

    // 가맹점 물건찾기 할 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseObjectFind(Long bcId, String filterFromDt, String filterToDt, HttpServletRequest request, String searchTag) {
        log.info("franchiseObjectFind 호출");

        log.info("bcId : "+bcId);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        log.info("searchTag : "+searchTag);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 가맹점 코드 : "+frCode);

        List<RequestFindListDto> requestFindListDtos = requestRepository.findByRequestFindList(bcId, frCode, filterFromDt, filterToDt, searchTag);
        data.put("gridListData",requestFindListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 물건찾기 할 항목 저장 호출
    public ResponseEntity<Map<String, Object>> franchiseObjectFindSave(List<Long> fdIdList, HttpServletRequest request) {
        log.info("franchiseObjectFindSave 호출");

        log.info("fdIdList : "+fdIdList);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("금일날짜 : "+nowDate);

        List<Find> findList = new ArrayList<>();
        List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailList(fdIdList);
        Find find;
        for (RequestDetail requestDetail : requestDetailList) {
            find = new Find();
            find.setFdId(requestDetail);
            find.setFrCode(frCode);
            find.setBrCode(frbrCode);
            find.setFfYyyymmdd(nowDate);
            find.setFfState("01");
            find.setInsert_id(login_id);
            find.setInsert_date(LocalDateTime.now());
            findList.add(find);
        }

        findRepository.saveAll(findList);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
