package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-03-24
 * Time :
 * Remark : Toppos 가맹점 택번호 반환 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestFdTagDto {
    private String fdTag; // 택번호
}
