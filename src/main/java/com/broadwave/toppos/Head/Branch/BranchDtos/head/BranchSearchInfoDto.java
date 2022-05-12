package com.broadwave.toppos.Head.Branch.BranchDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-05-12
 * Time :
 * Remark : Toppos 본사 현황 검색을 위해 지사 정보 가져오는 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchSearchInfoDto {

    private Long brId; // 지사 ID
    private String brName; // 지사명

}
