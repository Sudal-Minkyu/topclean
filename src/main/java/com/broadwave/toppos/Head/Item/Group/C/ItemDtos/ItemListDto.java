package com.broadwave.toppos.Head.Item.Group.C.ItemDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-24
 * Time :
 * Remark : Toppos 상품그룹관리 소재상품 ListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemListDto {

    private String bgItemGroupcode; // 대분류코드
    private String bgName; // 대분류명칭
    private String bsItemGroupcodeS; // 중분류코드
    private String bsName; // 중분류명칭
    private String biItemcode; // 상품코드
    private String biItemSequence; // 상품순번
    private String biName; // 상품명
    private String biUseYn; // 사용여부
    private String biRemark; // 특이사항

    public String getBiUseYn() {
        return biUseYn;
    }

    public String getBiItemcode() {
        return biItemcode;
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

    public String getBgName() {
        return bgName;
    }

    public String getBsItemGroupcodeS() {
        return bsItemGroupcodeS;
    }

    public String getBgItemGroupcode() {
        return bgItemGroupcode;
    }

    public String getBsName() {
        return bsName;
    }

}
