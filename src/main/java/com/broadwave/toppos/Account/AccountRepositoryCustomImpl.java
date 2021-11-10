package com.broadwave.toppos.Account;

import com.querydsl.core.types.Projections;
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
            query.where(account.userid.likeIgnoreCase(s_userid.concat("%")));
        }

        if (!s_userid.equals("")){
            query.where(account.userid.likeIgnoreCase(s_userid.concat("%")));
        }

        if (!s_frCode.equals("")){
            query.where(account.frCode.likeIgnoreCase(s_frCode.concat("%")));
        }

        if (!s_brCode.equals("")){
            query.where(account.brCode.likeIgnoreCase(s_brCode.concat("%")));
        }

        return query.fetch();

    }


}
