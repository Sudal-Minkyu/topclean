package com.broadwave.toppos.User.ItemSort;

import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Head.Item.Price.QItemPrice;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark :
 */
@Repository
public class ItemSortRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemSortRepositoryCustom {

    public ItemSortRepositoryCustomImpl() {
        super(ItemSortRepository.class);
    }

    public  List<ItemSortUpdateDto> findByItemSortList(String biItemMgroup, String frCode) {
        QItemSort itemSort = QItemSort.itemSort;

        JPQLQuery<ItemSortUpdateDto> query = from(itemSort)
                .select(Projections.constructor(ItemSortUpdateDto.class,
                        itemSort.biItemcode,
                        itemSort.insert_id,
                        itemSort.insertDateTime
                ));

        query.where(itemSort.frCode.eq(frCode).and(itemSort.biItemMgroup.eq(biItemMgroup)));

        return query.fetch();
    }

    public List<ItemSortListDto> itemSortList(String frCode, String biItemMgroup, String nowDate) {
        QItemPrice itemPrice = QItemPrice.itemPrice; // 가격테이블
        QItem item= QItem.item; // 상품소재 테이블
        QItemSort itemSort = QItemSort.itemSort; // 가맹점의 상품정렬 테이블

        JPQLQuery<ItemSortListDto> query = from(itemPrice)
            .innerJoin(item).on(itemPrice.biItemcode.eq(item.biItemcode))
            .leftJoin(itemSort).on(itemSort.frCode.eq(frCode).and(itemSort.biItemcode.eq(item.biItemcode)))

            .where(item.biUseYn.eq("Y"))
            .where(itemPrice.setDt.loe(nowDate).and(itemPrice.closeDt.goe(nowDate)))

            .orderBy(itemSort.bfSort.coalesce(999).asc()).orderBy(item.biItemcode.asc())

            .select(Projections.constructor(ItemSortListDto.class,
                    item.biItemcode,
                    item.biName,
                    itemSort.bfSort
            ));

        query.where(item.bgItemGroupcode.concat(item.bsItemGroupcodeS).eq(biItemMgroup));

        return query.fetch();
    }

}
