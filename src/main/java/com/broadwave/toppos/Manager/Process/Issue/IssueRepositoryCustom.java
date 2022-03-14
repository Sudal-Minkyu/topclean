package com.broadwave.toppos.Manager.Process.Issue;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-08
 * Time :
 * Remark :
 */
public interface IssueRepositoryCustom {
    List<IssueWeekAmountDto> findByIssueWeekAmount(String brCode, String formatWeekDays);

    List<IssueDispatchDto> findByDispatchPrintData(List<String> miNoList); // 출고증인쇄 함수

}
