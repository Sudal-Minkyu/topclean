package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
@Repository
public class PaymentRepositoryCustomImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public PaymentRepositoryCustomImpl() {
        super(PaymentRepository.class);
    }


}
