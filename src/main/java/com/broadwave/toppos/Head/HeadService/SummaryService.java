package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryRepository;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryDaysDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryListDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryRepository;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthly;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.ReceiptMonthlyListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyBranchListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

    private final TokenProvider tokenProvider;
    private final DaliySummaryRepository daliySummaryRepository;
    private final MonthlySummaryRepository monthlySummaryRepository;
    private final ReceiptMonthlyRepository receiptMonthlyRepository;

    @Autowired
    public SummaryService(TokenProvider tokenProvider, DaliySummaryRepository daliySummaryRepository, MonthlySummaryRepository monthlySummaryRepository,
                          ReceiptMonthlyRepository receiptMonthlyRepository) {
        this.tokenProvider = tokenProvider;
        this.daliySummaryRepository = daliySummaryRepository;
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.receiptMonthlyRepository = receiptMonthlyRepository;
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

    // 본사 지사 월정산입금 리스트 호출API
    public ResponseEntity<Map<String, Object>> headBranchReceiptMonthlyList(Long branchId, String filterFromYearMonth, String filterToYearMonth) {
        log.info("headBranchReceiptMonthlyList 호출");

        log.info("branchId  : " + branchId);
        log.info("filterFromYearMonth  : " + filterFromYearMonth);
        log.info("filterToYearMonth  : " + filterToYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ReceiptMonthlyListDto> receiptMonthlyListDtos = monthlySummaryRepository.findByReceiptMonthlyList(branchId, filterFromYearMonth, filterToYearMonth);
//        log.info("receiptMonthlyListDtos  : " + receiptMonthlyListDtos);
        data.put("gridListData", receiptMonthlyListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 지사 월정산입금 저장 호출API
    public ResponseEntity<Map<String, Object>> headBranchMonthlySummarySave(String hsYyyymm, String brCode, String hrReceiptYyyymmdd, Integer hrReceiptBrRoyaltyAmt, Integer hrReceiptFrRoyaltyAmt) {
        log.info("headBranchMonthlySummarySave 호출");

        log.info("hsYyyymm  : " + hsYyyymm);
        log.info("brCode  : " + brCode);
        log.info("hrReceiptYyyymmdd  : " + hrReceiptYyyymmdd);
        log.info("hrReceiptBrRoyaltyAmt  : " + hrReceiptBrRoyaltyAmt);
        log.info("hrReceiptFrRoyaltyAmt  : " + hrReceiptFrRoyaltyAmt);

        AjaxResponse res = new AjaxResponse();

        ReceiptMonthly receiptMonthly = new ReceiptMonthly();
        if(!hsYyyymm.equals("") && !brCode.equals("")){
            receiptMonthly.setHsYyyymm(hsYyyymm);
            receiptMonthly.setBrCode(brCode);
            receiptMonthly.setHrReceiptYyyymmdd(hrReceiptYyyymmdd);
            receiptMonthly.setHrReceiptBrRoyaltyAmt(hrReceiptBrRoyaltyAmt);
            receiptMonthly.setHrReceiptFrRoyaltyAmt(hrReceiptFrRoyaltyAmt);
            receiptMonthly.setInsertDateTime(LocalDateTime.now());

            receiptMonthlyRepository.save(receiptMonthly);
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "월정산일자가 "+ResponseErrorCode.TP030.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
        }

        return ResponseEntity.ok(res.success());
    }

    // 본사 지사 월정산 입금현황 호출API
    public ResponseEntity<Map<String, Object>> headBranchMonthlyStatusList(String filterYear) {
        log.info("headBranchMonthlyStatusList 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ReceiptMonthlyBranchListDto> receiptMonthlyBranchListDtos = receiptMonthlyRepository.findByReceiptMonthlyBranchList(filterYear);
        data.put("gridListData", receiptMonthlyBranchListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 일일정산서 API 호출
    public ResponseEntity<Map<String, Object>> franchiseReceiptDaysList(String hsYyyymmdd, HttpServletRequest request) {
        log.info("franchiseReceiptDaysList 호출");

        log.info("hsYyyymmdd  : " + hsYyyymmdd);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 현재 소속된 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("현재 소속된 지사 코드 : "+frbrCode);

        List<MonthlySummaryDaysDto> monthlySummaryDaysDtos = monthlySummaryRepository.findByReceiptMonthlyDays(hsYyyymmdd, frbrCode, frCode);
        data.put("monthlySummaryDaysDtos", monthlySummaryDaysDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
