package com.broadwave.toppos.User.GroupSort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 MapperDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSortMapperDto {

    private String bgItemGroupcode; // 대분류 코드
    private Integer bgSort; // 정렬순서

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public Integer getBgSort() {
        return bgSort;
    }

}
