package com.broadwave.toppos.Head.Calculate.MonthlySummary;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.*;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
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
@Repository
public class MonthlySummaryRepositoryCustomImpl extends QuerydslRepositorySupport implements MonthlySummaryRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public MonthlySummaryRepositoryCustomImpl() {
        super(MonthlySummary.class);
    }

    // 본사 지사별 월정산 요약 리스트
    public List<MonthlyHeadSummaryListDto> findByHeadMonthlySummaryList(String filterYearMonth) {

        QMonthlySummary monthlySummary = QMonthlySummary.monthlySummary;
        QBranch branch = QBranch.branch;

        JPQLQuery<MonthlyHeadSummaryListDto> query = from(monthlySummary)
                .innerJoin(branch).on(branch.brCode.eq(monthlySummary.brCode))
                .where(monthlySummary.hsYyyymm.eq(filterYearMonth))
                .select(Projections.constructor(MonthlyHeadSummaryListDto.class,

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

    // 지사 월정산 요약 리스트
    public List<MonthlyBranchSummaryListDto> findByBranchMonthlySummaryList(String filterYearMonth, String brCode) {

        QMonthlySummary monthlySummary = QMonthlySummary.monthlySummary;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<MonthlyBranchSummaryListDto> query = from(monthlySummary)
                .innerJoin(franchise).on(franchise.frCode.eq(monthlySummary.frCode))
                .where(monthlySummary.hsYyyymm.eq(filterYearMonth))
                .select(Projections.constructor(MonthlyBranchSummaryListDto.class,

                        franchise.frName,

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

        query.where(monthlySummary.brCode.eq(brCode)).groupBy(monthlySummary.frCode);

        return query.fetch();
    }

    // 본사 가맹점멸 월정산 요약 리스트
    public List<MonthlyFranchiseSummaryListDto> findByFranchiseMonthlySummaryList(String filterYearMonth, String frCode) {

        QMonthlySummary monthlySummary = QMonthlySummary.monthlySummary;
        QBranch branch = QBranch.branch;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<MonthlyFranchiseSummaryListDto> query = from(monthlySummary)
                .innerJoin(branch).on(branch.brCode.eq(monthlySummary.brCode))
                .innerJoin(franchise).on(franchise.frCode.eq(monthlySummary.frCode))
                .select(Projections.constructor(MonthlyFranchiseSummaryListDto.class,

                        branch.brName,
                        franchise.frName,

                        monthlySummary.hsYyyymm,

                        monthlySummary.hsNormalAmt,
                        monthlySummary.hsPressed,
                        monthlySummary.hsWaterRepellent,
                        monthlySummary.hsStarch,

                        monthlySummary.hsSmsAmt,

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

                        monthlySummary.hsRolayltyAmtBr,

                        monthlySummary.hsCashSaleAmt,
                        monthlySummary.hsCardSaleAmt,
                        monthlySummary.hsPointSaleAmt,
                        monthlySummary.hsDeferredSaleAmt

                ));

        if(!frCode.equals("")){
            query.where(monthlySummary.frCode.eq(frCode).and(monthlySummary.hsYyyymm.likeIgnoreCase(filterYearMonth+"%"))).orderBy(monthlySummary.hsYyyymm.asc());
        }else{
            query.where(monthlySummary.hsYyyymm.eq(filterYearMonth)).orderBy(branch.brCode.asc());
        }

        return query.fetch();
    }

    // 본사 지사 월정산입금 리스트
    public List<ReceiptMonthlyListDto> findByReceiptMonthlyList(Long branchId, String filterFromYearMonth, String filterToYearMonth) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT x.hs_yyyymm, x.br_code, x.br_name,  \n");
        sb.append("SUM(hsRolayltyAmtBr), SUM(hsRolayltyAmtFr), \n");
        sb.append("SUM(hrReceiptBrRoyaltyAmt), SUM(hrReceiptFrRoyaltyAmt) \n");

        sb.append("FROM ( \n");

        sb.append("SELECT a.hs_yyyymm, b.br_code, b.br_name, \n");
        sb.append("SUM(a.hs_rolaylty_amt_br) AS hsRolayltyAmtBr, \n");
        sb.append("SUM(a.hs_rolaylty_amt_fr) AS hsRolayltyAmtFr, \n");
        sb.append("0 AS hrReceiptBrRoyaltyAmt, 0 AS hrReceiptFrRoyaltyAmt \n");
        sb.append("FROM hc_monthly_summary a  \n");
        sb.append("INNER JOIN bs_branch b ON b.br_code = a.br_code \n");
        sb.append("WHERE a.hs_yyyymm >= ?2 AND a.hs_yyyymm <= ?3 AND b.br_id = ?1 \n");
        sb.append("GROUP BY a.hs_yyyymm \n");

        sb.append("UNION ALL \n");

        sb.append("SELECT a.hs_yyyymm, b.br_code, b.br_name, \n");
        sb.append("0 AS hsRolayltyAmtBr, \n");
        sb.append("0 AS hsRolayltyAmtFr, \n");
        sb.append("SUM(a.hr_receipt_br_royalty_amt) AS hrReceiptBrRoyaltyAmt, \n");
        sb.append("SUM(a.hr_receipt_fr_royalty_amt) AS hrReceiptFrRoyaltyAmt \n");
        sb.append("FROM hc_receipt_monthly a  \n");
        sb.append("INNER JOIN bs_branch b ON b.br_code = a.br_code \n");
        sb.append("WHERE a.hs_yyyymm >= ?2 AND a.hs_yyyymm <= ?3 AND b.br_id = ?1 \n");
        sb.append("GROUP BY a.hs_yyyymm \n");

        sb.append(") x \n");
        sb.append("GROUP BY x.hs_yyyymm,x.br_name \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, branchId);
        query.setParameter(2, filterFromYearMonth);
        query.setParameter(3, filterToYearMonth);

        return jpaResultMapper.list(query, ReceiptMonthlyListDto.class);
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
