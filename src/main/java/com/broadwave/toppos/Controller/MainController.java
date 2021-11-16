package com.broadwave.toppos.Controller;

import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : Toppos 메인컨트롤러
 */
@Slf4j
@Controller
public class MainController {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    AccountService accountService;

    // 메인 페이지 이동
    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> index(HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("아이디 : "+login_id);
        Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Authorization"));
        String role = authentication.getAuthorities().toString();
        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String a = (String) claims.get("brCode");
//        String b = (String) claims.get("frCode");
//        log.info("a : "+a);
//        log.info("b : "+b);
        log.info("권한 : "+role);
        switch (role) {
            case "[ROLE_MANAGER]":
            case "[ROLE_NORMAL]":
                log.info("지사장 또는 지사일반 로그인");
                data.put("link","/manager");
                break;
            case "[ROLE_HEAD]":
            case "[ROLE_CALCULATE]":
                log.info("본사일반 또는 회계관리 로그인");
                data.put("link","/head");
                break;
            case "[ROLE_USER]":
                log.info("가맹점주 로그인");
                data.put("link","/user");
                break;
            default:
                log.info("관리자 로그인");
                data.put("link","/admin");
                break;
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

}
