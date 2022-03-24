package com.broadwave.toppos.Account;

import com.broadwave.toppos.Head.Branoh.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.UserDtos.UserIndexDto;
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

        if (s_role != null){
            query.where(account.role.eq(s_role));
        }

        if (!s_userid.equals("")){
            query.where(account.userid.contains(s_userid));
        }

        if (!s_username.equals("")){
            query.where(account.username.contains(s_username));
        }

        if (!s_frCode.equals("")){
            query.where(account.frCode.likeIgnoreCase(s_frCode.concat("%")));
        }

        if (!s_brCode.equals("")){
            query.where(account.brCode.likeIgnoreCase(s_brCode.concat("%")));
        }

        return query.fetch();

    }

    //  가맹점의 정보 가져오는 쿼리
    @Override
    public UserIndexDto findByUserInfo(String userid, String frCode) {

        QAccount account = QAccount.account;
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<UserIndexDto> query = from(account)
                .join(franchise).on(franchise.frCode.eq(frCode))
                .leftJoin(franchise.brId,branch)
                .select(Projections.constructor(UserIndexDto.class,
                        account.username,
                        account.usertel,
                        branch.brName,
                        franchise.frName
                ));

        query.where(account.userid.eq(userid));

        return query.fetchOne();
    }

    @Override
    public AccountHeaderDto findByHeaderInfo(String userid, String brCode) {

        QAccount account = QAccount.account;
        QBranch branch = QBranch.branch;

        JPQLQuery<AccountHeaderDto> query = from(account)
                .leftJoin(branch).on(branch.brCode.eq(account.brCode))
                .select(Projections.constructor(AccountHeaderDto.class,
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

}
