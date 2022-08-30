package com.broadwave.toppos.Head.Item.Group.C.ItemDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-24
 * Time :
 * Remark : Toppos 상품그룹관리 소재 상품 ItemDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private String biItemcode; // 상품코드
    private String bgItemGroupcode; // 대분류코드
    private String bsItemGroupcodeS; // 중분류코드
    private String biItemSequence; // 상품순번
    private String biName; // 상품명
    private String biUseYn; // 사용여부
    private String biRemark; // 특이사항

}
