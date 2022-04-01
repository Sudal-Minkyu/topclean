package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-01
 * Time :
 * Remark :
 */
public interface FindRepositoryCustom {
    List<FindListDto> findByFindList(Long franchiseId, String brCode, String filterFromDt, String filterToDt, String ffState);

    int findByFindCheckUpdate(List<Long> ffIdList, String login_id);

}
