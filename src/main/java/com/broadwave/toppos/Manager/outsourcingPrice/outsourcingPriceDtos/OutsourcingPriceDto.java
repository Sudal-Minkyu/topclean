package com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : Minkyu
 * Date : 2022-05-20
 * Time :
 * Remark : 지사 외주가격 Dto
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutsourcingPriceDto {
    private String bpOutsourcingYn; // 외주 대상품목 (Y/N)
    private Integer bpOutsourcingPrice; // 외주금액
}
