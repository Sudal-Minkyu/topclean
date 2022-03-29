package com.broadwave.toppos.Jwt.auth;

import com.broadwave.toppos.Account.AccountRequestDto;
import com.broadwave.toppos.Jwt.dto.TokenDto;
import com.broadwave.toppos.Jwt.dto.TokenRequestDto;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(HttpServletRequest request, @RequestBody AccountRequestDto accountRequestDto) {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        TokenDto tokenDto = authService.login(request, accountRequestDto);

        data.put("tokenDto",tokenDto);

        if(tokenDto != null){
            return ResponseEntity.ok(res.dataSendSuccess(data));
        }else{
            return ResponseEntity.ok(res.fail("문자","비밀번호가 틀렸습니다.",null,null));
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<Map<String,Object>> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
//        log.info("토큰 새로받기 시도");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        TokenDto tokenDto = authService.reissue(tokenRequestDto);
//        log.info("새로받은 tokenDto : "+tokenDto);
//        log.info("새로받은 AccessToken : "+tokenDto.getAccessToken());
        data.put("새로받은 tokenDto",tokenDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
