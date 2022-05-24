package com.broadwave.toppos.Head.Sales;

import com.broadwave.toppos.Head.Sales.Dtos.BranchMonthlySaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.BranchRankSaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.FranchiseRankSaleDto;

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

}
