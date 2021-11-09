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
public class BranchRepositoryCustomImpl extends QuerydslRepositorySupport implements BranchRepositoryCustom {

    public BranchRepositoryCustomImpl() {
        super(Branch.class);
    }

    @Override
    public List<BranchListDto> findByBranchList() {
        QBranch branch = QBranch.branch;

        JPQLQuery<BranchListDto> query = from(branch)
                .select(Projections.constructor(BranchListDto.class,
                        branch.brCode,
                        branch.brName,
                        branch.brContractDt,
                        branch.brContractFromDt,
                        branch.brContractToDt,
                        branch.brContractState,
                        branch.brCarculateRateHq,
                        branch.brCarculateRateBr,
                        branch.brCarculateRateFr,
                        branch.brRemark
                ));

        query.orderBy(branch.id.desc());

        return query.fetch();
    }

}
