package com.broadwave.toppos.Head.Item.Group.B;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-11-23
 * Time :
 * Remark : Toppos 상품그룹관리 중분류 클래스
 */
@Data
class ItemGroupPK implements Serializable {

    private String bsItemGroupcodeS;
    private String bgItemGroupcode;

}
