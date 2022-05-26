package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : DongA
 * Date : 2022-05-26
 * Time :
 * Remark : 월간 접수 현황 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReceiptMonthlyStatusDto {
    private String yyyymm; // 년, 월
    private BigDecimal monthlyCnt; // 월간 접수
    private BigDecimal accumulationCnt; // 누적 접수
}
