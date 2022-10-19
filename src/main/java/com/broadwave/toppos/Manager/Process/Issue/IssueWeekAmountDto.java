package com.broadwave.toppos.Manager.Process.Issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-03-08
 * Time :
 * Remark : Toppos 지사 메인페이지 일주일간 지사 출고 금액 그래프용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueWeekAmountDto {

    private String name; // 출고날짜
    private BigDecimal value; // 접수금액 합계

}
