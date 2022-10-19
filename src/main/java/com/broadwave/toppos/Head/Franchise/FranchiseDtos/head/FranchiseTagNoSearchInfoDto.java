package com.broadwave.toppos.Head.Franchise.FranchiseDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark : Toppos 본사 택번호 조회를 위해 가맹점 정보 가져오는 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseTagNoSearchInfoDto {

    private Long franchiseId; // 가맹점 ID
    private Long branchId; // 소속된지사 ID
    private String frName; // 가맹점명
    private String frTagNo; // 가맹점 택번호 2자리 or 3자리

}
