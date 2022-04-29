package com.broadwave.toppos.User.UserLoginLog;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-03
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class UserLoginLogRepositoryCustomImpl extends QuerydslRepositorySupport implements UserLoginLogRepositoryCustom {

    public UserLoginLogRepositoryCustomImpl() {
        super(UserLoginLog.class);
    }

    @Override
    public List<UserLoginLogDto> findByFranchiseLog(String brCode, String nowDate) {

        QBranch branch = QBranch.branch;

        QUserLoginLog userLoginLog = QUserLoginLog.userLoginLog;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<UserLoginLogDto> query =
                from(branch)
                        .where(branch.brCode.eq(brCode))
                        .innerJoin(franchise).on(franchise.brCode.eq(branch.brCode))
                        .innerJoin(userLoginLog).on(userLoginLog.frCode.eq(franchise.frCode))
                        .groupBy(userLoginLog.frCode)
                        .where(userLoginLog.blLoginDt.eq(nowDate))
                        .select(Projections.constructor(UserLoginLogDto.class,
                                userLoginLog.frCode
                        ));
        return query.fetch();
    }




}
