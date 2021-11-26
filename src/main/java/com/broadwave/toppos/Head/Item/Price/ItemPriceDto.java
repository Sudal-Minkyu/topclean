package com.broadwave.toppos.Head.Item.Price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-26
 * Time :
 * Remark : Toppos 상품그룹 가격관리  테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceDto {

    private String biItemcode; // 상품코드
    private String setDt; // 가격시작일(적용일자)

    private Integer bpBasePrice; // 기본가격
    private String highClassYn; // 명품여부
    private Integer bpAddPrice; // 추가금액

    private Integer bpPriceA; // 최종가격A
    private Integer bpPriceB; // 최종가격B
    private Integer bpPriceC; // 최종가격C
    private Integer bpPriceD; // 최종가격D
    private Integer bpPriceE; // 최종가격E

    private String biRemark; // 특이사항

    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getInsert_id() {
        return insert_id;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

    public String getBiItemcode() {
        return biItemcode;
    }

    public String getSetDt() {
        return setDt;
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
