package com.broadwave.toppos.User.Addprocess;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.Addprocess.AddprocessDtos.AddprocessDto;
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
    public List<Addprocess> findByAddProcessList(String frCode, String baType) {
        QAddprocess addprocess = QAddprocess.addprocess;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<Addprocess> query = from(addprocess)
                .innerJoin(franchise).on(franchise.frCode.eq(addprocess.frCode))
                .select(Projections.constructor(Addprocess.class,
                        addprocess.baId,
                        franchise,
                        franchise.frCode,
                        addprocess.baType,
                        addprocess.baSort,
                        addprocess.baName,
                        addprocess.baRemark,
                        addprocess.insert_id,
                        addprocess.insertDateTime
                ));

        query.where(addprocess.frCode.eq(frCode).and(addprocess.baType.eq(baType)));

        return query.fetch();
    }

    @Override
    public List<AddprocessDto> findByAddProcessDtoList(String frCode, String baType) {
         QAddprocess addprocess = QAddprocess.addprocess;

        JPQLQuery<AddprocessDto> query = from(addprocess)
                .select(Projections.constructor(AddprocessDto.class,
                        addprocess.baSort,
                        addprocess.baName,
                        addprocess.baRemark
                ));

        query.orderBy(addprocess.baSort.asc());
        query.where(addprocess.frCode.eq(frCode).and(addprocess.baType.eq(baType)));

        return query.fetch();
    }

}
