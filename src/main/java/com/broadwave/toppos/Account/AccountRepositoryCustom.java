package com.broadwave.toppos.Account;

import com.broadwave.toppos.User.UserDtos.UserIndexDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-10-13
 * Time :
 * Remark :
 */
public interface AccountRepositoryCustom {
    List<AccountListDto> findByAccountList(String s_userid, String s_username, AccountRole s_role, String s_frCode, String s_brCode);

    UserIndexDto findByUserInfo(String userid, String frCode);
}
