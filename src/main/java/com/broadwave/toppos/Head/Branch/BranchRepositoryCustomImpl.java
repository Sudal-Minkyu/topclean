package com.broadwave.toppos.Head.Branch;

import com.broadwave.toppos.Head.Branch.BranchDtos.head.BranchEventListDto;
import com.broadwave.toppos.Head.Branch.BranchDtos.head.BranchSearchInfoDto;
import com.broadwave.toppos.Head.Branoh.BranchDtos.BranchListDto;
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
    public List<BranchListDto> findByBranchList(String brName, String brCode, String brContractState) {
        QBranch branch = QBranch.branch;

        JPQLQuery<BranchListDto> query = from(branch)
                .select(Projections.constructor(BranchListDto.class,
                        branch.brCode,
                        branch.brName,
                        branch.brTelNo,
                        branch.brContractDt,
                        branch.brContractFromDt,
                        branch.brContractToDt,
                        branch.brContractState,
                        branch.brCarculateRateBr,
                        branch.brCarculateRateFr,
                        branch.brRoyaltyRateBr,
                        branch.brRoyaltyRateFr,
                        branch.brRemark
                ));

        query.orderBy(branch.id.desc());

        if (!brName.equals("")){
            query.where(branch.brName.likeIgnoreCase("%"+brName+"%"));
        }

        if (!brCode.equals("")){
            query.where(branch.brCode.likeIgnoreCase("%"+brCode+"%"));
        }

        if (!brContractState.equals("")){
            query.where(branch.brContractState.eq(brContractState));
        }

        return query.fetch();
    }

    @Override
    public List<BranchSearchInfoDto> findByBranchSearchInfo() {
        QBranch branch = QBranch.branch;

        JPQLQuery<BranchSearchInfoDto> query = from(branch)
                .select(Projections.constructor(BranchSearchInfoDto.class,
                        branch.id,
                        branch.brName
                ));

        return query.fetch();
    }

    @Override
    public List<BranchEventListDto> findByEventList(String nowDate) {
        QBranch branch = QBranch.branch;

        JPQLQuery<BranchEventListDto> query = from(branch)
                .where(branch.brContractFromDt.loe(nowDate).and(branch.brContractToDt.goe(nowDate)))
                .select(Projections.constructor(BranchEventListDto.class,
                        branch.id,
                        branch.brName
                ));

        return query.fetch();
    }

}
