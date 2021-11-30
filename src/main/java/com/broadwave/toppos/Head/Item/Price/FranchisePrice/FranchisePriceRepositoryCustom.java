package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-30
 * Time :
 * Remark :
 */
public interface FranchisePriceRepositoryCustom {
    List<FranchisePriceListDto> findByFranchisePriceList(String frCode);
}
