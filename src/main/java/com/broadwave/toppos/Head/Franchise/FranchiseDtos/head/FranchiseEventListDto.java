package com.broadwave.toppos.Head.Franchise.FranchiseDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-07-28
 * Time :
 * Remark : Toppos 본사 행사용 가맹점 조회를 위한 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseEventListDto {

    private Long franchiseId; // 가맹점 ID
    private String frCode; // 가맹점코드
    private String frName; // 가맹점명

}
