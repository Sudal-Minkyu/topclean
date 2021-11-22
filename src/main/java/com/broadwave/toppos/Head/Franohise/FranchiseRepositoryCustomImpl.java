package com.broadwave.toppos.Head.Franohise;

import com.broadwave.toppos.Head.Branoh.QBranch;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class FranchiseRepositoryCustomImpl extends QuerydslRepositorySupport implements FranchiseRepositoryCustom {

    public FranchiseRepositoryCustomImpl() {
        super(Franchise.class);
    }

    @Override
    public List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frContractState) {
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchiseListDto> query = from(franchise)
                .leftJoin(franchise.brId,branch)
                .select(Projections.constructor(FranchiseListDto.class,
                        franchise.frCode,
                        franchise.frName,
                        franchise.frRefCode,
                        franchise.frContractDt,
                        franchise.frContractFromDt,
                        franchise.frContractToDt,
                        franchise.frContractState,
                        franchise.brAssignState,
                        franchise.frPriceGrade,
                        franchise.frTagNo,
                        franchise.frEstimateDuration,
                        franchise.frRemark,
                        branch.brName
                ));

        query.orderBy(franchise.id.desc());

        if (!brCode.equals("")){
            query.where(franchise.BrCode.eq(brCode));
        }

        if (!brAssignState.equals("")){
            query.where(franchise.brAssignState.eq(brAssignState));
        }

        if (!frName.equals("")){
            query.where(franchise.frName.likeIgnoreCase(frName.concat("%")));
        }

        if (!frCode.equals("")){
            query.where(franchise.frCode.likeIgnoreCase(frCode.concat("%")));
        }

        if (!frContractState.equals("")){
            query.where(franchise.frContractState.eq(frContractState));
        }

        return query.fetch();
    }

    @Override
    public FranchisInfoDto findByFranchiseInfo(String frCode) {

        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchisInfoDto> query = from(franchise)
                .leftJoin(franchise.brId,branch)
                .select(Projections.constructor(FranchisInfoDto.class,
                        franchise.frCode,
                        franchise.frName,
                        franchise.frContractDt,
                        franchise.frContractFromDt,
                        franchise.frContractToDt,
                        franchise.frContractState,
                        franchise.brAssignState,
                        franchise.BrCode,
                        branch.brName,
                        branch.brCarculateRateHq,
                        branch.brCarculateRateBr,
                        branch.brCarculateRateFr
                ));

        query.where(franchise.frCode.eq(frCode));

        return query.fetchOne();
    }

}
