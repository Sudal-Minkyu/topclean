package com.broadwave.toppos.User.CashReceipt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-04-27
 * Time :
 * Remark :
 */
@Repository
public interface CashReceiptRepository extends JpaRepository<CashReceipt,Long>, CashReceiptRepositoryCustom {

}