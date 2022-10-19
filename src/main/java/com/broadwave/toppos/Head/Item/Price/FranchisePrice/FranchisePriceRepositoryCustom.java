package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.FranchisePriceListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-30
 * Time :
 * Remark :
 */
public interface FranchisePriceRepositoryCustom {
    List<FranchisePriceListDto> findByFranchisePriceList(String frCode);    // 가맹점 특정품목가격 리스트
}
