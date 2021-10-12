package com.broadwave.toppos.Account;

import com.broadwave.toppos.jwt.exception.ErrorCode;
import com.broadwave.toppos.jwt.exception.UserIdDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository,PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account save(Account account){
        Optional<Account> optionalAccount = accountRepository.findByUserid(account.getUserid());
        if( optionalAccount.isPresent()){
            throw new UserIdDuplicateException("이미 존재하는 아이디입니다.", ErrorCode.EMAIL_DUPLICATION);
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return account;
    }

    public Optional<Account> findByUserid(String userid){
        return accountRepository.findByUserid(userid);
    }
}
