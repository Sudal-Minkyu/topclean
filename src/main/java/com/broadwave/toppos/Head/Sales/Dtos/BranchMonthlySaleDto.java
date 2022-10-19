package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-05-24
 * Time :
 * Remark : Toppos 지사 월간매출,누적매출 그래프데이터 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchMonthlySaleDto {

    private String yyyymm; // 년월
    private BigDecimal cumulationAmt; // 누적매출
    private BigDecimal monthAmt; // 월간매출

}
