package com.broadwave.toppos.Head.Promotion.PromotionFr;

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
public class PromotionFrRepositoryCustomImpl extends QuerydslRepositorySupport implements PromotionFrRepositoryCustom {

    public PromotionFrRepositoryCustomImpl() {
        super(PromotionFr.class);
    }

    @Override
    public List<PromotionFrDto> findByPromotionFrList(Long hpId) {

        QPromotionFr promotionFr = QPromotionFr.promotionFr;

        JPQLQuery<PromotionFrDto> query = from(promotionFr)
                .where(promotionFr.hpId.eq(hpId))
                .select(Projections.constructor(PromotionFrDto.class,
                        promotionFr.franchiseId,
                        promotionFr.frCode
                ));

        return query.fetch();
    }

    @Override
    public void findByPromotionFrUpdate(Long hpId, String modify_id, List<Long> franchiseIdList){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE hb_promotion_franchise a \n");
        sb.append("SET a.modify_id = ?3, a.modify_date= NOW() \n");
        sb.append("WHERE a.hp_id = ?1 AND a.fr_id IN ?2 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, hpId);
        query.setParameter(2, franchiseIdList);
        query.setParameter(3, modify_id);

        query.executeUpdate();
    }

    @Override
    public void findByPromotionFrDelete(Long hpId, List<Long> franchiseIdList){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM hb_promotion_franchise \n");
        sb.append("WHERE hp_id = ?1 AND fr_id NOT IN ?2 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, hpId);
        query.setParameter(2, franchiseIdList);

        query.executeUpdate();
    }

}