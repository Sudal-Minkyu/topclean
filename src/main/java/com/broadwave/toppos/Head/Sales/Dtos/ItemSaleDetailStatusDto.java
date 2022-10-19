package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : DongA
 * Date : 2022-05-25
 * Time :
 * Remark : 세부품목별 매출현황 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ItemSaleDetailStatusDto {
    private String biItemcode; // 상품 코드
    private String biName; // 상품 이름
    private BigDecimal amt01;  // 1월 매출액
    private BigDecimal rate01; // 1월 매출비중
    private BigDecimal amt02;  // 2월 매출액
    private BigDecimal rate02; // 2월 매출비중
    private BigDecimal amt03;  // 3월 매출액
    private BigDecimal rate03; // 3월 매출비중
    private BigDecimal amt04;  // 4월 매출액
    private BigDecimal rate04; // 4월 매출비중
    private BigDecimal amt05;  // 5월 매출액
    private BigDecimal rate05; // 5월 매출비중
    private BigDecimal amt06;  // 6월 매출액
    private BigDecimal rate06; // 6월 매출비중
    private BigDecimal amt07;  // 7월 매출액
    private BigDecimal rate07; // 7월 매출비중
    private BigDecimal amt08;  // 8월 매출액
    private BigDecimal rate08; // 8월 매출비중
    private BigDecimal amt09;  // 9월 매출액
    private BigDecimal rate09; // 9월 매출비중
    private BigDecimal amt10;  // 10월 매출액
    private BigDecimal rate10; // 10월 매출비중
    private BigDecimal amt11;  // 11월 매출액
    private BigDecimal rate11; // 11월 매출비중
    private BigDecimal amt12;  // 12월 매출액
    private BigDecimal rate12; // 12월 매출비중

    private BigDecimal amtTotal; // 전체 매출액
    private BigDecimal rateTotal; // 전체 매출비중
}
