package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseManagerListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseRepositoryCustom;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailTagSearchListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    private final FranchiseRepositoryCustom franchiseRepositoryCustom;

    @Autowired
    public ManagerService(TokenProvider tokenProvider, FranchiseRepositoryCustom franchiseRepositoryCustom, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.franchiseRepositoryCustom = franchiseRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
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

        List<FranchiseManagerListDto> franchiseManagerListDtos =  franchiseRepositoryCustom.findByManagerInFranchise(brCode);
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

        List<RequestDetailTagSearchListDto> requestDetailTagSearchListDtos =  requestDetailRepositoryCustom.findByRequestDetailTagSearchList(brCode, franchiseId, tagNo);
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



//        data.put("noticeData",noticeData); // 공지사항 리스트(본사의 공지사항) - 최근3개
//        data.put("checkformData",checkformData); // 확인폼(검품) 리스트 - 최근3개만
//        data.put("chartFranchReceipData",chartFranchReceipData); // 1주일간의 가맹점 접수금액
//        data.put("chartFranchOpenData",chartFranchOpenData); // 가맹점 오픈 현황
//        data.put("chartBranchReleaseData",chartBranchReleaseData); // 1주일간의 지사 출고금액

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
