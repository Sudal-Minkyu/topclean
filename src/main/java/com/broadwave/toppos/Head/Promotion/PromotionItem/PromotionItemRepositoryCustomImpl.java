package com.broadwave.toppos.Head.Promotion.PromotionItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-08-01
 * Time :
 * Remark :
 */
@Repository
public class PromotionItemRepositoryCustomImpl extends QuerydslRepositorySupport implements PromotionItemRepositoryCustom {

    public PromotionItemRepositoryCustomImpl() {
        super(PromotionItem.class);
    }

    @Override
    public List<PromotionItemDto> findByPromotionItemList(Long hpId) {

        QPromotionItem promotionItem = QPromotionItem.promotionItem;

        JPQLQuery<PromotionItemDto> query = from(promotionItem)
                .where(promotionItem.hpId.eq(hpId))
                .select(Projections.constructor(PromotionItemDto.class,
                        promotionItem.biItemcode,
                        promotionItem.hiDiscountRt
                ));

        return query.fetch();
    }

//    @Override
//    public void findByPromotionItemUpdate(Long hpId, String modify_id, String biItemcode, Double discount){
//
//        EntityManager em = getEntityManager();
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("UPDATE hb_promotion_item a \n");
//        sb.append("SET a.hi_discount_rt = ?4, a.modify_id = ?2, a.modify_date= NOW() \n");
//        sb.append("WHERE a.hp_id = ?1 AND a.bi_itemcode = ?3 \n");
//
//        Query query = em.createNativeQuery(sb.toString());
//        query.setParameter(1, hpId);
//        query.setParameter(2, modify_id);
//        query.setParameter(3, biItemcode);
//        query.setParameter(4, discount);
//
//        query.executeUpdate();
//    }

    @Override
    public void findByPromotionItemDelete(Long hpId, List<String> biItemcodeList){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM hb_promotion_item \n");
        sb.append("WHERE hp_id = ?1 AND bi_itemcode NOT IN ?2 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, hpId);
        query.setParameter(2, biItemcodeList);

        query.executeUpdate();
    }

}