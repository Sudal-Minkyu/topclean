package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-22
 * Time :
 * Remark : Toppos 가맹점 FranchiseSearchDto 삭제조회용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseSearchDto {
    private String frName; // 가맹점명
}
