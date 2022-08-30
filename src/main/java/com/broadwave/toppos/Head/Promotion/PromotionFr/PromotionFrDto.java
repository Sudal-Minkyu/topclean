package com.broadwave.toppos.Head.Promotion.PromotionFr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사관련 가맹점 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionFrDto {

    private Long franchiseId; // 가맹점 ID
    private String frCode; // 가맹점 코드
}