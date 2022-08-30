package com.broadwave.toppos.Head.Branch.BranchDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-07-28
 * Time :
 * Remark : Toppos 본사 행사용 지사 조회를 위한 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchEventListDto {

    private Long branchId; // 지사 ID
    private String brName; // 지사명

}
