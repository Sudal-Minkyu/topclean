package com.broadwave.toppos.User.ReuqestMoney.Requset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark : Toppos 가맹점 미수금관련 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCollectDto {

    private String frNo; // 접수코드
    private String frYyyymmdd; // 접수일자
    private String frUncollectYn; // 미수여부
    private Integer frTotalAmount; // 합계금액
    private Integer frPayAmount; // 결제금액

    public String getFrYyyymmdd() {
        return frYyyymmdd;
    }

    public String getFrNo() {
        return frNo;
    }

    public String getFrUncollectYn() {
        return frUncollectYn;
    }

    public Integer getFrTotalAmount() {
        return frTotalAmount;
    }

    public Integer getFrPayAmount() {
        return frPayAmount;
    }
}
