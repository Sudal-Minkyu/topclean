package com.broadwave.toppos.Head.Item.Group.C;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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
//
//    @Override
//    public List<ItemGroupSListDto> findByItemGroupSList(ItemGroup bgItemGroupcode) {
//        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
//        QItemGroup itemGroup = QItemGroup.itemGroup;
//
//        JPQLQuery<ItemGroupSListDto> query = from(itemGroupS)
//                .innerJoin(itemGroupS.bgItemGroupcode,itemGroup)
//                .select(Projections.constructor(ItemGroupSListDto.class,
//                        itemGroupS.bsItemGroupcodeS,
//                        itemGroup.bgItemGroupcode,
//                        itemGroup.bgName,
//                        itemGroupS.bsName,
//                        itemGroupS.bsRemark
//                ));
//
//        query.where(itemGroupS.bgItemGroupcode.eq(bgItemGroupcode));
//
//        return query.fetch();
//    }
//
//    @Override
//    public ItemGroupSInfo findByBsItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
//        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
//
//        JPQLQuery<ItemGroupSInfo> query = from(itemGroupS)
//                .select(Projections.constructor(ItemGroupSInfo.class,
//                        itemGroupS.bsItemGroupcodeS,
//                        itemGroupS.bgItemGroupcode.bgItemGroupcode,
//                        itemGroupS.insert_id,
//                        itemGroupS.insertDateTime
//                ));
//
//        query.where(itemGroupS.bgItemGroupcode.bgItemGroupcode.eq(bgItemGroupcode));
//        query.where(itemGroupS.bsItemGroupcodeS.eq(bsItemGroupcodeS));
//
//        return query.fetchOne();
//    }
//
//    @Override
//    public ItemGroupS findByItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
//        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
//        QItemGroup itemGroup = QItemGroup.itemGroup;
//
//        JPQLQuery<ItemGroupS> query = from(itemGroupS)
//                .innerJoin(itemGroupS.bgItemGroupcode,itemGroup)
//                .select(Projections.constructor(ItemGroupS.class,
//                        itemGroupS.bsItemGroupcodeS,
//                        itemGroup,
//                        itemGroupS.bsName,
//                        itemGroupS.bsRemark,
//                        itemGroupS.insert_id,
//                        itemGroupS.insertDateTime,
//                        itemGroupS.modify_id,
//                        itemGroupS.modifyDateTime
//                ));
//
//        query.where(itemGroupS.bgItemGroupcode.bgItemGroupcode.eq(bgItemGroupcode));
//        query.where(itemGroupS.bsItemGroupcodeS.eq(bsItemGroupcodeS));
//
//        return query.fetchOne();
//    }

}
