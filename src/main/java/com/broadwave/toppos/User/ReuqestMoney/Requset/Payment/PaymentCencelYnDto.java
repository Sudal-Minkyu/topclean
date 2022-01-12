package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2022-01-12
 * Time :
 * Remark : Toppos 가맹점 접수 결제취소여부 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCencelYnDto {

    private Long frId; // 마스터테이블 고유ID값
    private String fpCancelYn; // 결제취소 여부
//    private String countFpCancelYn; // 결제취소 여부 갯수

    public Long getFrId() {
        return frId;
    }

    public String getFpCancelYn() {
        return fpCancelYn;
    }

//    public String getCountFpCancelYn() {
//        return countFpCancelYn;
//    }
}
