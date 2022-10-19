package com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos;

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
public class ItemGroupSEventListDto {

    private String bsItemGroupcodeS; // 중분류코드
    private String bsName; // 중분류명칭

}
