package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Manager.Process.Issue.QIssue;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.QInspeot;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
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

    @Autowired
    JpaResultMapper jpaResultMapper;

    public RequestDetailRepositoryCustomImpl() {
        super(RequestDetailRepository.class);
    }

    public List<RequestDetailDto> findByRequestTempDetailList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        // 접수 세부 리스트
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
                        requestDetail.fdPollutionLocFcn,
                        requestDetail.fdPollutionLocFcs,
                        requestDetail.fdPollutionLocFcb,
                        requestDetail.fdPollutionLocFlh,
                        requestDetail.fdPollutionLocFrh,
                        requestDetail.fdPollutionLocFlf,
                        requestDetail.fdPollutionLocFrf,
                        requestDetail.fdPollutionLocBcn,
                        requestDetail.fdPollutionLocBcs,
                        requestDetail.fdPollutionLocBcb,
                        requestDetail.fdPollutionLocBrh,
                        requestDetail.fdPollutionLocBlh,
                        requestDetail.fdPollutionLocBrf,
                        requestDetail.fdPollutionLocBlf,
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
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailSearchDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .leftJoin(inspeot).on(inspeot.fdId.eq(requestDetail))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .where(request.frYyyymmdd.loe(filterFromDt).and(request.frYyyymmdd.goe(filterToDt)).and(request.frConfirmYn.eq("Y")))
                .select(Projections.constructor(RequestDetailSearchDto.class,
                        customer.bcName,
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
                        requestDetail.fdPollutionLocFcn,
                        requestDetail.fdPollutionLocFcs,
                        requestDetail.fdPollutionLocFcb,
                        requestDetail.fdPollutionLocFlh,
                        requestDetail.fdPollutionLocFrh,
                        requestDetail.fdPollutionLocFlf,
                        requestDetail.fdPollutionLocFrf,
                        requestDetail.fdPollutionLocBcn,
                        requestDetail.fdPollutionLocBcs,
                        requestDetail.fdPollutionLocBcb,
                        requestDetail.fdPollutionLocBrh,
                        requestDetail.fdPollutionLocBlh,
                        requestDetail.fdPollutionLocBrf,
                        requestDetail.fdPollutionLocBlf,
                        request.frRefType
                ));
        query.orderBy(requestDetail.id.asc());
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
                .innerJoin(requestDetail.frId, request)
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
                 .innerJoin(requestDetail.frId, request)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
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
                .innerJoin(requestDetail.frId, request)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
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
                .innerJoin(requestDetail.frId, request)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
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
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailForceListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.fdState.eq("S7")))
                .select(Projections.constructor(RequestDetailForceListDto.class,
                        requestDetail.id,
                        requestDetail.fdS4Dt,
                        customer.bcName,
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
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailDeliveryDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailDeliveryDto.class,
                        request.frRefType,
                        customer.bcName,
                        requestDetail.id,
                        request.frYyyymmdd,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdState,
                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,
                        requestDetail.fdS5Dt,
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
                        requestDetail.fdEstimateDt
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S5").or(requestDetail.fdState.eq("S8")));
        if(bcId != null){
            query.where(request.bcId.bcId.eq(bcId));
        }
        return query.fetch();
    }

    // 검품이력 조회 및 메세지 querydsl
    public List<RequestDetailInspectDto> findByRequestDetailInspectList(String frCode, Long bcId, String searchTag, String filterFromDt, String filterToDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailInspectDto> query = from(requestDetail)

                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .where(request.frYyyymmdd.loe(filterToDt).and(request.frYyyymmdd.goe(filterFromDt)).and(request.frConfirmYn.eq("Y")))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailInspectDto.class,
                        request.frRefType,
                        customer.bcName,
                        requestDetail.id,
                        request.frYyyymmdd,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdState,
                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,
                        requestDetail.fdS5Dt,
                        requestDetail.fdS6Dt,
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
                        requestDetail.fdRemark
                ));
        query.orderBy(requestDetail.id.asc());
        if(!searchTag.equals("")){
            query.where(requestDetail.fdTag.likeIgnoreCase(searchTag));
        }
        if(bcId != null){
            query.where(request.bcId.bcId.eq(bcId));
        }
        return query.fetch();
    }

    // 영업일보 통계 재세탁, 부착물 sum querydsl - 5
    @Override
    public List<RequestDetailBusinessdayListDto> findByRequestDetailBusinessdayList(String frCode, String filterFromDt, String filterToDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        JPQLQuery<RequestDetailBusinessdayListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)))
                .select(Projections.constructor(RequestDetailBusinessdayListDto.class,
                        request.frYyyymmdd,
                        new CaseBuilder()
                                .when(requestDetail.fdRetryYn.eq("Y")).then(1)
                                .otherwise(0).sum(),
                        new CaseBuilder()
                                .when(requestDetail.biItemcode.substring(0,3).eq("D17")).then(1)
                                .otherwise(0).sum()
                ));
        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());
        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));
        return query.fetch();
    }
    // 영업일보 통계 총 출고 sum querydsl - 6
    @Override
    public List<RequestDetailBusinessdayDeliveryDto> findByRequestDetailBusinessdayDeliveryList(String frCode, String filterFromDt, String filterToDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBusinessdayDeliveryDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .where(requestDetail.fdS6Dt.goe(filterFromDt).and(requestDetail.fdS6Dt.loe(filterToDt)).and(requestDetail.fdS6Dt.isNotNull()))
                .select(Projections.constructor(RequestDetailBusinessdayDeliveryDto.class,
                        requestDetail.fdS6Dt,
                        customer.countDistinct()
                ));
        query.groupBy(requestDetail.fdS6Dt).orderBy(requestDetail.fdS6Dt.asc());
        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));
        return query.fetch();
    }

    // 영수증출력
    public List<RequestDetailPaymentPaper> findByRequestDetailPaymentPaper(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailPaymentPaper> query = from(requestDetail)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .select(Projections.constructor(RequestDetailPaymentPaper.class,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdEstimateDt
                ));
        query.where(requestDetail.frNo.eq(frNo));
        return query.fetch();
    }

    // 지사출고 querydsl
    public List<RequestDetailReleaseListDto> findByRequestDetailReleaseList(String brCode, Long frId, LocalDateTime fromDt, java.time.LocalDateTime toDt, String isUrgent){
//        Long frId, LocalDateTime fromDt, java.time.LocalDateTime toDt, String isUrgent
//        brCode
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailReleaseListDto> query = from(requestDetail)
                 .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailReleaseListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        franchise.frCode,
                        requestDetail.fdS2Dt,
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
                        request.bcId.bcName,
                        requestDetail.fdEstimateDt,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        if(frId != 0){
            query.where(franchise.id.eq(frId));
        }
        if(isUrgent.equals("Y")){
            query.where(requestDetail.fdUrgentYn.eq(isUrgent));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Time.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Time.loe(toDt));
        }
        return query.fetch();
    }

    // 지사출고취소 querydsl
    public  List<RequestDetailReleaseCancelListDto> findByRequestDetailReleaseCancelList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailReleaseCancelListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailReleaseCancelListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.fdS2Time,
                        requestDetail.fdS4Time,
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
                        requestDetail.fdRemark,
                        request.bcId.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S4"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS4Time.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS4Time.loe(toDt));
        }
        return query.fetch();
    }

    // 지사반송 querydsl
    public  List<RequestDetailBranchReturnListDto> findByRequestDetailBranchReturnList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReturnListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReturnListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Time.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Time.loe(toDt));
        }
        return query.fetch();
    }

    // 가맹점강제출고 querydsl
    public  List<RequestDetailBranchForceListDto> findByRequestDetailBranchForceList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailBranchForceListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchForceListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        request.bcId.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Time.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Time.loe(toDt));
        }
        return query.fetch();
    }

    // 확인품등록 querydsl
    public  List<RequestDetailBranchInspectListDto> findByRequestDetailBranchInspectList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailBranchInspectListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchInspectListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        request.bcId.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Time.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Time.loe(toDt));
        }
        return query.fetch();
    }

    // 확인품현황 querydsl
    public  List<RequestDetailBranchInspectionCurrentListDto> findByRequestDetailBranchInspectionCurrentList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QInspeot inspeot = QInspeot.inspeot;
        JPQLQuery<RequestDetailBranchInspectionCurrentListDto> query = from(requestDetail)
                 .innerJoin(requestDetail.frId, request)
                .innerJoin(inspeot).on(inspeot.fdId.eq(requestDetail))
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchInspectionCurrentListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        request.bcId.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        inspeot.fiAddAmt.sum()
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Time.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Time.loe(toDt));
        }
        return query.fetch();
    }

    // 택번호조회 querydsl
    public List<RequestDetailTagSearchListDto> findByRequestDetailTagSearchList(String brCode, Long frId, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailTagSearchListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailTagSearchListDto.class,
                        franchise.frName,
                        request.frRefType,
                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,
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
                        request.bcId.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark,
                        requestDetail.fdS3Dt,
                        requestDetail.fdS7Dt,
                        requestDetail.fdS8Dt
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
        }
        return query.fetch();
    }

    // 입고현황, 체류세탁물현황 - 왼쪽 리스트 호출API
    @Override
    public List<RequestDetailBranchStoreCurrentListDto> findByRequestDetailBranchStoreCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.fr_code, c.fr_name, b.fd_s2_dt, \n");
        sb.append("  COUNT(*),  \n");
        sb.append("  sum(case when IFNULL(b.fd_s4_dt,'') = '' THEN 0 ELSE 1 END),  \n");
        sb.append("  (COUNT(*) -sum(case when IFNULL(b.fd_s4_dt,'') = '' THEN 0 ELSE 1 END)) as remain_cnt,  \n");
        sb.append("  sum(b.fd_tot_amt)");
        sb.append("         FROM fs_request a \n");
        sb.append("             INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id  \n");
        sb.append("             INNER JOIN bs_franchise c ON a.fr_code = c.fr_code  \n");
        sb.append("             WHERE a.br_code = ?1 AND a.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
        sb.append("             AND b.fd_s2_dt >= ?2 \n");
        sb.append("             AND b.fd_s2_dt <= ?3  \n");
        if(franchiseId != null){
            sb.append("             AND c.fr_id = ?4  \n");
        }
        sb.append("             GROUP BY a.fr_code, c.fr_name, b.fd_s2_dt  \n");
        if(type.equals("2")){
            sb.append("          HAVING remain_cnt > 0  \n");
        }
        sb.append("             ORDER BY c.fr_name ASC, b.fd_s2_dt ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != null) {
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestDetailBranchStoreCurrentListDto.class);
    }

    // 지사입고현황 - querydsl
    public  List<RequestDetailBranchInputCurrentListDto> findByRequestDetailBranchInputCurrentList(String brCode, String frCode, String fdS2Dt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchInputCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchInputCurrentListDto.class,
                        request.frRefType,
                        requestDetail.fdS2Type,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark
                ));

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));
        query.where(requestDetail.fdS2Dt.eq(fdS2Dt));

        return query.fetch();
    }

    // 체류세탁물현황 - querydsl
    public  List<RequestDetailBranchRemainCurrentListDto> findByRequestDetailBranchRemainCurrentList(String brCode, String frCode, String fdS2Dt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchRemainCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchRemainCurrentListDto.class,

                        request.frRefType,
                        requestDetail.fdS2Type,

                        requestDetail.fdS2Dt,
                        requestDetail.fdEstimateDt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark
                ));

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));
        query.where(requestDetail.fdS2Dt.eq(fdS2Dt).and(requestDetail.fdS4Dt.isNull()));

        return query.fetch();
    }

    // 지사출고현황, 지사강제출고현황 - 왼쪽 리스트 호출API
    @Override
    public List<RequestDetailBranchReleaseCurrentListDto> findByRequestDetailBranchReleaseCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        if(type.equals("1")){
            sb.append("SELECT a.fr_code, d.fr_name, a.mi_dt, \n");
            sb.append("  sum(case when IFNULL(b.fd_s4_dt,'') = '' THEN 0 ELSE 1 END) as output_cnt,  \n");
            sb.append("  sum(b.fd_tot_amt)");
            sb.append("         FROM mr_issue a \n");
            sb.append("             INNER JOIN fs_request_dtl b ON a.mi_id = b.mi_id  \n");
            sb.append("             INNER JOIN fs_request c ON c.fr_id = b.fr_id  \n");
            sb.append("             INNER JOIN bs_franchise d ON c.fr_code = d.fr_code  \n");
            sb.append("             WHERE a.br_code = ?1 AND c.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
            sb.append("             AND a.mi_dt >= ?2 \n");
            sb.append("             AND a.mi_dt <= ?3  \n");
            if(franchiseId != null){
                sb.append("             AND d.fr_id = ?4  \n");
            }
            sb.append("             GROUP BY a.fr_code, d.fr_name, a.mi_dt  \n");
            sb.append("             HAVING output_cnt > 0  \n");
            sb.append("             ORDER BY d.fr_name ASC, a.mi_dt ASC \n");
        }else{
            sb.append("SELECT a.fr_code, d.fr_name, a.mr_dt, \n");
            sb.append("  sum(case when IFNULL(b.fd_s7_dt,'') = '' THEN 0 ELSE 1 END) as output_cnt,  \n");
            sb.append("  sum(b.fd_tot_amt)");
            sb.append("         FROM mr_issue_force a \n");
            sb.append("             INNER JOIN fs_request_dtl b ON a.fd_id = b.fd_id  \n");
            sb.append("             INNER JOIN fs_request c ON b.fr_id = c.fr_id  \n");
            sb.append("             INNER JOIN bs_franchise d ON c.fr_code = d.fr_code  \n");
            sb.append("             WHERE a.br_code = ?1 AND c.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
            sb.append("             AND a.mr_dt >= ?2 \n");
            sb.append("             AND a.mr_dt <= ?3  \n");
            if(franchiseId != null){
                sb.append("             AND d.fr_id = ?4  \n");
            }
            sb.append("             GROUP BY a.fr_code, d.fr_name, a.mr_dt  \n");
            sb.append("             HAVING output_cnt > 0  \n");
            sb.append("             ORDER BY d.fr_name ASC, a.mr_dt ASC \n");
        }

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != null) {
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestDetailBranchReleaseCurrentListDto.class);
    }

    //  지사출고현황 - querydsl
    public List<RequestDetailBranchReleaseCurrentRightListDto> findByRequestDetailBranchReleaseCurrentRightList(String brCode, String frCode, String fdS4Dt){
        QIssue issue = QIssue.issue;
        QRequest request = QRequest.request;
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReleaseCurrentRightListDto> query = from(issue)
                .innerJoin(requestDetail).on(issue.eq(requestDetail.miId))
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(issue.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReleaseCurrentRightListDto.class,

                        request.frRefType,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark
                ));

        query.orderBy(requestDetail.id.asc());
        query.where(issue.frCode.eq(frCode));
        query.where(requestDetail.fdS4Dt.eq(fdS4Dt));

        return query.fetch();
    }

    // 지사강제출고현황 - querydsl
    public  List<RequestDetailBranchReleaseForceCurrentRightListDto> findByRequestDetailBranchReleaseForceCurrentRightList(String brCode, String frCode, String fdS7Dt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReleaseForceCurrentRightListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReleaseForceCurrentRightListDto.class,

                        request.frRefType,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS7Dt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark
                ));

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));
        query.where(requestDetail.fdS7Dt.eq(fdS7Dt));

        return query.fetch();
    }

    // 미출고현황 - querydsl
    public List<RequestDetailBranchUnReleaseCurrentListDto> findByRequestDetailBranchUnReleaseList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<RequestDetailBranchUnReleaseCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchUnReleaseCurrentListDto.class,
                        request.frCode,
                        franchise.frName,
                        requestDetail.count(),
                        requestDetail.fdTotAmt.sum()
                ));

        query.orderBy(franchise.frName.asc()).groupBy(request.frCode,franchise.frName).having(requestDetail.count().gt(0));;

        if(franchiseId != null) {
            query.where(franchise.id.eq(franchiseId));
        }

        if(type.equals("1")) {
            query.where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)));
        }else{
            query.where(requestDetail.fdEstimateDt.goe(filterFromDt).and(requestDetail.fdEstimateDt.loe(filterToDt)));
        }

        query.where(requestDetail.fdState.eq("S2"));

        return query.fetch();
    }

    // 미출고현황 오른쪽리스트 - querydsl
    public List<RequestDetailBranchUnReleaseCurrentRightListDto> findByRequestDetailBranchUnReleaseCurrentRightList(String brCode, String frCode, String filterFromDt, String filterToDt, String type) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchUnReleaseCurrentRightListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchUnReleaseCurrentRightListDto.class,

                        request.frRefType,

                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,
                        requestDetail.fdEstimateDt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark
                ));

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));

        if(type.equals("1")) {
            query.where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)));
        }else{
            query.where(requestDetail.fdEstimateDt.goe(filterFromDt).and(requestDetail.fdEstimateDt.loe(filterToDt)));
        }

        query.where(requestDetail.fdState.eq("S2"));

        return query.fetch();
    }

    // 가맹반송현황 - querydsl
    public List<RequestDetailBranchReturnCurrentListDto> findByRequestDetailBranchReturnCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.fr_code, c.fr_name, b.fd_s3_dt, \n");
        sb.append("  sum(case when IFNULL(b.fd_s3_dt,'') = '' THEN 0 ELSE 1 END) as tot_amt,  \n");
        sb.append("  sum(b.fd_tot_amt)");
        sb.append("         FROM fs_request a \n");
        sb.append("             INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id  \n");
        sb.append("             INNER JOIN bs_franchise c ON a.fr_code = c.fr_code  \n");
        sb.append("             WHERE a.br_code = ?1 AND a.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
        sb.append("             AND b.fd_s2_dt >= ?2 \n");
        sb.append("             AND b.fd_s2_dt <= ?3  \n");
        if(franchiseId != null){
            sb.append("             AND c.fr_id = ?4  \n");
        }
        sb.append("             GROUP BY a.fr_code, c.fr_name, b.fd_s3_dt  \n");
        sb.append("             HAVING tot_amt > 0  \n");
        sb.append("             ORDER BY c.fr_name ASC, b.fd_s3_dt ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != null) {
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestDetailBranchReturnCurrentListDto.class);
    }

    // 가맹반송현황 오른쪽리스트 - querydsl
    public List<RequestDetailBranchReturnCurrentRightListDto> findByRequestDetailBranchReturnRightCurrentList(String brCode, String frCode, String fdS3Dt) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReturnCurrentRightListDto> query = from(requestDetail)
          .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReturnCurrentRightListDto.class,

                        request.frRefType,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS3Dt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark
                ));

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));

        query.where(requestDetail.fdS3Dt.eq(fdS3Dt));

        return query.fetch();
    }










}
