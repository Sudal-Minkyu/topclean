package com.broadwave.toppos.Head.Calculate.DailySummary;

import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.DaliySummaryListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
public interface DaliySummaryRepositoryCustom {
    List<DaliySummaryListDto> findByDaliySummaryList(Long franchiseId, String filterYearMonth);
}
