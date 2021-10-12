package com.broadwave.toppos.jwt.user;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountService.findByUserid(userid);

        Account account = accountOptional.orElseThrow(()->new UsernameNotFoundException("존재하지 않은 유저입니다!"));
        return new org.springframework.security.core.userdetails.User(account.getUserid(),account.getPassword(),new ArrayList<>());

    }
}