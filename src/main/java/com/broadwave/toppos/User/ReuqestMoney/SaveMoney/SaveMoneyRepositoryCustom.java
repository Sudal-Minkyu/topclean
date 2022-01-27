package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyBusinessdayListDto;
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

    List<SaveMoneyBusinessdayListDto> findBySaveMoneyBusinessdayListDto(String frCode, String filterFromDt, String filterToDt); // 영업일보 통계 적립금사용금액 sum querydsl
}
