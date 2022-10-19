package com.broadwave.toppos.Head.Franchise;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.*;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseEventListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseSearchInfoDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseTagNoSearchInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frRefCode, String frContractState) {
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
                    franchise.frTagType,
                    franchise.frEstimateDuration,
                    franchise.frRemark,
                    branch.brName,

                    franchise.frBusinessNo,
                    franchise.frRpreName,
                    franchise.frTelNo,

                    franchise.frPostNo,
                    franchise.frAddress,
                    franchise.frAddressDetail,

                    franchise.frCarculateRateBr,
                    franchise.frCarculateRateFr,
                    franchise.frRoyaltyRateBr,
                    franchise.frRoyaltyRateFr,

                    franchise.frUrgentDayYn,

                    franchise.frManualPromotionYn,
                    franchise.frCardTid
            ));

        query.orderBy(franchise.id.desc());

        if (!brCode.equals("")){
            query.where(franchise.brCode.eq(brCode));
        }

        if (!brAssignState.equals("")){
            query.where(franchise.brAssignState.eq(brAssignState));
        }

        if (!frName.equals("")){
            query.where(franchise.frName.likeIgnoreCase("%"+frName+"%"));
        }

        if (!frCode.equals("")){
            query.where(franchise.frCode.likeIgnoreCase("%"+frCode+"%"));
        }

        if (!frRefCode.equals("")){
            query.where(franchise.frRefCode.likeIgnoreCase("%"+frRefCode+"%"));
        }

        if (!frContractState.equals("")){
            query.where(franchise.frContractState.eq(frContractState));
        }

        return query.fetch();
    }

    @Override
    public FranchiseInfoDto findByFranchiseInfo(String frCode) {

        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchiseInfoDto> query = from(franchise)
            .leftJoin(franchise.brId,branch)
            .select(Projections.constructor(FranchiseInfoDto.class,
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

                    franchise.frCarculateRateBr,
                    franchise.frCarculateRateFr,
                    franchise.frRoyaltyRateBr,
                    franchise.frRoyaltyRateFr,

                    franchise.frEstimateDuration,
                    franchise.frLastTagno,
                    franchise.frTagNo,
                    franchise.frTagType,

                    franchise.frBusinessNo,
                    franchise.frRpreName,
                    franchise.frTelNo,

                    franchise.frPostNo,
                    franchise.frAddress,
                    franchise.frAddressDetail,

                    franchise.frMultiscreenYn,

                    franchise.frUrgentDayYn,

                    franchise.frManualPromotionYn,
                    franchise.frCardTid
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
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchiseManagerListDto> query = from(franchise)
                .innerJoin(branch).on(branch.eq(franchise.brId))
                .select(Projections.constructor(FranchiseManagerListDto.class,
                        branch.id,
                        franchise.id,
                        franchise.frName,
                        franchise.frTagNo
                ));

        query.where(franchise.brCode.eq(brCode));

        return query.fetch();
    }

    @Override
    public FranchiseUserDto findByFranchiseUserInfo(String frCode) {

        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchiseUserDto> query = from(franchise)
                .leftJoin(franchise.brId,branch)
                .select(Projections.constructor(FranchiseUserDto.class,
                        franchise.frCode,
                        franchise.frName,
                        franchise.frContractDt,
                        franchise.frContractFromDt,
                        franchise.frContractToDt,

                        branch.brName,

                        franchise.frCarculateRateBr,
                        franchise.frCarculateRateFr,
                        franchise.frRoyaltyRateBr,
                        franchise.frRoyaltyRateFr,

                        franchise.frDepositAmount,
                        franchise.frRentalAmount,

                        franchise.frEstimateDuration,
                        franchise.frTagNo,
                        franchise.frRemark,

                        franchise.frBusinessNo,
                        franchise.frRpreName,
                        franchise.frTelNo,

                        franchise.frPostNo,
                        franchise.frAddress,
                        franchise.frAddressDetail,
                        franchise.frMultiscreenYn,

                        franchise.frOpenWeekday,
                        franchise.frOpenSaturday,
                        franchise.frOpenHoliday,
                        franchise.frCloseWeekday,
                        franchise.frCloseSaturday,
                        franchise.frCloseHoliday,
                        franchise.frStatWeekday,
                        franchise.frStatSaturday,
                        franchise.frStatHoliday
                ));

        query.where(franchise.frCode.eq(frCode));

        return query.fetchOne();
    }

    @Override
    public FranchiseNameInfoDto findByFranchiseNameInfo(String frCode) {

        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<FranchiseNameInfoDto> query = from(franchise)
                .select(Projections.constructor(FranchiseNameInfoDto.class,
                        franchise.frName
                ));

        query.where(franchise.frCode.eq(frCode));

        return query.fetchOne();
    }

    @Override
    public FranchiseMultiscreenDto findByFranchiseMultiscreen(String frCode) {

        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<FranchiseMultiscreenDto> query = from(franchise)
                .select(Projections.constructor(FranchiseMultiscreenDto.class,
                        new CaseBuilder()
                                .when(franchise.frMultiscreenYn.isNull()).then("N")
                                .otherwise(franchise.frMultiscreenYn)
                ));

        query.where(franchise.frCode.eq(frCode));
        return query.fetchOne();
    }

    @Override
    public FranchiseTagDataDto findByFranchiseTag(String frCode) {

        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<FranchiseTagDataDto> query = from(franchise)
                .select(Projections.constructor(FranchiseTagDataDto.class,
                        franchise.frTagNo,
                        franchise.frTagType
                ));

        query.where(franchise.frCode.eq(frCode));
        return query.fetchOne();
    }


    @Override
    public List<FranchiseSearchInfoDto> findByFranchiseSearchInfo() {
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchiseSearchInfoDto> query = from(franchise)
                .innerJoin(branch).on(branch.eq(franchise.brId))
                .select(Projections.constructor(FranchiseSearchInfoDto.class,
                        franchise.id,
                        branch.id,
                        franchise.frName
                ));

        query.where(franchise.brId.isNotNull());

        return query.fetch();
    }

    @Override
    public List<FranchiseTagNoSearchInfoDto> findByFranchiseTagNoSearchInfo() {
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<FranchiseTagNoSearchInfoDto> query = from(franchise)
                .innerJoin(branch).on(branch.eq(franchise.brId))
                .select(Projections.constructor(FranchiseTagNoSearchInfoDto.class,
                        franchise.id,
                        branch.id,
                        franchise.frName,
                        franchise.frTagNo
                ));

        query.where(franchise.brId.isNotNull());

        return query.fetch();
    }

    @Override
    public void findByFranchiseDue(String yyyymmdd, String frCode) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("CALL proc_hc_daily_fr(?1,?2)");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, yyyymmdd);
        query.setParameter(2, frCode);

        query.getSingleResult();
    }

    @Override
    public List<FranchiseEventListDto> findByEventList(Long brId, String nowDate) {
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<FranchiseEventListDto> query = from(franchise)
                .where(franchise.frContractFromDt.loe(nowDate).and(franchise.frContractToDt.goe(nowDate)).and(franchise.brId.id.eq(brId)))
                .select(Projections.constructor(FranchiseEventListDto.class,
                        franchise.id,
                        franchise.frCode,
                        franchise.frName
                ));

        return query.fetch();
    }

}
