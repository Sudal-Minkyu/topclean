package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
public interface RequestDetailRepositoryCustom {
    List<RequestDetailDto> findByRequestTempDetailList(String frNo);
    List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo);
    List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt);

    List<RequestDetailCloseListDto> findByRequestDetailCloseList(String frCode); // 수기마감
    List<RequestDetailFranchiseInListDto> findByRequestDetailFranchiseInList(String frCode); // 가맹점입고
    List<RequestDetailReturnListDto> findByRequestDetailReturnList(String frCode); // 지사반송
    List<RequestDetailForceListDto> findByRequestDetailForceList(String frCode); // 가맹점강제입고
    List<RequestDetailDeliveryDto> findByRequestDetailDeliveryList(String frCode, Long bcId); // 세탁인도

    List<RequestDetailUncollectDto> findByRequestDetailUncollectList(String frCode, Long frId);
    List<RequestDetailInspectDto> findByRequestDetailInspectList(String frCode, Long bcId, String searchTag, String filterFromDt, String filterToDt); // 검품이력 및 메세지

}
