package com.broadwave.toppos.Account;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-10-09
 * Time :
 * Remark : Toppos Account RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/account") //  ( 권한 : 어드민, 본사일반 )
public class AccountRestController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    TokenProvider tokenProvider;

    @Autowired
    public AccountRestController(TokenProvider tokenProvider, ModelMapper modelMapper, AccountService accountService) {
        this.modelMapper = modelMapper;
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
    }
















}
