package com.broadwave.toppos.Jwt.auth;

import com.broadwave.toppos.Account.AccountRequestDto;
import com.broadwave.toppos.Account.AccountResponseDto;
import com.broadwave.toppos.Jwt.dto.TokenDto;
import com.broadwave.toppos.Jwt.dto.TokenRequestDto;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

//    @PostMapping("/signup")
//    public ResponseEntity<AccountResponseDto> signup(@RequestBody AccountRequestDto accountRequestDto) {
//        return ResponseEntity.ok(authService.signup(accountRequestDto));
//    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AccountRequestDto accountRequestDto) {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        TokenDto tokenDto = authService.login(accountRequestDto);

        data.put("tokenDto",tokenDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    @PostMapping("/reissue")
    public ResponseEntity<Map<String,Object>> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        log.info("토큰 새로받기 시도");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        TokenDto tokenDto = authService.reissue(tokenRequestDto);
        log.info("새로받은 tokenDto : "+tokenDto);
        log.info("새로받은 AccessToken : "+tokenDto.getAccessToken());
        data.put("새로받은 tokenDto",tokenDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
