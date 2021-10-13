package com.broadwave.toppos.Account;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-10-13
 * Time :
 * Remark :
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListDto {
    private String userid;
    private String username;
    private AccountRole role;

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role.getDesc();
    }
}
