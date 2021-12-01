package com.broadwave.toppos.Head.Item.Price;

import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark :
 */
public interface ItemPriceRepositoryCustom {
    List<ItemPriceListDto> findByItemPriceList();

    ItemPriceDto findByItemPrice(String biItemcode, String highClassYn, String setDtReplace);

}
