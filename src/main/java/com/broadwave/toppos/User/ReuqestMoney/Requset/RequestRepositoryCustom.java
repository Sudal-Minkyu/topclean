package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory.MessageHistory;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestFindListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestRealTimeListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestRealTimeListSubDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestWeekAmountDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user.*;

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
    List<RequestUnCollectDto> findByUnCollect(Customer customer);

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

    // 카카오 메세지 테이블 Native쿼리 호출
    boolean kakaoMessage(String message, String nextmessage, String buttonJson, String templatecode, String tableName, Long tableId, String TelNumber, String templatecodeNumber, String messageType);

    // 문자 메세지 테이블 Native쿼리 호출(사용하지않음)
    boolean smsMessage(String message, String TelNumber, String tableName, Long tableId, String templatecodeNumber, String messageType, LocalDateTime sendreqTime);

    // 문자 메세지 배치 테스트
    boolean smsMessageBatchInsert(String message, List<String> bcHpList, String tableName, List<MessageHistory> saveMessageHistorieList, String templatecodeNumber, String msgType, LocalDateTime sendreqTime);

    RequestFdTagDto findByRequestDetailFdTag(String frCode, Long frId); // 통합조회 택번호 반환용

    List<RequestRealTimeListDto> findByRequestRealTimeList(Long franchiseId, String brCode, String filterFromDt, String filterToDt); // 실시간접수현황 왼쪽 NativeQuery

    List<RequestRealTimeListSubDto> findByRequestRealTimeSubList(String frYyyymmdd, String brCode); // 실시간접수현황 오른쪽

    List<RequestFindListDto> findByRequestFindList(Long bcId, String frCode, String filterFromDt, String filterToDt, String searchTag, String ffStateType); // 물건찾기 등록 리스트 Dto

    List<RequestReceiptListDto> findByHeadReceiptList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt); // 본사 접수현황 왼쪽 NativeQuery
    List<RequestReceiptListSubDto> findByHeadReceiptSubList(Long branchId, Long franchiseId, String frYyyymmdd); // 본사 접수현황 오른쪽 - querydsl

    List<RequestUrgentReceiptListDto> findByHeadUrgentReceiptList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt, String fdUrgentYn, String fdUrgentType); // 본사 세탁상태 접수 왼쪽 NativeQuery
    List<RequestUrgentReceiptListSubDto> findByHeadUrgentReceiptSubList(Long branchId, Long franchiseId, String frYyyymmdd, String fdUrgentYn, String fdUrgentType); // 본사 세탁상태 접수 오른쪽 - querydsl

    List<RequestReturnReceiptListDto> findByHeadReturnReceiptList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt); // 본사 반품현황 왼쪽 NativeQuery
    List<RequestReturnReceiptListSubDto> findByHeadReturnReceiptSubList(Long branchId, Long franchiseId, String fdS6Dt); // 본사 접수현황 오른쪽 - querydsl

    List<RequestTagNoReceiptListDto> findByHeadTagNoReceiptSubList(Long branchId, Long franchiseId, String tagNo); // 본사 택번호조회 - querydsl

}
