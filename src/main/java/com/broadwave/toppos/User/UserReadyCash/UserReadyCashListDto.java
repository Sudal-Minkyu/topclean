package com.broadwave.toppos.User.UserReadyCash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author Minkyu
 * Date : 2022-06-07
 * Time :
 * Remark : Toppos 가맹점 일자별 현금 준비금 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadyCashListDto {

    private String bcYyyymmdd; // 코드
    private Integer bcReadyAmt; // 준비금

}
