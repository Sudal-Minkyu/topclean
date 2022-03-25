package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-21
 * Time :
 * Remark : Toppos 가맹점 미수금관리 결제테이블 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUncollectMapperDto {

    private Integer fpRealAmt; // 위 frIdList의 결제된 총 미수금액

    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)
    private Integer fpMonth; // 카드할부 ( 0: 일시불, 2: 2개월 ~ 12 : 12개월) - 기본값 0

    private String fpCatApprovalno; // 카드승인번호 ex 73536757
    private String fpCatApprovaltime; // 카드승인시간 ex 2111241411144
    private String fpCatCardno; // 카드번호 ex 942003******8000
    private String fpCatIssuercode; // 이슈코드 ex > 01
    private String fpCatIssuername; // 이슈명칭 ex > IBK 비씨카드
    private String fpCatMuechantnumber; // 카드가맹점코드 ex > 72729972
    private String fpCatMessage1; // 단말기메세제1 : ex> IBK 비씨카드
    private String fpCatMessage2; // 단말기메세제2 : ex> IBK 비씨카드
    private String fpCatNotice1; // 단말기Notice1 : ex> EDC매출표
    private String fpCatTotamount; // 결제금액 : ex> 000012000
    private String fpCatVatamount; // 부가세금액 : ex> 000001090
    private String fpCatTelegramflagt; // 전문구분 : ex a1

}
