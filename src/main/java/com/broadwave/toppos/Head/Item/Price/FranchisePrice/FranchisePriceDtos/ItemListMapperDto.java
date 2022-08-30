package com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-25
 * Time :
 * Remark : 상품 가격관리 페이지 업데이트, 삭제 배열 가져오는 클래스
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemListMapperDto {

    private String bgName; // 대문류명칭
    private String biItemcode; // 상품코드
    private String biName; // 상품명
    private String setDt; // 가격 시작일

}
