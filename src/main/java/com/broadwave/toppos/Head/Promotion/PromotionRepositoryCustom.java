package com.broadwave.toppos.Head.Promotion;

import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionDto;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark :
 */
public interface PromotionRepositoryCustom {
    List<PromotionDto> findByPromotion(Long branchId, Long franchiseId, String filterDt, String hpName, String hpStatus);

    List<PromotionListDto> findByPromotionList(String frCode, int dayOfWeek, String nowDate);
}
