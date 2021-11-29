package com.broadwave.toppos.Head.Item.Price.FranchPrice;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-11-29
 * Time :
 * Remark : Toppos 가맹점 특정상품관리 다중아이디
 */
@Data
class FranchPricePK implements Serializable {

    private String biItemcode;
    private String frCode;
    private String highClassYn;

}
