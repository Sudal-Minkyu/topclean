package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.Head.Franohise.Franchise;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수세부 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailDto {

    private String frNo; // 택번호
    private String biItemcode; // 상품코드
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
    private String fdPattert; // 패턴 (00: 미선택 , 01:체크, 02:혼합, 03: 줄)
    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동
    private Integer fdOriginAmt; // 최초 정상금액(일반상태일경우) ??
    private Integer fdNormalAmt; // 정상금액
    private String fdRepairRemark; // 수선내용
    private Integer fdRepairAmt; // 수선금액
    private String fdAdd1Remark; // 추가내용1(접수시점)
    private String fdAdd1SpecialYn; // 특수여부(접수시점)
    private Integer fdAdd1Amt; // 추가비용1(접수시점)

    private Integer fdPressed; // 다림질 요금
    private Integer fdWhitening; // 표백 요금
    private Integer fdPollution; // 오염 추가요금
    private Integer fdPollutionLevel; // 오염 선택레벨(0~5)
    private Integer fdStarch; // 풀먹임 요금
    private Integer fdWaterRepellent; // 발수가공요금

    private String fdDiscountGrade; // 할인등급 1:일반(할인0%), 2: vip, 3.vvip
    private Integer fdDiscountAmt; // 할인금액
    private Integer fdQty; // 수량

    private Integer fdRequestAmt; // 접수금액( (정상 + 수선 + 추가1 -할인) * 수량 )

    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 아래 합계금액이 0이다
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    private String fdRemark; // 특이사항
    private String fdEstimateDt; // 출고예정일

}
