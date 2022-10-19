package com.broadwave.toppos.Manager.Calendar.CalendarDtos;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark : Toppos 지사업무휴무일지정 ListDto, Toppos 지사업무휴무일 가맹점 메인페이지용 Dto 현재날짜로부터 10일이후안의 날짜
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchCalendarListDto {

    private String bcDate; // 날짜 : yyyymmdd
    private String bcDayoffYn; // 휴무상태 : Y/N

}
