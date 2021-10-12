package com.broadwave.toppos.jwt.user;

import com.broadwave.toppos.Account.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignVo {
    String userid;
    String password;
    String name;
    AccountRole role;
}