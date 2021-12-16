package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark : Toppos 가맹점 접수 결제테이블 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)
    private Integer fpMonth; // 카드할부 ( 0: 일시불, 2: 2개월 ~ 12 : 12개월) - 기본값 0
    private Integer fpAmt; // 결제금액( 미수에서 카드결제시 카드결제금액보다작을수있다.)
    private Integer fpRealAmt; // 실제결제금액(총결제금액 -> 여러건을 미수관리에서 합쳐서 결제할경우 총결제금액)
    private Integer fpCollectAmt; // 미수 완납금액
//    private String fpCancelYn; // 결제취소 여부 : 기본값 N
//    private String fpSavedMoneyYn; // 결제취소후 적립금전환 여부 : 기본값 N
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

    public Integer getFpCollectAmt() {
        return fpCollectAmt;
    }

    public String getFpType() {
        return fpType;
    }

    public Integer getFpMonth() {
        return fpMonth;
    }

    public Integer getFpAmt() {
        return fpAmt;
    }

    public Integer getFpRealAmt() {
        return fpRealAmt;
    }

//    public String getFpCancelYn() {
//        return fpCancelYn;
//    }
//
//    public String getFpSavedMoneyYn() {
//        return fpSavedMoneyYn;
//    }

    public String getFpCatApprovalno() {
        return fpCatApprovalno;
    }

    public String getFpCatApprovaltime() {
        return fpCatApprovaltime;
    }

    public String getFpCatCardno() {
        return fpCatCardno;
    }

    public String getFpCatIssuercode() {
        return fpCatIssuercode;
    }

    public String getFpCatIssuername() {
        return fpCatIssuername;
    }

    public String getFpCatMuechantnumber() {
        return fpCatMuechantnumber;
    }

    public String getFpCatMessage1() {
        return fpCatMessage1;
    }

    public String getFpCatMessage2() {
        return fpCatMessage2;
    }

    public String getFpCatNotice1() {
        return fpCatNotice1;
    }

    public String getFpCatTotamount() {
        return fpCatTotamount;
    }

    public String getFpCatVatamount() {
        return fpCatVatamount;
    }

    public String getFpCatTelegramflagt() {
        return fpCatTelegramflagt;
    }
}
