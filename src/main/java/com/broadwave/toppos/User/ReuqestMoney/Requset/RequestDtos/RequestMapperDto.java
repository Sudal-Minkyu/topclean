package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수마스트 MapperDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestMapperDto {

    private String checkNum; // 임시저장인지 : 1 , 걸제후 저장인지 확인하는 번호 : 2(문자열 형태)

    private String frNo; // 접수코드
    private Long bcId; // 고객 고유ID값
    private Integer frNormalAmount; // 총 접수금액
    private Integer frDiscountAmount; // 총 할인금액
    private Integer frTotalAmount; // 총 합계금액
    private Integer frQty; // 접수건수

    public Integer getFrQty() {
        return frQty;
    }

    public String getFrNo() {
        return frNo;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public Long getBcId() {
        return bcId;
    }

    public Integer getFrNormalAmount() {
        return frNormalAmount;
    }

    public Integer getFrDiscountAmount() {
        return frDiscountAmount;
    }

    public Integer getFrTotalAmount() {
        return frTotalAmount;
    }

}
