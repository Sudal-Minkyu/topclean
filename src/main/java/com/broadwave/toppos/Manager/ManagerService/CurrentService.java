package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head.*;
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
 * UpDate : 2022-05-31
 * Remark : Toppos 지사 지사입고현황, 체류세탁물현황, 출고현황, 강제출고현황, 미출고현황, 가맹반송현황 페이지조회 - 서비스
 *               Toppos 본사 지사입고현황, 지사출고현황, 강제출고현황, 강제입고현황, 미출고현황, 재세탁 입고현황 페이지조회 - 서비스
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

//        log.info("franchiseId : "+franchiseId);
//        log.info("filterFromDt : "+filterFromDt);
//        log.info("filterToDt : "+filterToDt);
//        log.info("type : "+type);

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

//        log.info("frCode : "+frCode);
//        log.info("fdS4Dt : "+fdS4Dt);
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

    // 본사 지사입고현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headStoreInputList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("headStoreInputList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestIncomeListDto> requestIncomeListDtos =  requestDetailRepository.findByHeadIncomeList(branchId, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestIncomeListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 지사입고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headStoreInputSubList(Long branchId, Long franchiseId, String fdS2Dt) {
        log.info("headStoreInputSubList 호출");

        log.info("branchId  : "+branchId);
        log.info("franchiseId  : "+franchiseId);
        log.info("fdS2Dt  : "+fdS2Dt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<RequestIncomeListSubDto> requestIncomeListSubDtos = requestDetailRepository.findByHeadIncomeSubList(branchId, franchiseId, fdS2Dt);
//        log.info("requestIncomeListSubDtos : "+requestIncomeListSubDtos);
        log.info("requestIncomeListSubDtos.size() : "+requestIncomeListSubDtos.size());

        data.put("gridListData",requestIncomeListSubDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 지사출고현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headReleaseInputList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("headReleaseInputList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("branchId : "+branchId);
        log.info("franchiseId : "+franchiseId);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestOutgoListDto> requestOutgoListDtos =  requestDetailRepository.findByHeadOutgoList(branchId, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestOutgoListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사출고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headReleaseInputSubList(Long branchId, Long franchiseId, String fdS4Dt) {
        log.info("headReleaseInputSubList 호출");

        log.info("branchId  : "+branchId);
        log.info("franchiseId  : "+franchiseId);
        log.info("fdS4Dt  : "+fdS4Dt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<RequestOutgoListSubDto> requestOutgoListSubDtos = requestDetailRepository.findByHeadOutgoSubList(branchId, franchiseId, fdS4Dt);
        log.info("requestOutgoListSubDtos : "+requestOutgoListSubDtos);

        data.put("gridListData",requestOutgoListSubDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 강제출고현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headForceReleaseInputList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("headForceReleaseInputList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestForceOutgoListDto> requestForceOutgoListDtos =  requestDetailRepository.findByHeadForceOutgoList(branchId, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestForceOutgoListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 강제출고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> forceReleaseInputSubList(Long branchId, Long franchiseId, String fdS7Dt) {
        log.info("headForceReleaseInputSubList 호출");

        log.info("branchId  : "+branchId);
        log.info("franchiseId  : "+franchiseId);
        log.info("fdS7Dt  : "+fdS7Dt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<RequestForceOutgoListSubDto> requestForceOutgoListSubDtos = requestDetailRepository.findByHeadForceOutgoSubList(branchId, franchiseId, fdS7Dt);
        log.info("requestForceOutgoListSubDtos : "+requestForceOutgoListSubDtos);

        data.put("gridListData",requestForceOutgoListSubDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 강제입고현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> forceStoreInputList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("forceStoreInputList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestForceIncomeListDto> requestForceIncomeListDtos =  requestDetailRepository.findByHeadForceIncomeList(branchId, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestForceIncomeListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 강제입고현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> forceStoreInputSubList(Long branchId, Long franchiseId, String fdS8Dt) {
        log.info("forceStoreInputSubList 호출");

        log.info("branchId  : "+branchId);
        log.info("franchiseId  : "+franchiseId);
        log.info("fdS8Dt  : "+fdS8Dt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<RequestForceIncomeListSubDto> requestForceIncomeListSubDtos = requestDetailRepository.findByHeadForceIncomeSubList(branchId, franchiseId, fdS8Dt);
        log.info("requestForceIncomeListSubDtos : "+requestForceIncomeListSubDtos);

        data.put("gridListData",requestForceIncomeListSubDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 미출고 현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headNoReleaseInputList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("headNoReleaseInputList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestNoOutgoListDto> requestNoOutgoListDtos =  requestDetailRepository.findByHeadNoOutgoList(branchId, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestNoOutgoListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 미출고 현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> headNoReleaseInputSubList(Long branchId, Long franchiseId, String fdEstimateDt) {
        log.info("headNoReleaseInputSubList 호출");

        log.info("branchId  : "+branchId);
        log.info("franchiseId  : "+franchiseId);
        log.info("fdEstimateDt  : "+fdEstimateDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<RequestNoOutgoListSubDto> requestNoOutgoListSubDtos = requestDetailRepository.findByHeadNoOutgoSubList(branchId, franchiseId, fdEstimateDt);
        log.info("requestNoOutgoListSubDtos : "+requestNoOutgoListSubDtos);

        data.put("gridListData",requestNoOutgoListSubDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 재세탁입고 현황 - 왼쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> retryList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("retryList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestRetryListDto> requestRetryListDtos =  requestDetailRepository.findByHeadRetryList(branchId, franchiseId, filterFromDt, filterToDt);
        data.put("gridListData",requestRetryListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 재세탁입고 현황 - 오른쪽 리스트 호출API
    public ResponseEntity<Map<String, Object>> retrySubList(Long branchId, Long franchiseId, String frYyyymmdd) {
        log.info("retrySubList 호출");

        log.info("branchId  : "+branchId);
        log.info("franchiseId  : "+franchiseId);
        log.info("frYyyymmdd  : "+frYyyymmdd);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<RequestRetryListSubDto> requestRetryListSubDtos = requestDetailRepository.findByHeadRetrySubList(branchId, franchiseId, frYyyymmdd);
        log.info("requestRetryListSubDtos : "+requestRetryListSubDtos);

        data.put("gridListData",requestRetryListSubDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
