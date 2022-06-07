package com.broadwave.toppos.User.UserDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-12-06
 * Time :
 * Remark : 가맹점 메인페이지 전용 개인정보 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIndexDto {

    private String username; // 가맹점주 명
    private String usertel; // 휴대전화 번호
    private String brName; // 소속지사 명
    private String frName; // 가맹점 명
    private Integer bcReadyAmt; // 준비금

}
