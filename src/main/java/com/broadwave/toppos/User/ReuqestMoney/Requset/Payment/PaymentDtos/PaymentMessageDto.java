package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-16
 * Time :
 * Remark : Toppos 가맹점 메세지전용 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMessageDto {

    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)
    private String fpCatIssuername; // 이슈명칭 ex > IBK 비씨카드
    private Integer fpAmt; // 결제금액( 미수에서 카드결제시 카드결제금액보다작을수있다.)
    private Integer fpCollectAmt; // 미수 완납금액

}
