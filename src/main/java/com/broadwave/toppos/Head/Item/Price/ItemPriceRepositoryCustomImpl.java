package com.broadwave.toppos.Head.Item.Price;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceListDto;
import com.broadwave.toppos.Head.Item.ItemDtos.UserItemPriceSortDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.ItemPriceSetDtDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.QFranchisePrice;
import com.broadwave.toppos.User.ItemSort.QItemSort;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    @Autowired
    JpaResultMapper jpaResultMapper;

    public ItemPriceRepositoryCustomImpl() {
        super(ItemPrice.class);
    }

    @Override
    public List<ItemPriceListDto> findByItemPriceList(String bgName, String biItemcode, String biName, String setDt) {
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

                        itemPrice.bpRemark
                ));

        if(!bgName.equals("")){
            query.where(itemGroup.bgName.likeIgnoreCase("%"+bgName+"%"));
        }

        if(!biItemcode.equals("")){
            query.where(itemPrice.biItemcode.likeIgnoreCase("%"+biItemcode+"%"));
        }

        if(!biName.equals("")){
            query.where(item.biName.likeIgnoreCase("%"+biName+"%"));
        }

        if(!setDt.equals("")){
            query.where(itemPrice.setDt.eq(setDt));
        }

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
                        itemPrice.bpRemark,
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

    @Override
    public List<ItemPriceSetDtDto> findByItemPriceSetDtList() {
        QItemPrice itemPrice = QItemPrice.itemPrice;

        JPQLQuery<ItemPriceSetDtDto> query = from(itemPrice)
                .select(Projections.constructor(ItemPriceSetDtDto.class,
                        itemPrice.setDt,
                        itemPrice.setDt
                ));

        query.orderBy(itemPrice.setDt.desc()).distinct();

        return query.fetch();
    }

    // 상품 가격등록되지 않은 상품 조회
    @Override
    public List<ItemPriceNotList> findByItemPricedNotList(String bgName, String biItemcode, String biName, String setDt){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT b.bg_name, c.bs_name, a.bi_name,  a.bi_itemcode, ?1 AS set_dt \n");
        sb.append("FROM bs_item a \n");
        sb.append("INNER JOIN bs_item_group b ON b.bg_item_groupcode = a.bg_item_groupcode \n");
        sb.append("INNER JOIN bs_item_group_s c ON c.bs_item_groupcode_s = a.bs_item_groupcode_s  \n");
        sb.append("AND c.bg_item_groupcode = b.bg_item_groupcode \n");
        sb.append("WHERE a.bi_use_yn = 'Y' \n");
        sb.append("AND a.bi_itemcode NOT IN( \n");
        sb.append("SELECT a.bi_itemcode FROM bs_item_price a WHERE a.set_dt = ?1 \n");
        sb.append(") \n");

        if(!bgName.equals("")) {
            sb.append("AND b.bg_name LIKE ?2 \n");
            if(!biItemcode.equals("")){
                sb.append("AND a.bi_itemcode LIKE ?3 \n");
                if(!biName.equals("")){
                    sb.append("AND a.bi_name LIKE ?4 \n");
                }
            }else{
                if(!biName.equals("")){
                    sb.append("AND a.bi_name LIKE ?3 \n");
                }
            }
        }
        else if(!biItemcode.equals("")) {
            sb.append("AND a.bi_itemcode LIKE ?2 \n");
            if(!biName.equals("")){
                sb.append("AND a.bi_name LIKE ?3 \n");
            }
        }
        else if(!biName.equals("")) {
            sb.append("AND a.bi_name LIKE ?2 \n");
        }

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, setDt);
        if(!bgName.equals("")) {
            query.setParameter(2, "%" + bgName + "%");
            if(!biItemcode.equals("")){
                query.setParameter(3, "%"+biItemcode+"%");
                if(!biName.equals("")){
                    query.setParameter(4, "%"+biName+"%");
                }
            }else{
                if(!biName.equals("")){
                    query.setParameter(3, "%"+biName+"%");
                }
            }
        }
        else if(!biItemcode.equals("")) {
            query.setParameter(2, "%" + biItemcode + "%");
            if(!biName.equals("")){
                query.setParameter(3, "%"+biName+"%");
            }
        }
        else if(!biName.equals("")) {
            query.setParameter(2, "%" + biName + "%");
        }

        return jpaResultMapper.list(query, ItemPriceNotList.class);
    }

}
