package com.broadwave.toppos.Manager.Process.Issue;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.QRequestDetail;
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
 * Date : 2022-03-08
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class IssueRepositoryCustomImpl extends QuerydslRepositorySupport implements IssueRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public IssueRepositoryCustomImpl() {
        super(IssueRepository.class);
    }

    // 1주일간의 지사 출고금액 그래프용
    @Override
    public List<IssueWeekAmountDto> findByIssueWeekAmount(String brCode){

//        WITH RECURSIVE chart4 AS (
//                SELECT a.mi_dt yyyymmdd ,sum(b.fd_tot_amt) amount
//        FROM mr_issue a

//        INNER JOIN fs_request_dtl b ON a.mi_id = b.mi_id
//        INNER JOIN  fs_request b1 ON b.fr_id = b1.fr_id
//        INNER JOIN bs_franchise c ON a.fr_code = c.fr_code
//        WHERE a.br_code ='02' # 지점코드
//        AND b1.fr_confirm_yn ='Y'
//        AND b.fd_cancel ='N'
//        AND a.mi_dt > DATE_FORMAT(DATE_SUB(now(),INTERVAL 7 DAY  ),'%Y%m%d') #7일전
//        GROUP BY a.mi_dt
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 6 DAY  ),'%Y%m%d'), 0 amount
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 5 DAY  ),'%Y%m%d'), 0 amount
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 4 DAY  ),'%Y%m%d'), 0 amount
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 3 DAY  ),'%Y%m%d'), 0 amount
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 2 DAY  ),'%Y%m%d'), 0 amount
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 1 DAY  ),'%Y%m%d'), 0 amount
//        UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 0 DAY  ),'%Y%m%d'), 0 amount
//)
//        SELECT yyyymmdd,SUM(amount) amount
//        from chart4
//        GROUP BY yyyymmdd
//        UNION All
//        SELECT 'Total',SUM(amount) amount
//        from chart4
//        ORDER BY FIELD(yyyymmdd,'Total') DESC, yyyymmdd ASC;

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE chart4 AS ( \n");
        sb.append("SELECT DATE_FORMAT(a.mi_dt,'%m.%d') yyyymmdd, sum(b.fd_tot_amt) amount \n");
        sb.append("FROM mr_issue a \n");
        sb.append("INNER JOIN fs_request_dtl b ON a.mi_id = b.mi_id \n");
        sb.append("INNER JOIN fs_request b1 ON b.fr_id = b1.fr_id \n");
        sb.append("INNER JOIN bs_franchise c ON a.fr_code = c.fr_code \n");
        sb.append("WHERE a.br_code = ?1 \n");
        sb.append("AND b1.fr_confirm_yn ='Y' AND b.fd_cancel ='N' \n");
        sb.append("AND a.mi_dt > DATE_FORMAT(DATE_SUB(now(),INTERVAL 7 DAY  ),'%Y%m%d') \n"); // 7일전 조건문
        sb.append("GROUP BY DATE_FORMAT(a.mi_dt,'%m.%d') \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 6 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 5 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 4 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 3 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 2 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 1 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 0 DAY  ),'%m.%d'), 0 amount \n");
        sb.append(") \n");

        sb.append("SELECT yyyymmdd,SUM(amount) amount \n");
        sb.append("FROM chart4 \n");
        sb.append("GROUP BY yyyymmdd \n");
        sb.append("UNION ALL \n");
        sb.append("SELECT '합계',SUM(amount) amount \n");
        sb.append("FROM chart4 \n");
        sb.append("ORDER BY FIELD(yyyymmdd,'합계') DESC, yyyymmdd ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);

        return jpaResultMapper.list(query, IssueWeekAmountDto.class);
    }

    // 출고증인쇄 데이터 호출
    @Override
    public List<IssueDispatchDto> findByDispatchPrintData(List<String> miNoList) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE printdata AS ( \n");
        sb.append("SELECT b.fd_tag, a.mi_no, \n");
        sb.append("CASE  WHEN IFNULL(b.fd_s6_type,'') ='02' THEN 'T02' \n"); // 반품
        sb.append("WHEN IFNULL(b.fd_retry_yn,'') ='Y' THEN 'T03' \n"); // 재세탁
        sb.append("WHEN left(b.bi_itemcode,3) ='D15' THEN 'T04' \n"); // 가죽, 특수세탁
        sb.append("WHEN left(b.bi_itemcode,3) ='D17' THEN 'T05' \n"); // 털,부속품
        sb.append("WHEN left(b.bi_itemcode,3) ='D03' THEN 'T06' \n"); // 운동화
        sb.append("WHEN left(b.bi_itemcode,3) ='D13' THEN 'T07' \n"); // 침구,커튼
        sb.append("WHEN b.fd_pollution_level != 0 THEN 'T08' \n"); // 오염제거
        sb.append("ELSE 'T01' END type, \n"); // 일반
        sb.append("d.fr_code as frCode, d.fr_name, e.br_name, d.fr_tel_no, e.br_tel_no, c.fr_code as fr, a.mi_dt, a.mi_degree  \n");

        sb.append("FROM mr_issue a \n");
        sb.append("INNER JOIN fs_request_dtl b ON a.mi_id = b.mi_id \n");
        sb.append("INNER JOIN fs_request c ON b.fr_id = c.fr_id \n");
        sb.append("LEFT OUTER JOIN bs_franchise d ON c.fr_code = d.fr_code \n");
        sb.append("LEFT OUTER JOIN bs_branch e ON e.br_code = d.br_code \n");
        sb.append("WHERE a.mi_no IN ?1 \n");
        sb.append(") \n");

        sb.append("SELECT a.fd_tag, a.mi_no, a.type, frCode, a.fr_name, a.br_name, a.fr_tel_no, a.br_tel_no, fr, a.mi_dt, a.mi_degree,  \n");
        sb.append("(SELECT COUNT(*) from printdata x1 WHERE a.mi_no = x1.mi_no AND x1.type ='T01') AS t01Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x2 WHERE a.mi_no = x2.mi_no AND x2.type ='T02') AS t02Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x3 WHERE a.mi_no = x3.mi_no AND x3.type ='T03') AS t03Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x4 WHERE a.mi_no = x4.mi_no AND x4.type ='T04') AS t04Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x5 WHERE a.mi_no = x5.mi_no AND x5.type ='T05') AS t05Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x6 WHERE a.mi_no = x6.mi_no AND x6.type ='T06') AS t06Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x7 WHERE a.mi_no = x7.mi_no AND x7.type ='T07') AS t07Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x8 WHERE a.mi_no = x8.mi_no AND x8.type ='T08') AS t08Count, \n");
        sb.append("(SELECT COUNT(*) from printdata x9 WHERE a.mi_no = x9.mi_no) AS total \n");
        sb.append("FROM printdata a \n");
        sb.append("ORDER BY a.mi_no, a.type, a.fd_tag \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, miNoList);

        return jpaResultMapper.list(query, IssueDispatchDto.class);
    }
}
