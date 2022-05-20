package com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : DongA
 * Date : 2022-05-20
 * Time :
 * Remark : 지사 외주가격 리스트 입력 dto
 */

@NoArgsConstructor
@Getter
public class OutsourcingPriceListInputDto {
    private String biItemcode;
    private String biName;
    private String bpOutsourcingYn;
}
