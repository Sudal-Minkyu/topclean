package com.broadwave.toppos.User.Customer;

import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerInfoDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerListDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerMessageListDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerUncollectListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-14
 * Time :
 * Remark :
 */
public interface CustomerRepositoryCustom {

    List<CustomerListDto> findByCustomerList(String frCode, String searchType, String searchString);

    List<CustomerInfoDto> findByCustomerInfo(String frCode, String searchType, String searchString);

    List<CustomerUncollectListDto> findByCustomerUncollectList(String frCode, String searchType, String searchString);

    // 메세지 보낼 고객 리스트 호출
    List<CustomerMessageListDto> findByMessageCustomerList(String visitDayRange, String bcLastRequestDt, String frCode);

}
