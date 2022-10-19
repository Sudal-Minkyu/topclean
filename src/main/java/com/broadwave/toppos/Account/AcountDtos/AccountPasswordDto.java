package com.broadwave.toppos.Account.AcountDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2022-12-29
 * Time :
 * Remark : 나의정보 비밀번호수정 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPasswordDto {
    private String oldpassword;
    private String newpassword;
    private String passwordconfirm;
}
