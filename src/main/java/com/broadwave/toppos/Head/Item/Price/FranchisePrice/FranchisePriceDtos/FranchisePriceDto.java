package com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-30
 * Time :
 * Remark : Toppos 가맹점 특정상품관리  Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchisePriceDto {

    private String frCode; // 가맹점 코드(3자리)
    private String biItemcode; // 상품코드
    private Integer bfPrice; // 가맹점 적용가격
    private String bfRemark; // 특이사항

}
