package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import com.broadwave.toppos.Head.Franohise.QFranchise;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
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
                        payment.fpAmt,

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


}
