package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-25
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBusinessdayListDto {

    private String yyyymmdd;

    private Integer fpAmtType02All; // Payment 카드결제 fpType = "02"이고 fpInType = "01"인 fpAmt -> sum()
    private Integer fpAmtType01All; // Payment 현금결제 fpType = "01"이고 fpInType = "01"인 fpAmt -> sum()
    private Integer fpAmtCancelAll; // Payment 취소금액 fpCancelYn = "Y" 인 fpAmt -> sum()
    private Integer fpAmtUncollectAll; // Payment 미수결제 fpInType = "02"인 fpAmt -> sum()

}
