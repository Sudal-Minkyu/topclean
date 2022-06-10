package com.broadwave.toppos.User.CashReceipt;

import com.broadwave.toppos.User.CashReceipt.CashReceiptDtos.CashReceiptDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-04-27
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class CashReceiptRepositoryCustomImpl extends QuerydslRepositorySupport implements CashReceiptRepositoryCustom {

    public CashReceiptRepositoryCustomImpl() {
        super(CashReceipt.class);
    }

    public CashReceiptDto findByCashReceipt(Long frId, String frCode) {

        QCashReceipt cashReceipt = QCashReceipt.cashReceipt;
        QRequest request = QRequest.request;

        JPQLQuery<CashReceiptDto> query = from(cashReceipt)
                .where(cashReceipt.frId.id.eq(frId).and(cashReceipt.fcCancelYn.eq("N")))
                .innerJoin(request).on(request.eq(cashReceipt.frId).and(request.frCode.eq(frCode)))
                .select(Projections.constructor(CashReceiptDto.class,
                        request.frCode,
                        cashReceipt.fcId,
                        cashReceipt.fcYyyymmdd,
                        cashReceipt.fcType,
                        cashReceipt.fcCatApprovalno,
                        cashReceipt.fcCatApprovaltime,
                        cashReceipt.fcCatTotamount
                ));

        return query.fetchOne();
    }

}
