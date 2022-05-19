package com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 리스트 dto
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutsourcingPriceListDto {
    private String biItemcode; // 대분류 상품코드
    private String bgName; // 대분류 이름
    private String bsName; // 중분류 이름
    private String biName; // 삼품명
    private LocalDateTime setDate; // 적용일자
    private String bpBasePrice; // 기본금액
    private String bpAddPrice; // 추가금액
    private String bpPriceA; // 최종금액(A)
    private String bpOutsourcingYn; // 외주처리대상(Y/N)
    private String bpOutsourcingPrice; // 외주금액
    private String bpRemark; // 특이사항

}
