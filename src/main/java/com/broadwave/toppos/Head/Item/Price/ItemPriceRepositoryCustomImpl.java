package com.broadwave.toppos.Head.Item.Price;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class ItemPriceRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemPriceRepositoryCustom {

    public ItemPriceRepositoryCustomImpl() {
        super(ItemPrice.class);
    }


}
