package com.broadwave.toppos.Head.Item.ItemDtos;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2021-11-26
 * Time :
 * Remark : Toppos 상품그룹 가격관리  테이블 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceDto {

    private String biItemcode; // 상품코드
    private String setDt; // 가격시작일(적용일자)
    private String closeDt; // 가격종료일(적용일자)

    private Integer bpBasePrice; // 기본가격
    private Integer bpAddPrice; // 추가금액

    private Integer bpPriceA; // 최종가격A
    private Integer bpPriceB; // 최종가격B
    private Integer bpPriceC; // 최종가격C
    private Integer bpPriceD; // 최종가격D
    private Integer bpPriceE; // 최종가격E

    private String biRemark; // 특이사항

    private String insert_id;
    private LocalDateTime insertDateTime;

}
