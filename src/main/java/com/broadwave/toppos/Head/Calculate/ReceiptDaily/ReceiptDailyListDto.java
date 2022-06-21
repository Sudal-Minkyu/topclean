package com.broadwave.toppos.Head.Calculate.ReceiptDaily;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-06-21
 * Time :
 * Remark : Toppos  지사 일정산 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDailyListDto {

    private String hsYyyymmdd; // 일정산일자 (hc_daily_summary)

    private String frCode; // 가맹점코드
    private String frName; // 가맹점명

    private BigDecimal hsSettleAmtBr;  // 지사 매출액 (정산 대상 금액)
    private BigDecimal hsRolayltyAmtFr; // 가맹점 로열티 정산액

    private BigDecimal hrReceiptSaleAmt; // 입금액(가맹점의 지사 입금액)
    private BigDecimal hrReceiptRoyaltyAmt; // 입금액 (가맹점 본사 로열티)

}
