package com.broadwave.toppos.Head.Item.Group.C.ItemDtos;

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
public class ItemEventListDto {

    private String biItemcode; // 상품코드
    private String biName; // 상품명

}
