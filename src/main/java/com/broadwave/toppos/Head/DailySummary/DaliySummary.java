package com.broadwave.toppos.Head.DailySummary;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark : Toppos 가맹점별 일정산요약
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(DaliySummaryPK.class)
@Table(name="hc_daily_summary")
public class DaliySummary {

    @Id
    @Column(name="hs_yyyymmdd")
    private String hsYyyymmdd; // 일정산일자

    @Id
    @Column(name="br_code")
    private String brCode; // 지사 코드 2자리

    @Id
    @Column(name="fr_code")
    private String frCode; // 가맹점 코드 3자리

    @Column(name="hs_normal_amt")
    private Integer hsNormalAmt; // 정상금액

    @Column(name="hs_repair_amt")
    private Integer hsRepairAmt; // 수선금액

    @Column(name="hs_add1_amt")
    private Integer hsAdd1Amt; // 추가비용1(접수시점)

    @Column(name="hs_add2_amt")
    private Integer hsAdd2Amt; // 추가비용2(검품후 추가비용 발생)

    @Column(name="hs_pressed")
    private Integer hsPressed; // 다림질 요금

    @Column(name="hs_whitening")
    private Integer hsWhitening; // 표백 요금

    @Column(name="hs_pollution")
    private Integer hsPollution; // 오염  추가요금

    @Column(name="hs_starch")
    private Integer hsStarch; // 풀먹임 요금

    @Column(name="hs_water_repellent")
    private Integer hsWaterRepellent; // 발수가공요금

    @Column(name="hs_discount_amt")
    private Integer hsDiscountAmt; // 할인금액

    @Column(name="hs_urgent_amt")
    private Integer hsUrgentAmt; // 급세탁 추가비용

    @Column(name="hs_qty")
    private Integer hsQty; // 접수수량

    @Column(name="hs_request_amt")
    private Integer hsRequestAmt; // 접수금액( (정상 + 수선 + 추가1 -할인) * 수량 )

    @Column(name="hs_tot_amt")
    private Integer hsTotAmt; // 일정산 - 총매출액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    @Column(name="hs_tot_return_amt")
    private Integer hsTotReturnAmt; // 일정산 - 총반품액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    @Column(name="hs_except_amt")
    private Integer hsExceptAmt; // 일정산- 정산대상 제외 총매출액

    @Column(name="hs_except_return_amt")
    private Integer hsExceptReturnAmt; // 일정산- 정산대상 제외 총반품액

    @Column(name="hs_settle_tot_amt")
    private Integer hsSettleTotAmt; // 일정산 - 정산대상 총매출액(A)

    @Column(name="hs_settle_return_amt")
    private Integer hsSettleReturnAmt; // 일정산 - 정산대상  총반품액(B)

    @Column(name="hs_settle_amt")
    private Integer hsSettleAmt; // 일정산 - 총매출액(A - B)

    @Column(name="hs_settle_amt_br")
    private Integer hsSettleAmtBr; // 일정산 - 지사 매출액

    @Column(name="hs_settle_amt_fr")
    private Integer hsSettleAmtFr; // 일정산 - 가맹 매출액

    @Column(name="hs_rolaylty_amt_br")
    private Integer hsRolayltyAmtBr; // 일정산 - 지사 로열티 금액

    @Column(name="hs_rolaylty_amt_fr")
    private Integer hsRolayltyAmtFr; // 일정산 - 가맹점 로열티 금액

    @Column(name="hs_sms_cnt")
    private Integer hsSmsCnt; // SMS발송 건수

    @Column(name="hs_sms_amt")
    private Integer hsSmsAmt; // SMS발송 금액

    @Column(name="hs_fr_ready_amt")
    private Integer hsFrReadyAmt; // 가맹점 일일 현금 준비금

    @Column(name="hs_cash_sale_amt")
    private Integer hsCashSaleAmt; // 현금 결제매출

    @Column(name="hs_card_sale_amt")
    private Integer hsCardSaleAmt; // 카드 결제매출

    @Column(name="hs_point_sale_amt")
    private Integer hsPointSaleAmt; // 포인트 결제 매출

    @Column(name="hs_deferred_sale_amt")
    private Integer hsDeferredSaleAmt; // 후불 결제매출

    @Column(name="hs_uncollect_cash_amt")
    private Integer hsUncollectCashAmt; // 미수금입금(현금)

    @Column(name="hs_uncollect_card_amt")
    private Integer hsUncollectCardAmt; // 미수금입금(카드)

    @Column(name="hs_normal_cnt")
    private Integer hsNormalCnt; // 일일정산서용 - 기본매출수량

    @Column(name="hs_add1_cnt")
    private Integer hsAdd1Cnt; // 일일정산서용 - 추가(다림질,발수가공,품먹임,확인품추가액) 수량

    @Column(name="hs_add2_cnt")
    private Integer hsAdd2Cnt; // 일일정산서용 - 수선 건수

    @Column(name="hs_add3_cnt")
    private Integer hsAdd3Cnt; // 일일정산서용 -  표백,오염 건수

    @Column(name="hs_add4_cnt")
    private Integer hsAdd4Cnt; // 일일정산서용 - 긴급건수

    @Column(name="hs_add5_cnt")
    private Integer hsAdd5Cnt; // 일일정산서용 - 할인건수

    @Column(name="hs_return_cnt")
    private Integer hsReturnCnt; // 일일정산서용 - 반품건수

    @Column(name="hs_return_amt")
    private Integer hsReturnAmt; // 일일정산서용 - 반품금액

    @Column(name="hs_cancel_cnt")
    private Integer hsCancelCnt; // 일일정산서용 - 접수취소건수

    @Column(name="hs_cancel_amt")
    private Integer hsCancelAmt; // 일일정산서용 - 접수취소금액'

    @Column(name="hs_modify_cnt")
    private Integer hsModifyCnt; // 일일정산서용 - 수정건수

    @Column(name="hs_modify_amt")
    private Integer hsModifyAmt; // 일일정산서용 - 수정금액

    @Column(name="hs_delivery_cnt")
    private Integer hsDeliveryCnt; // 일일정산서용 - 인도건수

    @Column(name="hs_delivery_amt")
    private Integer hsDeliveryAmt; // 일일정산서용 - 인도금액

    @Column(name="hs_uncollect_amt")
    private Integer hsUncollectAmt; // 일일정산서용 - 미수금

    @Column(name="hs_new_customer_cnt")
    private Integer hsNewCustomerCnt; // 일일정산서용 - 신규고객수

    @Column(name="hs_point_collect_amt")
    private Integer hsPointCollectAmt; // 일일정산서용 - 포인트적립금액

    @Column(name="hs_point_use_cnt")
    private Integer hsPointUseCnt; // 일일정산서용 - 포인트사용건수

    @Column(name="hs_point_use_amt")
    private Integer hsPointUseAmt; // 일일정산서용 - 포인트사용금액

    @Column(name="carculate_rate_br")
    private Double carculateRateBr; // 정산비율(지사)

    @Column(name="carculate_rate_fr")
    private Double carculateRateFr; // 정산비율(가맹점)

    @Column(name="royalty_rate_br")
    private Double royaltyRateBr; // 로얄티율(지사)

    @Column(name="royalty_rate_fr")
    private Double royaltyRateFr; // 로얄티율(가맹점)

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

}
