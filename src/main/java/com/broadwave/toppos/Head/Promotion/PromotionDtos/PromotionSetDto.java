package com.broadwave.toppos.Head.Promotion.PromotionDtos;

import com.broadwave.toppos.Head.Promotion.PromotionFr.PromotionFrDto;
import com.broadwave.toppos.Head.Promotion.PromotionItem.PromotionItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사 등록/삭제 할 데이터 SetDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionSetDto {

    private PromotionDto hbPromotion;

    private List<PromotionFrDto> hbPromotionFranchise;

    private List<PromotionItemDto> hbPromotionItem;

}
