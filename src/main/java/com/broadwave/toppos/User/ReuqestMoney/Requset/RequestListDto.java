package com.broadwave.toppos.User.ReuqestMoney.Requset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수마스트 ListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestListDto {

    private String frNo; // 접수코드
    private LocalDateTime fr_insert_date; // 접수시간
    private String bcName; // 고객명
    private String bcHp; // 고객번호

    public String getFrNo() {
        return frNo;
    }

    public String getFr_insert_date() {
        return fr_insert_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getBcName() {
        return bcName;
    }

    public String getBcHp() {
        return bcHp;
    }
}
