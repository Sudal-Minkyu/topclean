package com.broadwave.toppos.User.GroupSort;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : 상품그룹정렬 업데이트 Dto
 */
@Setter
public class GroupSortSet {

    // 상품그룹정렬 업데이트 list
    private ArrayList<GroupSortMapperDto> list;

    public ArrayList<GroupSortMapperDto> getList() {
        return list;
    }

}
