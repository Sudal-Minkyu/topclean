package com.broadwave.toppos.Head.Item.Group.C;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSEventListDto;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemEventListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-24
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {

    public ItemRepositoryCustomImpl() {
        super(Item.class);
    }

    @Override
    public List<ItemListDto> findByItemList(String bgItemGroupcode, String bsItemGroupcodeS, String biItemcode, String biName) {

        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<ItemListDto> query = from(item)
                .join(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .select(Projections.constructor(ItemListDto.class,
                        item.bgItemGroupcode,
                        itemGroup.bgName,
                        item.bsItemGroupcodeS,
                        itemGroupS.bsName,
                        item.biItemcode,
                        item.biItemSequence,
                        item.biName,
                        item.biUseYn,
                        item.biRemark
                ))
                .distinct(); // 중복제거

        if (!bgItemGroupcode.equals("")){
            query.where(itemGroup.bgItemGroupcode.eq(bgItemGroupcode));
        }

        if (!bsItemGroupcodeS.equals("")){
            query.where(itemGroupS.bsItemGroupcodeS.eq(bsItemGroupcodeS));
        }

        if (!biItemcode.equals("")){
            query.where(item.biItemcode.likeIgnoreCase(biItemcode+"%"));
        }

        if (!biName.equals("")){
            query.where(item.biName.likeIgnoreCase("%"+biName+"%"));
        }

        return query.fetch();
    }

    @Override
    public List<ItemEventListDto> findByItemEventList(String bgItemGroupcode, String bsItemGroupcodeS) {
        QItem item = QItem.item;

        JPQLQuery<ItemEventListDto> query = from(item)
                .select(Projections.constructor(ItemEventListDto.class,
                        item.biItemcode,
                        item.biName
                ));

        query.where(item.bgItemGroupcode.eq(bgItemGroupcode).and(item.bsItemGroupcodeS.eq(bsItemGroupcodeS)));

        return query.fetch();
    }


}
