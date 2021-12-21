package com.broadwave.toppos.Account;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public AccountRestController(ModelMapper modelMapper, AccountService accountService) {
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }
















}
