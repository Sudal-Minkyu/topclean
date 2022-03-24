package com.broadwave.toppos.Head.Item.Group.C;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
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
                .join(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS))
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
            query.where(itemGroup.bgItemGroupcode.likeIgnoreCase("%"+bgItemGroupcode+"%"));
        }

        if (!bsItemGroupcodeS.equals("")){
            query.where(itemGroupS.bsItemGroupcodeS.likeIgnoreCase("%"+bsItemGroupcodeS+"%"));
        }

        if (!biItemcode.equals("")){
            query.where(item.biItemcode.likeIgnoreCase("%"+biItemcode+"%"));
        }

        if (!biName.equals("")){
            query.where(item.biName.likeIgnoreCase("%"+biName+"%"));
        }

        return query.fetch();
    }

}
