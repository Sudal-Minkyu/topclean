package com.broadwave.toppos.Head.Item.Price;

import com.broadwave.toppos.Head.Franohise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.QFranchisePrice;
import com.broadwave.toppos.User.ItemSort.QItemSort;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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
    public List<ItemPriceListDto> findByItemPriceList() {
        QItemPrice itemPrice = QItemPrice.itemPrice;
        QItem item= QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<ItemPriceListDto> query = from(itemPrice)

                .join(item).on(itemPrice.biItemcode.eq(item.biItemcode))
                .join(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .select(Projections.constructor(ItemPriceListDto.class,

                        itemPrice.biItemcode,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        itemPrice.setDt,
                        itemPrice.closeDt,

                        itemPrice.bpBasePrice,
                        itemPrice.bpAddPrice,

                        itemPrice.bpPriceA,
                        itemPrice.bpPriceB,
                        itemPrice.bpPriceC,
                        itemPrice.bpPriceD,
                        itemPrice.bpPriceE,

                        itemPrice.biRemark
                ));

        query.orderBy(itemPrice.biItemcode.asc());

        return query.fetch();
    }

    @Override
    public ItemPriceDto findByItemPrice(String biItemcode, String closeDt) {

        QItemPrice itemPrice = QItemPrice.itemPrice;

        JPQLQuery<ItemPriceDto> query = from(itemPrice)
                .select(Projections.constructor(ItemPriceDto.class,
                        itemPrice.biItemcode,
                        itemPrice.setDt,
                        itemPrice.closeDt,
                        itemPrice.bpBasePrice,
                        itemPrice.bpAddPrice,
                        itemPrice.bpPriceA,
                        itemPrice.bpPriceB,
                        itemPrice.bpPriceC,
                        itemPrice.bpPriceD,
                        itemPrice.bpPriceE,
                        itemPrice.biRemark,
                        itemPrice.insert_id,
                        itemPrice.insertDateTime
                ));

        query.where(itemPrice.biItemcode.eq(biItemcode).and(itemPrice.closeDt.eq(closeDt)));

        return query.fetchOne();
    }

    // 가맹점 가격 전용 순번적용
    @Override
    public List<UserItemPriceSortDto> findByUserItemPriceSortList(String frCode, String nowDate) {
        QItemPrice itemPrice = QItemPrice.itemPrice; // 가격테이블

        QItem item= QItem.item; // 상품소재 테이블
        QItemGroup itemGroup = QItemGroup.itemGroup; // 대분류테이블
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS; // 중분류테이블

        QFranchise franchise = QFranchise.franchise; // 가맹점 테이블 : Grade를 가져오기 위함
        QItemSort itemSort = QItemSort.itemSort; // 가맹점의 상품정렬 테이블

        QFranchisePrice franchisePrice = QFranchisePrice.franchisePrice; // 가맹점 특정항목 가격 테이블

        JPQLQuery<UserItemPriceSortDto> query = from(itemPrice)
                .innerJoin(item).on(itemPrice.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .innerJoin(franchise).on(franchise.frCode.eq(frCode))
                .leftJoin(itemSort).on(itemSort.frCode.eq(frCode).and(itemSort.biItemcode.eq(item.biItemcode)))

                .leftJoin(franchisePrice).on(franchisePrice.biItemcode.eq(itemPrice.biItemcode).and(franchisePrice.frCode.eq(frCode)))

                .where(item.biUseYn.eq("Y"))
                .where(itemPrice.setDt.loe(nowDate).and(itemPrice.closeDt.goe(nowDate)))

                .orderBy(itemSort.bfSort.coalesce(999).asc()).orderBy(item.biItemcode.asc())

                .select(Projections.constructor(UserItemPriceSortDto.class,

                        itemSort.bfSort,

                        itemPrice.biItemcode,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        new CaseBuilder()
                            .when(franchisePrice.bfPrice.isNotNull()).then(franchisePrice.bfPrice)
                            .otherwise(
                                new CaseBuilder()
                                .when(franchise.frPriceGrade.eq("A")).then(itemPrice.bpPriceA)
                                .when(franchise.frPriceGrade.eq("B")).then(itemPrice.bpPriceB)
                                .when(franchise.frPriceGrade.eq("C")).then(itemPrice.bpPriceC)
                                .when(franchise.frPriceGrade.eq("D")).then(itemPrice.bpPriceD)
                                .when(franchise.frPriceGrade.eq("E")).then(itemPrice.bpPriceE)
                                .otherwise(0))
                ));

        return query.fetch();
    }

}
