package com.broadwave.toppos.Head.Promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark :
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long>, PromotionRepositoryCustom {

}