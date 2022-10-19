package com.broadwave.toppos.Head.Item.Price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-08-25
 * Time :
 * Remark : Toppos 가격이 등록되지 않은 상품  Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceNotList {

    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭
    private String biName; // 상품명
    private String biItemcode; // 상품코드
    private String setDt; // 조회날짜

}
