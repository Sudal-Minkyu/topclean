package com.broadwave.toppos.User.Customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2021-11-16
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class CustomerRepositoryCustomImpl extends QuerydslRepositorySupport implements CustomerRepositoryCustom {

    public CustomerRepositoryCustomImpl() {
        super(Customer.class);
    }


}
