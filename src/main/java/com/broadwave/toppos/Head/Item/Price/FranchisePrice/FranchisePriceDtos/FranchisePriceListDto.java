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
public class FranchisePriceListDto {

    private String biItemcode; // 상품코드
    private String bgName; // 대분류명칭
    private String bsName; // 중분류명칭
    private String biName; // 상품명
    private Integer bfPrice; // 특이사항
    private String bfRemark; // 특이사항

}
