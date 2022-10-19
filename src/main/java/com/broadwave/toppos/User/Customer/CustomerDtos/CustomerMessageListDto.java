package com.broadwave.toppos.User.Customer.CustomerDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-04-15
 * Time :
 * Remark : Toppos 문자 메세지 보낼 고객 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMessageListDto {

    private Long bcId; // 고유ID값
    private String bcName; // 고객명
    private String bcHp; // 휴대폰번호( "-" 빼고저장)

}
