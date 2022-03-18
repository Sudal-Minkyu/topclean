package com.broadwave.toppos.Manager.Process.Issue;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMainListDto;
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

    // 지사 메인페이지 일주일간 출고 금액 그래프용
    @Override
    public List<IssueWeekAmountDto> findByIssueWeekAmount(String brCode, String formatWeekDays){

        QIssue issue = QIssue.issue;
        QRequestDetail requestDetail = QRequestDetail.requestDetail;

        JPQLQuery<IssueWeekAmountDto> query = from(issue)
//                .innerJoin(issue, requestDetail.miId)
                .innerJoin(requestDetail).on(requestDetail.miId.eq(issue))
                .where(issue.miDt.goe(formatWeekDays).and(issue.brCode.eq(brCode)))
                .select(Projections.constructor(IssueWeekAmountDto.class,
                        issue.miDt,
                        requestDetail.fdTotAmt.sum()
                ));

        query.groupBy(issue.miDt).orderBy(issue.miDt.desc());

        return query.fetch();
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
        sb.append("INNER JOIN bs_franchise d ON c.fr_code = d.fr_code \n");
        sb.append("INNER JOIN bs_branch e ON e.br_code = d.br_code \n");
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
        sb.append("ORDER BY a.mi_no, a.type \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, miNoList);

        return jpaResultMapper.list(query, IssueDispatchDto.class);
    }
}
