package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
public interface RequestDetailRepositoryCustom {
    List<RequestDetailDto> findByRequestTempDetailList(String frNo);
    List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo);
    List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt);
}
