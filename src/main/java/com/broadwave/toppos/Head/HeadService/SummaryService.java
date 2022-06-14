package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.DailySummary.DaliySummaryRepository;
import com.broadwave.toppos.Head.MonthlySummary.MonthlySummaryDtos.MonthlySummaryListDto;
import com.broadwave.toppos.Head.MonthlySummary.MonthlySummaryRepository;
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
 * Date : 2022-06-14
 * Time :
 * Remark : Toppos 본사 - 정산 페이지 관련 서비스
 */
@Slf4j
@Service
public class SummaryService {

    private final DaliySummaryRepository daliySummaryRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;

    @Autowired
    public SummaryService(DaliySummaryRepository daliySummaryRepository, MonthlySummaryRepository monthlySummaryRepository) {
        this.daliySummaryRepository = daliySummaryRepository;
        this.monthlySummaryRepository = monthlySummaryRepository;
    }

    // 본사 일정산 요역 리스트 호출API
    public ResponseEntity<Map<String, Object>> headFranchiseDaliySummaryList(Long franchiseId, String filterYearMonth) {
        log.info("headFranchiseDaliySummaryList 호출");

        log.info("franchiseId  : " + franchiseId);
        log.info("filterYearMonth  : " + filterYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<DaliySummaryListDto> daliySummaryListDtos = daliySummaryRepository.findByDaliySummaryList(franchiseId, filterYearMonth);

        data.put("gridListData", daliySummaryListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 월정산 요역 리스트 호출API
    public ResponseEntity<Map<String, Object>> headBranchMonthlySummaryList(String filterYearMonth) {
        log.info("headBranchMonthlySummaryList 호출");

        log.info("filterYearMonth  : " + filterYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<MonthlySummaryListDto> monthlySummaryListDtos = monthlySummaryRepository.findByMonthlySummaryList(filterYearMonth);

        data.put("gridListData", monthlySummaryListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
