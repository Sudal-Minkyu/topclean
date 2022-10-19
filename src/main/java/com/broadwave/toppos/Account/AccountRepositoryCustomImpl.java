package com.broadwave.toppos.Account;

import com.broadwave.toppos.Account.AcountDtos.*;
import com.broadwave.toppos.Head.Branch.BranchDtos.BranchInfoDto;
import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.UserDtos.UserIndexDto;
import com.broadwave.toppos.User.UserReadyCash.QUserReadyCash;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-10-13
 * Time :
 * Remark :
 */
@Repository
public class AccountRepositoryCustomImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom {

    public AccountRepositoryCustomImpl() {
        super(Account.class);
    }

    @Override
    public List<AccountListDto> findByAccountList(String s_userid, String s_username, AccountRole s_role, String s_frCode, String s_brCode) {
        QAccount account = QAccount.account;

        JPQLQuery<AccountListDto> query = from(account)
                .select(Projections.constructor(AccountListDto.class,
                        account.userid,
                        account.role,
                        account.username,
                        account.usertel,
                        account.useremail,
                        account.frCode,
                        account.brCode,
                        account.userremark
                ));

        query.orderBy(account.id.desc());

        if (s_role != null) {
            query.where(account.role.eq(s_role));
        }

        if (!s_userid.equals("")) {
            query.where(account.userid.contains(s_userid));
        }

        if (!s_username.equals("")) {
            query.where(account.username.contains(s_username));
        }

        if (!s_frCode.equals("")) {
            query.where(account.frCode.likeIgnoreCase(s_frCode.concat("%")));
        }

        if (!s_brCode.equals("")) {
            query.where(account.brCode.likeIgnoreCase(s_brCode.concat("%")));
        }

        return query.fetch();

    }

    //  가맹점의 정보 가져오는 쿼리
    @Override
    public UserIndexDto findByUserInfo(String userid, String frCode, String nowDate) {

        QAccount account = QAccount.account;
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;
        QUserReadyCash userReadyCash = QUserReadyCash.userReadyCash;

        JPQLQuery<UserIndexDto> query = from(account)
                .innerJoin(franchise).on(franchise.frCode.eq(frCode))
                .leftJoin(franchise.brId, branch)
                .leftJoin(userReadyCash).on(userReadyCash.frId.eq(franchise.id).and(userReadyCash.bcYyyymmdd.eq(nowDate)))
                .select(Projections.constructor(UserIndexDto.class,
                        franchise.frRpreName,
                        franchise.frTelNo,
                        branch.brName,
                        franchise.frName,
                        userReadyCash.bcReadyAmt
                ));

        query.where(account.userid.eq(userid));

        return query.fetchOne();
    }

    @Override
    public AccountBranchHeaderDto findByBranchHeaderInfo(String userid) {

        QAccount account = QAccount.account;
        QBranch branch = QBranch.branch;

        JPQLQuery<AccountBranchHeaderDto> query = from(account)
                .leftJoin(branch).on(branch.brCode.eq(account.brCode))
                .select(Projections.constructor(AccountBranchHeaderDto.class,
                        account.username,
                        new CaseBuilder()
                                .when(account.brCode.isNull()).then("")
                                .otherwise(
                                        new CaseBuilder()
                                                .when(account.brCode.eq("no")).then("")
                                                .otherwise(branch.brName))
                ));

        query.where(account.userid.eq(userid));

        return query.fetchOne();
    }

    @Override
    public AccountHeadHeaderDto findByHeadHeaderInfo(String userid) {

        QAccount account = QAccount.account;

        JPQLQuery<AccountHeadHeaderDto> query = from(account)
                .select(Projections.constructor(AccountHeadHeaderDto.class,
                        account.username
                ));

        query.where(account.userid.eq(userid));

        return query.fetchOne();
    }

    //  지사의 나의 정보 가져오는 쿼리
    @Override
    public BranchInfoDto findByBranchInfo(String login_id) {

        QAccount account = QAccount.account;
        QBranch branch = QBranch.branch;

        JPQLQuery<BranchInfoDto> query = from(account)
                .innerJoin(branch).on(account.brCode.eq(branch.brCode))
                .select(Projections.constructor(BranchInfoDto.class,
                        branch.brCode,
                        branch.brName,
                        branch.brTelNo,

                        account.userid,
                        account.useremail,
                        account.usertel
                ));

        query.where(account.userid.eq(login_id));

        return query.fetchOne();
    }

    //  본사의 나의 정보 가져오는 쿼리
    @Override
    public AccountHeadInfoDto findByHeadInfo(String login_id) {

        QAccount account = QAccount.account;

        JPQLQuery<AccountHeadInfoDto> query = from(account)
                .select(Projections.constructor(AccountHeadInfoDto.class,
                        account.userid,
                        account.useremail,
                        account.usertel
                ));

        query.where(account.userid.eq(login_id));

        return query.fetchOne();
    }

}
