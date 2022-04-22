package com.broadwave.toppos.Account;

import com.broadwave.toppos.Account.AcountDtos.AccountListDto;
import com.broadwave.toppos.Account.AcountDtos.AccountRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository,  PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return account;
    }

    public Account saveUpdate(Account account){
        accountRepository.save(account);
        return account;
    }

    public void findByAccountDelete(Account account) {
        accountRepository.delete(account);
    }

    public Account updateAccount(Account account){
        return this.accountRepository.save(account);
    }

    // 고정ID 값으로 찾기
//    public Optional<Account> findById(Long id){
//        return accountRepository.findById(id);
//    }

    // 입력ID 값으로 찾기
    public Optional<Account> findByUserid(String userid){
        return accountRepository.findByUserid(userid);
    }

    public List<AccountListDto> findByAccountList(String s_userid, String s_username, AccountRole s_role, String s_frCode, String s_brCode) {
        return accountRepository.findByAccountList(s_userid, s_username, s_role, s_frCode, s_brCode);
    }

}
