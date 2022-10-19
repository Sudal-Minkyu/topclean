package com.broadwave.toppos.Head.Promotion.PromotionFr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2022-08-01
 * Time :
 * Remark :
 */
@Repository
public interface PromotionFrRepository extends JpaRepository<PromotionFr,Long>, PromotionFrRepositoryCustom {
    @Query("select a from PromotionFr a where a.hpId = :hpId and a.franchiseId = :franchiseId")
    Optional<PromotionFr> findByPromotionFr(Long hpId, Long franchiseId);
}