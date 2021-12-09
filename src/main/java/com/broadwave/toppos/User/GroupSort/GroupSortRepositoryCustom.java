package com.broadwave.toppos.User.GroupSort;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark :
 */
public interface GroupSortRepositoryCustom {
    List<GroupSortDto> findByGroupSortList(String frCode);
}
