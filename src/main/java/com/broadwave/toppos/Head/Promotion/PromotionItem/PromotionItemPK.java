package com.broadwave.toppos.Head.Promotion.PromotionItem;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사마스터 상품테이블 PK
 */
@Data
public class PromotionItemPK implements Serializable {
    private Long hpId; // 일정산일자
    private String biItemcode; // 상품코드
}