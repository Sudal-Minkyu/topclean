package com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 가맹점접수페이지 전용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItemGroupSListDto {

    private String bgItemGroupcode; // 대분류코드
    private String bsItemGroupcodeS; // 중분류코드
    private String bsName; // 중분류명칭

}
