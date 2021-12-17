package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-17
 * Time :
 * Remark : Toppos 가맹점 접수 결제후 보낼 Etc데이터 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEtcDto {
    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)
    private Integer fpAmt; // 결제금액( 미수에서 카드결제시 카드결제금액보다작을수있다.)
    private String fpCatIssuername; // 이슈명칭 ex > IBK 비씨카드

    public String getFpType() {
        return fpType;
    }

    public Integer getFpAmt() {
        return fpAmt;
    }

    public String getFpCatIssuername() {
        return fpCatIssuername;
    }
}