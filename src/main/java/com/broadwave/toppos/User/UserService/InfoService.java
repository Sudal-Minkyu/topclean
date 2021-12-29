package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Head.Franohise.FranchisInfoDto;
import com.broadwave.toppos.Head.Franohise.FranchisUserDto;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 나의정보 서비스
 */
@Slf4j
@Service
public class InfoService {

    private final HeadService headService;
    private final TokenProvider tokenProvider;

    @Autowired
    public InfoService(TokenProvider tokenProvider, HeadService headService){
        this.tokenProvider = tokenProvider;
        this.headService = headService;
    }

    // 현재 가맹점의 정보 호출하기
    public ResponseEntity<Map<String, Object>> myInfo(HttpServletRequest request) {
        log.info("myInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        FranchisUserDto franchisInfoDto = headService.findByFranchiseUserInfo(frCode);
        data.put("franchisInfoDto",franchisInfoDto);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }













}
