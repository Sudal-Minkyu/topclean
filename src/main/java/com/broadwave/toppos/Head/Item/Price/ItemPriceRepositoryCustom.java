package com.broadwave.toppos.Head.Item.Price;

import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceListDto;
import com.broadwave.toppos.Head.Item.ItemDtos.UserItemPriceSortDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.ItemPriceSetDtDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark :
 */
public interface ItemPriceRepositoryCustom {

    List<ItemPriceListDto> findByItemPriceList(String bgName, String biItemcode, String biName, String setDt);

    ItemPriceDto findByItemPrice(String biItemcode, String closeDt);   // 상품 가격 검색

    List<UserItemPriceSortDto> findByUserItemPriceSortList(String frCode, String nowDate);

    List<ItemPriceSetDtDto> findByItemPriceSetDtList();

    List<ItemPriceNotList> findByItemPricedNotList(String bgName, String biItemcode, String biName, String setDt);

}
