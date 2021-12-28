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
                        itemGroupS.bsRemark,
                        itemGroupS.bsUseYn
                ));

        query.where(itemGroupS.bgItemGroupcode.eq(bgItemGroupcode));

        return query.fetch();
    }

    @Override
    public List<ItemGroupSUserListDto> findByItemGroupSUserList(String filterCode, String filterName) {
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<ItemGroupSUserListDto> query = from(itemGroupS)
                .innerJoin(itemGroupS.bgItemGroupcode,itemGroup)
                .select(Projections.constructor(ItemGroupSUserListDto.class,
                        itemGroupS.bsItemGroupcodeS,
                        itemGroup.bgItemGroupcode,
                        itemGroup.bgName,
                        itemGroupS.bsName
                ));

        if(!filterCode.equals("")){
            query.where(itemGroup.bgItemGroupcode.concat(itemGroupS.bsItemGroupcodeS).likeIgnoreCase(filterCode.concat("%")));
        }

        if(!filterName.equals("")){
            query.where(itemGroup.bgName.concat(itemGroupS.bsName).likeIgnoreCase(filterName.concat("%")));
        }

        return query.fetch();
    }

    @Override
    public List<UserItemGroupSListDto> findByUserItemGroupSList() {
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<UserItemGroupSListDto> query = from(itemGroupS)
                .innerJoin(itemGroupS.bgItemGroupcode,itemGroup)
                .select(Projections.constructor(UserItemGroupSListDto.class,
                        itemGroup.bgItemGroupcode,
                        itemGroupS.bsItemGroupcodeS,
                        itemGroupS.bsName
                ));

        query.where(itemGroupS.bsUseYn.eq("Y"));

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

    @Override
    public ItemGroupS findByItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItemGroup itemGroup = QItemGroup.itemGroup;

        JPQLQuery<ItemGroupS> query = from(itemGroupS)
                .innerJoin(itemGroupS.bgItemGroupcode,itemGroup)
                .select(Projections.constructor(ItemGroupS.class,
                        itemGroupS.bsItemGroupcodeS,
                        itemGroup,
                        itemGroupS.bsName,
                        itemGroupS.bsUseYn,
                        itemGroupS.bsRemark,
                        itemGroupS.insert_id,
                        itemGroupS.insertDateTime,
                        itemGroupS.modify_id,
                        itemGroupS.modifyDateTime
                ));

        if(bsItemGroupcodeS != null){
            query.where(itemGroupS.bsItemGroupcodeS.eq(bsItemGroupcodeS));
        }

        query.where(itemGroupS.bgItemGroupcode.bgItemGroupcode.eq(bgItemGroupcode));

        return query.fetchOne();
    }

}
