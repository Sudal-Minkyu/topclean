package com.broadwave.toppos.Manager.outsourcingPrice;

import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListDto;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.broadwave.toppos.Head.Item.Group.A.QItemGroup.itemGroup;
import static com.broadwave.toppos.Head.Item.Group.B.QItemGroupS.itemGroupS;
import static com.broadwave.toppos.Head.Item.Group.C.QItem.item;
import static com.broadwave.toppos.Head.Item.Price.QItemPrice.itemPrice;
import static com.broadwave.toppos.Manager.outsourcingPrice.QOutsourcingPrice.outsourcingPrice;

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

    // 외주 가격 리스트 출력
    @Override
    public List<OutsourcingPriceListDto> findByOutsourcingPriceList(String biItemcode, String biName, String bpOutsourcingYn, String brCode) {

        return jpaQueryFactory
                .select(Projections.constructor(
                                OutsourcingPriceListDto.class,
                                item.biItemcode,
                                itemGroup.bgName,
                                itemGroupS.bsName,
                                item.biName,
                                itemPrice.setDt,
                                itemPrice.bpBasePrice,
                                itemPrice.bpAddPrice,
                                itemPrice.bpPriceA,
                                outsourcingPrice.bpOutsourcingYn.coalesce("N"),
                                outsourcingPrice.bpOutsourcingPrice.coalesce(0),
                                outsourcingPrice.bpRemark.coalesce("")
                        )
                )
                .from(item)
                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .join(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .join(itemPrice).on(item.biItemcode.eq(itemPrice.biItemcode))
                .leftJoin(outsourcingPrice).on(item.biItemcode.eq(outsourcingPrice.biItemcode).and(outsourcingPrice.brCode.eq(brCode)))
                .where(itemPrice.closeDt.eq("99991231")
                        .and(biItemcodeEq(biItemcode))
                        .and(biNameEq(biName))
                        .and(bpOutsourcingYnEq(bpOutsourcingYn))
                )
                .fetch();
    }

    private BooleanExpression biItemcodeEq(String biItemcode) {
        return biItemcode == "" ? null : item.biItemcode.startsWith(biItemcode);
    }

    private BooleanExpression biNameEq(String biName) {
        return biName == "" ? null : item.biName.contains(biName);
    }

    private BooleanExpression bpOutsourcingYnEq(String bpOutsourcingYn) {
        return bpOutsourcingYn == "" ? null : outsourcingPrice.bpOutsourcingYn.eq(bpOutsourcingYn);
    }


    // 지사 외주가격 조회 쿼리
    @Override
    public OutsourcingPriceDto findByOutsourcingPrice(String biItemcode, String brCode) {
        QOutsourcingPrice outsourcingPrice = QOutsourcingPrice.outsourcingPrice;
        return jpaQueryFactory
                .select(Projections.constructor(OutsourcingPriceDto.class,
                                outsourcingPrice.bpOutsourcingYn,
                                outsourcingPrice.bpOutsourcingPrice
                        )
                )
                .from(outsourcingPrice)
                .where(outsourcingPrice.biItemcode.eq(biItemcode).and(outsourcingPrice.brCode.eq(brCode)))
                .fetchOne();
    }

    @Override
    public OutsourcingPrice findByOutsourcingPriceAll(String biItemcodes, String brCode) {
        return jpaQueryFactory
                .selectFrom(outsourcingPrice)
                .where(outsourcingPrice.brCode.eq(brCode).and(outsourcingPrice.biItemcode.eq(biItemcodes)))
                .fetchOne();
    }

}
