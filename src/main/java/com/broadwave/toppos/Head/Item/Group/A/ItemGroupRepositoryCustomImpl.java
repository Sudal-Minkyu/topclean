package com.broadwave.toppos.Head.Item.Group.A;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-18
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class ItemGroupRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemGroupRepositoryCustom {

    public ItemGroupRepositoryCustomImpl() {
        super(ItemGroup.class);
    }

    @Override
    public List<ItemGroupDto> findByItemGroupList() {
         QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<ItemGroupDto> query = from(itemGroup)
                .select(Projections.constructor(ItemGroupDto.class,
                        itemGroup.bgItemGroupcode,
                        itemGroup.bgName,
                        itemGroup.bgRemark,
                        itemGroup.bgUseYn
                ));
        return query.fetch();
    }

    @Override
    public List<ItemGroupNameListDto> findByItemGroupName() {
        QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<ItemGroupNameListDto> query = from(itemGroup)
                .select(Projections.constructor(ItemGroupNameListDto.class,
                        itemGroup.bgName
                ));
        return query.fetch();
    }


}
