package com.broadwave.toppos.User.ItemSort.ItemSortDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 상품 정렬 MapperDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSortMapperDto {

    private String biItemcode; // 대분류 코드
    private Integer bfSort; // 정렬순서

    public String getBiItemcode() {
        return biItemcode;
    }

    public Integer getBfSort() {
        return bfSort;
    }

}
