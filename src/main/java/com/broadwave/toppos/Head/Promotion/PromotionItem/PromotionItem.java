package com.broadwave.toppos.Head.Promotion.PromotionItem;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사관련 상품테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PromotionItemPK.class)
@Table(name="hb_promotion_item")
public class PromotionItem {

    @Id
    @Column(name="hp_id")
    private Long hpId; // 행사등록 마스터 ID

    @Id
    @Column(name="bi_itemcode")
    private String biItemcode; // 상품코드

    @Column(name="hi_discount_rt")
    private Double hiDiscountRt; // 할인율 ( 마스터의 행사타입이 1+1,2+1 행사이면0이 저장된다.

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}