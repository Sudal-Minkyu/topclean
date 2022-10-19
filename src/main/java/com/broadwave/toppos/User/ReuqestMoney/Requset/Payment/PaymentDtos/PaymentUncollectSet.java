package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-21
 * Time :
 * Remark : Toppos 가맹점 미수금관리 결제테이블 Set
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUncollectSet {

    private List<Long> frIdList;
    private PaymentUncollectMapperDto data;

}
