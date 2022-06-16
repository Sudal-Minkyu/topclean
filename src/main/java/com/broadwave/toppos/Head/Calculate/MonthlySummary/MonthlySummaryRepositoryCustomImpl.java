package com.broadwave.toppos.Head.Calculate.MonthlySummary;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryDaysDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.QReceiptMonthly;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    @Autowired
    JpaResultMapper jpaResultMapper;

    public MonthlySummaryRepositoryCustomImpl() {
        super(MonthlySummary.class);
    }

    // 본사 월정산 요역 리스트
    public List<MonthlySummaryListDto> findByMonthlySummaryList(String filterYearMonth) {

        QMonthlySummary monthlySummary = QMonthlySummary.monthlySummary;
        QBranch branch = QBranch.branch;

        JPQLQuery<MonthlySummaryListDto> query = from(monthlySummary)
                .innerJoin(branch).on(branch.brCode.eq(monthlySummary.brCode))
                .where(monthlySummary.hsYyyymm.eq(filterYearMonth))
                .select(Projections.constructor(MonthlySummaryListDto.class,

                        branch.brName,
                        
                        monthlySummary.hsYyyymm,

                        monthlySummary.hsNormalAmt.sum(),
                        monthlySummary.hsPressed.sum(),
                        monthlySummary.hsWaterRepellent.sum(),
                        monthlySummary.hsStarch.sum(),

                        monthlySummary.hsAdd1Amt.sum(),
                        monthlySummary.hsAdd2Amt.sum(),
                        monthlySummary.hsRepairAmt.sum(),
                        monthlySummary.hsWhitening.sum(),
                        monthlySummary.hsPollution.sum(),

                        monthlySummary.hsUrgentAmt.sum(),
                        monthlySummary.hsDiscountAmt.sum(),
                        monthlySummary.hsTotAmt.sum(),
                        monthlySummary.hsExceptAmt.sum(),

                        monthlySummary.hsSettleTotAmt.sum(),
                        monthlySummary.hsSettleReturnAmt.sum(),

                        monthlySummary.hsSettleAmt.sum(),
                        monthlySummary.hsSettleAmtBr.sum(),
                        monthlySummary.hsSettleAmtFr.sum(),

                        monthlySummary.hsRolayltyAmtBr.sum()

                ));

        query.groupBy(branch.brCode);

        return query.fetch();
    }

    // 본사 지사 월정산입금 리스트
    public List<ReceiptMonthlyListDto> findByReceiptMonthlyList(Long branchId, String filterFromYearMonth, String filterToYearMonth) {

        QMonthlySummary monthlySummary = QMonthlySummary.monthlySummary;
        QReceiptMonthly receiptMonthly = QReceiptMonthly.receiptMonthly;
        QBranch branch = QBranch.branch;

        JPQLQuery<ReceiptMonthlyListDto> query = from(monthlySummary)
                .leftJoin(receiptMonthly).on(receiptMonthly.hsYyyymm.eq(monthlySummary.hsYyyymm))
                .innerJoin(branch).on(branch.id.eq(branchId).and(branch.brCode.eq(monthlySummary.brCode)))
                .where(monthlySummary.hsYyyymm.goe(filterFromYearMonth).and(monthlySummary.hsYyyymm.loe(filterToYearMonth)))
                .select(Projections.constructor(ReceiptMonthlyListDto.class,

                        branch.brName,
                        branch.brCode,
                        monthlySummary.hsYyyymm,

                        new CaseBuilder()
                                .when(monthlySummary.hsRolayltyAmtBr.isNull()).then(0)
                                .otherwise(monthlySummary.hsRolayltyAmtBr.sum()),

                        new CaseBuilder()
                                .when(monthlySummary.hsRolayltyAmtFr.isNull()).then(0)
                                .otherwise(monthlySummary.hsRolayltyAmtFr.sum()),

                        new CaseBuilder()
                                .when(receiptMonthly.hrReceiptBrRoyaltyAmt.isNull()).then(0)
                                .otherwise(receiptMonthly.hrReceiptBrRoyaltyAmt.sum()),

                        new CaseBuilder()
                                .when(receiptMonthly.hrReceiptFrRoyaltyAmt.isNull()).then(0)
                                .otherwise(receiptMonthly.hrReceiptFrRoyaltyAmt.sum())

                ));

        query.groupBy(branch.brCode);

        return query.fetch();
    }

    // 본사 일일정산서 리스트
    @Override
    public List<MonthlySummaryDaysDto> findByReceiptMonthlyDays(String hsYyyymmdd, String frbrCode, String frCode) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT  a.hs_fr_ready_amt a1 , a.hs_cash_sale_amt a2, a.hs_uncollect_cash_amt a3,a.hs_card_sale_amt a4, a.hs_uncollect_card_amt a5 \n");
        sb.append(",a.hs_deferred_sale_amt a6 \n");
        sb.append(",a.hs_normal_cnt b0, a.hs_add1_cnt b1, a.hs_add2_cnt b2, a.hs_add3_cnt b3, a.hs_add4_cnt b4 , a.hs_add5_cnt b5,a.hs_return_cnt b6 ,a.hs_sms_cnt b7 \n");
        sb.append(",a.hs_normal_amt b8, a.hs_pressed +a.hs_starch + a.hs_water_repellent + a.hs_add1_amt +a.hs_add2_amt b9 \n");
        sb.append(",a.hs_repair_amt b10 , a.hs_whitening +hs_pollution b11, a.hs_urgent_amt b12 ,a.hs_discount_amt b13 \n");
        sb.append(",a.hs_tot_return_amt b14 ,a.hs_settle_amt_br b15,a.hs_tot_amt -a.hs_tot_return_amt - a.hs_settle_amt_br b16 ,a.hs_sms_amt b17 \n");
        sb.append(",a.hs_cancel_cnt c1,a.hs_cancel_amt c2, a.hs_modify_cnt c3, a.hs_modify_amt c4, a.hs_delivery_cnt c5 , a.hs_delivery_amt c6 \n");
        sb.append(",a.hs_return_cnt c7, hs_return_amt c8 ,a.hs_uncollect_amt c9,a.hs_new_customer_cnt c10 \n");
        sb.append(",a.hs_point_collect_amt c11 ,a.hs_point_use_cnt c12,a.hs_point_use_amt c13 \n");
        sb.append(",nvl(b.insert_date,'') d1, nvl(c.modify_date,'') d2 \n");
        sb.append("FROM hc_daily_summary a \n");
        sb.append("LEFT OUTER JOIN bs_franchise_login_log  b ON a.fr_code =b.fr_code AND a.hs_yyyymmdd = b.bl_login_dt \n");
        sb.append("LEFT OUTER JOIN bs_franchise_logout_log  c ON a.fr_code =c.fr_code AND a.hs_yyyymmdd = c.bl_logout_dt \n");
        sb.append("WHERE a.hs_yyyymmdd = ?1 \n");
        sb.append("AND a.br_code= ?2 AND a.fr_code= ?3 \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, hsYyyymmdd);
        query.setParameter(2, frbrCode);
        query.setParameter(3, frCode);

        return jpaResultMapper.list(query, MonthlySummaryDaysDto.class);
    }

}
