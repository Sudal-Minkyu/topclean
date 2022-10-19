package com.broadwave.toppos.Head.Calculate.DailySummary;

import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.ReceiptDailySummaryListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptDaily.ReceiptDailyListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
public interface DaliySummaryRepositoryCustom {

    List<DaliySummaryListDto> findByDaliySummaryList(Long franchiseId, String filterYearMonth);

    List<ReceiptDailySummaryListDto> findByReceiptDailySummaryList(String filterYearMonth, String brCode); // 본사,지사 가맹점별 일정산 입금현황

    List<ReceiptDailyListDto> findByBranchReceiptDailySummaryList(Long franchiseId, String filterFromDt, String filterToDt); // 지사 가맹점 일정산 입금 리스트

}
