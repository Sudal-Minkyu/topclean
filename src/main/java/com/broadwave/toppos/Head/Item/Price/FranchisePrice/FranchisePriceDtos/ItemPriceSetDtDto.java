package com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-03-25
 * Time :
 * Remark :  가격시작일(적용일자) 셀렉트박스 테이터 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceSetDtDto {
    private String setDt; // 가격시작일(적용일자)
    private String setDtview; // 가격시작일(적용일자)

    public StringBuffer getSetDtview() {
        if(setDtview != null && !setDtview.equals("")){
            StringBuffer getSetDtview = new StringBuffer(setDtview);
            getSetDtview.insert(4,'-');
            getSetDtview.insert(7,'-');
            return getSetDtview;
        }else{
            return null;
        }
    }

}
