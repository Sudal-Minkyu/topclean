package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2021-01-05
 * Time :
 * Remark : Toppos 가맹점 접수세부 통합조회용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailSearchDtoSub {
    // 마스터 테이블에서 가져 올 데이터
    private String bcName; // 고객이름
    private String frYyyymmdd; // 접수일자

    // 세부 테이블에서 가져 올 데이터
    private Long fdId; // 접수세부테이블 고유값 ID
    private Long frId; // 접수마스터테이블 고유값 ID
    private String frNo; // 접수코드

    private String fdTag; // 택번호
    private String biItemcode; // 상품코드
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고, S3 지사반송, S4 지사출고 ,S5:가맹점입고, S6: 고객인도, S7 강제출고,S8 강제입고)
    private String fdPreState; // 이전상태 ( S1 : 접수, S2: 지사입고, S3 지사반송, S4 지사출고 ,S5:가맹점입고, S6: 고객인도, S7 강제출고,S8 강제입고)

//    private LocalDateTime fdStateDt; // 현재상태변경시간 = d

    private String fdS2Dt; // 지사입고일
    private String fdS3Dt; // 지사반송일
    private String fdS4Dt; // 지사출고일
    private String fdS5Dt; // 가맹점입고일
    private String fdS6Dt; // 고객인도일

//    private LocalDateTime fdPreStateDt; // 이전상태변경시간 = d

    private String fdCancel; // 접수취소 Y 기본값 N
    private LocalDateTime fdCacelDt; // 접수취소 시간

    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
    private String fdPattern; // 패턴 (00: 미선택 , 01:체크, 02:혼합, 03: 줄)
    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동

    private Integer fdOriginAmt; // 최초 정상금액(일반상태일경우) ??
    private Integer fdNormalAmt; // 접수금액

    private Integer fdAdd2Amt; // 추가비용2(검품후 추가비용 발생)
    private String fdAdd2Remark; // 추가내용2(검품후 추가비용 발생)

    private Integer fdPollution; // 오염 추가요금
    private String fdDiscountGrade; // 할인등급 1:일반(할인0%), 2: vip, 3.vvip
    private Integer fdDiscountAmt; // 할인금액
    private Integer fdQty; // 수량

    private Integer fdRequestAmt; // 접수금액( (정상 + 수선 + 추가1 -할인) * 수량 )
    private String fdSpecialYn; // 특수여부(접수시점)
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private String fdRemark; // 특이사항
    private String fdEstimateDt; // 출고예정일

    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 합계금액이 0이다
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N

    private Integer fdPressed; // 다림질 요금
    private Integer fdAdd1Amt; // 추가비용1(접수시점)
    private String fdAdd1Remark; // 추가내용1(접수시점)
    private Integer fdRepairAmt; // 수선금액
    private String fdRepairRemark; // 수선내용
    private Integer fdWhitening; // 표백 요금
    private Integer fdPollutionLevel; // 오염 선택레벨(0~5)
    private Integer fdWaterRepellent; // 발수가공요금

    private String fdPollutionLocFcn; // 오염위치 앞 - 목  체크면 Y 아니면 N
    private String fdPollutionLocFcs; // 오염위치 앞 - 배  체크면 Y 아니면 N
    private String fdPollutionLocFcb; // 오염위치 앞 - 하의  체크면 Y 아니면 N
    private String fdPollutionLocFlh; // 오염위치 앞 - 왼손  체크면 Y 아니면 N
    private String fdPollutionLocFrh; // 오염위치 앞 - 오른손  체크면 Y 아니면 N
    private String fdPollutionLocFlf; // 오염위치 앞 - 왼발  체크면 Y 아니면 N
    private String fdPollutionLocFrf; // 오염위치 앞 - 오른발  체크면 Y 아니면 N
    private String fdPollutionLocBcn; // 오염위치 뒤 - 목  체크면 Y 아니면 N
    private String fdPollutionLocBcs; // 오염위치 뒤 - 배  체크면 Y 아니면 N
    private String fdPollutionLocBcb; // 오염위치 뒤 - 하의  체크면 Y 아니면 N
    private String fdPollutionLocBrh; // 오염위치 뒤 - 오른손  체크면 Y 아니면 N
    private String fdPollutionLocBlh; // 오염위치 뒤 - 왼손  체크면 Y 아니면 N
    private String fdPollutionLocBrf; // 오염위치 뒤 - 오른발  체크면 Y 아니면 N
    private String fdPollutionLocBlf; // 오염위치 뒤 - 왼발  체크면 Y 아니면 N

    private Integer fdStarch; // 풀먹임 요금

    private String fpCancelYn; // 결제취소 여부

    private String frRefType; // 접수타입(01:일반, 02:무인보관함, 03:배송APP)

}
