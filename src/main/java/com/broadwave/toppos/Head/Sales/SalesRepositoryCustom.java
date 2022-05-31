package com.broadwave.toppos.Head.Sales;

import com.broadwave.toppos.Head.Sales.Dtos.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-05-24
 * Time :
 * Remark : 지사 월간매출, 누적매출 등등.. 데이터 호출 인터페이스
 */
public interface SalesRepositoryCustom {

    List<BranchMonthlySaleDto> findByBranchMonthlySale(String filterYear); // 지사 월간매출, 누적매출 그래프 데이터

    List<BranchRankSaleDto> findByBranchRankSale(String filterYear); // 지사 매출순위 데이터

    List<FranchiseRankSaleDto> findByFranchiseRankSale(String brCode, String filterYear); // 가맹점 매출순위 데이터

    List<ItemSaleStatusDto> findByItemSaleStatus(String brId, String frId, String filterYear); // 지사,가맹점 품목별 매출현황 데이터

    List<ItemSaleDetailStatusDto> findByItemSaleDetailStatus(String bgCode, String brId, String frId, String filterYear); // 지사,가맹점 세부품목별 매출현황 데이터

    List<ReceiptMonthlyStatusDto> findByMonthlyReceiptList(String filterYear); // 월간 접수 현황 데이터

    List<ReceiptBranchRankDto> findByBranchReceiptRank(String filterYear); // 지사별 접수 순위 데이터

    List<ReceiptFranchiseRankDto> findByFranchiseReceiptRank(String brCode, String filterYear); // 가맹점별 접수 순위 데이터

    List<ItemReceiptStatusDto> findByItemReceiptStatus(String brId, String frId, String filterYear); // 지사,가맹점 품목별 접수현황 데이터

    List<ItemReceiptDetailStatusDto> findByItemReceiptDetailStatus(String bgCode, String brId, String frId, String filterYear); // 지사,가맹점 세부품목별 접수현황 데이터

    List<CustomTransactionStatusDto> findByCustomTransactionStatus(String filterYear); // 지사별 객단가 현황 데이터

    List<CustomTransactionDetailStatusDto> findByCustomTransactionDetailStatus(String brCode, String filterYear); // 가맹점별 객단가 현황 데이터

    CustomTransactionTotalDto findByCustomTransactionTotalStatus(String filterYear); // // 객단가, pcs단가 전체 평균 데이터

}
