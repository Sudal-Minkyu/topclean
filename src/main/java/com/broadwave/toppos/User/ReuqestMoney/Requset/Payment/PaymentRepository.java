package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
;
}