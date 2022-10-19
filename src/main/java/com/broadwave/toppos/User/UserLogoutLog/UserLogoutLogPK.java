package com.broadwave.toppos.User.UserLogoutLog;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2022-05-12
 * Time :
 * Remark : Toppos 가맹점별 로그아웃 로그 PK
 */
@Data
class UserLogoutLogPK implements Serializable {

    private String frCode;
    private String blLogoutDt;

}
