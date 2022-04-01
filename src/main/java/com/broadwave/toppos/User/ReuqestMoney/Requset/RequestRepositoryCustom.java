package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.*;

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

    // 1주일간의 가맹점 접수금액
    List<RequestWeekAmountDto> findByRequestWeekAmount(String brCode);

    // 1주일간의 일자별 가맹점 접수금액
    List<RequestWeekAmountDto> findByRequestWeekDaysAmount(String brCode);

    // 메세지 테이블 Native쿼리 호출
    boolean InsertMessage(String message, String nextmessage, String buttonJson, String templatecode, String tableName, Long tableId, String bcHp, String templatecodeNumber);

    RequestFdTagDto findByRequestDetailFdTag(String frCode, Long frId); // 통합조회 택번호 반환용

    List<RequestRealTimeListDto> findByRequestRealTimeList(Long franchiseId, String brCode, String filterFromDt, String filterToDt); // 실시간접수현황 왼쪽 NativeQuery


}
