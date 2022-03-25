package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 MapperDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotMapperDto {

    private Long fdId; // 마스터테이블  ID
    private Integer fiAddAmt; // 세탁 추가발생 비용
    private String fiType; // 검품 타입 , F : 가맹검품, B:지사검품(확인품)
    private String fiComment; // 검품 특이사항

}
