package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 메인페이지 6개 ListDto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHistoryListDto {

    private String typename; // 상태
    private Timestamp requestTime; // 시간
    private String bcName; // 고객명
    private String bcHp; // 고객번호

    public String getRequestTime() {
//        return requestTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        return requestTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
