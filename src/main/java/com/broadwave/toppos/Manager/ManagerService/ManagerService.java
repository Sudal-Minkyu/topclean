package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Head.Franohise.FranchiseManagerListDto;
import com.broadwave.toppos.Head.Franohise.FranchiseRepositoryCustom;
import com.broadwave.toppos.Jwt.token.TokenProvider;
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

    private final FranchiseRepositoryCustom franchiseRepositoryCustom;

    @Autowired
    public ManagerService(TokenProvider tokenProvider, FranchiseRepositoryCustom franchiseRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.franchiseRepositoryCustom = franchiseRepositoryCustom;
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



}
