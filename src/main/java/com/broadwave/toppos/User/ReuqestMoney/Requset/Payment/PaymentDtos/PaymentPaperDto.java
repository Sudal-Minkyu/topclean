package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark : Toppos 가맹점 영수증 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPaperDto {

    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)

    private String fpCatCardno;  // 카드번호 ex 942003******8000
    private String fpCatIssuername; // 이슈명칭 ex > IBK 비씨카드
    private String fpCatApprovaltime; // 카드승인시간 ex 2111241411144
    private String fpCatApprovalno; // 카드승인번호 ex 73536757

    private Integer fpMonth; // 카드할부 ( 0: 일시불, 2: 2개월 ~ 12 : 12개월) - 기본값 0
    private Integer fpAmt; // 결제금액( 미수에서 카드결제시 카드결제금액보다작을수있다.)
    private Integer fpCollectAmt; // 미수 완납금액

}
