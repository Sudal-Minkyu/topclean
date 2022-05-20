package com.broadwave.toppos.Manager.outsourcingPrice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DongA
 * Date : 2022-05-13
 * Time :
 * Remark : OutsourcingPrice 복합키 설정
 */
@NoArgsConstructor
@AllArgsConstructor
public class OutsourcingPricePK implements Serializable {
    private String biItemcode;

    private String brCode;

}
