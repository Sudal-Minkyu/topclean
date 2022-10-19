package com.broadwave.toppos.Head.Promotion.PromotionDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-08-08
 * Time :
 * Remark : Toppos 세탁접수시 보낼 행사데이터 ListDto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionListDto {

    private Long hpId; // 행사등록 마스터 ID
    private String hpType; // 행사유형 ( 일반행사:01, 1+1행사 : 02 , 2+1행사 : 03 )
    private String hpName; // 행사명칭
    private String biItemcode; // 상품코드
    private Double hiDiscountRt; // 할인율 ( 마스터의 행사타입이 1+1,2+1 행사이면0이 저장된다.

}
