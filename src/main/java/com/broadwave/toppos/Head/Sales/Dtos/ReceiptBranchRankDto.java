package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : DongA
 * Date : 2022-05-26
 * Time :
 * Remark : 지사별 접수 순위 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReceiptBranchRankDto {
    private String brCode; // 지사 코드
    private String brName; // 지사 이름
    private BigDecimal cnt01; // 1월 접속건수
    private BigDecimal cnt02; // 2월 접속건수
    private BigDecimal cnt03; // 3월 접속건수
    private BigDecimal cnt04; // 4월 접속건수
    private BigDecimal cnt05; // 5월 접속건수
    private BigDecimal cnt06; // 6월 접속건수
    private BigDecimal cnt07; // 7월 접속건수
    private BigDecimal cnt08; // 8월 접속건수
    private BigDecimal cnt09; // 9월 접속건수
    private BigDecimal cnt10; // 10월 접속건수
    private BigDecimal cnt11; // 11월 접속건수
    private BigDecimal cnt12; // 12월 접속건수
    private BigDecimal totalCnt; // 총 접속건수
}
