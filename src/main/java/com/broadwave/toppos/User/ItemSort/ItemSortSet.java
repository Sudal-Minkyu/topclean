package com.broadwave.toppos.User.ItemSort;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : 상품정렬 업데이트 SortSet
 */
@Setter
public class ItemSortSet {

    // 상품정렬 업데이트 list
    private ArrayList<ItemSortMapperDto> list;

    public ArrayList<ItemSortMapperDto> getList() {
        return list;
    }

}