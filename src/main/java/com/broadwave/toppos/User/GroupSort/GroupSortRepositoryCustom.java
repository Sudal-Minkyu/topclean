package com.broadwave.toppos.User.GroupSort;

import com.broadwave.toppos.User.GroupSort.GroupSortDtos.GroupSortUpdateDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark :
 */
public interface GroupSortRepositoryCustom {

    List<GroupSortUpdateDto> findByGroupSortList(String frCode);

}
