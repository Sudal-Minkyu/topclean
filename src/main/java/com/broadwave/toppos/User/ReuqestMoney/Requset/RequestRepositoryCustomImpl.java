package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.User.Customer.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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
                        request.frUncollectYn,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("N")
                .and(request.frConfirmYn.eq("Y"))
                .and(request.bcId.eq(customer))
                .and(request.frYyyymmdd.lt(nowDate))
        );

        return query.fetch();
    }

}
