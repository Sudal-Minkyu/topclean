package com.broadwave.toppos.Head.Calculate.DailySummary;

import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.ReceiptDailySummaryListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyBranchListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
public interface DaliySummaryRepositoryCustom {
    List<DaliySummaryListDto> findByDaliySummaryList(Long franchiseId, String filterYearMonth);

    List<ReceiptDailySummaryListDto> findByReceiptDailySummaryList(String filterYearMonth); // 본사 가맹점별 일정산 입금현황

}
