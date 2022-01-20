package com.broadwave.toppos.User.GroupSort.GroupSortDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupSortDto {

    private String frCode; // 가맹점 코드 3자리
    private String bgItemGroupcode; // 대분류 코드
    private Integer bgSort; // 정렬순서

    public String getFrCode() {
        return frCode;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public Integer getBgSort() {
        return bgSort;
    }
}
