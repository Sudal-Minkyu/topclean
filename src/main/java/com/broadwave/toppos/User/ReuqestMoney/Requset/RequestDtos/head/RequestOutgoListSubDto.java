package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-05-13
 * Time :
 * Remark : Toppos 본사 출고현황 오른쪽 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOutgoListSubDto {

    private String brName; // 지사명
    private String frName; // 가맹점명

    private String bcName; // 고객명
    private String bcHp; // 휴대폰 번호
    private String bcGrade; // 고객등급

    private String fdTag; // 택번호
    private LocalDateTime fr_insert_date; // 접수시간
    private String fdEstimateDt; // 출고예정일

    private String bgName; // 대분류명
    private String bsName; // 중분류명
    private String biName; // 상품명

    private Integer fdQty; // 수량
    private String fdColor; // 색상코드 (00:미선택 01 흰색 02:검정 03: 회색, 04 빨강 05:주황, 06: 노랑, 07 초록 08 파랑 09:남색 10 보라 11 핑크)
    private String fdPattern; // 패턴 (00: 미선택 , 01:체크, 02:혼합, 03: 줄)

    private Integer fdPromotionDiscountAmt; // 행사할인금액

    private String fdUrgentYn; // 급세탁 여부 (Y  / N) 기본값 : N
    private String fdUrgentType; // 급세탁 타입(1: 당일세탁, 2: 특급세탁(1박2일), 3급세탁(2박3일))

    private Integer fdPressed; // 다림질 요금
    private Integer fdRepairAmt; // 수선금액
    private Integer fdAdd1Amt; // 추가비용1(접수시점)
    private Integer fdWhitening; // 표백 요금

    private Integer fdPollution; // 오염 추가요금
    private Integer fdPollutionType;
    private Integer fdPollutionBack;

    private Integer fdWaterRepellent; // 발수가공요금
    private Integer fdStarch; // 풀먹임 요금
    private Integer fdAdd2Amt; // 추가비용2(검품후 추가비용 발생)
    private Integer fdUrgentAmt; // 급세탁 추가비용
    private Integer fdNormalAmt; // 정상금액

    private Integer fdTotAmt; // 합계금액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private Integer fdDiscountAmt; // 할인금액

    private String fdState; // 현재상태 ( S1 : 접수, S2: 지사입고,S3 강제출고, S4:가맹점입고, S6: 고객인도, S7: 지사강제출고, S8: 가맹점강제입고)
    private String fdRetryYn; // 재세탁 여부 (Y  / N) Y 이면 아래 합계금액이 0이다

    private String fdS2Dt; // 지사입고일
    private String fdS5Dt; // 가맹점입고일
    private String fdS4Dt; // 지사출고일
    private String fdS3Dt; // 지사반송일
    private String fdS6Dt; // 고객인도일
    private LocalDateTime fdS6Time; // 고객인도시간

    private String fdPromotionType; // 행사타입(H1 : 본사직영 수기행사 할인- 기본금액대비할인)
    private Double fdPromotionDiscountRate; // 행사할인율

    public Integer getFdPollutionType() {
        return fdPollutionType + fdPollutionBack;
    }

    public String getFr_insert_date() {
        if(fr_insert_date != null) {
             return fr_insert_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        }else{
            return null;
        }
    }

    public String getFdS6Time() {
        if(fdS6Time != null) {
            return fdS6Time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        }else{
            return null;
        }
    }

}
