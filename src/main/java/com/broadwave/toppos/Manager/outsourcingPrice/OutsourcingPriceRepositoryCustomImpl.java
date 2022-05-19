package com.broadwave.toppos.Manager.outsourcingPrice;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupS;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.broadwave.toppos.Head.Item.Group.A.QItemGroup.itemGroup;
import static com.broadwave.toppos.Head.Item.Group.B.QItemGroupS.itemGroupS;
import static com.broadwave.toppos.Head.Item.Group.C.QItem.item;

/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 QueryDSL 구현체
 */
public class OutsourcingPriceRepositoryCustomImpl implements OutsourcingPriceRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;

    public OutsourcingPriceRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<OutsourcingPriceListDto> findByOutsourcingPriceList(String biItemcode, String biName, String bpOutsourcingYn, String brCode) {
        return null;
//
//        List<> fetch = jpaQueryFactory
//                .select(Projections.constructor(
//                            OutsourcingPriceListDto.class,
//                            )
//                )
//                .from(item)
//                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS))
//                .join(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
//                .fetch();

    }
}
