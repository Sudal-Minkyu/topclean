package com.broadwave.toppos.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2021-10-08
 * Time :
 * Remark
 */
@Slf4j
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapperDto {
    private String userid;
    private String password;
    private AccountRole role;
    private String username;
    private String usertel;
    private String useremail;
    private String frCode; // 가맹점코드 3자리
    private String brCode; // 지사코드 2자리
    private String userremark;

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public AccountRole getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getUsertel() {
        return usertel;
    }

    public String getUseremail() {
        return useremail;
    }

    public String getFrCode() {
        if(role.getCode().equals("ROLE_CALCULATE") || role.getCode().equals("ROLE_HEAD") || role.getCode().equals("ROLE_ADMIN") || role.getCode().equals("ROLE_MANAGER") || role.getCode().equals("ROLE_NORMAL")){
            return "not";
        }else{
            return frCode;
        }
    }

    public String getBrCode() {
        if(role.getCode().equals("ROLE_CALCULATE") || role.getCode().equals("ROLE_HEAD") || role.getCode().equals("ROLE_ADMIN") || role.getCode().equals("ROLE_USER")){
            return "no";
        }else{
            return brCode;
        }
    }

    public String getUserremark() {
        return userremark;
    }
}
