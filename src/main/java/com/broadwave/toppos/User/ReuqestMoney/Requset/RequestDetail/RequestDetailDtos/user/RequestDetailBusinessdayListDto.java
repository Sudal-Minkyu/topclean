package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-26
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 재세탁, 부착물 카운트
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailBusinessdayListDto {
    private String yyyymmdd; // 접수일자
    private Integer fdRetryYnAll; // 재새탁 - RequestDatail의 재세탁건수 -> count()
    private Integer biItemGroupAll; // 부착물 - RequestDatail에서 join Itemgroup의 bg_item_groupcode가 D17인(부착물) 건수 -> sum()
}
