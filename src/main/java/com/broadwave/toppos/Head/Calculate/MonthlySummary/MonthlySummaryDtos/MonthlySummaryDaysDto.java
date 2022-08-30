package com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark : Toppos  본사 일일정산서 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryDaysDto {

    private Integer a1; // 가맹점 일일 현금 준비금
    private Integer a2; // 현금결제매출
    private Integer a3; // 미수금입금(현금)
    private Integer a4; // 카드결제매출
    private Integer a5; // 미수금입금(카드)
    private Integer a6; // 후불결제매출
    private Integer b0; // 기본매출수량
    private Integer b1; // 추가(다림질,발수가공,품먹임,확인품추가액) 수량
    private Integer b2; // 수선 건수
    private Integer b3; // 표백,오염 건수
    private Integer b4; // 긴급건수
    private Integer b5; // 할인건수
    private Integer b6; // 반품건수
    private Integer b7; // SMS발송 건수
    private Integer b8; // 정상금액
    private BigInteger b9; // 다림질 요금 + 풀먹임 요금 + 발수가공요금 + 추가비용1(접수시점) + 추가비용2(검품후 추가비용 발생)
    private Integer b10; // 수선금액
    private BigInteger b11; // 표백 요금 + 오염  추가요금
    private Integer b12; // 급세탁 추가비용
    private Integer b13; // 할인금액
    private Integer b14; // 총반품액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 )
    private Integer b15; // 지사 매출액
    private BigInteger b16; // 총매출액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 ) - 총반품액( (정상 + 수선 + 추가1 + 추가2 -할인) * 수량 ) - 지사 매출액
    private Integer b17; // SMS발송 금액
    private Integer c1; // 접수취소건수
    private Integer c2; // 접수취소금액
    private Integer c3; // 수정건수
    private Integer c4; // 수정금액
    private Integer c5; // 인도건수
    private Integer c6; // 인도금액
    private Integer c7; // 반품건수
    private Integer c8; // 반품금액
    private Integer c9; // 미수금
    private Integer c10; // 신규고객수
    private Integer c11; // 포인트적립금액
    private Integer c12; // 포인트사용건수
    private Integer c13; // 포인트사용금액

    private String d1; // nvl(입력시간) 없으면 공백
    private String d2; // nvl(수정시간) 없으면 공백

}
