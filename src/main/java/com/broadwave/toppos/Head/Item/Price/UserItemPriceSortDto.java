package com.broadwave.toppos.Head.Item.Price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-09
 * Time :
 * Remark : Toppos 상품그룹관리 가맹점 전용 PriceSortDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItemPriceSortDto {

    private Integer bfSort; // 정렬순번

    private String biItemcode; // 상품코드

    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칠
    private String biName; // 상품명

    private Integer price; // 최종가격A
//    private Integer bpPriceB; // 최종가격B
//    private Integer bpPriceC; // 최종가격C
//    private Integer bpPriceD; // 최종가격D
//    private Integer bpPriceE; // 최종가격E

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

    public Integer getPrice() {
        return price;
    }

    public Integer getBfSort() {
        if(bfSort==null){
            return 999;
        }else{
            return bfSort;
        }
    }
}
