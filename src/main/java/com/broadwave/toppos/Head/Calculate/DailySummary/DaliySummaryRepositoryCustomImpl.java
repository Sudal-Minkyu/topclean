package com.broadwave.toppos.Head.Calculate.DailySummary;

import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.ReceiptDailySummaryListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyBranchListDto;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.querydsl.core.types.Projections;
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
@Repository
public class DaliySummaryRepositoryCustomImpl extends QuerydslRepositorySupport implements DaliySummaryRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

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
                        daliySummary.hsRolayltyAmtFr

                ));

        return query.fetch();
    }

    // 본사 가맹점별 일정산 입금현황
    @Override
    public List<ReceiptDailySummaryListDto> findByReceiptDailySummaryList(String filterYearMonth) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH daliyReceiptData as ( \n");
        sb.append("SELECT x.hs_yyyymmdd,x.br_code, x.fr_code, SUM(amt) amt, SUM(inamt) inamt \n");
        sb.append("FROM ( \n");
        sb.append("SELECT a.hs_yyyymmdd , a.br_code, a.fr_code ,SUM(a.hs_settle_amt_br) amt,0 inamt \n");
        sb.append("FROM hc_daily_summary a \n");
        sb.append("WHERE LEFT(a.hs_yyyymmdd,6) = ?1 \n");
        sb.append("GROUP BY a.hs_yyyymmdd,a.fr_code \n");

        sb.append("UNION all \n");

        sb.append("SELECT a.hs_yyyymmdd , a.br_code, a.fr_code,0, SUM(a.hr_receipt_sale_amt) inamt \n");
        sb.append("FROM hc_receipt_daily a \n");
        sb.append("WHERE left(a.hs_yyyymmdd,6) = ?1 \n");
        sb.append("GROUP BY a.hs_yyyymmdd,a.fr_code \n");

        sb.append("UNION all \n");

        sb.append("SELECT CONCAT(?1,a.dd) hs_yyyymmdd, b.br_code ,b.fr_code, 0 amt,0 inamt \n");
        sb.append("FROM bs_day a \n");
        sb.append("INNER JOIN (SELECT DISTINCT br_code, fr_code FROM hc_daily_summary \n");
        sb.append("WHERE LEFT(hs_yyyymmdd,6) = ?1) b GROUP BY hs_yyyymmdd,fr_code \n");
        sb.append(") x \n");
        sb.append("GROUP BY x.hs_yyyymmdd, x.br_code, x.fr_code \n");
        sb.append(") \n");

        sb.append("SELECT a.br_name,a.fr_name, x32.total total, '' inamtcnt, \n");

        sb.append("CASE WHEN x1.amt = 0 && x1.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x1.amt-x1.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x1.amt-x1.inamt = x1.amt THEN '미입금' ELSE '일부 입금' END amt01, \n");
        sb.append("x1.amt-x1.inamt inamt01, \n");

        sb.append("CASE WHEN x2.amt = 0 && x2.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x2.amt-x2.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x2.amt-x2.inamt = x2.amt THEN '미입금' ELSE '일부 입금' END amt02, \n");
        sb.append("x2.amt-x2.inamt inamt02, \n");

        sb.append("CASE WHEN x3.amt = 0 && x3.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x3.amt-x3.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x3.amt-x3.inamt = x3.amt THEN '미입금' ELSE '일부 입금' END amt03, \n");
        sb.append("x3.amt-x3.inamt inamt03, \n");

        sb.append("CASE WHEN x4.amt = 0 && x4.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x4.amt-x4.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x4.amt-x4.inamt = x4.amt THEN '미입금' ELSE '일부 입금' END amt04, \n");
        sb.append("x4.amt-x4.inamt inamt04, \n");

        sb.append("CASE WHEN x5.amt = 0 && x5.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x5.amt-x5.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x5.amt-x5.inamt = x5.amt THEN '미입금' ELSE '일부 입금' END amt05, \n");
        sb.append("x5.amt-x5.inamt inamt05, \n");

        sb.append("CASE WHEN x6.amt = 0 && x6.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x6.amt-x6.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x6.amt-x6.inamt = x6.amt THEN '미입금' ELSE '일부 입금' END amt06, \n");
        sb.append("x6.amt-x6.inamt inamt06, \n");

        sb.append("CASE WHEN x7.amt = 0 && x7.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x7.amt-x7.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x7.amt-x7.inamt = x7.amt THEN '미입금' ELSE '일부 입금' END amt07, \n");
        sb.append("x7.amt-x7.inamt inamt07, \n");

        sb.append("CASE WHEN x8.amt = 0 && x8.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x8.amt-x8.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x8.amt-x8.inamt = x8.amt THEN '미입금' ELSE '일부 입금' END amt08, \n");
        sb.append("x8.amt-x8.inamt inamt08, \n");

        sb.append("CASE WHEN x9.amt = 0 && x9.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x9.amt-x9.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x9.amt-x9.inamt = x9.amt THEN '미입금' ELSE '일부 입금' END amt09, \n");
        sb.append("x9.amt-x9.inamt inamt09, \n");

        sb.append("CASE WHEN x10.amt = 0 && x10.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x10.amt-x10.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x10.amt-x10.inamt = x10.amt THEN '미입금' ELSE '일부 입금' END amt10, \n");
        sb.append("x10.amt-x10.inamt inamt10, \n");

        sb.append("CASE WHEN x11.amt = 0 && x11.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x11.amt-x11.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x11.amt-x11.inamt = x11.amt THEN '미입금' ELSE '일부 입금' END amt11, \n");
        sb.append("x11.amt-x11.inamt inamt11, \n");

        sb.append("CASE WHEN x12.amt = 0 && x12.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x12.amt-x12.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x12.amt-x12.inamt = x12.amt THEN '미입금' ELSE '일부 입금' END amt12, \n");
        sb.append("x12.amt-x12.inamt inamt12, \n");

        sb.append("CASE WHEN x13.amt = 0 && x13.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x13.amt-x13.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x13.amt-x13.inamt = x13.amt THEN '미입금' ELSE '일부 입금' END amt13, \n");
        sb.append("x13.amt-x13.inamt inamt13, \n");

        sb.append("CASE WHEN x14.amt = 0 && x14.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x14.amt-x14.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x14.amt-x14.inamt = x14.amt THEN '미입금' ELSE '일부 입금' END amt14, \n");
        sb.append("x14.amt-x14.inamt inamt14, \n");

        sb.append("CASE WHEN x15.amt = 0 && x15.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x15.amt-x15.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x15.amt-x15.inamt = x15.amt THEN '미입금' ELSE '일부 입금' END amt15, \n");
        sb.append("x15.amt-x15.inamt inamt15, \n");

        sb.append("CASE WHEN x16.amt = 0 && x16.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x16.amt-x16.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x16.amt-x16.inamt = x16.amt THEN '미입금' ELSE '일부 입금' END amt16, \n");
        sb.append("x16.amt-x16.inamt inamt16, \n");

        sb.append("CASE WHEN x17.amt = 0 && x17.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x17.amt-x17.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x17.amt-x17.inamt = x17.amt THEN '미입금' ELSE '일부 입금' END amt17, \n");
        sb.append("x17.amt-x17.inamt inamt17, \n");

        sb.append("CASE WHEN x18.amt = 0 && x18.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x18.amt-x18.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x18.amt-x18.inamt = x18.amt THEN '미입금' ELSE '일부 입금' END amt18, \n");
        sb.append("x18.amt-x18.inamt inamt18, \n");

        sb.append("CASE WHEN x19.amt = 0 && x19.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x19.amt-x19.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x19.amt-x19.inamt = x19.amt THEN '미입금' ELSE '일부 입금' END amt19, \n");
        sb.append("x19.amt-x19.inamt inamt19, \n");

        sb.append("CASE WHEN x20.amt = 0 && x20.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x20.amt-x20.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x20.amt-x20.inamt = x20.amt THEN '미입금' ELSE '일부 입금' END amt20, \n");
        sb.append("x20.amt-x20.inamt inamt20, \n");

        sb.append("CASE WHEN x21.amt = 0 && x21.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x21.amt-x21.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x21.amt-x21.inamt = x21.amt THEN '미입금' ELSE '일부 입금' END amt21, \n");
        sb.append("x21.amt-x21.inamt inamt21, \n");

        sb.append("CASE WHEN x22.amt = 0 && x22.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x22.amt-x22.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x22.amt-x22.inamt = x22.amt THEN '미입금' ELSE '일부 입금' END amt22, \n");
        sb.append("x22.amt-x22.inamt inamt22, \n");

        sb.append("CASE WHEN x23.amt = 0 && x23.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x23.amt-x23.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x23.amt-x23.inamt = x23.amt THEN '미입금' ELSE '일부 입금' END amt23, \n");
        sb.append("x23.amt-x23.inamt inamt23, \n");

        sb.append("CASE WHEN x24.amt = 0 && x24.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x24.amt-x24.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x24.amt-x24.inamt = x24.amt THEN '미입금' ELSE '일부 입금' END amt24, \n");
        sb.append("x24.amt-x24.inamt inamt24, \n");

        sb.append("CASE WHEN x25.amt = 0 && x25.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x25.amt-x25.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x25.amt-x25.inamt = x25.amt THEN '미입금' ELSE '일부 입금' END amt25, \n");
        sb.append("x25.amt-x25.inamt inamt25, \n");

        sb.append("CASE WHEN x26.amt = 0 && x26.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x26.amt-x26.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x26.amt-x26.inamt = x26.amt THEN '미입금' ELSE '일부 입금' END amt26, \n");
        sb.append("x26.amt-x26.inamt inamt26, \n");

        sb.append("CASE WHEN x27.amt = 0 && x27.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x27.amt-x27.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x27.amt-x27.inamt = x27.amt THEN '미입금' ELSE '일부 입금' END amt27, \n");
        sb.append("x27.amt-x27.inamt inamt27, \n");

        sb.append("CASE WHEN x28.amt = 0 && x28.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x28.amt-x28.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x28.amt-x28.inamt = x28.amt THEN '미입금' ELSE '일부 입금' END amt28, \n");
        sb.append("x28.amt-x28.inamt inamt28, \n");

        sb.append("CASE WHEN x29.amt = 0 && x29.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x29.amt-x29.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x29.amt-x29.inamt = x29.amt THEN '미입금' ELSE '일부 입금' END amt29, \n");
        sb.append("x29.amt-x29.inamt inamt29, \n");

        sb.append("CASE WHEN x30.amt = 0 && x30.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x30.amt-x30.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x30.amt-x30.inamt = x30.amt THEN '미입금' ELSE '일부 입금' END amt30, \n");
        sb.append("x30.amt-x30.inamt inamt30, \n");

        sb.append("CASE WHEN x31.amt = 0 && x31.inamt = 0 THEN '입금 완료' \n");
        sb.append("WHEN x31.amt-x31.inamt <= 0 THEN '입금 완료' \n");
        sb.append("WHEN x31.amt-x31.inamt = x31.amt THEN '미입금' ELSE '일부 입금' END amt31, \n");
        sb.append("x31.amt-x31.inamt inamt31 \n");

        sb.append("FROM ( \n");
        sb.append("SELECT DISTINCT a.br_code, a.fr_code, b.br_name, c.fr_name FROM daliyReceiptData a \n");
        sb.append("INNER JOIN bs_branch b ON b.br_code = a.br_code \n");
        sb.append("INNER JOIN bs_franchise c ON c.fr_code = a.fr_code \n");
        sb.append(") a \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='01') x1 ON a.br_code= x1.br_code AND a.fr_code = x1.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='02') x2 ON a.br_code= x2.br_code AND a.fr_code = x2.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='03') x3 ON a.br_code= x3.br_code AND a.fr_code = x3.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='04') x4 ON a.br_code= x4.br_code AND a.fr_code = x4.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='05') x5 ON a.br_code= x5.br_code AND a.fr_code = x5.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='06') x6 ON a.br_code= x6.br_code AND a.fr_code = x6.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='07') x7 ON a.br_code= x7.br_code AND a.fr_code = x7.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='08') x8 ON a.br_code= x8.br_code AND a.fr_code = x8.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='09') x9 ON a.br_code= x9.br_code AND a.fr_code = x9.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='10') x10 ON a.br_code= x10.br_code AND a.fr_code = x10.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='11') x11 ON a.br_code= x11.br_code AND a.fr_code = x11.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='12') x12 ON a.br_code= x12.br_code AND a.fr_code = x12.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='13') x13 ON a.br_code= x13.br_code AND a.fr_code = x13.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='14') x14 ON a.br_code= x14.br_code AND a.fr_code = x14.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='15') x15 ON a.br_code= x15.br_code AND a.fr_code = x15.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='16') x16 ON a.br_code= x16.br_code AND a.fr_code = x16.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='17') x17 ON a.br_code= x17.br_code AND a.fr_code = x17.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='18') x18 ON a.br_code= x18.br_code AND a.fr_code = x18.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='19') x19 ON a.br_code= x19.br_code AND a.fr_code = x19.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='20') x20 ON a.br_code= x20.br_code AND a.fr_code = x20.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='21') x21 ON a.br_code= x21.br_code AND a.fr_code = x21.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='22') x22 ON a.br_code= x22.br_code AND a.fr_code = x22.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='23') x23 ON a.br_code= x23.br_code AND a.fr_code = x23.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='24') x24 ON a.br_code= x24.br_code AND a.fr_code = x24.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='25') x25 ON a.br_code= x25.br_code AND a.fr_code = x25.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='26') x26 ON a.br_code= x26.br_code AND a.fr_code = x26.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='27') x27 ON a.br_code= x27.br_code AND a.fr_code = x27.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='28') x28 ON a.br_code= x28.br_code AND a.fr_code = x28.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='29') x29 ON a.br_code= x29.br_code AND a.fr_code = x29.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='30') x30 ON a.br_code= x30.br_code AND a.fr_code = x30.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, amt, inamt FROM daliyReceiptData WHERE right(hs_yyyymmdd ,2) ='31') x31 ON a.br_code= x31.br_code AND a.fr_code = x31.fr_code \n");
        sb.append("INNER JOIN (SELECT br_code, fr_code, sum(amt)-sum(inamt) total FROM daliyReceiptData GROUP BY br_code,fr_code) x32 ON a.br_code= x32.br_code AND a.fr_code = x32.fr_code \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterYearMonth);

        return jpaResultMapper.list(query, ReceiptDailySummaryListDto.class);
    }


}
