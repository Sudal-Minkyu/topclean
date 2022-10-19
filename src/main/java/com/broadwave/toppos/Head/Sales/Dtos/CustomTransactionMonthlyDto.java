package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : DongA
 * Date : 2022-06-07
 * Time :
 * Remark : 월별 단가 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomTransactionMonthlyDto {
    private String month; // 월
    private Double avgPrice; // 객단가
    private Double pcsPrice; // pcs단가
}
