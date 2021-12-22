package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.QCustomer;
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


        if(nowDate != null) {
            query.where(request.frYyyymmdd.lt(nowDate)); //전일미수금만 출력 -> 2021/12/17일 변경 해당조건의 대한 모든 날짜의 데이터를 받아온다.
        }

        return query.fetch();
    }

    // 고객리스트의 전일미수금 리스트 호출
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
                        request.frRefBoxCode,
                        request.fr_insert_id,
                        request.fr_insert_date,
                        request.modity_id,
                        request.modity_date
                ));

        query.where(request.frUncollectYn.eq("Y")
                        .and(request.frConfirmYn.eq("Y")
                        .and(request.frYyyymmdd.lt(nowDate)
                        .and(request.frCode.eq(frCode)
                        .and(request.bcId.eq(customer)
                        )))));

        return query.fetch();
    }

}
