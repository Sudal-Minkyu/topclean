package com.broadwave.toppos.Head.Promotion.PromotionItem;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-08-01
 * Time :
 * Remark :
 */
public interface PromotionItemRepositoryCustom {
    List<PromotionItemDto> findByPromotionItemList(Long hpId);

//    void findByPromotionItemUpdate(Long hpId, String modify_id, String biItemcode, Double discount);

    void findByPromotionItemDelete(Long hpId, List<String> biItemcodeList);
}
