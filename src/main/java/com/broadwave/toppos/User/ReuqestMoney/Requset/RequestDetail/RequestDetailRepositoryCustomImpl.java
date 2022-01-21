package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.QInspeot;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class RequestDetailRepositoryCustomImpl extends QuerydslRepositorySupport implements RequestDetailRepositoryCustom {

    public RequestDetailRepositoryCustomImpl() {
        super(RequestDetailRepository.class);
    }

    public List<RequestDetailDto> findByRequestTempDetailList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailDto> query = from(requestDetail)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .select(Projections.constructor(RequestDetailDto.class,
                        requestDetail.id,

                        requestDetail.biItemcode,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        requestDetail.fdPattern,
                        requestDetail.fdPriceGrade,

                        requestDetail.fdOriginAmt,
                        requestDetail.fdNormalAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdRepairAmt,

                        requestDetail.fdAdd1Remark,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdAdd1Amt,

                        requestDetail.fdPressed,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdPollutionLevel,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,

                        requestDetail.fdDiscountGrade,
                        requestDetail.fdDiscountAmt,
                        requestDetail.fdQty,

                        requestDetail.fdRequestAmt,

                        requestDetail.fdRetryYn,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdRemark,
                        requestDetail.fdEstimateDt,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName
                    ));

        query.where(requestDetail.frNo.eq(frNo));

        return query.fetch();
    }

    public List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;

        JPQLQuery<RequestDetailAmtDto> query = from(requestDetail)
                .select(Projections.constructor(RequestDetailAmtDto.class,
                        requestDetail.fdNormalAmt,
                        requestDetail.fdTotAmt
                ));

        query.where(requestDetail.frNo.eq(frNo));

        return query.fetch();
    }

    // 통합조회 querydsl
    public List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<RequestDetailSearchDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .leftJoin(inspeot).on(inspeot.fdId.eq(requestDetail))

                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .where(request.frYyyymmdd.loe(filterFromDt).and(request.frYyyymmdd.goe(filterToDt)).and(request.frConfirmYn.eq("Y")))

                .select(Projections.constructor(RequestDetailSearchDto.class,

                        request.bcId.bcName,
                        request.frYyyymmdd,

                        requestDetail.id,
                        request.id,
                        request.frNo,

                        requestDetail.fdTag,
                        requestDetail.biItemcode,
                        requestDetail.fdState,
                        requestDetail.fdPreState,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS3Dt,
                        requestDetail.fdS4Dt,
                        requestDetail.fdS5Dt,
                        requestDetail.fdS6Dt,

                        requestDetail.fdCancel,
                        requestDetail.fdCacelDt,

                        requestDetail.fdColor,
                        requestDetail.fdPattern,
                        requestDetail.fdPriceGrade,

                        requestDetail.fdOriginAmt,
                        requestDetail.fdNormalAmt,

                        requestDetail.fdAdd2Amt,
                        requestDetail.fdAdd2Remark,

                        requestDetail.fdPollution,
                        requestDetail.fdDiscountGrade,
                        requestDetail.fdDiscountAmt,
                        requestDetail.fdQty,

                        requestDetail.fdRequestAmt,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,
                        requestDetail.fdEstimateDt,

                        requestDetail.fdRetryYn,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollutionLevel,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,

                        request.frRefType

                ));

        query.orderBy(requestDetail.id.asc()).groupBy(requestDetail);

        if(bcId != null){
            query.where(request.bcId.bcId.eq(bcId));
        }

        if(!searchTag.equals("")){
            query.where(requestDetail.fdTag.likeIgnoreCase(searchTag));
        }

        if(!filterCondition.equals("")){
            if(filterCondition.equals("B") || filterCondition.equals("F")){
                query.where(inspeot.fiType.eq(filterCondition));
            }else{
                query.where(requestDetail.fdState.eq(filterCondition));
            }
        }

        if(!filterFromDt.equals("") && !filterToDt.equals("")){
            query.where(request.frYyyymmdd.loe(filterFromDt).and(request.frYyyymmdd.goe(filterToDt)));
        }

        return query.fetch();
    }

    // 미수관리 페이지의 접수 상세보기 querydsl
    public List<RequestDetailUncollectDto> findByRequestDetailUncollectList(String frCode, Long frId){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailUncollectDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.frId.id.eq(frId)))
                .select(Projections.constructor(RequestDetailUncollectDto.class,

                        request.frYyyymmdd,
                        requestDetail.fdTag,

                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdS6Dt
                ));

        query.orderBy(requestDetail.id.desc());

        return query.fetch();
    }

    // 수기마감 querydsl
    public List<RequestDetailCloseListDto> findByRequestDetailCloseList(String frCode){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailCloseListDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.fdState.eq("S1")))
                .select(Projections.constructor(RequestDetailCloseListDto.class,
                        requestDetail.id,
                        request.frYyyymmdd,
                        request.bcId.bcName,
                        requestDetail.fdTag,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,
                        requestDetail.fdColor
                ));

        query.orderBy(requestDetail.id.asc());

        return query.fetch();
    }

    // 가맹접입고 querydsl
    public List<RequestDetailFranchiseInListDto> findByRequestDetailFranchiseInList(String frCode){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailFranchiseInListDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.fdState.eq("S4")))
                .select(Projections.constructor(RequestDetailFranchiseInListDto.class,
                        requestDetail.id,
                        requestDetail.fdS4Dt,
                        request.bcId.bcName,
                        requestDetail.fdTag,

                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.biItemcode,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,

                        request.frYyyymmdd,
                        requestDetail.fdS2Dt
                ));

        query.orderBy(requestDetail.id.asc());

        return query.fetch();
    }

    // 지사반송 querydsl
    public List<RequestDetailReturnListDto> findByRequestDetailReturnList(String frCode){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailReturnListDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.fdState.eq("S3")))
                .select(Projections.constructor(RequestDetailReturnListDto.class,
                        requestDetail.id,
                        request.frYyyymmdd,
                        request.bcId.bcName,
                        requestDetail.fdTag,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,

                        requestDetail.fdColor
                ));

        query.orderBy(requestDetail.id.asc());

        return query.fetch();
    }

    // 가맹접강제입고 querydsl
    public List<RequestDetailForceListDto> findByRequestDetailForceList(String frCode){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailForceListDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.fdState.eq("S7")))
                .select(Projections.constructor(RequestDetailForceListDto.class,
                        requestDetail.id,
                        requestDetail.fdS4Dt,
                        request.bcId.bcName,
                        requestDetail.fdTag,

                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,

                        request.frYyyymmdd,
                        requestDetail.fdS2Dt
                ));

        query.orderBy(requestDetail.id.asc());

        return query.fetch();
    }

    // 세탁인도 querydsl
    public List<RequestDetailDeliveryDto> findByRequestDetailDeliveryList(String frCode, Long bcId){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;

        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<RequestDetailDeliveryDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .leftJoin(inspeot).on(inspeot.fdId.eq(requestDetail))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailDeliveryDto.class,

                        request.bcId.bcName,
                        request.frYyyymmdd,

                        requestDetail.id,
                        request.id,
                        request.frNo,

                        requestDetail.fdTag,
                        requestDetail.biItemcode,
                        requestDetail.fdState,
                        requestDetail.fdPreState,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS3Dt,
                        requestDetail.fdS4Dt,
                        requestDetail.fdS5Dt,
                        requestDetail.fdS6Dt,

                        requestDetail.fdCancel,
                        requestDetail.fdCacelDt,

                        requestDetail.fdColor,
                        requestDetail.fdPattern,
                        requestDetail.fdPriceGrade,

                        requestDetail.fdOriginAmt,
                        requestDetail.fdNormalAmt,

                        requestDetail.fdAdd2Amt,
                        requestDetail.fdAdd2Remark,

                        requestDetail.fdPollution,
                        requestDetail.fdDiscountGrade,
                        requestDetail.fdDiscountAmt,
                        requestDetail.fdQty,

                        requestDetail.fdRequestAmt,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,
                        requestDetail.fdEstimateDt,

                        requestDetail.fdRetryYn,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollutionLevel,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,

                        request.frRefType

                ));

        query.orderBy(requestDetail.id.asc()).groupBy(requestDetail);

        query.where(requestDetail.fdState.eq("S5").or(requestDetail.fdState.eq("S8")));

        if(bcId != null){
            query.where(request.bcId.bcId.eq(bcId));
        }

        return query.fetch();
    }

}
