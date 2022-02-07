package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.Head.Franohise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.QRequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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
public class RequestRepositoryCustomImpl extends QuerydslRepositorySupport implements RequestRepositoryCustom {

    public RequestRepositoryCustomImpl() {
        super(Request.class);
    }

    @Override
    public List<RequestListDto> findByRequestTempList(String frCode){
        QRequest request = QRequest.request;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestListDto> query = from(request)
                .innerJoin(customer).on(request.bcId.eq(customer))
                .select(Projections.constructor(RequestListDto.class,
                        request.frNo,
                        request.fr_insert_date,
                        customer.bcName,
                        customer.bcHp
                ));

        query.orderBy(request.fr_insert_date.desc());
        query.where(request.frConfirmYn.eq("N").and(request.frCode.eq(frCode)));
        return query.fetch();
    }

    @Override
    public List<RequestCollectDto> findByRequestCollectList(Customer customer, String nowDate){
        QRequest request = QRequest.request;

        JPQLQuery<RequestCollectDto> query = from(request)
                .select(Projections.constructor(RequestCollectDto.class,
                        request.frNo,
                        request.frYyyymmdd,
                        request.frUncollectYn,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y"))
                .and(request.bcId.eq(customer)));

//        if(nowDate != null) {
//            query.where(request.frYyyymmdd.lt(nowDate)); //전일미수금만 출력 -> 2021/12/17일 변경 해당조건의 대한 모든 날짜의 데이터를 받아온다.
//        }

        return query.fetch();
    }

    // 고객리스트의 오늘까지 포함미수금 리스트 호출
    @Override
    public List<RequestUnCollectDto> findByUnCollectList(List<Long> customerIdList, String nowDate){
        QRequest request = QRequest.request;

        JPQLQuery<RequestUnCollectDto> query = from(request)
                .groupBy(request.bcId)
                .orderBy(request.bcId.bcId.desc())
                .select(Projections.constructor(RequestUnCollectDto.class,
                        request.bcId.bcId,
                        new CaseBuilder()
                                .when(request.frTotalAmount.isNotNull()).then(request.frTotalAmount.sum())
                                .otherwise(0),
                        new CaseBuilder()
                                .when(request.frPayAmount.isNotNull()).then(request.frPayAmount.sum())
                                .otherwise(0)
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y")
                .and(request.frYyyymmdd.loe(nowDate)
                .and(request.bcId.bcId.in(customerIdList))
        )));

        return query.fetch();
    }

    // 고객리스트의 전일미수금 리스트 호출
    @Override
    public List<RequestUnCollectDto> findByBeforeUnCollectList(List<Long> customerIdList, String nowDate){
        QRequest request = QRequest.request;

        JPQLQuery<RequestUnCollectDto> query = from(request)
                .groupBy(request.bcId)
                .orderBy(request.bcId.bcId.desc())
                .select(Projections.constructor(RequestUnCollectDto.class,
                        request.bcId.bcId,
                        new CaseBuilder()
                                .when(request.frTotalAmount.isNotNull()).then(request.frTotalAmount.sum())
                                .otherwise(0),
                        new CaseBuilder()
                                .when(request.frPayAmount.isNotNull()).then(request.frPayAmount.sum())
                                .otherwise(0)
                ));

        query.where(request.frUncollectYn.eq("Y")
                        .and(request.frConfirmYn.eq("Y")
                        .and(request.frYyyymmdd.lt(nowDate)
                        .and(request.bcId.bcId.in(customerIdList))
                )));

        return query.fetch();
    }

    // 미수금 완납처리시 사용하는 쿼리
    @Override
    public List<RequestInfoDto> findByRequestList(String frCode, String nowDate, Customer customer){
        QRequest request = QRequest.request;
        QCustomer qcustomer = QCustomer.customer;

        JPQLQuery<RequestInfoDto> query = from(request)
                .innerJoin(qcustomer).on(request.bcId.eq(qcustomer))
                .select(Projections.constructor(RequestInfoDto.class,
                        request.id,
                        request.frNo,
                        qcustomer,
                        request.frCode,
                        request.bcCode,
                        request.frYyyymmdd,
                        request.frQty,
                        request.frNormalAmount,
                        request.frDiscountAmount,
                        request.frTotalAmount,
                        request.frPayAmount,
                        request.frUncollectYn,
                        request.frConfirmYn,
                        request.frRefType,
                        request.frRefBoxCode,
                        request.fr_insert_id,
                        request.fr_insert_date,
                        request.modify_id,
                        request.modify_date
                ));

        query.where(request.frUncollectYn.eq("Y")
                        .and(request.frConfirmYn.eq("Y")
                        .and(request.frYyyymmdd.lt(nowDate)
                        .and(request.frCode.eq(frCode)
                        .and(request.bcId.eq(customer)
                        )))));

        return query.fetch();
    }

    @Override
    public List<RequestSearchDto> findByRequestFrCode(String frCode){
        QRequest request = QRequest.request;

        JPQLQuery<RequestSearchDto> query = from(request)
                .select(Projections.constructor(RequestSearchDto.class,
                        request.frNo
                ));

        query.where(request.frCode.eq(frCode));
        query.limit(1);

        return query.fetch();
    }

    // 미수관리 페이지의 고객선택 후 리스트 Dto
    @Override
    public  List<RequestCustomerUnCollectDto> findByRequestCustomerUnCollectList(Long bcId, String frCode){
        QRequest request = QRequest.request;

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<RequestCustomerUnCollectDto> query = from(request)
                .innerJoin(requestDetail).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .groupBy(request.id).orderBy(request.id.desc())
                .select(Projections.constructor(RequestCustomerUnCollectDto.class,
                        request.id,
                        request.frYyyymmdd,
                        request.id.count(),
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y")
                        .and(request.bcId.bcId.eq(bcId)
                                .and(request.frCode.eq(frCode))
                        )));

        return query.fetch();
    }

    // 미수관리 페이지의 미수금 결제할 접수테이블 선택후 리스트 Dto
    @Override
    public List<RequestCustomerUnCollectDto> findByRequestUnCollectPayList(List<Long> frIdList, String frCode){
        QRequest request = QRequest.request;

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<RequestCustomerUnCollectDto> query = from(request)
                .innerJoin(requestDetail).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .groupBy(request.id).orderBy(request.id.desc())
                .select(Projections.constructor(RequestCustomerUnCollectDto.class,
                        request.id,
                        request.frYyyymmdd,
                        request.id.count(),
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y")
                        .and(request.id.in(frIdList)
                                .and(request.frCode.eq(frCode))
                        )));

        return query.fetch();
    }

    // 영업일보 통계 리스트 querydsl - 1
    @Override
    public List<RequestBusinessdayListDto> findByBusinessDayList(String frCode, String filterFromDt, String filterToDt){
        QRequest request = QRequest.request;

        JPQLQuery<RequestBusinessdayListDto> query = from(request)
                .where(request.frYyyymmdd.loe(filterToDt).and(request.frYyyymmdd.goe(filterFromDt)))
                .select(Projections.constructor(RequestBusinessdayListDto.class,
                        request.frYyyymmdd,
                        request.frQty.sum(),
                        request.frTotalAmount.sum()

                ));

        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());

        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));

        return query.fetch();
    }

    // 영업일보 통계 리스트 querydsl - 2
    @Override
    public List<RequestBusinessdayCustomerListDto> findByBusinessDayCustomerList(String frCode, String filterFromDt, String filterToDt){
        QRequest request = QRequest.request;

        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestBusinessdayCustomerListDto> query = from(request)
                .innerJoin(customer).on(request.bcId.eq(customer))
                .where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)))
                .select(Projections.constructor(RequestBusinessdayCustomerListDto.class,
                        request.frYyyymmdd,
                        customer.countDistinct()
                ));

        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());

        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));

        return query.fetch();
    }

    // 영수증 출력 가맹점정보 데이터 호출
    @Override
    public RequestPaymentPaperDto findByRequestPaymentPaper(String frNo, Long frId, String frCode) {

        QRequest request = QRequest.request;
        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<RequestPaymentPaperDto> query =
                from(request)
                        .innerJoin(customer).on(customer.eq(request.bcId))
                        .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                        .select(Projections.constructor(RequestPaymentPaperDto.class,
                                request.frNo,

                                franchise.frCode,
                                franchise.frName,
                                franchise.frBusinessNo,
                                franchise.frRpreName,
                                franchise.frTelNo,

                                customer,

                                request.frYyyymmdd,
                                request.frNormalAmount,
                                request.frDiscountAmount,
                                request.frTotalAmount,
                                request.frPayAmount
                        ));

        query.where(request.frNo.eq(frNo).or(request.id.eq(frId)));

        query.where(request.frCode.eq(frCode));

        return query.fetchOne();
    }

}
