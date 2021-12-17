package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.QPayment;
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

    @Override
    public List<RequestInfoDto> findByRequestList(String frCode, String nowDate){
        QRequest request = QRequest.request;
        QPayment payment = QPayment.payment;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestInfoDto> query = from(request)
                .innerJoin(customer).on(request.bcId.eq(customer))
                .select(Projections.constructor(RequestInfoDto.class,
                        request.id,
                        request.frNo,
                        customer,
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

        query.where(request.frUncollectYn.eq("Y"));
        query.where(request.frConfirmYn.eq("Y"));

        return query.fetch();
    }

}
