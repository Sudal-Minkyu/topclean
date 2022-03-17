package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-03-08
 * Time :
 * Remark : Toppos 접수세부 검품정보 메인페이지용 테이블 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeotMainListDto {

    private String frName; // 가맹점명
    private String bcName; // 고객명
    private String bgName; // 대분류명
    private String fdTag; // 택번호
    private String fiCustomerConfirm; // 검품 상태
    private String frYyyymmdd; // 접수날짜
    private String fdS2Dt; // 지사입고일

}