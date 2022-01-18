package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-01-18
 * Time :
 * Remark : Toppos 가맹점 수기마감 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailCloseListDto {
    private Long fdId; // 고유ID값

    private String bcName; // 고객명
    private String frYyyymmdd; // 접수일자

//    private String biItemcode; // 상품코드
    private String fdTag; // 택번호
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
    private String fdPattern; // 패턴 (00: 미선택 , 01:체크, 02:혼합, 03: 줄)
//    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동

//    private String fdRepairRemark; // 수선내용
//
//    private String fdAdd1Remark; // 추가내용1(접수시점)
//    private String fdSpecialYn; // 특수여부(접수시점)
//    private Integer fdAdd1Amt; // 추가비용1(접수시점)
//
//    private Integer fdPressed; // 다림질 요금
//    private Integer fdWhitening; // 표백 요금
//    private Integer fdPollution; // 오염 추가요금
//    private Integer fdPollutionLevel; // 오염 선택레벨(0~5)
//    private Integer fdStarch; // 풀먹임 요금
//    private Integer fdWaterRepellent; // 발수가공요금


    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )

    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 합계금액이 0이다
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N

    private String fdRemark; // 특이사항

//    private String bgName; // 대분류명
//    private String bsName; // 중분류명
//    private String biName; // 상품명

}
