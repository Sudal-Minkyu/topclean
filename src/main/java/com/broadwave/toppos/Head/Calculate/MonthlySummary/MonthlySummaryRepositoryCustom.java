package com.broadwave.toppos.Head.Calculate.MonthlySummary;

import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryDaysDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryListDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.ReceiptMonthlyListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
public interface MonthlySummaryRepositoryCustom {

    List<MonthlySummaryListDto> findByMonthlySummaryList(String filterYearMonth); // 본사 월정산 요역 리스트

    List<ReceiptMonthlyListDto> findByReceiptMonthlyList(Long branchId, String filterFromYearMonth, String filterToYearMonth); // 본사 지사 월정산입금 리스트

    List<MonthlySummaryDaysDto> findByReceiptMonthlyDays(String hsYyyymmdd, String frbrCode, String frCode); // 본사 일일정산서 리스트

}
