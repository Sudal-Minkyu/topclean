package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
public interface PaymentRepositoryCustom {
    List<PaymentCencelDto> findByRequestDetailCencelDataList(String frCode, Long frId);

    List<PaymentCencelYnDto> findByPaymentCancelYn(List<Long> frIdList);

    List<PaymentBusinessdayListDto> findByPaymentBusinessdayListDto(String frCode, String filterFromDt, String filterToDt); // 영업일보 통계 카드결제금액, 현금결제금액, 취소결제금액, 미수결제금액 sum querydsl

    List<PaymentPaperDto> findByPaymentPaper(String frNo);

    List<PaymentMessageDto> findByPaymentMessage(String frNo);
}
