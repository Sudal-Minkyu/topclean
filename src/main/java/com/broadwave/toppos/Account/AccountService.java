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

    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }

    public Optional<Account> findByUserid(String userid){
        return accountRepository.findByUserid(userid);
    }

    public List<AccountListDto> findAllByAccountList() {
        return accountRepositoryCustom.findAllByAccountList();
    }

    @Transactional(readOnly = true)
    public AccountResponseDto getMemberInfo(String userid) {
        return accountRepository.findByUserid(userid)
                .map(AccountResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public AccountResponseDto getMyInfo() {
        return accountRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(AccountResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

}
