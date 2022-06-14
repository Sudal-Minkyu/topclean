package com.broadwave.toppos.Head.MonthlySummary;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.MonthlySummary.MonthlySummaryDtos.MonthlySummaryListDto;
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
public class MonthlySummaryRepositoryCustomImpl extends QuerydslRepositorySupport implements MonthlySummaryRepositoryCustom {

    public MonthlySummaryRepositoryCustomImpl() {
        super(MonthlySummary.class);
    }

    public List<MonthlySummaryListDto> findByMonthlySummaryList(String filterYearMonth) {

        QMonthlySummary monthlySummary = QMonthlySummary.monthlySummary;
        QBranch branch = QBranch.branch;

        JPQLQuery<MonthlySummaryListDto> query = from(monthlySummary)
                .innerJoin(branch).on(branch.brCode.eq(monthlySummary.brCode))
                .where(monthlySummary.hsYyyymm.eq(filterYearMonth))
                .select(Projections.constructor(MonthlySummaryListDto.class,

                        branch.brName,
                        
                        monthlySummary.hsYyyymm,

                        monthlySummary.hsNormalAmt,
                        monthlySummary.hsPressed,
                        monthlySummary.hsWaterRepellent,
                        monthlySummary.hsStarch,

                        monthlySummary.hsAdd1Amt,
                        monthlySummary.hsAdd2Amt,
                        monthlySummary.hsRepairAmt,
                        monthlySummary.hsWhitening,
                        monthlySummary.hsPollution,

                        monthlySummary.hsUrgentAmt,
                        monthlySummary.hsDiscountAmt,
                        monthlySummary.hsTotAmt,
                        monthlySummary.hsExceptAmt,

                        monthlySummary.hsSettleTotAmt,
                        monthlySummary.hsSettleReturnAmt,

                        monthlySummary.hsSettleAmt,
                        monthlySummary.hsSettleAmtBr,
                        monthlySummary.hsSettleAmtFr,

                        monthlySummary.hsRolayltyAmtBr

                ));

        return query.fetch();
    }

}
