package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-18
 * Time :
 * Remark : Toppos 가맹점 메세지 송신이력 RepositoryCustom
 */
public interface MessageHistoryRepositoryCustom {

    List<MessageHistoryListDto> findByMessageHistoryList(String frCode, String filterFromDt, String filterToDt);

    List<MessageHistorySubListDto> findByMessageHistorySubList(String frCode, String insertYyyymmdd);

}
