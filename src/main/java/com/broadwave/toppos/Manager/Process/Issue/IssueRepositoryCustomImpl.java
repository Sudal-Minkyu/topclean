package com.broadwave.toppos.Manager.Process.Issue;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.QRequestDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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

}
