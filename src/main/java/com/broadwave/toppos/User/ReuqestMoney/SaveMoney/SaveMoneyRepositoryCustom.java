package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import com.broadwave.toppos.User.Customer.Customer;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
public interface SaveMoneyRepositoryCustom {
    List<SaveMoneyDto> findBySaveMoneyList(Customer customer);
}
