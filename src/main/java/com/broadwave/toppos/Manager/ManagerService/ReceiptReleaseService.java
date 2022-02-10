package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.RequestDetailReleaseListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    private final TokenProvider tokenProvider;

    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    @Autowired
    public ReceiptReleaseService(TokenProvider tokenProvider, RequestDetailRepository requestDetailRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
       this.tokenProvider = tokenProvider;
        this.requestDetailRepository = requestDetailRepository;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    //  접수테이블의 상태 변화 API - 지사출고 실행함수
    public ResponseEntity<Map<String, Object>> branchStateChange(List<Long> fdIdList, HttpServletRequest request) {
        log.info("branchStateChange 호출");

        log.info("fdIdList : "+fdIdList);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        // stateType 상태값
        // "S2"이면 지사출고 페이지 버튼 "S2" -> "S4"
        log.info("지사출고 처리");
        List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS2List(fdIdList);
        log.info("requestDetailList : "+requestDetailList);
        for (RequestDetail requestDetail : requestDetailList) {
            log.info("가져온 frID 값 : "+requestDetail.getFrId());
            requestDetail.setFdPreState("S2"); // 이전상태 값
            requestDetail.setFdPreStateDt(LocalDateTime.now());

            requestDetail.setFdState("S4");
            requestDetail.setFdStateDt(LocalDateTime.now());

            requestDetail.setFdS4Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            requestDetail.setFdS4Time(LocalDateTime.now());
            requestDetail.setFdS4Id(login_id);

            requestDetail.setModify_id(login_id);
            requestDetail.setModify_date(LocalDateTime.now());
        }
        requestDetailRepository.saveAll(requestDetailList);

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


}
