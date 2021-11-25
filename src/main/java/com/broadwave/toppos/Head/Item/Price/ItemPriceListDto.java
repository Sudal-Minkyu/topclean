package com.broadwave.toppos.Head.Item.Price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark : Toppos 상품그룹 가격관리  테이블 ListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceListDto {

    private String biItemcode; // 상품코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칠
    private String biName; // 상품명
    private String setDt; // 가격시작일(적용일자)
    private String closeDt; // 가격 종료일

    private Integer bpBasePrice; // 기본가격
    private String highClassYn; // 명품여부
    private Integer bpAddPrice; // 추가금액

    private Integer bpPriceA; // 최종가격A
    private Integer bpPriceB; // 최종가격B
    private Integer bpPriceC; // 최종가격C
    private Integer bpPriceD; // 최종가격D
    private Integer bpPriceE; // 최종가격E

    private String biRemark; // 특이사항

    public String getBiItemcode() {
        return biItemcode;
    }

    public String getBgName() {
        return bgName;
    }

    public String getBsName() {
        return bsName;
    }

    public String getBiName() {
        return biName;
    }

    public String getSetDt() {
        return setDt;
    }

    public String getCloseDt() {
        return closeDt;
    }

    public Integer getBpBasePrice() {
        return bpBasePrice;
    }

    public String getHighClassYn() {
        return highClassYn;
    }

    public Integer getBpAddPrice() {
        return bpAddPrice;
    }

    public Integer getBpPriceA() {
        return bpPriceA;
    }

    public Integer getBpPriceB() {
        return bpPriceB;
    }

    public Integer getBpPriceC() {
        return bpPriceC;
    }

    public Integer getBpPriceD() {
        return bpPriceD;
    }

    public Integer getBpPriceE() {
        return bpPriceE;
    }

    public String getBiRemark() {
        return biRemark;
    }
}
