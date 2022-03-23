package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class PaymentRepositoryCustomImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public PaymentRepositoryCustomImpl() {
        super(PaymentRepository.class);
    }

    @Override
    public List<PaymentCencelDto> findByRequestDetailCencelDataList(String frCode, Long frId){
        QPayment payment = QPayment.payment;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<PaymentCencelDto> query = from(payment)
                .innerJoin(franchise).on(franchise.frCode.eq(frCode))
                .where(payment.frId.id.eq(frId).and(payment.fpCancelYn.eq("N").and(payment.fpSavedMoneyYn.eq("N"))))
                .select(Projections.constructor(PaymentCencelDto.class,
                        franchise.frCode,
                        franchise.frName,

                        payment.id,
                        payment.fpType,
                        payment.fpRealAmt, // 2022/3/23 MK -> fpAmt(결제금액)에서 fpRealAmt(실제결제금액)으로 변경

                        payment.fpCatIssuername,
                        payment.fpCatApprovalno,
                        payment.fpCatApprovaltime,
                        payment.fpCatTotamount,
                        payment.fpCatVatamount,
                        payment.fpMonth,

                        payment.insert_date
                ));

        query.orderBy(payment.id.desc());

        return query.fetch();
    }

    @Override
    public List<PaymentCencelYnDto> findByPaymentCancelYn(List<Long> frIdList){
        QPayment payment = QPayment.payment;

        JPQLQuery<PaymentCencelYnDto> query = from(payment)
                .where(payment.frId.id.in(frIdList)
                        .and(payment.fpCancelYn.eq("N")))
                .groupBy(payment.frId)

                .select(Projections.constructor(PaymentCencelYnDto.class,
                        payment.frId.id
                ));

        query.orderBy(payment.id.desc());

        return query.fetch();
    }

    // 영업일보 통계 카드결제금액, 현금결제금액, 취소결제금액, 미수결제금액 sum querydsl - 3
    @Override
    public List<PaymentBusinessdayListDto> findByPaymentBusinessdayListDto(String frCode, String filterFromDt, String filterToDt){
        QPayment payment = QPayment.payment;

        QRequest request = QRequest.request;

        JPQLQuery<PaymentBusinessdayListDto> query = from(payment)
                .innerJoin(request).on(request.eq(payment.frId))
                .where(payment.fpYyyymmdd.goe(filterFromDt).and(payment.fpYyyymmdd.loe(filterToDt)))
                .select(Projections.constructor(PaymentBusinessdayListDto.class,
                        payment.fpYyyymmdd,
                        new CaseBuilder()
                                .when(payment.fpType.eq("02").and(payment.fpInType.eq("01").and(payment.fpCancelYn.eq("N")))).then(payment.fpAmt)
                                .otherwise(0).sum(),
                        new CaseBuilder()
                                .when(payment.fpType.eq("01").and(payment.fpInType.eq("01").and(payment.fpCancelYn.eq("N")))).then(payment.fpAmt)
                                .otherwise(0).sum(),
                        new CaseBuilder()
                                .when(payment.fpCancelYn.eq("Y")).then(payment.fpAmt)
                                .otherwise(0).sum(),
                        new CaseBuilder()
                                .when(payment.fpInType.eq("02").and(payment.fpCancelYn.eq("N"))).then(payment.fpAmt)
                                .otherwise(0).sum()
                    ));

        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());

        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));

        return query.fetch();
    }

    @Override
    public  List<PaymentPaperDto> findByPaymentPaper(String frNo) {
        QPayment payment = QPayment.payment;

        JPQLQuery<PaymentPaperDto> query = from(payment)
                .where(payment.frId.frNo.eq(frNo).and(payment.fpCancelYn.eq("N")))

                .select(Projections.constructor(PaymentPaperDto.class,
                        payment.fpType,
                        payment.fpCatCardno,
                        payment.fpCatIssuername,
                        payment.fpCatApprovaltime,
                        payment.fpCatApprovalno,
                        payment.fpMonth,
                        payment.fpAmt,
                        payment.fpCollectAmt
                ));

        query.orderBy(payment.id.desc());

        return query.fetch();
    }

    @Override
    public List<PaymentMessageDto> findByPaymentMessage(String frNo) {
        QPayment payment = QPayment.payment;

        JPQLQuery<PaymentMessageDto> query = from(payment)
                .where(payment.frId.frNo.eq(frNo).and(payment.fpCancelYn.eq("N")))
                .select(Projections.constructor(PaymentMessageDto.class,
                        payment.fpType,
                        payment.fpCatIssuername,
                        payment.fpAmt,
                        payment.fpCollectAmt
                ));

        query.orderBy(payment.id.desc());

        return query.fetch();
    }

}
