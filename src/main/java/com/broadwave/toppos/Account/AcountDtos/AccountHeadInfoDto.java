package com.broadwave.toppos.Account.AcountDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-05-10
 * Time :
 * Remark : Toppos  본사 정보가져오는 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountHeadInfoDto {

    private String userId; // 계정 아이디
    private String userEmail; // 계정 이메일
    private String userTel; // 계정 전화번호

}
