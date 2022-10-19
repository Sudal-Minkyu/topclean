package com.broadwave.toppos.Head.Promotion.PromotionItem;

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
public interface PromotionItemRepository extends JpaRepository<PromotionItem,Long>, PromotionItemRepositoryCustom {
    @Query("select a from PromotionItem a where a.hpId = :hpId and a.biItemcode = :biItemcode")
    Optional<PromotionItem> findByPromotionItem(Long hpId, String biItemcode);
}