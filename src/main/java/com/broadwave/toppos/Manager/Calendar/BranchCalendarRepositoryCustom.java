package com.broadwave.toppos.Manager.Calendar;

import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarListDto;
import com.broadwave.toppos.User.UserDtos.EtcDataDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark :
 */
public interface BranchCalendarRepositoryCustom {
    List<BranchCalendarListDto> branchCalendarDtoList(String brCode, String targetYear);

    List<EtcDataDto> findByEtc(Long frEstimateDuration, String frCode, String nowDate);

    List<BranchCalendarListDto> branchCalendarSlidingDtoList(String brCode, String nowDate);
}
