package com.broadwave.toppos.Account;

import com.broadwave.toppos.Jwt.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    AccountRepository accountRepository;
    AccountRepositoryCustom accountRepositoryCustom;
    PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountRepositoryCustom accountRepositoryCustom, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.accountRepositoryCustom = accountRepositoryCustom;
        this.passwordEncoder = passwordEncoder;
    }

    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return account;
    }

    // 고정ID 값으로 찾기
    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }

    // 입력ID 값으로 찾기
    public Optional<Account> findByUserid(String userid){
        return accountRepository.findByUserid(userid);
    }

    public List<AccountListDto> findAllByAccountList() {
        return accountRepositoryCustom.findAllByAccountList();
    }

}
