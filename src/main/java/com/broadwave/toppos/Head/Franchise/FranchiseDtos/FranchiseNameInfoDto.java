package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-03-04
 * Time :
 * Remark : Toppos 모바일전용 가맹점명 호출 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseNameInfoDto {
    private String frName; // 가맹점명
}
