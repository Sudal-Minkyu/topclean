package com.broadwave.toppos.User.Customer.CustomerDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-09-19
 * Time :
 * Remark : Toppos 고객 핸드폰번호 체크 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerHpCheckDto {

    private Long bcId; // 고유ID값
    private String bcName; // 고객명
    private String bcHp; // 휴대폰번호( "-" 빼고저장)

}
