package com.broadwave.toppos.Head.AddCost;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품 가격셋팅 할인율 테이블
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_item_addcost_set")
public class AddCost {

    @Id
    @Column(name="bc_id")
    private String bcId; // "000" 문자열 강제고정

    @Column(name="bc_vip_dc_rt")
    private Double bcVipDcRt; // VIP 고객 할인율

    @Column(name="bc_vvip_dc_rt")
    private Double bcVvipDcRt; // VVIP고객 할인율'

    @Column(name="bc_high_rt")
    private Double bcHighRt; // 고급 가격 비율 ( ex> 150.0%)

    @Column(name="bc_premium_rt")
    private Double bcPremiumRt; // 명품 가격 비율 ( ex> 250.0%)

    @Column(name="bc_child_rt")
    private Double bcChildRt; // 아동 가격 비율 ( ex> 80.0%)

    @Column(name="bc_pressed")
    private Integer bcPressed; // 다림질 요금

    @Column(name="bc_whitening")
    private Integer bcWhitening; // 표백 요금

    @Column(name="bc_pollution_1")
    private Integer bcPollution1; // 오염레벨1  추가요금

    @Column(name="bc_pollution_2")
    private Integer bcPollution2; // 오염레벨2  추가요금

    @Column(name="bc_pollution_3")
    private Integer bcPollution3; // 오염레벨3  추가요금

    @Column(name="bc_pollution_4")
    private Integer bcPollution4; // 오염레벨4  추가요금

    @Column(name="bc_pollution_5")
    private Integer bcPollution5; // 오염레벨5  추가요금

    @Column(name="bc_starch")
    private Integer bcStarch; // 풀먹임 요금

    @Column(name="bc_water_repellent")
    private Integer bcWaterRepellent; // 발수가공요금

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

}
