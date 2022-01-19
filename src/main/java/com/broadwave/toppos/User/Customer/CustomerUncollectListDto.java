package com.broadwave.toppos.User.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2022-01-19
 * Time :
 * Remark : Toppos 고객 미수관리 테이블 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUncollectListDto {

    private Long bcId; // 고유ID값
    private String bcName; // 고객명
    private String bcHp; // 휴대폰번호( "-" 빼고저장)
    private String bcAddress; // 주소

}
