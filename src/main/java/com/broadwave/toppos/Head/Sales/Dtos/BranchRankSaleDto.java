package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Minkyu
 * Date : 2022-05-24
 * Time :
 * Remark : Toppos 지사 매출순위 그래프데이터 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchRankSaleDto {

    private String brCode; // 지사코드
    private String brName; // 지사명

    private BigDecimal amt01; // 1월 매출
    private BigDecimal amt02; // 2월 매출
    private BigDecimal amt03; // 3월 매출
    private BigDecimal amt04; // 4월 매출
    private BigDecimal amt05; // 5월 매출
    private BigDecimal amt06; // 6월 매출
    private BigDecimal amt07; // 7월 매출
    private BigDecimal amt08; // 8월 매출
    private BigDecimal amt09; // 9월 매출
    private BigDecimal amt10; // 10월 매출
    private BigDecimal amt11; // 11월 매출
    private BigDecimal amt12; // 12월 매출
    private BigDecimal amtTotal; // 전체 매출
}
