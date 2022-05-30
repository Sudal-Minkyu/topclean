package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : DongA
 * Date : 2022-05-30
 * Time :
 * Remark : 가맹점별 객단가 현황 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomTransactionDetailStatusDto {
    private String frCode; // 상품 코드
    private String frName; // 상품 이름
    private Double avgPrice01; // 1월 객단가
    private Double pcsPrice01; // 1월 pcs단가
    private Double avgPrice02; // 2월 객단가
    private Double pcsPrice02; // 2월 pcs단가
    private Double avgPrice03; // 3월 객단가
    private Double pcsPrice03; // 3월 pcs단가
    private Double avgPrice04; // 4월 객단가
    private Double pcsPrice04; // 4월 pcs단가
    private Double avgPrice05; // 5월 객단가
    private Double pcsPrice05; // 5월 pcs단가
    private Double avgPrice06; // 6월 객단가
    private Double pcsPrice06; // 6월 pcs단가
    private Double avgPrice07; // 7월 객단가
    private Double pcsPrice07; // 7월 pcs단가
    private Double avgPrice08; // 8월 객단가
    private Double pcsPrice08; // 8월 pcs단가
    private Double avgPrice09; // 9월 객단가
    private Double pcsPrice09; // 9월 pcs단가
    private Double avgPrice10; // 10월 객단가
    private Double pcsPrice10; // 10월 pcs단가
    private Double avgPrice11; // 11월 객단가
    private Double pcsPrice11; // 11월 pcs단가
    private Double avgPrice12; // 12월 객단가
    private Double pcsPrice12; // 12월 pcs단가

    private Double avgPriceTotal; // 전체 객단가
    private Double pcsPriceTotal; // 전체 pcs단가
}
