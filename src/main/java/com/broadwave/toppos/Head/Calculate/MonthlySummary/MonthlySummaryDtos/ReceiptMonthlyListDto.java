package com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-06-15
 * Time :
 * Remark : Toppos  본사 월정산 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptMonthlyListDto {

    private String hsYyyymm; // 월정산일자 (hc_monthly_summary)
    private String brCode; // 지사코드
    private String brName; // 지사명

    private BigDecimal hsRolayltyAmtBr; // 정산 - 지사 로열티 금액
    private BigDecimal hsRolayltyAmtFr; // 정산 - 가맹점 로열티 금액

    private BigDecimal hrReceiptBrRoyaltyAmt; // 입금액(지사로열티)
    private BigDecimal hrReceiptFrRoyaltyAmt; // 입금액(가맹점 본사 로열티)

}
