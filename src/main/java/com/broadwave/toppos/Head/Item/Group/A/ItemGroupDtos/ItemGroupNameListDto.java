package com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-26
 * Time :
 * Remark : Toppos 상품그룹관리 대분류 NameListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupNameListDto {

    private String bgName; // 대문류명칭

    public String getBgName() {
        return bgName;
    }
}
