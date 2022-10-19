package com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark : Toppos  본사 지사별 월정산요약 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyHeadSummaryListDto {

    private String brName; // 지사명

    private String hsYyyymm; // 정산월

    private Integer hsNormalAmt; // 정상금액
    private Integer hsPressed; // 다림질 요금
    private Integer hsWaterRepellent; // 발수가공요금
    private Integer hsStarch; // 풀먹임 요금

    private Integer hsAdd1Amt; // 추가비용1(접수시점)
    private Integer hsAdd2Amt; // 추가비용2(검품후 추가비용 발생)
    private Integer hsRepairAmt; // 수선금액
    private Integer hsWhitening; // 표백 요금
    private Integer hsPollution; // 오염  추가요금

    private Integer hsUrgentAmt; // 급세탁 추가비용
    private Integer hsDiscountAmt; // 할인금액
    private Integer hsTotAmt; // 정산 - 총매출액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private Integer hsExceptAmt; // 정산- 정산대상 제외 총매출액

    private Integer hsSettleTotAmt; // 정산 - 정산대상 총매출액(A)
    private Integer hsSettleReturnAmt; // 정산 - 정산대상  총반품액(B)

    private Integer hsSettleAmt; // 정산 - 총매출액(A - B)
    private Integer hsSettleAmtBr; // 정산 - 지사 매출액
    private Integer hsSettleAmtFr; // 정산 - 가맹 매출액

    private Integer hsRolayltyAmtBr; // 정산 - 가맹점 로열티 금액

}
