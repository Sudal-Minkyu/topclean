package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyDto;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
public interface SaveMoneyRepositoryCustom {
    List<SaveMoneyDto> findBySaveMoney(Customer customer);

    List<SaveMoneyListDto> findBySaveMoneyList(List<Long> customerIdList, String fsType);
}
