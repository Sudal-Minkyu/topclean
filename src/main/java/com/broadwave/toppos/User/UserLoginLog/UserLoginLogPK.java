package com.broadwave.toppos.User.UserLoginLog;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Minkyu
 * Date : 2021-12-14
 * Time :
 * Remark : Toppos 가맹점별 로그인 로그 PK
 */
@Data
class UserLoginLogPK implements Serializable {

    private String frCode;
    private String blLoginDt;

}
