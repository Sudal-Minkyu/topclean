package com.broadwave.toppos.Head.Item.Price;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public void itemPriceSave(ItemPrice itemPrice) {

    }

    @Override
    public List<ItemPriceListDto> findByItemPriceList() {
        QItemPrice itemPrice = QItemPrice.itemPrice;
        QItem item= QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<ItemPriceListDto> query = from(itemPrice)
                .join(item).on(itemPrice.biItemcode.eq(item.biItemcode))
                .join(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS))
                .select(Projections.constructor(ItemPriceListDto.class,

                        itemPrice.biItemcode,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        itemPrice.setDt,
                        itemPrice.closeDt,

                        itemPrice.bpBasePrice,
                        itemPrice.highClassYn,
                        itemPrice.bpAddPrice,

                        itemPrice.bpPriceA,
                        itemPrice.bpPriceB,
                        itemPrice.bpPriceC,
                        itemPrice.bpPriceD,
                        itemPrice.bpPriceE,

                        itemPrice.biRemark
                ));

        return query.fetch();
    }

}