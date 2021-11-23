package com.broadwave.toppos.Head.Item.Group.B;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class ItemGroupSRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemGroupSRepositoryCustom {

    public ItemGroupSRepositoryCustomImpl() {
        super(ItemGroupS.class);
    }

    @Override
    public List<ItemGroupSListDto> findByItemGroupSList(ItemGroup bgItemGroupcode) {
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<ItemGroupSListDto> query = from(itemGroupS)
                .innerJoin(itemGroupS.bgItemGroupcode,itemGroup)
                .select(Projections.constructor(ItemGroupSListDto.class,
                        itemGroupS.bsItemGroupcodeS,
                        itemGroup.bgItemGroupcode,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        itemGroupS.bsRemark
                ));

        query.where(itemGroupS.bgItemGroupcode.eq(bgItemGroupcode));

        return query.fetch();
    }

    @Override
    public ItemGroupSInfo findByBsItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<ItemGroupSInfo> query = from(itemGroupS)
                .select(Projections.constructor(ItemGroupSInfo.class,
                        itemGroupS.bsItemGroupcodeS,
                        itemGroupS.bgItemGroupcode.bgItemGroupcode,
                        itemGroupS.insert_id,
                        itemGroupS.insertDateTime
                ));

        query.where(itemGroupS.bgItemGroupcode.bgItemGroupcode.eq(bgItemGroupcode));
        query.where(itemGroupS.bsItemGroupcodeS.eq(bsItemGroupcodeS));

        return query.fetchOne();
    }

}
