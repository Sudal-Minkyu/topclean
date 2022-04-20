package com.broadwave.toppos.Account.AcountDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    private String userid;
    private String password;

//    public Account toAccount(PasswordEncoder passwordEncoder) {
//        return Account.builder()
//                .userid(userid)
//                .password(passwordEncoder.encode(password))
//                .role(AccountRole.ROLE_USER)
//                .build();
//    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userid, password);
    }
}
