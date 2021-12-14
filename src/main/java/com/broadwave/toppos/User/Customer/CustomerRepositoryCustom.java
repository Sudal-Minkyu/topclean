package com.broadwave.toppos.User.Customer;

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

}
