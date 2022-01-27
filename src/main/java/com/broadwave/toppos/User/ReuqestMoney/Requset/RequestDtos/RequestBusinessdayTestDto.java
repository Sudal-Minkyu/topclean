package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-25
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 정리 - ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBusinessdayTestDto {

    private String yyyymmdd; // 접수일자

    private Integer frQtyAll; // 건수
    private Long fdRetryYnAll; // 재세탁
    private Integer biItemGroupAll; // 부속품
    private Integer frTotalAmountAll; // 접수금액
    private Integer totalAverageAll; // 1점 평균단가

    private Integer fpAmtType02All; // 카드결제금액
    private Integer fpAmtType01All; // 현금결제금액
    private Integer fpAmtCancelAll; // 결제취소금액
    private Integer fpAmtUncollectAll; // 미수금결제

    private Integer fsAmtType02All; // 적림금사용금액

    private Long totalReceipt; // 접수
    private Long totalDelivery; // 출고

    private Integer averageReceiptMoney; // 고객평균 점수단가

}
