package com.broadwave.toppos.Account.AcountDtos;

import com.broadwave.toppos.Account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private String userid;

    public static AccountResponseDto of(Account account) {
        return new AccountResponseDto(account.getUserid());
    }
}
