package com.broadwave.toppos.Head.MonthlySummary;

import com.broadwave.toppos.Head.MonthlySummary.MonthlySummaryDtos.MonthlySummaryListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
public interface MonthlySummaryRepositoryCustom {
    List<MonthlySummaryListDto> findByMonthlySummaryList(String filterYearMonth);
}
