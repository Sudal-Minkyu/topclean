package com.broadwave.toppos.Manager.Process.IssueOutsourcing;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-05-23
 * Time :
 * Remark :
 */
public interface IssueOutsourcingRepositoryCustom {
    List<IssueOutsourcingListDto> findByIssueOutsourcingList(String brCode, Long franchiseId, String filterFromDt, String filterToDt); // // 지사 외주/출고 현황 NativeQuery
}
