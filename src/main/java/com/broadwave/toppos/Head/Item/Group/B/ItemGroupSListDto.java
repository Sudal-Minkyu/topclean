package com.broadwave.toppos.Head.Item.Group.B;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupSListDto {

    private String bsItemGroupcodeS; // 중분류코드
    private String bgItemGroupcode; // 대분류코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭
    private String bsRemark; // 특이사항
    private String bsUseYn; // 사용여부

}
