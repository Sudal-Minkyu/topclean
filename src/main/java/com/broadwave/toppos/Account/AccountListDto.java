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
    private AccountRole role;
    private String username;
    private String usertel;
    private String useremail;
    private String frCode;
    private String brCode;
    private String userremark;

    public String getUsertel() {
        return usertel;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getFrCode() {
        return frCode;
    }

    public String getBrCode() {
        return brCode;
    }

    public String getUserremark() {
        return userremark;
    }

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
