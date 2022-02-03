package com.broadwave.toppos.Manager.Calendar.CalendarDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-18
 * Time :
 * Remark : Toppos 지사업무휴무일지정 Dto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchCalendarDto {
    private Integer year; // 년도 : year

    private String bcDate; // 날짜 : yyyymmdd
    private String bcDayoffYn; // 휴무상태 : Y/N

    public Integer getYear() {
        return year;
    }

    public String getBcDate() {
        return bcDate;
    }

    public String getBcDayoffYn() {
        return bcDayoffYn;
    }
}
