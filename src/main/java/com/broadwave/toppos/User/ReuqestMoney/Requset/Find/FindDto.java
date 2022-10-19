package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-04-22
 * Time :
 * Remark : Toppos 물건찾기 FindDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindDto {

    private Long fdId; // 물건찾기 등록한 상품 Id

}
