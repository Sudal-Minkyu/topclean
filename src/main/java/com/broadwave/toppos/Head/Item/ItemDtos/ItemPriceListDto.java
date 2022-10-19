package com.broadwave.toppos.Head.Item.ItemDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-25
 * Time :
 * Remark : Toppos 상품그룹 가격관리  테이블 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceListDto {

    private String biItemcode; // 상품코드

    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칠
    private String biName; // 상품명

    private String setDt; // 가격시작일(적용일자)
    private String closeDt; // 가격 종료일

    private Integer bpBasePrice; // 기본가격
    private Integer bpAddPrice; // 추가금액

    private Integer bpPriceA; // 최종가격A
    private Integer bpPriceB; // 최종가격B
    private Integer bpPriceC; // 최종가격C
    private Integer bpPriceD; // 최종가격D
    private Integer bpPriceE; // 최종가격E

    private String biRemark; // 특이사항

}
