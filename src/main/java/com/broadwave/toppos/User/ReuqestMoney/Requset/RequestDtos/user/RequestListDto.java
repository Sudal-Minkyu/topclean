package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수마스터 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestListDto {

    private String frNo; // 접수코드
    private LocalDateTime frInsertDate; // 접수시간
    private String bcName; // 고객명
    private String bcHp; // 고객번호

    public String getFrInsertDate() {
        return frInsertDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
