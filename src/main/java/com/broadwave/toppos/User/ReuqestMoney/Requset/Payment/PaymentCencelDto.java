package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2022-01-10
 * Time :
 * Remark : Toppos 가맹점 접수 결제취소 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCencelDto {

    private String frCode; // 가맹점 코드
    private String frName; // 가맹점명

    private Long fpId; // 결제 고유ID값
    private String fpType; // 결제타입 (01:현금, 02:카드,03:적립금)
    private Integer fpAmt; // 결제금액( 미수에서 카드결제시 카드결제금액보다작을수있다.)

    private String fpCatIssuername; // 이슈명칭 ex > IBK 비씨카드
    private String fpCatApprovalno; // 카드승인번호 ex 73536757
    private String fpCatApprovaltime; // 카드승인시간 ex 2111241411144
    private String fpCatTotamount; // 결제금액 : ex> 000012000
    private String fpCatVatamount; // 부가세금액 : ex> 000001090
    private Integer fpMonth; // 카드할부 ( 0: 일시불, 2: 2개월 ~ 12 : 12개월) - 기본값 0

    public String getFrCode() {
        return frCode;
    }

    public String getFrName() {
        return frName;
    }

    public Long getFpId() {
        return fpId;
    }

    public String getFpType() {
        return fpType;
    }

    public Integer getFpAmt() {
        return fpAmt;
    }

    public String getFpCatIssuername() {
        return fpCatIssuername;
    }

    public String getFpCatApprovalno() {
        return fpCatApprovalno;
    }

    public String getFpCatApprovaltime() {
        return fpCatApprovaltime;
    }

    public String getFpCatTotamount() {
        return fpCatTotamount;
    }

    public String getFpCatVatamount() {
        return fpCatVatamount;
    }

    public Integer getFpMonth() {
        return fpMonth;
    }
}
