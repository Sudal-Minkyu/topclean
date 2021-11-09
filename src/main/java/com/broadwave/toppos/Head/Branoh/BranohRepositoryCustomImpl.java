package com.broadwave.toppos.Head.Branoh;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark :
 */

@Repository
public class BranohRepositoryCustomImpl extends QuerydslRepositorySupport implements BranohRepositoryCustom {

    public BranohRepositoryCustomImpl() {
        super(Branoh.class);
    }

    @Override
    public List<BranohListDto> findByBranohList() {
        QBranoh branoh = QBranoh.branoh;

        JPQLQuery<BranohListDto> query = from(branoh)
                .select(Projections.constructor(BranohListDto.class,
                        branoh.brCode,
                        branoh.brName,
                        branoh.brContractDt,
                        branoh.brContractFromDt,
                        branoh.brContractToDt,
                        branoh.brContractState,
                        branoh.brCarculateRateHq,
                        branoh.brCarculateRateBr,
                        branoh.brCarculateRateFr,
                        branoh.brRemark
                ));

        query.orderBy(branoh.id.desc());

        return query.fetch();
    }

}
