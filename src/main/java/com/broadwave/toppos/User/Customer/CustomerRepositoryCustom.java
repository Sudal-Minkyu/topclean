package com.broadwave.toppos.User.Customer;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-16
 * Time :
 * Remark :
 */
public interface CustomerRepositoryCustom {

    CustomerInfoDto findByCustomerInfo(String frCode, String searchType, String searchString);

    List<CustomerListDto> findByCustomerList(String frCode, String searchType, String searchString);
}
