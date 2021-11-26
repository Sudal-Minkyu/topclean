package com.broadwave.toppos.Head.Item.Price;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark : Toppos 상품그룹 가격관리 클래스
 */
@Data
class ItemPricePK implements Serializable {

    private String biItemcode;
    private String setDt;
    private String highClassYn;

}
