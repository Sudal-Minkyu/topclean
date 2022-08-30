package com.broadwave.toppos.Head.Promotion.PromotionFr;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-08-01
 * Time :
 * Remark :
 */
public interface PromotionFrRepositoryCustom {
    List<PromotionFrDto> findByPromotionFrList(Long hpId);

    void findByPromotionFrUpdate(Long hpId, String modify_id, List<Long> franchiseIdList);

    void findByPromotionFrDelete(Long hpId, List<Long> franchiseIdList);

}
