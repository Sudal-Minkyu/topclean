package com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-01-12
 * Time :
 * Remark : Toppos 가맹점 접수 결제취소여부 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCencelYnDto {

    private Long frId; // 마스터테이블 고유ID값

}
