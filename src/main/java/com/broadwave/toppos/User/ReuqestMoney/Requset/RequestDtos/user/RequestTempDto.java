package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2022-03-03
 * Time :
 * Remark : Toppos 가맹점 접수임시저장 내역 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestTempDto {

    private String frNo; // 접수코드
    private String bcName; // 임시저장한 고객명

}