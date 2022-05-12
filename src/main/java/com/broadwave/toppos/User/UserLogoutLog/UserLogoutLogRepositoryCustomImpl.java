package com.broadwave.toppos.User.UserLogoutLog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-05-12
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class UserLogoutLogRepositoryCustomImpl extends QuerydslRepositorySupport implements UserLogoutLogRepositoryCustom {

    public UserLogoutLogRepositoryCustomImpl() {
        super(UserLogoutLog.class);
    }


}
