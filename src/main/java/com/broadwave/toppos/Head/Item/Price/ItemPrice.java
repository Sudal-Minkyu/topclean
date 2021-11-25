package com.broadwave.toppos.Head.Item.Price;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark : Toppos 상품그룹 가격관리  테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(ItemPricePK.class)
@Table(name="bs_item_price")
public class ItemPrice {

    @Id
    @Column(name="bi_itemcode")
    private String biItemcode; // 상품코드

    @Id
    @Column(name="set_dt")
    private String setDt; // 가격 시작일

    @Id
    @Column(name="high_class_yn")
    private String highClassYn; // 명품여부

    @Column(name="close_dt")
    private String closeDt; // 가격 종료일

    @Column(name="bp_base_price")
    private Integer bpBasePrice; // 기본가격

    @Column(name="bp_add_price")
    private Integer bpAddPrice; // 추가금액

    @Column(name="bp_price_a")
    private Integer bpPriceA; // 최종가격A

    @Column(name="bp_price_b")
    private Integer bpPriceB; // 최종가격B

    @Column(name="bp_price_c")
    private Integer bpPriceC; // 최종가격C

    @Column(name="bp_price_d")
    private Integer bpPriceD; // 최종가격D

    @Column(name="bp_price_e")
    private Integer bpPriceE; // 최종가격E

    @Column(name="bp_remark")
    private String biRemark; // 특이사항

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
