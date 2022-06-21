package com.broadwave.toppos.Head.Calculate.ReceiptMonthly;

import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryDaysDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyBranchListDto;
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
public class ReceiptMonthlyRepositoryCustomImpl extends QuerydslRepositorySupport implements ReceiptMonthlyRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public ReceiptMonthlyRepositoryCustomImpl() {
        super(ReceiptMonthly.class);
    }

    // 본사 지사별 월정산 입금현황 리스트
    @Override
    public List<ReceiptMonthlyBranchListDto> findByReceiptMonthlyBranchList(String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH receiptdata as ( \n");
            sb.append("SELECT x.yyyymm,x.br_code,SUM(amt) amt,SUM(inamt) inamt \n");
            sb.append("FROM ( \n");
                sb.append("SELECT hs_yyyymm yyyymm ,br_code ,SUM(hs_rolaylty_amt_br) amt,0 inamt \n");
                sb.append("FROM hc_monthly_summary \n");
                sb.append("WHERE left(hs_yyyymm,4) =?1 \n");
                sb.append("GROUP BY hs_yyyymm,br_code \n");

                sb.append("UNION all \n");

                sb.append("SELECT hs_yyyymm,br_code,0, SUM(hr_receipt_br_royalty_amt) inamt \n");
                sb.append("FROM hc_receipt_monthly \n");
                sb.append("WHERE left(hs_yyyymm,4) =?1 \n");
                sb.append("GROUP BY hs_yyyymm,br_code \n");

                sb.append("UNION all \n");

                sb.append("SELECT CONCAT(?1,a.mm),b.br_code, 0 amt,0 inamt \n");
                sb.append("FROM bs_month a \n");
                sb.append("INNER JOIN (SELECT DISTINCT br_code FROM hc_monthly_summary \n");
                sb.append("WHERE LEFT(hs_yyyymm,4) =?1) b \n");
            sb.append(") x \n");
            sb.append("GROUP BY x.yyyymm,x.br_code \n");
        sb.append(") \n");

        sb.append("SELECT a.br_name, \n");

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

        sb.append("x13.total total \n");
        sb.append("FROM ( \n");
            sb.append("SELECT DISTINCT a.br_code,b.br_name \n");
            sb.append("FROM receiptdata\ta  \n");
            sb.append("INNER JOIN bs_branch b ON a.br_code =b.br_code \n");
        sb.append(")  a \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='01') x1 ON a.br_code= x1.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='02') x2 ON a.br_code= x2.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='03') x3 ON a.br_code= x3.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='04') x4 ON a.br_code= x4.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='05') x5 ON a.br_code= x5.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='06') x6 ON a.br_code= x6.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='07') x7 ON a.br_code= x7.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='08') x8 ON a.br_code= x8.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='09') x9 ON a.br_code= x9.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='10') x10 ON a.br_code= x10.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='11') x11 ON a.br_code= x11.br_code \n");
        sb.append("INNER JOIN (SELECT br_code,amt, inamt FROM receiptdata WHERE right(yyyymm ,2) ='12') x12 ON a.br_code= x12.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, sum(amt)-sum(inamt) total FROM receiptdata GROUP BY br_code) x13 ON a.br_code= x13.br_code \n");
        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterYear);

        return jpaResultMapper.list(query, ReceiptMonthlyBranchListDto.class);
    }

}
