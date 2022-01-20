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

    List<RequestInfoDto> findByRequestList(String frCode, String nowDate, Customer customer);

    List<RequestUnCollectDto> findByUnCollectList(List<Long> customerIdList, String nowDate);
    List<RequestUnCollectDto> findByBeforeUnCollectList(List<Long> customerIdList, String nowDate);

    List<RequestSearchDto> findByRequestFrCode(String frCode);

    List<RequestCustomerUnCollectDto> findByRequestCustomerUnCollectList(Long bcId, String frCode);
    List<RequestCustomerUnCollectDto> findByRequestUnCollectPayList(List<Long> frIdList, String frCode);

}
