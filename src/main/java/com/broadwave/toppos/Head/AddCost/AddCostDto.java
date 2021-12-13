package com.broadwave.toppos.Head.AddCost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark : Toppos 상품 가격셋팅 할인율 테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCostDto {

    private Double bcVipDcRt; // VIP 고객 할인율
    private Double bcVvipDcRt; // VVIP고객 할인율'
    private Double bcHighRt; // 고급 가격 비율 ( ex> 150.0%)
    private Double bcPremiumRt; // 명품 가격 비율 ( ex> 250.0%)
    private Double bcChildRt; // 아동 가격 비율 ( ex> 80.0%)
    private Integer bcPressed; // 다림질 요금
    private Integer bcWhitening; // 표백 요금
    private Integer bcPollution1; // 오염레벨1  추가요금
    private Integer bcPollution2; // 오염레벨2  추가요금
    private Integer bcPollution3; // 오염레벨3  추가요금
    private Integer bcPollution4; // 오염레벨4  추가요금
    private Integer bcPollution5; // 오염레벨5  추가요금
    private Integer bcStarch; // 풀먹임 요금
    private Integer bcWaterRepellent; // 발수가공요금

    public Double getBcVipDcRt() {
        return bcVipDcRt;
    }

    public Double getBcVvipDcRt() {
        return bcVvipDcRt;
    }

    public Double getBcHighRt() {
        return bcHighRt;
    }

    public Double getBcPremiumRt() {
        return bcPremiumRt;
    }

    public Double getBcChildRt() {
        return bcChildRt;
    }

    public Integer getBcPressed() {
        return bcPressed;
    }

    public Integer getBcWhitening() {
        return bcWhitening;
    }

    public Integer getBcPollution1() {
        return bcPollution1;
    }

    public Integer getBcPollution2() {
        return bcPollution2;
    }

    public Integer getBcPollution3() {
        return bcPollution3;
    }

    public Integer getBcPollution4() {
        return bcPollution4;
    }

    public Integer getBcPollution5() {
        return bcPollution5;
    }

    public Integer getBcStarch() {
        return bcStarch;
    }

    public Integer getBcWaterRepellent() {
        return bcWaterRepellent;
    }
}