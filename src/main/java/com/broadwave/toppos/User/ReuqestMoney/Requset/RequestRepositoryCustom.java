package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.*;

import java.time.LocalDateTime;
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

    List<RequestBusinessdayListDto> findByBusinessDayList(String frCode, String filterFromDt, String filterToDt);
    List<RequestBusinessdayCustomerListDto> findByBusinessDayCustomerList(String frCode, String filterFromDt, String filterToDt); // 영업일보 통계 리스트-4 querydsl

    RequestPaymentPaperDto findByRequestPaymentPaper(String frNo, Long frId, String frCode);

    List<RequestHistoryListDto> findByRequestHistory(String frCode, String nowDate);

    RequestTempDto findByRequestTemp(String frCode);

    List<RequestWeekAmountDto> findByRequestWeekAmount(String brCode, List<String> frNameList, LocalDateTime weekDays);

}
