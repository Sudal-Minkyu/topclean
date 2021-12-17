package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
public interface RequestRepositoryCustom {
    List<RequestListDto> findByRequestTempList(String frCode);

    List<RequestCollectDto> findByRequestCollectList(Customer customer, String nowDate);

    List<RequestInfoDto> findByRequestList(String frCode, String nowDate);
}
