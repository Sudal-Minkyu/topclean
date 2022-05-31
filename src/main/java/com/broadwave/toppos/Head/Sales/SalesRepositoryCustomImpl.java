package com.broadwave.toppos.Head.Sales;

import com.broadwave.toppos.Head.Sales.Dtos.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
        sb.append("     ,xt.amt amtTotal,xt.amt_rate rateTotal \n");
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

    // 세부품목별 매출현황 데이터 NativeQuery
    @Override
    public List<ItemSaleDetailStatusDto> findByItemSaleDetailStatus(String bgCode, String brId, String frId, String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("with saleData as \n");
        sb.append("( \n");
        sb.append("     SELECT x.yyyymm,x.bi_itemcode,x.bi_name,SUM(amt) amt \n");
        sb.append("     FROM ( \n");
        sb.append("         SELECT a.cs_yyyymm yyyymm,b.bi_itemcode,b.bi_name,sum(a.cs_tot_amount) amt \n");
        sb.append("         FROM cl_sales_data_month a \n");
        sb.append("         INNER JOIN bs_item b ON a.bi_itemcode = b.bi_itemcode \n");
        sb.append("         WHERE left(a.cs_yyyymm ,4) =?1 \n");
        sb.append("         AND left(a.bi_itemcode,3)=?2 \n");
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            sb.append("     AND a.br_code = ( SELECT br_code FROM bs_branch WHERE br_id =?3) \n");
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            sb.append("     AND a.fr_code = ( SELECT fr_code FROM bs_franchise WHERE fr_id =?3) \n");
        }
        sb.append("         AND a.cs_type IN('01','02') \n");
        sb.append("         GROUP BY a.cs_yyyymm,b.bi_itemcode,b.bi_name \n");
        sb.append("         UNION all \n");
        sb.append("         SELECT a.by_yyyymm,b.bi_itemcode ,b.bi_name,0 amt \n");
        sb.append("         from bs_yyyymm a \n");
        sb.append("         INNER JOIN bs_item b ON b.bi_use_yn='Y' and LEFT(b.bi_itemcode,3)=?2 \n");
        sb.append("         WHERE left(a.by_yyyymm,4)=?1\n");
        sb.append("     ) x \n");
        sb.append("     GROUP BY x.yyyymm,x.bi_itemcode \n");
        sb.append(") \n");
        sb.append("SELECT a.bi_itemcode,a.bi_name \n");
        sb.append(",x1.amt amt01,x1.amt_rate rate01 \n");
        sb.append(",x2.amt amt02,x2.amt_rate rate02 \n");
        sb.append(",x3.amt amt03,x3.amt_rate rate03 \n");
        sb.append(",x4.amt amt04,x4.amt_rate rate04 \n");
        sb.append(",x5.amt amt05,x5.amt_rate rate05 \n");
        sb.append(",x6.amt amt06,x6.amt_rate rate06 \n");
        sb.append(",x7.amt amt07,x7.amt_rate rate07 \n");
        sb.append(",x8.amt amt08,x8.amt_rate rate08 \n");
        sb.append(",x9.amt amt09,x9.amt_rate rate09 \n");
        sb.append(",x10.amt amt10,x10.amt_rate rate10 \n");
        sb.append(",x11.amt amt11,x11.amt_rate rate11 \n");
        sb.append(",x12.amt amt12,x12.amt_rate rate12 \n");
        sb.append(",xt.amt amtTotal,xt.amt_rate rateTotal \n");
        sb.append("FROM (select distinct bi_itemcode,bi_name from saleData) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='01'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='01' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x1 ON a.bi_itemcode = x1.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='02'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='02' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x2 ON a.bi_itemcode = x2.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='03'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='03' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x3 ON a.bi_itemcode = x3.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='04'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='04' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x4 ON a.bi_itemcode = x4.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='05'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='05' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x5 ON a.bi_itemcode = x5.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='06'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='06' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x6 ON a.bi_itemcode = x6.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='07'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='07' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x7 ON a.bi_itemcode = x7.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='08'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='08' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x8 ON a.bi_itemcode = x8.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='09'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='09' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x9 ON a.bi_itemcode = x9.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='10'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='10' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x10 ON a.bi_itemcode = x10.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='11'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='11' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x11 ON a.bi_itemcode = x11.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm,a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT yyyymm,SUM(amt) total_amt FROM saleData WHERE right(yyyymm,2) ='12'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2) ='12' \n");
        sb.append("     GROUP BY a.yyyymm,a.bi_itemcode ) x12 ON a.bi_itemcode = x12.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.bi_itemcode,SUM(amt) amt,IF(b.total_amt =0,0,sum(a.amt)/b.total_amt * 100.0) amt_rate \n");
        sb.append("     FROM saleData a \n");
        sb.append("     INNER JOIN (SELECT left(yyyymm,4) yyyy, SUM(amt) total_amt FROM saleData GROUP BY left(yyyymm,4) ) b ON left(a.yyyymm,4) = b.yyyy \n");
        sb.append("     GROUP BY a.bi_itemcode ) xt ON a.bi_itemcode = xt.bi_itemcode \n");
        sb.append("ORDER BY  a.bi_itemcode \n");


        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterYear);
        query.setParameter(2, bgCode);
        // 파라미터 설정과 네이티브 쿼리의 입력인자와 맞춰야함
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            query.setParameter(3, brId);
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            query.setParameter(3, frId);
        }

        return jpaResultMapper.list(query, ItemSaleDetailStatusDto.class);
    }

    // 월간 접수 현황 데이터 NativeQuery
    @Override
    public List<ReceiptMonthlyStatusDto> findByMonthlyReceiptList(String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH gData AS ( \n");
        sb.append("        SELECT x.cs_yyyymm yyyymm, SUM(cnt) cnt \n");
        sb.append("        FROM ( \n");
        sb.append("                SELECT a.cs_yyyymm, SUM(a.cs_count) cnt \n");
        sb.append("                FROM cl_sales_count_month a \n");
        sb.append("                WHERE LEFT(a.cs_yyyymm, 4) =?1 \n");
        sb.append("                GROUP BY a.cs_yyyymm \n");
        sb.append("                UNION all \n");
        sb.append("                SELECT a.by_yyyymm, 0 cnt \n");
        sb.append("                FROM bs_yyyymm a \n");
        sb.append("                WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("        ) x \n");
        sb.append("        GROUP BY x.cs_yyyymm \n");
        sb.append(") \n");
        sb.append("SELECT a.yyyymm, a.cnt monthly_cnt, sum(b.cnt) accumulation_cnt \n");
        sb.append("FROM gData a \n");
        sb.append("INNER JOIN gData b ON a.yyyymm >= b.yyyymm \n");
        sb.append("GROUP BY a.yyyymm \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);
        return jpaResultMapper.list(query, ReceiptMonthlyStatusDto.class);
    }

    // 지사 접수 순위 데이터 NativeQuery
    @Override
    public List<ReceiptBranchRankDto> findByBranchReceiptRank(String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();
        sb.append("WITH gData AS ( \n");
        sb.append("        SELECT x.yyyymm, x.br_code, x.br_name, SUM(cnt) cnt \n");
        sb.append("        FROM( \n");
        sb.append("                SELECT a.cs_yyyymm yyyymm, b.br_code, b.br_name, SUM(a.cs_count) cnt \n");
        sb.append("                FROM cl_sales_count_month a \n");
        sb.append("                INNER JOIN bs_branch b ON a.br_code = b.br_code \n");
        sb.append("                WHERE LEFT(a.cs_yyyymm, 4) =?1 \n");
        sb.append("                GROUP BY a.cs_yyyymm, b.br_code ,b.br_name \n");
        sb.append("                UNION ALL \n");
        sb.append("                SELECT a.by_yyyymm yyyymm,b.br_code, b.br_name, 0 cnt \n");
        sb.append("                FROM bs_yyyymm a \n");
        sb.append("                JOIN bs_branch b \n");
        sb.append("                WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("        ) x \n");
        sb.append("        GROUP BY x.yyyymm, x.br_code \n");
        sb.append(") \n");
        sb.append("SELECT a.br_code, a.br_name \n");
        sb.append("        , x1.cnt cnt01, x2.cnt cnt02 \n");
        sb.append("        , x3.cnt cnt03, x4.cnt cnt04 \n");
        sb.append("        , x5.cnt cnt05, x6.cnt cnt06 \n");
        sb.append("        , x7.cnt cnt07, x8.cnt cnt08 \n");
        sb.append("        , x9.cnt cnt09, x10.cnt cnt10 \n");
        sb.append("        , x11.cnt cnt11, x12.cnt cnt12 \n");
        sb.append("        , xt.cnt total_cnt \n");
        sb.append("FROM (SELECT DISTINCT br_code, br_name FROM gData) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='01' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x1 on a.br_code = x1.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='02' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x2 on a.br_code = x2.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='03' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x3 on a.br_code = x3.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='04' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x4 on a.br_code = x4.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='05' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x5 on a.br_code = x5.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='06' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x6 on a.br_code = x6.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='07' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x7 on a.br_code = x7.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='08' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x8 on a.br_code = x8.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='09' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x9 on a.br_code = x9.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='10' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x10 on a.br_code = x10.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='11' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x11 on a.br_code = x11.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.yyyymm, a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     WHERE RIGHT(a.yyyymm,2)='12' \n");
        sb.append("     GROUP BY a.yyyymm, a.br_code \n");
        sb.append("     ) x12 on a.br_code = x12.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("     SELECT a.br_code, SUM(cnt) cnt \n");
        sb.append("     FROM gData a \n");
        sb.append("     GROUP BY a.br_code \n");
        sb.append("     ) xt on a.br_code = xt.br_code \n");
        sb.append("ORDER BY xt.cnt DESC \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);
        return jpaResultMapper.list(query, ReceiptBranchRankDto.class);
    }

    // 가맹점 접수 순위 데이터 NativeQuery
    @Override
    public List<ReceiptFranchiseRankDto> findByFranchiseReceiptRank(String brCode, String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH gData AS ( \n");
        sb.append("        SELECT x.yyyymm, x.fr_code, x.fr_name, SUM(cnt) cnt \n");
        sb.append("        FROM( \n");
        sb.append("             SELECT a.cs_yyyymm yyyymm, b.fr_code, b.fr_name, SUM(a.cs_count) cnt \n");
        sb.append("             FROM cl_sales_count_month a \n");
        sb.append("             INNER JOIN bs_franchise b ON a.fr_code = b.fr_code \n");
        sb.append("             WHERE LEFT(a.cs_yyyymm, 4) =?1 \n");
        sb.append("             AND a.br_code =?2 \n");
        sb.append("             GROUP BY a.cs_yyyymm, b.fr_code ,b.fr_name \n");
        sb.append("             UNION ALL \n");
        sb.append("             SELECT a.by_yyyymm yyyymm,b.fr_code, b.fr_name, 0 cnt \n");
        sb.append("             FROM bs_yyyymm a \n");
        sb.append("             JOIN bs_franchise b \n");
        sb.append("             WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("             AND b.br_code =?2 \n");
        sb.append("        ) x \n");
        sb.append("        GROUP BY x.yyyymm, x.fr_code \n");
        sb.append(") \n");
        sb.append("SELECT a.fr_code, a.fr_name \n");
        sb.append(", x1.cnt cnt01, x2.cnt cnt02 \n");
        sb.append(", x3.cnt cnt03, x4.cnt cnt04 \n");
        sb.append(", x5.cnt cnt05, x6.cnt cnt06 \n");
        sb.append(", x7.cnt cnt07, x8.cnt cnt08 \n");
        sb.append(", x9.cnt cnt09, x10.cnt cnt10 \n");
        sb.append(", x11.cnt cnt11, x12.cnt cnt12 \n");
        sb.append(", xt.cnt total_cnt \n");
        sb.append("FROM (SELECT DISTINCT fr_code, fr_name FROM gData) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='01' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x1 on a.fr_code = x1.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='02' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x2 on a.fr_code = x2.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='03' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x3 on a.fr_code = x3.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='04' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x4 on a.fr_code = x4.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='05' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x5 on a.fr_code = x5.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='06' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x6 on a.fr_code = x6.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='07' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x7 on a.fr_code = x7.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='08' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x8 on a.fr_code = x8.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='09' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x9 on a.fr_code = x9.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='10' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x10 on a.fr_code = x10.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='11' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x11 on a.fr_code = x11.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.yyyymm, fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         WHERE RIGHT(a.yyyymm,2)='12' \n");
        sb.append("         GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("         ) x12 on a.fr_code = x12.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("         SELECT a.fr_code, SUM(cnt) cnt \n");
        sb.append("         FROM gData a \n");
        sb.append("         GROUP BY a.fr_code \n");
        sb.append("         ) xt on a.fr_code = xt.fr_code \n");
        sb.append("ORDER BY xt.cnt DESC \n");


        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);
        query.setParameter(2, brCode);
        return jpaResultMapper.list(query, ReceiptFranchiseRankDto.class);
    }

    // 품목별 접수현황 데이터 NativeQuery
    @Override
    public List<ItemReceiptStatusDto> findByItemReceiptStatus(String brId, String frId, String filterYear) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("with receiptData as ( \n");
        sb.append("                SELECT x.yyyymm,x.bg_code,x.bg_name,SUM(cnt) cnt \n");
        sb.append("                FROM ( \n");
        sb.append("                        SELECT a.cs_yyyymm yyyymm,b.bg_item_groupcode bg_code,b.bg_name,sum(a.cs_count) cnt \n");
        sb.append("                        FROM cl_sales_data_month a \n");
        sb.append("                        INNER JOIN bs_item_group b ON left(a.bi_itemcode,3) = b.bg_item_groupcode \n");
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            sb.append("                    AND a.br_code = ( SELECT br_code FROM bs_branch WHERE br_id =?2) \n");
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            sb.append("                    AND a.fr_code = ( SELECT fr_code FROM bs_franchise WHERE fr_id =?2 ) \n");
        }
        sb.append("                        WHERE left(a.cs_yyyymm ,4) =?1 \n");
        sb.append("                        AND a.cs_type = '03' \n");
        sb.append("                        GROUP BY a.cs_yyyymm,b.bg_item_groupcode,b.bg_name \n");
        sb.append("                        UNION all \n");
        sb.append("                        SELECT a.by_yyyymm,b.bg_item_groupcode bg_code ,b.bg_name,0 cnt \n");
        sb.append("                        from bs_yyyymm a \n");
        sb.append("                        INNER JOIN bs_item_group b ON b.bg_use_yn ='Y' \n");
        sb.append("                        WHERE left(a.by_yyyymm,4)=?1 \n");
        sb.append("                ) x \n");
        sb.append("                GROUP BY x.yyyymm,x.bg_code \n");
        sb.append(") \n");
        sb.append("SELECT a.bg_code,a.bg_name \n");
        sb.append(",x1.cnt cnt01,x1.cnt_rate rate01 \n");
        sb.append(",x2.cnt cnt02,x2.cnt_rate rate02 \n");
        sb.append(",x3.cnt cnt03,x3.cnt_rate rate03 \n");
        sb.append(",x4.cnt cnt04,x4.cnt_rate rate04 \n");
        sb.append(",x5.cnt cnt05,x5.cnt_rate rate05 \n");
        sb.append(",x6.cnt cnt06,x6.cnt_rate rate06 \n");
        sb.append(",x7.cnt cnt07,x7.cnt_rate rate07 \n");
        sb.append(",x8.cnt cnt08,x8.cnt_rate rate08 \n");
        sb.append(",x9.cnt cnt09,x9.cnt_rate rate09 \n");
        sb.append(",x10.cnt cnt10,x10.cnt_rate rate10 \n");
        sb.append(",x11.cnt cnt11,x11.cnt_rate rate11 \n");
        sb.append(",x12.cnt cnt12,x12.cnt_rate rate12 \n");
        sb.append(",xt.cnt cnttot,xt.cnt_rate ratetot \n");
        sb.append("FROM (select distinct bg_code,bg_name from receiptData) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='01'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='01' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x1 ON a.bg_code = x1.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='02'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='02' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x2 ON a.bg_code = x2.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='03'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='03' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x3 ON a.bg_code = x3.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='04'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='04' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x4 ON a.bg_code = x4.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='05'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='05' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x5 ON a.bg_code = x5.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='06'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='06' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x6 ON a.bg_code = x6.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='07'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='07' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x7 ON a.bg_code = x7.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='08'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='08' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x8 ON a.bg_code = x8.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='09'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='09' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x9 ON a.bg_code = x9.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='10'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='10' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x10 ON a.bg_code = x10.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='11'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='11' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x11 ON a.bg_code = x11.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM receiptData WHERE right(yyyymm,2) ='12'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='12' \n");
        sb.append("        GROUP BY a.yyyymm,a.bg_code ) x12 ON a.bg_code = x12.bg_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.bg_code,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM receiptData a \n");
        sb.append("        INNER JOIN (SELECT left(yyyymm,4) yyyy, SUM(cnt) total_cnt FROM receiptData GROUP BY left(yyyymm,4) ) b ON left(a.yyyymm,4) = b.yyyy \n");
        sb.append("        GROUP BY a.bg_code ) xt ON a.bg_code = xt.bg_code \n");
        sb.append("ORDER BY a.bg_code \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterYear);
        // 파라미터 설정과 네이티브 쿼리의 입력인자와 맞춰야함
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            query.setParameter(2, brId);
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            query.setParameter(2, frId);
        }

        return jpaResultMapper.list(query, ItemReceiptStatusDto.class);
    }

    // 세부품목별 접수현황 데이터 NativeQuery
    @Override
    public List<ItemReceiptDetailStatusDto> findByItemReceiptDetailStatus(String bgCode, String brId, String frId, String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("with saleData as ( \n");
        sb.append("        SELECT x.yyyymm,x.bi_itemcode,x.bi_name,SUM(cnt) cnt \n");
        sb.append("        FROM ( \n");
        sb.append("                SELECT a.cs_yyyymm yyyymm,b.bi_itemcode,b.bi_name,sum(a.cs_count) cnt \n");
        sb.append("                FROM cl_sales_data_month a \n");
        sb.append("                INNER JOIN bs_item b ON a.bi_itemcode = b.bi_itemcode \n");
        sb.append("                WHERE left(a.cs_yyyymm ,4) =?1 \n");
        sb.append("                AND left(a.bi_itemcode,3)=?2 \n");
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            sb.append("            AND a.br_code = ( SELECT br_code FROM bs_branch WHERE br_id =?3) \n");
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            sb.append("            AND a.fr_code = ( SELECT fr_code FROM bs_franchise WHERE fr_id = ?3) \n");
        }
        sb.append("                AND a.cs_type = '03' \n");
        sb.append("                GROUP BY a.cs_yyyymm,b.bi_itemcode,b.bi_name \n");
        sb.append("                UNION all \n");
        sb.append("                SELECT a.by_yyyymm,b.bi_itemcode ,b.bi_name,0 cnt \n");
        sb.append("                from bs_yyyymm a \n");
        sb.append("                INNER JOIN bs_item b ON b.bi_use_yn='Y' and LEFT(b.bi_itemcode,3)='D01' \n");
        sb.append("                WHERE left(a.by_yyyymm,4)='2022'            \n");
        sb.append("        ) x \n");
        sb.append("        GROUP BY x.yyyymm,x.bi_itemcode \n");
        sb.append(") \n");
        sb.append("SELECT a.bi_itemcode,a.bi_name \n");
        sb.append(",x1.cnt cnt01,x1.cnt_rate rate01 \n");
        sb.append(",x2.cnt cnt02,x2.cnt_rate rate02 \n");
        sb.append(",x3.cnt cnt03,x3.cnt_rate rate03 \n");
        sb.append(",x4.cnt cnt04,x4.cnt_rate rate04 \n");
        sb.append(",x5.cnt cnt05,x5.cnt_rate rate05 \n");
        sb.append(",x6.cnt cnt06,x6.cnt_rate rate06 \n");
        sb.append(",x7.cnt cnt07,x7.cnt_rate rate07 \n");
        sb.append(",x8.cnt cnt08,x8.cnt_rate rate08 \n");
        sb.append(",x9.cnt cnt09,x9.cnt_rate rate09 \n");
        sb.append(",x10.cnt cnt10,x10.cnt_rate rate10 \n");
        sb.append(",x11.cnt cnt11,x11.cnt_rate rate11 \n");
        sb.append(",x12.cnt cnt12,x12.cnt_rate rate12 \n");
        sb.append(",xt.cnt cntTotal,xt.cnt_rate rateTotal \n");
        sb.append("FROM (select distinct bi_itemcode,bi_name from saleData) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='01'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='01' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x1 ON a.bi_itemcode = x1.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='02'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='02' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x2 ON a.bi_itemcode = x2.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='03'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='03' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x3 ON a.bi_itemcode = x3.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='04'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='04' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x4 ON a.bi_itemcode = x4.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='05'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='05' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x5 ON a.bi_itemcode = x5.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='06'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='06' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x6 ON a.bi_itemcode = x6.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='07'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='07' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x7 ON a.bi_itemcode = x7.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='08'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='08' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x8 ON a.bi_itemcode = x8.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='09'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='09' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x9 ON a.bi_itemcode = x9.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='10'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='10' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x10 ON a.bi_itemcode = x10.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='11'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='11' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x11 ON a.bi_itemcode = x11.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm,a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT yyyymm,SUM(cnt) total_cnt FROM saleData WHERE right(yyyymm,2) ='12'  GROUP BY yyyymm) b ON a.yyyymm = b.yyyymm \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2) ='12' \n");
        sb.append("        GROUP BY a.yyyymm,a.bi_itemcode ) x12 ON a.bi_itemcode = x12.bi_itemcode \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.bi_itemcode,SUM(cnt) cnt,IF(b.total_cnt =0,0,sum(a.cnt)/b.total_cnt * 100.0) cnt_rate \n");
        sb.append("        FROM saleData a \n");
        sb.append("        INNER JOIN (SELECT left(yyyymm,4) yyyy, SUM(cnt) total_cnt FROM saleData GROUP BY left(yyyymm,4) ) b ON left(a.yyyymm,4) = b.yyyy \n");
        sb.append("        GROUP BY a.bi_itemcode ) xt ON a.bi_itemcode = xt.bi_itemcode \n");
        sb.append("ORDER BY a.bi_itemcode \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterYear);
        query.setParameter(2, bgCode);
        // 파라미터 설정과 네이티브 쿼리의 입력인자와 맞춰야함
        if (!brId.equals("0") && frId.equals("0")) { // 지사 정보 입력시
            query.setParameter(3, brId);
        } else if (!brId.equals("0") && !frId.equals("0")) { // 가맹점 정보 입력시
            query.setParameter(3, frId);
        }

        return jpaResultMapper.list(query, ItemReceiptDetailStatusDto.class);
    }

    // 지사별 객단가 현황 데이터 NativeQuery
    @Override
    public List<CustomTransactionStatusDto> findByCustomTransactionStatus(String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();


        sb.append("WITH aPrice as( \n");
        sb.append("       SELECT x.yyyymm, x.br_code, x.br_name, avg_price \n");
        sb.append("       FROM( \n");
        sb.append("               SELECT a.cs_yyyymm yyyymm, b.br_code, b.br_name, AVG(a.unit_price) avg_price \n");
        sb.append("               FROM cl_sales_unit_price_month a \n");
        sb.append("               INNER JOIN bs_branch b ON a.br_code = b.br_code \n");
        sb.append("               WHERE LEFT(a.cs_yyyymm, 4) =?1 AND a.price_type = '01' \n");
        sb.append("               GROUP BY a.cs_yyyymm, b.br_code ,b.br_name \n");
        sb.append("               UNION ALL \n");
        sb.append("               SELECT a.by_yyyymm yyyymm,b.br_code, b.br_name, 0 avg_price \n");
        sb.append("               FROM bs_yyyymm a \n");
        sb.append("               JOIN bs_branch b \n");
        sb.append("               WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("       ) x \n");
        sb.append("       GROUP BY x.yyyymm, x.br_code \n");
        sb.append("), \n");
        sb.append("pPrice as( \n");
        sb.append("       SELECT x.yyyymm, x.br_code, x.br_name, pcs_price \n");
        sb.append("       FROM( \n");
        sb.append("               SELECT a.cs_yyyymm yyyymm, b.br_code, b.br_name, AVG(a.unit_price) pcs_price \n");
        sb.append("               FROM cl_sales_unit_price_month a \n");
        sb.append("               INNER JOIN bs_branch b ON a.br_code = b.br_code \n");
        sb.append("               WHERE LEFT(a.cs_yyyymm, 4) =?1 AND a.price_type = '02' \n");
        sb.append("               GROUP BY a.cs_yyyymm, b.br_code ,b.br_name \n");
        sb.append("               UNION ALL \n");
        sb.append("               SELECT a.by_yyyymm yyyymm,b.br_code, b.br_name, 0 pcs_price \n");
        sb.append("               FROM bs_yyyymm a \n");
        sb.append("               JOIN bs_branch b \n");
        sb.append("               WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("       ) x \n");
        sb.append("       GROUP BY x.yyyymm, x.br_code \n");
        sb.append(") \n");
        sb.append("SELECT a.br_code, a.br_name, \n");
        sb.append("a1.avg_price avg_price01, p1.pcs_price pcs_price01, \n");
        sb.append("a2.avg_price avg_price02, p2.pcs_price pcs_price02, \n");
        sb.append("a3.avg_price avg_price03, p3.pcs_price pcs_price03, \n");
        sb.append("a4.avg_price avg_price04, p4.pcs_price pcs_price04, \n");
        sb.append("a5.avg_price avg_price05, p5.pcs_price pcs_price05, \n");
        sb.append("a6.avg_price avg_price06, p6.pcs_price pcs_price06, \n");
        sb.append("a7.avg_price avg_price07, p7.pcs_price pcs_price07, \n");
        sb.append("a8.avg_price avg_price08, p8.pcs_price pcs_price08, \n");
        sb.append("a9.avg_price avg_price09, p9.pcs_price pcs_price09, \n");
        sb.append("a10.avg_price avg_price10, p10.pcs_price pcs_price10, \n");
        sb.append("a11.avg_price avg_price11, p11.pcs_price pcs_price11, \n");
        sb.append("a12.avg_price avg_price12, p12.pcs_price pcs_price12, \n");
        sb.append("atot.avg_price avg_price_total, ptot.pcs_price pcs_price_total \n");
        sb.append("FROM (SELECT DISTINCT br_code, br_name FROM aPrice) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='01' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("	     	 ) a1 on a.br_code = a1.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='01' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("	         ) p1 on a.br_code = p1.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='02' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("	         ) a2 on a.br_code = a2.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='02' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p2 on a.br_code = p2.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='03' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a3 on a.br_code = a3.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='03' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p3 on a.br_code = p3.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='04' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a4 on a.br_code = a4.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='04' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p4 on a.br_code = p4.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='05' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a5 on a.br_code = a5.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='05' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p5 on a.br_code = p5.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='06' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a6 on a.br_code = a6.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='06' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p6 on a.br_code = p6.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='07' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a7 on a.br_code = a7.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='07' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p7 on a.br_code = p7.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='08' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a8 on a.br_code = a8.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='08' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p8 on a.br_code = p8.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='09' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a9 on a.br_code = a9.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='09' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p9 on a.br_code = p9.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='10' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a10 on a.br_code = a10.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='10' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p10 on a.br_code = p10.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='11' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("		     ) a11 on a.br_code = a11.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='11' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p11 on a.br_code = p11.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.yyyymm, a.br_code, avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          WHERE RIGHT(a.yyyymm,2)='12' \n");
        sb.append("          GROUP BY a.yyyymm, a.br_code \n");
        sb.append("	     	) a12 on a.br_code = a12.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.yyyymm, p.br_code, pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          WHERE RIGHT(p.yyyymm,2)='12' \n");
        sb.append("          GROUP BY p.yyyymm, p.br_code \n");
        sb.append("		     ) p12 on a.br_code = p12.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT a.br_code, SUM(a.avg_price)/if(count(if(a.avg_price = 0, null,1)) = '0', 1, count(if(a.avg_price = 0, null,1))) as avg_price \n");
        sb.append("          FROM aPrice a \n");
        sb.append("          GROUP BY a.br_code \n");
        sb.append("		     ) atot on a.br_code = atot.br_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("          SELECT p.br_code, SUM(p.pcs_price)/if(count(if(p.pcs_price = 0, null,1)) = '0', 1, count(if(p.pcs_price = 0, null,1))) as pcs_price \n");
        sb.append("          FROM pPrice p \n");
        sb.append("          GROUP BY p.br_code \n");
        sb.append("		     ) ptot on a.br_code = ptot.br_code \n");
        sb.append("ORDER BY a.br_name ASC");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);

        return jpaResultMapper.list(query, CustomTransactionStatusDto.class);
    }

    // 가맹점별 객단가 현황 데이터 NativeQuery
    @Override
    public List<CustomTransactionDetailStatusDto> findByCustomTransactionDetailStatus(String brCode, String filterYear) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH aPrice as( \n");
        sb.append("        SELECT x.yyyymm, x.fr_code, x.fr_name, avg_price \n");
        sb.append("        FROM( \n");
        sb.append("                SELECT a.cs_yyyymm yyyymm, b.fr_code, b.fr_name, AVG(a.unit_price) avg_price \n");
        sb.append("                FROM cl_sales_unit_price_month a \n");
        sb.append("                INNER JOIN bs_franchise b ON a.fr_code = b.fr_code \n");
        sb.append("                WHERE LEFT(a.cs_yyyymm, 4) =?1 AND a.price_type = '01' \n");
        sb.append("                AND a.br_code = ?2 \n");
        sb.append("                GROUP BY a.cs_yyyymm, b.fr_code ,b.fr_name \n");
        sb.append("                UNION ALL \n");
        sb.append("                SELECT a.by_yyyymm yyyymm, b.fr_code, b.fr_name, 0 avg_price \n");
        sb.append("                FROM bs_yyyymm a \n");
        sb.append("                JOIN bs_franchise b \n");
        sb.append("                WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("                And b.br_code = ?2 \n");
        sb.append("        ) x \n");
        sb.append("        GROUP BY x.yyyymm, x.fr_code \n");
        sb.append("), \n");
        sb.append("        pPrice as( \n");
        sb.append("        SELECT x.yyyymm, x.fr_code, x.fr_name, pcs_price \n");
        sb.append("        FROM( \n");
        sb.append("                SELECT a.cs_yyyymm yyyymm, b.fr_code, b.fr_name, AVG(a.unit_price) pcs_price \n");
        sb.append("                FROM cl_sales_unit_price_month a \n");
        sb.append("                INNER JOIN bs_franchise b ON a.fr_code = b.fr_code \n");
        sb.append("                WHERE LEFT(a.cs_yyyymm, 4) =?1 AND a.price_type = '02' \n");
        sb.append("                AND a.br_code = ?2 \n");
        sb.append("                GROUP BY a.cs_yyyymm, b.fr_code ,b.fr_name \n");
        sb.append("                UNION ALL \n");
        sb.append("                SELECT a.by_yyyymm yyyymm, b.fr_code, b.fr_name, 0 pcs_price \n");
        sb.append("                FROM bs_yyyymm a \n");
        sb.append("                JOIN bs_franchise b \n");
        sb.append("                WHERE LEFT(a.by_yyyymm, 4)=?1 \n");
        sb.append("                And b.br_code = ?2 \n");
        sb.append("        ) x \n");
        sb.append("        GROUP BY x.yyyymm, x.fr_code \n");
        sb.append(") \n");
        sb.append("SELECT a.fr_code, a.fr_name, \n");
        sb.append("a1.avg_price avg_price01, p1.pcs_price pcs_price01, \n");
        sb.append("a2.avg_price avg_price02, p2.pcs_price pcs_price02, \n");
        sb.append("a3.avg_price avg_price03, p3.pcs_price pcs_price03, \n");
        sb.append("a4.avg_price avg_price04, p4.pcs_price pcs_price04, \n");
        sb.append("a5.avg_price avg_price05, p5.pcs_price pcs_price05, \n");
        sb.append("a6.avg_price avg_price06, p6.pcs_price pcs_price06, \n");
        sb.append("a7.avg_price avg_price07, p7.pcs_price pcs_price07, \n");
        sb.append("a8.avg_price avg_price08, p8.pcs_price pcs_price08, \n");
        sb.append("a9.avg_price avg_price09, p9.pcs_price pcs_price09, \n");
        sb.append("a10.avg_price avg_price10, p10.pcs_price pcs_price10, \n");
        sb.append("a11.avg_price avg_price11, p11.pcs_price pcs_price11, \n");
        sb.append("a12.avg_price avg_price12, p12.pcs_price pcs_price12, \n");
        sb.append("atot.avg_price avg_price_total, ptot.pcs_price pcs_price_total \n");
        sb.append("FROM (SELECT DISTINCT fr_code, fr_name FROM aPrice) a \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='01' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("        ) a1 on a.fr_code = a1.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='01' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p1 on a.fr_code = p1.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='02' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a2 on a.fr_code = a2.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='02' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p2 on a.fr_code = p2.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='03' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a3 on a.fr_code = a3.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='03' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p3 on a.fr_code = p3.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='04' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a4 on a.fr_code = a4.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='04' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p4 on a.fr_code = p4.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='05' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a5 on a.fr_code = a5.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='05' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p5 on a.fr_code = p5.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='06' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a6 on a.fr_code = a6.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='06' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p6 on a.fr_code = p6.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='07' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a7 on a.fr_code = a7.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='07' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p7 on a.fr_code = p7.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='08' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a8 on a.fr_code = a8.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='08' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p8 on a.fr_code = p8.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='09' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a9 on a.fr_code = a9.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='09' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p9 on a.fr_code = p9.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='10' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a10 on a.fr_code = a10.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='10' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p10 on a.fr_code = p10.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='11' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("        ) a11 on a.fr_code = a11.fr_code \n");
        sb.append(" INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='11' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("        ) p11 on a.fr_code = p11.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.yyyymm, a.fr_code, avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        WHERE RIGHT(a.yyyymm,2)='12' \n");
        sb.append("        GROUP BY a.yyyymm, a.fr_code \n");
        sb.append("	       ) a12 on a.fr_code = a12.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.yyyymm, p.fr_code, pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        WHERE RIGHT(p.yyyymm,2)='12' \n");
        sb.append("        GROUP BY p.yyyymm, p.fr_code \n");
        sb.append("	       ) p12 on a.fr_code = p12.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT a.fr_code, SUM(a.avg_price)/if(count(if(a.avg_price = 0, null,1)) = '0', 1, count(if(a.avg_price = 0, null,1))) as avg_price \n");
        sb.append("        FROM aPrice a \n");
        sb.append("        GROUP BY a.fr_code \n");
        sb.append("	       ) atot on a.fr_code = atot.fr_code \n");
        sb.append("INNER JOIN ( \n");
        sb.append("        SELECT p.fr_code, SUM(p.pcs_price)/if(count(if(p.pcs_price = 0, null,1)) = '0', 1, count(if(p.pcs_price = 0, null,1))) as pcs_price \n");
        sb.append("        FROM pPrice p \n");
        sb.append("        GROUP BY p.fr_code \n");
        sb.append("	       ) ptot on a.fr_code = ptot.fr_code \n");
        sb.append("ORDER BY a.fr_name ASC");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, filterYear);
        query.setParameter(2, brCode);

        return jpaResultMapper.list(query, CustomTransactionDetailStatusDto.class);
    }
}