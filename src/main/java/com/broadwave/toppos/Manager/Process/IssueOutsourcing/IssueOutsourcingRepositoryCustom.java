package com.broadwave.toppos.Manager.Process.IssueOutsourcing;

import com.broadwave.toppos.Manager.Process.IssueOutsourcing.IssueOutsourcingDtos.IssueOutsourcingListDto;
import com.broadwave.toppos.Manager.Process.IssueOutsourcing.IssueOutsourcingDtos.IssueOutsourcingSubListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailOutsourcingReceiptListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-05-23
 * Time :
 * Remark :
 */
public interface IssueOutsourcingRepositoryCustom {
    List<RequestDetailOutsourcingReceiptListDto> findByRequestDetailOutsourcingReceiptList(String brCode, Long frId, String filterFromDt, String filterToDt); // 지사 외주입고 querydsl

    List<IssueOutsourcingListDto> findByIssueOutsourcingList(String brCode, Long franchiseId, String filterFromDt, String filterToDt); // // 지사 외주/출고 현황 NativeQuery

    List<IssueOutsourcingSubListDto> findByIssueOutsourcingSubList(String brCode, String fdO1Dt); // 지사 외주/출고 현황 오른쪽 Querydsl
}
