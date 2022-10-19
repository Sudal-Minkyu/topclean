package com.broadwave.toppos.Head.Calculate.ReceiptMonthly;

import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyBranchListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-06-15
 * Time :
 * Remark :
 */
public interface ReceiptMonthlyRepositoryCustom {
    List<ReceiptMonthlyBranchListDto> findByReceiptMonthlyBranchList(String filterYear);  // 본사 지사별 월정산 입금현황 리스트
}
