package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.*;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
public interface RequestDetailRepositoryCustom {
    // 가맹점 페이지 쿼리 //
    List<RequestDetailDto> findByRequestTempDetailList(String frNo);
    List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo);
    List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt);

    List<RequestDetailCloseListDto> findByRequestDetailCloseList(String frCode); // 수기마감

    @Transactional
    @Modifying
    int findByRequestDetailMobileCloseList(String frCode); // 모바일 수기마감

    List<RequestDetailFranchiseInListDto> findByRequestDetailFranchiseInList(String frCode); // 가맹점입고
//    List<RequestDetailReturnListDto> findByRequestDetailReturnList(String frCode); // 지사반송
    List<RequestDetailForceListDto> findByRequestDetailForceList(Long bcId, String fdTag, String frCode); // 가맹점강제입고
    List<RequestDetailDeliveryDto> findByRequestDetailDeliveryList(String frCode, Long bcId); // 세탁인도
    List<RequestDetailFranchiseInCancelListDto> findByRequestDetailFranchiseInCancelList(String frCode, String fromDt, String toDt); // 가맹점입고취소

    RequestDetailFdStateCountDto findByRequestDetailFdStateS5Count(String frCode, Long bcId); // 세탁접수에 보여줄 가맹입고(S5) 갯수 NativeQuery
    RequestDetailFdStateCountDto findByRequestDetailFdStateS8Count(String frCode, Long bcId); // 세탁접수에 보여줄 강제입고(S8) 갯수 NativeQuery

    List<RequestDetailUncollectDto> findByRequestDetailUncollectList(String frCode, Long frId);
    List<RequestDetailInspectDto> findByRequestDetailInspectList(String frCode, Long bcId, String searchTag, String filterFromDt, String filterToDt); // 검품이력 및 메세지

    List<RequestDetailBusinessdayListDto> findByRequestDetailBusinessdayList(String frCode, String filterFromDt, String filterToDt); // 재세탁, 부착물 카운트
    List<RequestDetailBusinessdayDeliveryDto> findByRequestDetailBusinessdayDeliveryList(String frCode, String filterFromDt, String filterToDt); // 영업일보 통계 총 출고 sum querydsl

    List<RequestDetailPaymentPaper> findByRequestDetailPaymentPaper(String frNo); // 영수증출력

    List<RequestDetailInputMessageDto> findByRequestDetailInputMessage(List<String> frNoList); // 가맹점입고시 고객에게 보내는 메세지 전달할 고객및데이터 리스트 호출

    RequestDetailMessageDto findByRequestDetailReceiptMessage(String frNo, String frCode); // 접수완료이후 메세지테이블에 등록할 데이터 호출

    // 지사 페이지 쿼리 //
    List<RequestDetailReleaseListDto> findByRequestDetailReleaseList(String brCode, Long frId, String fromDt, String toDt, String isUrgent); // 지사출고
    List<RequestDetailReleaseListDto> findByRequestDetailReleaseForceList(String brCode, Long frId, String fromDt, String toDt); // 지사강제 출고

    List<RequestDetailReleaseCancelListDto> findByRequestDetailReleaseCancelList(String brCode, Long frId, String fromDt, String toDt, String tagNo); // 지사출고취소
//    List<RequestDetailBranchReturnListDto> findByRequestDetailBranchReturnList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo); // 지사반송
    List<RequestDetailBranchForceListDto> findByRequestDetailBranchForceList(String brCode, Long frId, String fromDt, String toDt, String tagNo); // 지사 가맹점강제출고
    List<RequestDetailBranchReturnListDto> findByRequestDetailBranchReturnList(String brCode, Long frId, String fromDt, String toDt, String tagNo); // 지사 가맹점반품출고
    List<RequestDetailBranchInspectListDto> findByRequestDetailBranchInspectList(String brCode, Long franchiseId, String fromDt, String toDt, String tagNo); // 확인품등록
    List<RequestDetailBranchInspectionCurrentListDto> findByRequestDetailBranchInspectionCurrentList(String brCode, Long frId, String fromDt, String toDt, String tagNo); // 확인품현황
    List<RequestDetailTagSearchListDto> findByRequestDetailTagSearchList(String brCode, Long frId, String tagNo); // 택번호조회

    List<RequestDetailBranchStoreCurrentListDto> findByRequestDetailBranchStoreCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type); // 입고현황, 체류세탁물현황 - 왼쪽 리스트 호출API
    List<RequestDetailBranchInputCurrentListDto> findByRequestDetailBranchInputCurrentList(String brCode, String frCode, String fdS2Dt); // 지사입고현황 - 오른쪽 리스트 호출API
    List<RequestDetailBranchRemainCurrentListDto> findByRequestDetailBranchRemainCurrentList(String brCode, String frCode, String fdS2Dt); // 체류세탁물현황 - 오른쪽 리스트 호출API

    List<RequestDetailBranchReleaseCurrentListDto> findByRequestDetailBranchReleaseCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type); // 지사출고현황, 지사강제출고현황 - 왼쪽 리스트 호출API
    List<RequestDetailBranchReleaseCurrentRightListDto> findByRequestDetailBranchReleaseCurrentRightList(String brCode, String frCode, String fdS4Dt); // 지사출고현황 - 오른쪽 리스트 호출API
    List<RequestDetailBranchReleaseForceCurrentRightListDto> findByRequestDetailBranchReleaseForceCurrentRightList(String brCode, String frCode, String fdS7Dt); // 지사강제출고현황 - 오른쪽 리스트 호출API

    List<RequestDetailBranchUnReleaseCurrentListDto> findByRequestDetailBranchUnReleaseList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type); // 미출고현황 - 왼쪽 리스트 호출API
    List<RequestDetailBranchUnReleaseCurrentRightListDto> findByRequestDetailBranchUnReleaseCurrentRightList(String brCode, String frCode, String filterFromDt, String filterToDt, String type); // 미출고현황 - 오른쪽 리스트 호출API

    List<RequestDetailBranchReturnCurrentListDto> findByRequestDetailBranchReturnCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt); // 가맹반송현황 - 왼쪽 리스트 호출API
    List<RequestDetailBranchReturnCurrentRightListDto> findByRequestDetailBranchReturnRightCurrentList(String brCode, String frCode, String fdS3Dt); // 가맹반송현황 - 오른쪽 리스트 호출API

    RequestDetailBranchInspeotDto findByBranchInspeotDto(Long fdId); // 확인품등록전 받아올 데이터 호출API

}
