package com.broadwave.toppos.Head.Branoh.BranchDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-04-20
 * Time :
 * Remark : Toppos 지사 정보가져오는 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchInfoDto {

    private String brCode; // 지사코드
    private String brName; // 지사명
    private String brTelNo; // 지사 전화번호

    private String userId; // 계정 아이디
    private String userEmail; // 계정 이메일
    private String userTel; // 계정 전화번호

}
