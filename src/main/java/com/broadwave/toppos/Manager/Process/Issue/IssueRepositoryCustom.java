package com.broadwave.toppos.Manager.Process.Issue;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-08
 * Time :
 * Remark :
 */
public interface IssueRepositoryCustom {

    // 1주일간의 지사 출고금액 그래프용
    List<IssueWeekAmountDto> findByIssueWeekAmount(String brCode);

    List<IssueDispatchDto> findByDispatchPrintData(List<String> miNoList); // 출고증인쇄 함수

}
