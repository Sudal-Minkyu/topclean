package com.broadwave.toppos.User.CashReceipt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-04-27
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class CashReceiptRepositoryCustomImpl extends QuerydslRepositorySupport implements CashReceiptRepositoryCustom {

    public CashReceiptRepositoryCustomImpl() {
        super(CashReceipt.class);
    }

}
