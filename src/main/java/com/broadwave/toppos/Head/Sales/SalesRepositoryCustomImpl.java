package com.broadwave.toppos.Head.Sales;

import com.broadwave.toppos.Head.Sales.Dtos.BranchMonthlySaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.BranchRankSaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.FranchiseRankSaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.ItemSaleStatusDto;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-05-24
 * Time :
 * Remark :
 */
@Repository
public class SalesRepositoryCustomImpl extends QuerydslRepositorySupport implements SalesRepositoryCustom {


    @Autowired
    JpaResultMapper jpaResultMapper;

    public SalesRepositoryCustomImpl() {
        super(Object.class);
    }

    // 지사 월간매출, 누적매출 그래프 데이터 NativeQuery
    @Override
    public List<BranchMonthlySaleDto> findByBranchMonthlySale(String filterYear) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH gridData1 AS \n");
        sb.append("( \n");
        sb.append("SELECT x.cs_yyyymm yyyymm,SUM(amt) amt \n");
        sb.append("FROM ( \n");
        sb.append("SELECT a.cs_yyyymm,SUM(a.cs_tot_amount) amt \n");
        sb.append("FROM cl_sales_data_month a \n");
        sb.append("WHERE left(a.cs_yyyymm ,4) =?1 \n");
        sb.append("AND a.cs_type IN('01','02') \n");
        sb.append("GROUP BY a.cs_yyyymm \n");
        sb.append("UNION all \n");
        sb.append("SELECT a.by_yyyymm,0 amt \n");
        sb.append("from bs_yyyymm a \n");
        sb.append("WHERE left(a.by_yyyymm,4)=?1 \n");
        sb.append(") x \n");
        sb.append("GROUP BY x.cs_yyyymm \n");
        sb.append(") \n");
        sb.append("SELECT a.yyyymm ,a.amt, sum(b.amt) sum_amt \n");
        sb.append("FROM gridData1 a \n");
        sb.append("INNER JOIN gridData1 b ON b.yyyymm <=a.yyyymm \n");
        sb.append("GROUP BY a.yyyymm ,a.amt ORDER BY a.yyyymm \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);

        return jpaResultMapper.list(query, BranchMonthlySaleDto.class);
    }

    // 지사 매출순위 데이터 NativeQuery
    @Override
    public List<BranchRankSaleDto> findByBranchRankSale(String filterYear) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH gridData1 AS \n");
        sb.append("( \n");
        sb.append("SELECT x.cs_yyyymm,x.br_code,x.br_name,SUM(x.amt) amt FROM \n");
        sb.append("( \n");
        sb.append("SELECT a.cs_yyyymm,a.br_code,b.br_name,SUM(a.cs_tot_amount) amt \n");
        sb.append("FROM cl_sales_data_month a \n");
        sb.append("INNER JOIN bs_branch b ON a.br_code = b.br_code \n");
        sb.append("WHERE left(a.cs_yyyymm,4) =?1 \n");
        sb.append("AND a.cs_type IN('01','02') \n");
        sb.append("GROUP BY a.cs_yyyymm,b.br_name \n");
        sb.append("UNION all \n");
        sb.append("SELECT a.by_yyyymm,b.br_code,b.br_name,0 amt \n");
        sb.append("FROM bs_yyyymm a \n");
        sb.append("INNER JOIN bs_branch b \n");
        sb.append(" WHERE left(a.by_yyyymm,4)=?1 \n");
        sb.append(") x \n");
        sb.append("GROUP BY x.cs_yyyymm ,x.br_code,x.br_name \n");
        sb.append(") \n");
        sb.append("SELECT a.br_code,a.br_name, \n");
        sb.append("x1.amt amt01 ,x2.amt amt02 ,x3.amt amt03 ,x4.amt amt04 ,x5.amt amt05 ,x6.amt amt06, \n");
        sb.append("x7.amt amt07 ,x8.amt amt08 ,x9.amt amt09 ,x10.amt amt10 ,x11.amt amt11 ,x12.amt amt12, \n");
        sb.append("xt.amt tot_amt \n");
        sb.append("FROM (select DISTINCT br_code,br_name from gridData1 ) a \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='01') x1 ON a.br_code =x1.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='02') x2 ON a.br_code =x2.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='03') x3 ON a.br_code =x3.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='04') x4 ON a.br_code =x4.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='05') x5 ON a.br_code =x5.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='06') x6 ON a.br_code =x6.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='07') x7 ON a.br_code =x7.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='08') x8 ON a.br_code =x8.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='09') x9 ON a.br_code =x9.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='10') x10 ON a.br_code =x10.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='11') x11 ON a.br_code =x11.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, amt from gridData1 WHERE right(cs_yyyymm,2)='12') x12 ON a.br_code =x12.br_code \n");
        sb.append("INNER JOIN (SELECT br_code, sum(amt) amt from gridData1 GROUP BY br_code) xt ON a.br_code =xt.br_code \n");
        sb.append("ORDER BY a.br_name \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);

        return jpaResultMapper.list(query, BranchRankSaleDto.class);
    }

    // 가맹점 매출순위 데이터 NativeQuery
    @Override
    public List<FranchiseRankSaleDto> findByFranchiseRankSale(String brCode, String filterYear) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH gridData2 AS \n");
        sb.append("( \n");
        sb.append("SELECT x.cs_yyyymm,x.fr_code,x.fr_name,SUM(x.amt) amt FROM \n");
        sb.append("( \n");
        sb.append("SELECT a.cs_yyyymm,a.fr_code,b.fr_name,SUM(a.cs_tot_amount) amt \n");
        sb.append("FROM cl_sales_data_month a \n");
        sb.append("INNER JOIN bs_franchise b ON a.fr_code = b.fr_code \n");
        sb.append("WHERE left(a.cs_yyyymm,4) =?1 \n");
        sb.append("AND a.cs_type IN('01','02') \n");
        sb.append("AND a.br_code =?2 \n");
        sb.append("GROUP BY a.cs_yyyymm,b.fr_name \n");
        sb.append("UNION all \n");
        sb.append("SELECT a.by_yyyymm,b.fr_code,b.fr_name,0 amt \n");
        sb.append("FROM bs_yyyymm a \n");
        sb.append("JOIN bs_franchise b ON b.br_code =?2 \n");
        sb.append(" WHERE left(a.by_yyyymm,4)=?1 \n");
        sb.append(") x \n");
        sb.append("GROUP BY x.cs_yyyymm ,x.fr_code,x.fr_name \n");
        sb.append(") \n");

        sb.append("SELECT a.fr_code,a.fr_name, \n");
        sb.append("x1.amt amt01 ,x2.amt amt02 ,x3.amt amt03 ,x4.amt amt04 ,x5.amt amt05 ,x6.amt amt06, \n");
        sb.append("x7.amt amt07 ,x8.amt amt08 ,x9.amt amt09 ,x10.amt amt10 ,x11.amt amt11 ,x12.amt amt12, \n");
        sb.append("xt.amt tot_amt \n");
        sb.append("FROM (select DISTINCT fr_code,fr_name from gridData2 ) a \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='01') x1 ON a.fr_code =x1.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='02') x2 ON a.fr_code =x2.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='03') x3 ON a.fr_code =x3.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='04') x4 ON a.fr_code =x4.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='05') x5 ON a.fr_code =x5.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='06') x6 ON a.fr_code =x6.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='07') x7 ON a.fr_code =x7.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='08') x8 ON a.fr_code =x8.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='09') x9 ON a.fr_code =x9.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='10') x10 ON a.fr_code =x10.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='11') x11 ON a.fr_code =x11.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, amt from gridData2 WHERE right(cs_yyyymm,2)='12') x12 ON a.fr_code =x12.fr_code \n");
        sb.append("INNER JOIN (SELECT fr_code, sum(amt) amt from gridData2 GROUP BY fr_code) xt ON a.fr_code =xt.fr_code \n");
        sb.append("ORDER BY a.fr_name \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);
        query.setParameter(2, brCode);

        return jpaResultMapper.list(query, FranchiseRankSaleDto.class);
    }

    // 품목별 매출현황 데이터 NativeQuery
    @Override
    public List<ItemSaleStatusDto> findByItemSaleStatus(String brId, String frId, String filterYear) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("with saleData as \n");
        sb.append("( \n");
        sb.append("     SELECT x.yyyymm,x.bg_code,x.bg_name,SUM(amt) amt \n");
        sb.append("     FROM ( \n");
        sb.append("         SELECT a.cs_yyyymm yyyymm,b.bg_item_groupcode bg_code,b.bg_name,sum(a.cs_tot_amount) amt \n");
        sb.append("         FROM cl_sales_data_month a \n");
        sb.append("         INNER JOIN bs_item_group b ON left(a.bi_itemcode,3) = b.bg_item_groupcode \n");
        sb.append("         INNER JOIN bs_item b2 ON a.bi_itemcode = b2.bi_itemcode \n");
        sb.append("         WHERE left(a.cs_yyyymm ,4) =?1 \n");
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            sb.append("     AND a.br_code = ( SELECT br_code FROM bs_branch WHERE br_id =?2) \n");
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            sb.append("     AND a.fr_code = ( SELECT fr_code FROM bs_franchise WHERE fr_id =?2) \n");
        }
        sb.append("         AND a.cs_type IN('01','02') \n");
        sb.append("         GROUP BY a.cs_yyyymm,b.bg_item_groupcode,b.bg_name \n");
        sb.append("         UNION all \n");
        sb.append("         SELECT a.by_yyyymm,b.bg_item_groupcode bg_code ,b.bg_name,0 amt \n");
        sb.append("         from bs_yyyymm a \n");
        sb.append("         INNER JOIN bs_item_group b ON b.bg_use_yn ='Y' \n");
        sb.append("         WHERE left(a.by_yyyymm,4) =?1 \n");
        sb.append("     ) x \n");
        sb.append("     GROUP BY x.yyyymm,x.bg_code \n");
        sb.append(") \n");
        sb.append("SELECT a.bg_code,a.bg_name \n");
        sb.append("     ,x1.amt amt01,x1.amt_rate rate01 \n");
        sb.append("     ,x2.amt amt02,x2.amt_rate rate02 \n");
        sb.append("     ,x3.amt amt03,x3.amt_rate rate03 \n");
        sb.append("     ,x4.amt amt04,x4.amt_rate rate04 \n");
        sb.append("     ,x5.amt amt05,x5.amt_rate rate05 \n");
        sb.append("     ,x6.amt amt06,x6.amt_rate rate06 \n");
        sb.append("     ,x7.amt amt07,x7.amt_rate rate07 \n");
        sb.append("     ,x8.amt amt08,x8.amt_rate rate08 \n");
        sb.append("     ,x9.amt amt09,x9.amt_rate rate09 \n");
        sb.append("     ,x10.amt amt10,x10.amt_rate rate10 \n");
        sb.append("     ,x11.amt amt11,x11.amt_rate rate11 \n");
        sb.append("     ,x12.amt amt12,x12.amt_rate rate12 \n");
        sb.append("     ,xt.amt amttot,xt.amt_rate ratetot \n");
        sb.append("FROM (select distinct bg_code,bg_name from saleData) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='01'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='01' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x1 ON a.bg_code = x1.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='02'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='02' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x2 ON a.bg_code = x2.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='03'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='03' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x3 ON a.bg_code = x3.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='04'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='04' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x4 ON a.bg_code = x4.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='05'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='05' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x5 ON a.bg_code = x5.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='06'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='06' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x6 ON a.bg_code = x6.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='07'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='07' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x7 ON a.bg_code = x7.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='08'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='08' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x8 ON a.bg_code = x8.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='09'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='09' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x9 ON a.bg_code = x9.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='10'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='10' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x10 ON a.bg_code = x10.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='11'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='11' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x11 ON a.bg_code = x11.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='12'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='12' \n");
        sb.append("     GROUP BY a.yyyymm,a.bg_code ) x12 ON a.bg_code = x12.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.bg_code,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT left(yyyymm,4) yyyy, SUM(amt) total_amt FROM saleData GROUP BY left(yyyymm,4) ) b ON left(a.yyyymm,4) = b.yyyy \n");
        sb.append("     GROUP BY a.bg_code ) xt ON a.bg_code = xt.bg_code \n");
        sb.append("ORDER BY  a.bg_code \n");


        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterYear);
        // 파라미터 설정과 네이티브 쿼리의 입력인자와 맞춰야함
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            query.setParameter(2, brId);
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            query.setParameter(2, frId);
        }

        return jpaResultMapper.list(query, ItemSaleStatusDto.class);
    }

}
