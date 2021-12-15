package com.broadwave.toppos.Head.Item.Price;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark :
 */
public interface ItemPriceRepositoryCustom {
    List<ItemPriceListDto> findByItemPriceList();

    ItemPriceDto findByItemPrice(String biItemcode, String closeDt);

    List<UserItemPriceSortDto> findByUserItemPriceSortList(String frCode, String nowDate);
}
