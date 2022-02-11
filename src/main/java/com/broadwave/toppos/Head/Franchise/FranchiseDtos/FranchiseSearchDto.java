package com.broadwave.toppos.Head.Franchise.FranchiseDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-22
 * Time :
 * Remark : Toppos 가맹점 FranchiseSearchDto 삭제조회용
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseSearchDto {

    private String frName; // 가맹점명

    public String getFrName() {
        return frName;
    }
}
