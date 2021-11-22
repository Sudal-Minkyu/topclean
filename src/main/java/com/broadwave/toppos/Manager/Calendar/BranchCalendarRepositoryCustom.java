package com.broadwave.toppos.Manager.Calendar;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark :
 */
public interface BranchCalendarRepositoryCustom {
    List<BranchCalendarListDto> branchCalendarDtoList(String brCode, String targetYear);
}
