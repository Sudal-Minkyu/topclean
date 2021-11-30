package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-30
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class FranchisePriceRepositoryCustomImpl extends QuerydslRepositorySupport implements FranchisePriceRepositoryCustom {

    public FranchisePriceRepositoryCustomImpl() {
        super(FranchisePrice.class);
    }

    @Override
    public List<FranchisePriceListDto> findByFranchisePriceList(String frCode) {
        QFranchisePrice franchisePrice = QFranchisePrice.franchisePrice;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<FranchisePriceListDto> query = from(item)
                .join(item).on(franchisePrice.biItemcode.eq(item.biItemcode))
                .join(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS))
                .select(Projections.constructor(FranchisePriceListDto.class,
                        franchisePrice.biItemcode,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        franchisePrice.highClassYn,
                        franchisePrice.bfPrice,
                        franchisePrice.bfRemark
                ));

        query.where(itemGroup.bgItemGroupcode.eq(frCode));

        return query.fetch();
    }

}
