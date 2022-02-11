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
 * Remark :1
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
                    branch.brName,

                    franchise.frBusinessNo,
                    franchise.frRpreName,
                    franchise.frTelNo
            ));

        query.orderBy(franchise.id.desc());

        if (!brCode.equals("")){
            query.where(franchise.brCode.eq(brCode));
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
                    franchise.frContractState,
                    franchise.brAssignState,
                    franchise.brCode,
                    branch.brName,
                    branch.brCarculateRateHq,
                    branch.brCarculateRateBr,
                    branch.brCarculateRateFr,

                    franchise.frEstimateDuration,
                    franchise.frLastTagno,

                    franchise.frBusinessNo,
                    franchise.frRpreName,
                    franchise.frTelNo
            ));

        query.where(franchise.frCode.eq(frCode));

        return query.fetchOne();
    }

    @Override
    public List<FranchiseSearchDto> findByFranchiseBrcode(String brCode) {

        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<FranchiseSearchDto> query = from(franchise)
                .select(Projections.constructor(FranchiseSearchDto.class,
                        franchise.frName
                ));

        query.where(franchise.brCode.eq(brCode));
        query.limit(1);

        return query.fetch();
    }

    @Override
    public List<FranchiseManagerListDto> findByManagerInFranchise(String brCode) {

        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<FranchiseManagerListDto> query = from(franchise)
                .select(Projections.constructor(FranchiseManagerListDto.class,
                        franchise.id,
                        franchise.frName,
                        franchise.frTagNo
                ));

        query.where(franchise.brCode.eq(brCode));

        return query.fetch();
    }

    @Override
    public FranchisUserDto findByFranchiseUserInfo(String frCode) {

        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchisUserDto> query = from(franchise)
                .leftJoin(franchise.brId,branch)
                .select(Projections.constructor(FranchisUserDto.class,
                        franchise.frCode,
                        franchise.frName,
                        franchise.frContractDt,
                        franchise.frContractFromDt,
                        franchise.frContractToDt,
                        branch.brName,

                        branch.brCarculateRateHq,
                        branch.brCarculateRateBr,
                        branch.brCarculateRateFr,

                        franchise.frEstimateDuration,
                        franchise.frTagNo,

                        franchise.frBusinessNo,
                        franchise.frRpreName,
                        franchise.frTelNo
                ));

        query.where(franchise.frCode.eq(frCode));

        return query.fetchOne();
    }

}
