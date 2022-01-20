package com.broadwave.toppos.User.ItemSort.ItemSortDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점별 상품순서 ItemSortListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSortListDto {

    private String biItemcode; // 상품코드 7자리
    private String biName; // 상품명
    private Integer bfSort; // 정렬순서

    public String getBiItemcode() {
        return biItemcode;
    }

    public String getBiName() {
        return biName;
    }

    public Integer getBfSort() {
        if(bfSort==null){
            return 999;
        }else{
            return bfSort;
        }
    }
}
