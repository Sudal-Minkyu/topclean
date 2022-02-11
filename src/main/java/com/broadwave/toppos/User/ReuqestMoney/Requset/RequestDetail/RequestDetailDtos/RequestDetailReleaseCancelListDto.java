package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-02-11
 * Time :
 * Remark : Toppos 지사출고취소 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailReleaseCancelListDto {

    private Long fdId; // 고유ID값
    private String frName; // 가맹점명
    private LocalDateTime fdS2Time; // 지사입고일시
    private LocalDateTime fdS4Time; // 지사출고일시
    private String fdTag; // 택번호
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)

    private String bgName; // 대분류명
    private String bsName; // 중분류명
    private String biName; // 상품명

    private String fdPriceGrade; // 가격등급  1:일반, 2:고급: 3명품 4:아동
    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 합계금액이 0이다
    private Integer fdPressed; // 다림질 요금
    private Integer fdAdd1Amt; // 추가비용1(접수시점)
    private String fdAdd1Remark; // 추가내용1(접수시점)
    private Integer fdRepairAmt; // 수선금액
    private String fdRepairRemark; // 수선내용
    private Integer fdWhitening; // 표백 요금
    private Integer fdPollution; // 오염 추가요금
    private Integer fdWaterRepellent; // 발수가공요금
    private Integer fdStarch; // 풀먹임 요금
    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N
    private String fdRemark; // 특이사항

    private String bcName; // 고객명
    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 지사출고, S4:가맹점입고, S5: 고객인도)
    private String fdPreState; // 이전상태  ( S1 : 접수, S2: 지사입고,S3 지사출고, S4:가맹점입고, S5: 고객인도)

    public String getFdS2Time() {
        return fdS2Time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

    public String getFdS4Time() {
        return fdS4Time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

}
