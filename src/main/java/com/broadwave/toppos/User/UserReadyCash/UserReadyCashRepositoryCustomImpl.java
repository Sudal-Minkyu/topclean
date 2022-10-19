package com.broadwave.toppos.User.UserReadyCash;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class UserReadyCashRepositoryCustomImpl extends QuerydslRepositorySupport implements UserReadyCashRepositoryCustom {

    public UserReadyCashRepositoryCustomImpl() {
        super(UserReadyCash.class);
    }

    @Override
    public List<UserReadyCashListDto> findByReadyCashList(String frCode, String filterFromDt, String filterToDt) {

        QUserReadyCash userReadyCash = QUserReadyCash.userReadyCash;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<UserReadyCashListDto> query = from(userReadyCash)
                        .innerJoin(franchise).on(franchise.frCode.eq(frCode))
                        .where(userReadyCash.bcYyyymmdd.loe(filterToDt).and(userReadyCash.bcYyyymmdd.goe(filterFromDt).and(userReadyCash.frId.eq(franchise.id))))
                        .groupBy(userReadyCash.bcYyyymmdd).orderBy(userReadyCash.bcYyyymmdd.desc())
                        .select(Projections.constructor(UserReadyCashListDto.class,
                                userReadyCash.bcYyyymmdd,
                                userReadyCash.bcReadyAmt
                        ));
        return query.fetch();
    }




}
