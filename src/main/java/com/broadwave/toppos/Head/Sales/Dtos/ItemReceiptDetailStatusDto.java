package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : DongA
 * Date : 2022-05-27
 * Time :
 * Remark : 세부품목별 접수 현황 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemReceiptDetailStatusDto {
    private String biItemcode; // 상품 코드
    private String biName; // 상품 이름
    private BigDecimal cnt01;  // 1월 접수건수
    private BigDecimal rate01; // 1월 접수비중
    private BigDecimal cnt02;  // 2월 접수건수
    private BigDecimal rate02; // 2월 접수비중
    private BigDecimal cnt03;  // 3월 접수건수
    private BigDecimal rate03; // 3월 접수비중
    private BigDecimal cnt04;  // 4월 접수건수
    private BigDecimal rate04; // 4월 접수비중
    private BigDecimal cnt05;  // 5월 접수건수
    private BigDecimal rate05; // 5월 접수비중
    private BigDecimal cnt06;  // 6월 접수건수
    private BigDecimal rate06; // 6월 접수비중
    private BigDecimal cnt07;  // 7월 접수건수
    private BigDecimal rate07; // 7월 접수비중
    private BigDecimal cnt08;  // 8월 접수건수
    private BigDecimal rate08; // 8월 접수비중
    private BigDecimal cnt09;  // 9월 접수건수
    private BigDecimal rate09; // 9월 접수비중
    private BigDecimal cnt10;  // 10월 접수건수
    private BigDecimal rate10; // 10월 접수비중
    private BigDecimal cnt11;  // 11월 접수건수
    private BigDecimal rate11; // 11월 접수비중
    private BigDecimal cnt12;  // 12월 접수건수
    private BigDecimal rate12; // 12월 접수비중

    private BigDecimal cntTotal; // 전체 접수건수
    private BigDecimal rateTotal; // 전체 접수비중
}
