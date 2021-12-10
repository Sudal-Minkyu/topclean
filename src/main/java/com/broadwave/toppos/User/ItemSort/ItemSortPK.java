package com.broadwave.toppos.User.ItemSort;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark : Toppos 가맹점별 상품정렬 PK
 */
@Data
class ItemSortPK implements Serializable {

    private String frCode;
    private String biItemcode;

}
