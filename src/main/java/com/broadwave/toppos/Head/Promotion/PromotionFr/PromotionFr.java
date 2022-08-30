package com.broadwave.toppos.Head.Promotion.PromotionFr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사관련 가맹점테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PromotionFrPK.class)
@Table(name="hb_promotion_franchise")
public class PromotionFr {

    @Id
    @Column(name="hp_id")
    private Long hpId; // 행사등록 마스터 ID

    @Id
    @Column(name="fr_id")
    private Long franchiseId; // 가맹점 ID

    @Column(name="fr_code")
    private String frCode; // 가맹점 3자리 코드

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}