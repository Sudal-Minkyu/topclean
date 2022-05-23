package com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author : DongA
 * Date : 2022-05-20
 * Time :
 * Remark : 지사 입력 확인 체크용
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OutsourcingPriceSearchDto {

    private String bi_itemcode;

    private String br_code;
}
