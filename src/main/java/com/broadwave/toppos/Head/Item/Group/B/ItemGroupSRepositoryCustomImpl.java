package com.broadwave.toppos.Head.Item.Group.B;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepositoryCustom;
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
    public List<ItemGroupSDto> findByItemGroupSList(String bgItemGroupcode) {
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<ItemGroupSDto> query = from(itemGroupS)
                .select(Projections.constructor(ItemGroupSDto.class,
                        itemGroupS.bsItemGroupcodeS,
                        itemGroupS.bgItemGroupcode,
                        itemGroupS.bgName,
                        itemGroupS.bsName,
                        itemGroupS.bsRemark
                ));

        query.where(itemGroupS.bgItemGroupcode.eq(bgItemGroupcode));

        return query.fetch();
    }


}
