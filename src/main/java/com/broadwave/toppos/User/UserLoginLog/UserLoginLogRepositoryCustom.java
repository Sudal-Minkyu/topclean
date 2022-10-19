package com.broadwave.toppos.User.UserLoginLog;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-03
 * Time :
 * Remark :
 */
public interface UserLoginLogRepositoryCustom {
    List<UserLoginLogDto> findByFranchiseLog(String brCode, String nowDate);
}
