package com.broadwave.toppos.User.UserReadyCash;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark : Toppos 가맹점별 일자별 현금 준비금 PK
 */
@Data
class UserReadyCashPK implements Serializable {

    private Long frId;
    private String bcYyyymmdd;

}
