package com.broadwave.toppos.User.Customer;

import com.broadwave.toppos.User.Customer.CustomerDtos.*;

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

    // 지사가 문자메세지 보낼 고객 리스트
    List<CustomerMessageListDto> findByBrMessageCustomerList(String visitDayRange, String bcLastRequestDt, Long franchiseId, Long branchId, String brCode);

    // 고객 현황 지사,가맹점별 성별 비중
    List<CustomerGenderRateDto> findByCustomerGenderRate(Long brId, Long frId);

    // 고객 현황 지사,가맹점별 나이 비중
    List<CustomerAgeRateDto> findByCustomerAgeRate(Long brId, Long frId);
}
