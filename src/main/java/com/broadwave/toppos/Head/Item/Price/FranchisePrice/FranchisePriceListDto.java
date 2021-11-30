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
public class FranchisePriceListDto {

    private String biItemcode; // 상품코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭
    private String biName; // 상품명
    private String highClassYn; // 가맹점 적용가격
    private Integer bfPrice; // 특이사항
    private String bfRemark; // 특이사항

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

    public String getHighClassYn() {
        return highClassYn;
    }

    public Integer getBfPrice() {
        return bfPrice;
    }

    public String getBfRemark() {
        return bfRemark;
    }
}
