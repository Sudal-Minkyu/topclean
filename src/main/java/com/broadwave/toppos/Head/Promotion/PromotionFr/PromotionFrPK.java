package com.broadwave.toppos.Head.Promotion.PromotionFr;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사마스터 가맹점 테이블 PK
 */
@Data
public class PromotionFrPK implements Serializable {
    private Long hpId; // 행사등록 마스터 ID
    private Long franchiseId; // 가맹점 ID
}