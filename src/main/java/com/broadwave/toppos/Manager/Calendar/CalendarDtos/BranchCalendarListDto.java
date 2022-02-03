package com.broadwave.toppos.Manager.Calendar.CalendarDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 지사업무휴무일지정 ListDto
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchCalendarListDto {

    private String bcDate; // 날짜 : yyyymmdd
    private String bcDayoffYn; // 휴무상태 : Y/N

    public String getBcDate() {
        return bcDate;
    }

    public String getBcDayoffYn() {
        return bcDayoffYn;
    }
}