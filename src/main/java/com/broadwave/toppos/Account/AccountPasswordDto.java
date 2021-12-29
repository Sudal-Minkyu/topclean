package com.broadwave.toppos.Account;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2022-12-29
 * Time :
 * Remark : 나의정보 비밀번호수정 Dto
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountPasswordDto {
    private String oldpassword;
    private String newpassword;
    private String passwordconfirm;

    public String getOldpassword() {
        return oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public String getPasswordconfirm() {
        return passwordconfirm;
    }
}
