package com.broadwave.toppos.User.ItemSort;

import com.broadwave.toppos.User.ItemSort.ItemSortDtos.ItemSortListDto;
import com.broadwave.toppos.User.ItemSort.ItemSortDtos.ItemSortUpdateDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark :
 */
public interface ItemSortRepositoryCustom {
    List<ItemSortListDto> itemSortList(String frCode, String biItemMgroup, String nowDate);

    List<ItemSortUpdateDto> findByItemSortList(String biItemMgroup, String frCode);
}
