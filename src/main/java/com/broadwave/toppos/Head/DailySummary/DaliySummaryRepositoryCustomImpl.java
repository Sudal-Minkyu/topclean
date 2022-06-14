package com.broadwave.toppos.Head.DailySummary;

import com.broadwave.toppos.Head.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class DaliySummaryRepositoryCustomImpl extends QuerydslRepositorySupport implements DaliySummaryRepositoryCustom {

    public DaliySummaryRepositoryCustomImpl() {
        super(DaliySummary.class);
    }

    public List<DaliySummaryListDto> findByDaliySummaryList(Long franchiseId, String filterYearMonth) {

        QDaliySummary daliySummary = QDaliySummary.daliySummary;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<DaliySummaryListDto> query = from(daliySummary)
                .innerJoin(franchise).on(franchise.id.eq(franchiseId))
                .where(daliySummary.frCode.eq(franchise.frCode).and(daliySummary.hsYyyymmdd.substring(0,6).eq(filterYearMonth)))
                .select(Projections.constructor(DaliySummaryListDto.class,

                        daliySummary.hsYyyymmdd,

                        daliySummary.hsNormalAmt,
                        daliySummary.hsPressed,
                        daliySummary.hsWaterRepellent,
                        daliySummary.hsStarch,

                        daliySummary.hsAdd1Amt,
                        daliySummary.hsAdd2Amt,
                        daliySummary.hsRepairAmt,
                        daliySummary.hsWhitening,
                        daliySummary.hsPollution,

                        daliySummary.hsUrgentAmt,
                        daliySummary.hsDiscountAmt,
                        daliySummary.hsTotAmt,
                        daliySummary.hsExceptAmt,
                        daliySummary.hsSettleTotAmt,
                        daliySummary.hsSettleReturnAmt,
                        daliySummary.hsSettleAmt,

                        daliySummary.hsSettleAmtBr,
                        daliySummary.hsSettleAmtFr,

                        daliySummary.hsSmsAmt,
                        daliySummary.royaltyRateFr

                ));

        return query.fetch();
    }

}
