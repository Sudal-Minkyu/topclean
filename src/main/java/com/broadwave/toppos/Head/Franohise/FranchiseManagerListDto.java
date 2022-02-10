package com.broadwave.toppos.Head.Franohise;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-02-10
 * Time :
 * Remark : Toppos 지사의 소속된 가맹점명 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseManagerListDto {

//    private String frCode; // 가맹점코드 3자리
    private String frName; // 가맹점명

}
