package com.broadwave.toppos.User.Addprocess;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class AddprocessRepositoryCustomImpl extends QuerydslRepositorySupport implements AddprocessRepositoryCustom {

    public AddprocessRepositoryCustomImpl() {
        super(Addprocess.class);
    }

    @Override
    public List<AddprocessDto> findByAddProcessList(String frCode, String baType) {
         QAddprocess addprocess = QAddprocess.addprocess;

        JPQLQuery<AddprocessDto> query = from(addprocess)
                .select(Projections.constructor(AddprocessDto.class,
                        addprocess.baName,
                        addprocess.baRemark
                ));

        query.orderBy(addprocess.baId.asc());
        query.where(addprocess.frCode.eq(frCode).and(addprocess.baType.eq(baType)));

        return query.fetch();
    }

}
