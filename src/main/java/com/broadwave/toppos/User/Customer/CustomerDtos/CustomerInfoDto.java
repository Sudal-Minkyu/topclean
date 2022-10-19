package com.broadwave.toppos.User.Customer.CustomerDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-03
 * Time :
 * Remark : Toppos 고객테이블 InfoDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerInfoDto {

    private Long bcId; // 고객고유 ID값
    private String bcName; // 고객명
    private String bcHp; // 휴대폰번호( "-" 빼고저장)
    private String bcAddress; // 주소
    private String bcGrade; // 고객등급( 일반-기본값 : 01, vip: 02, vvip: 03)
    private String bcValuation; // 고객평가( 별1개: 1, 별2개: 2, 별3개: 3, 별4개: 4, 별5개: 5)
    private String bcRemark; // 특이사항
    private String bcLastRequestDt; // 마지막방문일자
    private String bcWeddingAnniversary; // 결혼기념일(8글자)

}
