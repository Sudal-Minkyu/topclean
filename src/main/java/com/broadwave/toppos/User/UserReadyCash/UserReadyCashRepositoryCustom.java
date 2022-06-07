package com.broadwave.toppos.User.UserReadyCash;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark :
 */
public interface UserReadyCashRepositoryCustom {
    List<UserReadyCashListDto> findByReadyCashList(String frCode, String filterFromDt, String filterToDt);
}
