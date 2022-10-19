package com.broadwave.toppos.User.GroupSort;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark : Toppos 가맹점별 대분류 정렬 PK
 */
@Data
class GroupSortPK implements Serializable {

    private String frCode;
    private String bgItemGroupcode;

}
