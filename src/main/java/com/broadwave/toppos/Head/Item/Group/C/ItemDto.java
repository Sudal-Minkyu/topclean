package com.broadwave.toppos.Head.Item.Group.C;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-24
 * Time :
 * Remark : Toppos 상품그룹관리 소재 상품 ItemDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private String biItemCode; // 상품코드
    private String bgItemGroupcode; // 대분류코드
    private String bsItemGroupcodeS; // 중분류코드
    private String biItemSequence; // 상품순번
    private String biName; // 상품명
    private String biRemark; // 특이사항

    public String getBiItemCode() {
        return biItemCode;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getBsItemGroupcodeS() {
        return bsItemGroupcodeS;
    }

    public String getBiItemSequence() {
        return biItemSequence;
    }

    public String getBiName() {
        return biName;
    }

    public String getBiRemark() {
        return biRemark;
    }

}
