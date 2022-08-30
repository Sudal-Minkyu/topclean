package com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : 본사의 행사페이지용 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemGroupEventListDto {

    private String bgItemGroupcode; // 대분류코드

    private String bgName; // 대문류명칭

}
