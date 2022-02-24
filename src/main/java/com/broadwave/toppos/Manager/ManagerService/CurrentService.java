package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-02-15
 * Time :
 * Remark : Toppos 지사 지사입고현황, 체류세탁물현황, 출고현황, 강제출고현황, 미출고현황, 가맹반송현황 페이지조회 - 서비스
 */
@Slf4j
@Service
public class CurrentService {

    private final TokenProvider tokenProvider;

    private final RequestDetailRepository requestDetailRepository;

    @Autowired
    public CurrentService(TokenProvider tokenProvider, RequestDetailRepository requestDetailRepository){
        this.tokenProvider = tokenProvider;
        this.requestDetailRepository = requestDetailRepository;
    }

    //  지사 입고현황, 체류세탁물현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchStoreCurrentList(Long franchiseId, String filterFromDt, String filterToDt, String type, HttpServletRequest request) {
        log.info("branchStoreCurrentList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("franchiseId : "+franchiseId);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        log.info("type : "+type);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchStoreCurrentListDto> requestDetailBranchStoreCurrentListDtos =  requestDetailRepository.findByRequestDetailBranchStoreCurrentList(brCode, franchiseId, filterFromDt, filterToDt, type);
        data.put("gridListData",requestDetailBranchStoreCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사입고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchStoreInputList(String frCode, String fdS2Dt, HttpServletRequest request) {
        log.info("branchStoreInputList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchInputCurrentListDto> requestDetailBranchInputCurrentListDtos =  requestDetailRepository.findByRequestDetailBranchInputCurrentList(brCode, frCode, fdS2Dt);
        data.put("gridListData",requestDetailBranchInputCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  체류세탁물현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchStoreRemainList(String frCode, String fdS2Dt, HttpServletRequest request) {
        log.info("branchStoreRemainList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchRemainCurrentListDto> requestDetailBranchRemainCurrentListDtos =  requestDetailRepository.findByRequestDetailBranchRemainCurrentList(brCode, frCode, fdS2Dt);
        data.put("gridListData",requestDetailBranchRemainCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사출고현황, 지사강제출고현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchReleaseCurrentList(Long franchiseId, String filterFromDt, String filterToDt, String type, HttpServletRequest request) {
        log.info("branchReleaseCurrentList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchReleaseCurrentListDto> requestDetailBranchReleaseCurrentListDtos =  requestDetailRepository.findByRequestDetailBranchReleaseCurrentList(brCode, franchiseId, filterFromDt, filterToDt, type);
        List<HashMap<String,Object>> releaseViewData = new ArrayList<>();
        HashMap<String,Object> releaseInfo;
        if(requestDetailBranchReleaseCurrentListDtos != null) {
            for (RequestDetailBranchReleaseCurrentListDto requestDetailBranchReleaseCurrentListDto : requestDetailBranchReleaseCurrentListDtos) {
                releaseInfo = new HashMap<>();
                releaseInfo.put("frCode", requestDetailBranchReleaseCurrentListDto.getFrCode());
                releaseInfo.put("frName", requestDetailBranchReleaseCurrentListDto.getFrName());
                if (type.equals("1")) {
                    releaseInfo.put("fdS4Dt", requestDetailBranchReleaseCurrentListDto.getFdSDt());
                } else {
                    releaseInfo.put("fdS7Dt", requestDetailBranchReleaseCurrentListDto.getFdSDt());
                }
                releaseInfo.put("output_cnt", requestDetailBranchReleaseCurrentListDto.getOutput_cnt());
                releaseInfo.put("tot_amt", requestDetailBranchReleaseCurrentListDto.getTot_amt());
                releaseViewData.add(releaseInfo);
            }
        }
        data.put("gridListData",releaseViewData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사출고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchReleaseInputList(String frCode, String fdS4Dt, HttpServletRequest request) {
        log.info("branchReleaseInputList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchReleaseCurrentRightListDto> requestDetailBranchReleaseCurrentRightListDtos =  requestDetailRepository.findByRequestDetailBranchReleaseCurrentRightList(brCode, frCode, fdS4Dt);
        data.put("gridListData",requestDetailBranchReleaseCurrentRightListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사강제출고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchReleaseForceList(String frCode, String fdS7Dt, HttpServletRequest request) {
        log.info("branchReleaseForceList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchReleaseForceCurrentRightListDto> requestDetailBranchReleaseForceCurrentRightListDtos =  requestDetailRepository.findByRequestDetailBranchReleaseForceCurrentRightList(brCode, frCode, fdS7Dt);
        data.put("gridListData",requestDetailBranchReleaseForceCurrentRightListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 미출고현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchUnReleaseCurrentList(Long franchiseId, String filterFromDt, String filterToDt, String type, HttpServletRequest request) {
        log.info("branchUnReleaseCurrentList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchUnReleaseCurrentListDto> requestDetailBranchUnReleaseCurrentListDtos =  requestDetailRepository.findByRequestDetailBranchUnReleaseList(brCode, franchiseId, filterFromDt, filterToDt, type);
        data.put("gridListData",requestDetailBranchUnReleaseCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 미출고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchUnReleaseCurrentInputList(String frCode, String filterFromDt, String filterToDt, String type, HttpServletRequest request) {
        log.info("branchUnReleaseCurrentInputList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchUnReleaseCurrentRightListDto> requestDetailBranchUnReleaseCurrentRightListDtos =  requestDetailRepository.findByRequestDetailBranchUnReleaseCurrentRightList(brCode, frCode, filterFromDt, filterToDt, type);
        data.put("gridListData",requestDetailBranchUnReleaseCurrentRightListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹반송현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchReturnCurrentList(Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("branchReturnCurrentList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchReturnCurrentListDto> requestDetailBranchReturnCurrentListDtos =  requestDetailRepository.findByRequestDetailBranchReturnCurrentList(brCode, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestDetailBranchReturnCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹반송현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchReturnCurrentInputList(String frCode, String fdS3Dt, HttpServletRequest request) {
        log.info("branchReturnCurrentInputList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchReturnCurrentRightListDto> requestDetailBranchReturnCurrentRightListDtos =  requestDetailRepository.findByRequestDetailBranchReturnRightCurrentList(brCode, frCode, fdS3Dt);
        data.put("gridListData",requestDetailBranchReturnCurrentRightListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
