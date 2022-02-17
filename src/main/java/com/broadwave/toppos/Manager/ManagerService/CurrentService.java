package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailBranchStoreCurrentListDto;
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

@Slf4j
@Service
public class CurrentService {

    private final TokenProvider tokenProvider;

    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    @Autowired
    public CurrentService(TokenProvider tokenProvider, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    //  지사 - 입고현황 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchStoreCurrentList(Long franchiseId, String filterFromDt, String filterToDt, String type, HttpServletRequest request) {
        log.info("branchStoreCurrentList 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<RequestDetailBranchStoreCurrentListDto> requestDetailBranchStoreCurrentListDtos =  requestDetailRepositoryCustom.findByRequestDetailBranchStoreCurrentList(brCode, franchiseId, filterFromDt, filterToDt, type);
        data.put("gridListData",requestDetailBranchStoreCurrentListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }











}
