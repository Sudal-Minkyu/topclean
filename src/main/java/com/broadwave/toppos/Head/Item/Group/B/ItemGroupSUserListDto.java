package com.broadwave.toppos.Head.Item.Group.B;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 상품순서 가져오는 리스트 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupSUserListDto {

    private String bsItemGroupcodeS; // 중분류코드
    private String bgItemGroupcode; // 대분류코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭

}
