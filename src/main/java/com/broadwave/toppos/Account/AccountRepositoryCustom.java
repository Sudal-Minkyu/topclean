package com.broadwave.toppos.Account;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-10-13
 * Time :
 * Remark :
 */
public interface AccountRepositoryCustom {
    List<AccountListDto> findAllByAccountList();
}
