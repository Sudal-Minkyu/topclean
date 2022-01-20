package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentMapperDto;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark : Toppos 가맹점 결제관련 Set
 */
@Setter
public class PaymentSet {
    // etc 데이터(고객정보, 상태값 등..)
    private PaymentMapperDto etc;

    // 결제 리스트
    private ArrayList<PaymentDto> payment;

    public ArrayList<PaymentDto> getPayment() {
        return payment;
    }

    public PaymentMapperDto getEtc() {
        return etc;
    }
}
