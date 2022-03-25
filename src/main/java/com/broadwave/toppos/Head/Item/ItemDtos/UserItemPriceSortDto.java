package com.broadwave.toppos.Head.Item.ItemDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-09
 * Time :
 * Remark : Toppos 상품그룹관리 가맹점 전용 PriceSortDto
 */
@Data
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

    public Integer getBfSort() {
        if(bfSort==null){
            return 999;
        }else{
            return bfSort;
        }
    }
}
