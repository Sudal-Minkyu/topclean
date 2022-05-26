package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Sales.Dtos.BranchMonthlySaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.BranchRankSaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.FranchiseRankSaleDto;
import com.broadwave.toppos.Head.Sales.Dtos.ItemSaleStatusDto;
import com.broadwave.toppos.Head.Sales.SalesRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-05-24
 * Time :
 * Remark : Toppos 본사 - 지사의 매출현황 관련 서비스
 */
@Slf4j
@Service
public class SalesService {

    private final SalesRepositoryCustom salesRepositoryCustom;

    @Autowired
    public SalesService(SalesRepositoryCustom salesRepositoryCustom) {
        this.salesRepositoryCustom = salesRepositoryCustom;
    }

    // 본사 - 지사 월간매출, 누적매출 그래프 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headBranchMonthlySale(String filterYear) {
        log.info("headBranchMonthlySale 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<BranchMonthlySaleDto> branchMonthlySaleDtos = salesRepositoryCustom.findByBranchMonthlySale(filterYear);

        data.put("gridListData", branchMonthlySaleDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사 매출순위 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headBranchRankSale(String filterYear) {
        log.info("headBranchRankSale 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<BranchRankSaleDto> branchRankSaleDtos = salesRepositoryCustom.findByBranchRankSale(filterYear);

        data.put("gridListData", branchRankSaleDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 가맹점 매출순위 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headFranchiseRankSale(String brCode, String filterYear) {
        log.info("headFranchiseRankSale 호출");

        log.info("brCode  : " + brCode);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<FranchiseRankSaleDto> franchiseRankSaleDtos = salesRepositoryCustom.findByFranchiseRankSale(brCode, filterYear);

        data.put("gridListData", franchiseRankSaleDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 가맹점 매출순위 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headItemSaleStatus(String brId, String frId, String filterYear) {
        log.info("headItemSaleStatus 호출");

        log.info("brId  : " + brId);
        log.info("frId  : " + frId);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemSaleStatusDto> ItemSaleStatus = salesRepositoryCustom.findByItemSaleStatus(brId, frId, filterYear);

        data.put("gridListData", ItemSaleStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


}
