package com.broadwave.toppos.Head.Item.Group.A;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupEventListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.UserItemGroupSortDto;
import com.broadwave.toppos.User.GroupSort.QGroupSort;
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
                        itemGroup.bgIconFilename,
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

    @Override
    public List<UserItemGroupSortDto> findByUserItemGroupSortDtoList(String frCode) {
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QGroupSort groupSort = QGroupSort.groupSort;

        JPQLQuery<UserItemGroupSortDto> query = from(itemGroup)
                .where(itemGroup.bgUseYn.eq("Y"))
                .leftJoin(groupSort).on(groupSort.frCode.eq(frCode).and(groupSort.bgItemGroupcode.eq(itemGroup.bgItemGroupcode)))
                .orderBy(groupSort.bgSort.coalesce(999).asc()).orderBy(itemGroup.bgItemGroupcode.asc())
                .select(Projections.constructor(UserItemGroupSortDto.class,
                        groupSort.bgSort,
                        groupSort.bgFavoriteYn,
                        itemGroup.bgItemGroupcode,
                        itemGroup.bgName,
                        itemGroup.bgIconFilename
                ));

        return query.fetch();
    }

    @Override
    public List<ItemGroupEventListDto> findByItemGroupEventList() {
        QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<ItemGroupEventListDto> query = from(itemGroup)
                .select(Projections.constructor(ItemGroupEventListDto.class,
                        itemGroup.bgItemGroupcode,
                        itemGroup.bgName
                ));
        return query.fetch();
    }

}
