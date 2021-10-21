package com.broadwave.toppos.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2021-10-08
 * Time :
 * Remark
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapperDto {
    private String userid;
    private String password;
    private AccountRole role;
    private String username;
}
