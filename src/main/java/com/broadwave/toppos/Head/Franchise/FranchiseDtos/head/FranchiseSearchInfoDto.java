package com.broadwave.toppos.Head.Franchise.FranchiseDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-05-12
 * Time :
 * Remark : Toppos 본사 현황 검색을 위해 가맹점 정보 가져오는 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseSearchInfoDto {

    private Long franchiseId; // 가맹점 ID
    private Long branchId; // 소속된지사 ID
    private String frName; // 가맹점명

}
