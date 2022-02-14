package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.*;

import java.time.LocalDateTime;
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

    List<RequestDetailBusinessdayListDto> findByRequestDetailBusinessdayList(String frCode, String filterFromDt, String filterToDt); // 재세탁, 부착물 카운트
    List<RequestDetailBusinessdayDeliveryDto> findByRequestDetailBusinessdayDeliveryList(String frCode, String filterFromDt, String filterToDt); // 영업일보 통계 총 출고 sum querydsl

    List<RequestDetailPaymentPaper> findByRequestDetailPaymentPaper(String frNo); // 영수증출력

    List<RequestDetailReleaseListDto> findByRequestDetailReleaseList(String brCode, Long frId, LocalDateTime fromDt, java.time.LocalDateTime toDt, String isUrgent); // 지사출고

    List<RequestDetailReleaseCancelListDto> findByRequestDetailReleaseCancelList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo); // 지사출고취소
    List<RequestDetailBranchReturnListDto> findByRequestDetailBranchReturnList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo); // 지사반송
    List<RequestDetailBranchForceListDto> findByRequestDetailBranchForceList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo); // 지사 가맹점강제출고
    List<RequestDetailBranchInspectListDto> findByRequestDetailBranchInspectList(String brCode, Long franchiseId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo); // 확인품등록
    List<RequestDetailBranchInspectionCurrentListDto> findByRequestDetailBranchInspectionCurrentList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo); // 확인품현황

}
