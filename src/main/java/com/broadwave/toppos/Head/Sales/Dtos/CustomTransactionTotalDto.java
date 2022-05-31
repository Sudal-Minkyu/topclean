package com.broadwave.toppos.Head.Sales.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : DongA
 * Date : 2022-05-31
 * Time :
 * Remark : 객단가, pcs단가 전체 평균 출력 dto
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomTransactionTotalDto {
    private Double avgPriceTotal;
    private Double pcsPriceTotal;
}
