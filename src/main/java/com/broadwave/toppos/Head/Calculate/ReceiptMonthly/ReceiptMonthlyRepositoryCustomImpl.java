package com.broadwave.toppos.Head.Calculate.ReceiptMonthly;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-06-14
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class ReceiptMonthlyRepositoryCustomImpl extends QuerydslRepositorySupport implements ReceiptMonthlyRepositoryCustom {

    public ReceiptMonthlyRepositoryCustomImpl() {
        super(ReceiptMonthly.class);
    }

}
