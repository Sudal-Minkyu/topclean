package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentCencelDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentCencelYnDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
public interface PaymentRepositoryCustom {
    List<PaymentCencelDto> findByRequestDetailCencelDataList(String frCode, Long frId);

    List<PaymentCencelYnDto> findByPaymentCancelYn(List<Long> frIdList);
}
