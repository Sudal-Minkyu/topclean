package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-30
 * Time :
 * Remark : Toppos 가맹점 특정상품관리  Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchisePriceDto {

    private String frCode; // 가맹점 코드(3자리)
    private String biItemcode; // 상품코드
    private Integer bfPrice; // 가맹점 적용가격
    private String bfRemark; // 특이사항

    public String getFrCode() {
        return frCode;
    }

    public String getBiItemcode() {
        return biItemcode;
    }

    public Integer getBfPrice() {
        return bfPrice;
    }

    public String getBfRemark() {
        return bfRemark;
    }
}
