package com.broadwave.toppos.Head.Promotion.PromotionItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-07-29
 * Time :
 * Remark : Toppos 본사 행사관련 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionItemDto {

    private String biItemcode; // 상품코드
    private Double hiDiscountRt; // 할인율 ( 마스터의 행사타입이 1+1,2+1 행사이면0이 저장된다.

}