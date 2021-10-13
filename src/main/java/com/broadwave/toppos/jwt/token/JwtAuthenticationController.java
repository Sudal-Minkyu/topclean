package com.broadwave.toppos.jwt.token;

import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.jwt.user.JwtRequest;
import com.broadwave.toppos.jwt.user.JwtUserDetailsService;
import com.broadwave.toppos.jwt.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailService;

    @PostMapping(value = "/api/authenticate")
    public ResponseEntity<Map<String,Object>> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {

        log.info("로그인 아이디 : "+authenticationRequest.getUserid());
        log.info("로그인 아비밀번호 : "+authenticationRequest.getPassword());

        // 아이디와 비밀번호를 확인한다.
        authenticate(authenticationRequest.getUserid(),authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUserid());
        final String token = jwtTokenUtil.generateToken(userDetails);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("토큰 발급성공 : "+token);

        data.put("token",token);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    private void authenticate(String username, String password){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException ex){
            throw new DisabledException("아이디와 비밀번호를 확인해주세요.",ex);
        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("인증되지않은 계정",ex);
        }
    }

}