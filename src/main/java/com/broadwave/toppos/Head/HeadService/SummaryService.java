package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.DaliySummaryListDto;
import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryDtos.ReceiptDailySummaryListDto;
import com.broadwave.toppos.Head.Calculate.DailySummary.DaliySummaryRepository;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlyBranchSummaryListDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlyFranchiseSummaryListDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlyHeadSummaryListDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.MonthlySummaryDaysDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryDtos.ReceiptMonthlyListDto;
import com.broadwave.toppos.Head.Calculate.MonthlySummary.MonthlySummaryRepository;
import com.broadwave.toppos.Head.Calculate.ReceiptDaily.ReceiptDaily;
import com.broadwave.toppos.Head.Calculate.ReceiptDaily.ReceiptDailyListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptDaily.ReceiptDailyRepository;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthly;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyDtos.ReceiptMonthlyBranchListDto;
import com.broadwave.toppos.Head.Calculate.ReceiptMonthly.ReceiptMonthlyRepository;
import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
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
import java.util.Optional;

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
    private final ReceiptDailyRepository receiptDailyRepository;
    private final FranchiseRepository franchiseRepository;

    @Autowired
    public SummaryService(TokenProvider tokenProvider, DaliySummaryRepository daliySummaryRepository, MonthlySummaryRepository monthlySummaryRepository,
                          ReceiptMonthlyRepository receiptMonthlyRepository, ReceiptDailyRepository receiptDailyRepository, FranchiseRepository franchiseRepository) {
        this.tokenProvider = tokenProvider;
        this.daliySummaryRepository = daliySummaryRepository;
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.receiptMonthlyRepository = receiptMonthlyRepository;
        this.receiptDailyRepository = receiptDailyRepository;
        this.franchiseRepository = franchiseRepository;
    }

    // 본사 지사 일정산 요역 리스트 호출API
    public ResponseEntity<Map<String, Object>> daliySummaryList(Long franchiseId, String filterYearMonth, HttpServletRequest request) {
        log.info("headFranchiseDaliySummaryList 호출");

        log.info("franchiseId  : " + franchiseId);
        log.info("filterYearMonth  : " + filterYearMonth);

        if(request != null){
            // 클레임데이터 가져오기
            Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
            String frCode = (String) claims.get("frCode");
            Optional<Franchise> optionalFranchise = franchiseRepository.findByFrCode(frCode);
            if(optionalFranchise.isPresent()){
                franchiseId = optionalFranchise.get().getId();
            }
        }

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<DaliySummaryListDto> daliySummaryListDtos = daliySummaryRepository.findByDaliySummaryList(franchiseId, filterYearMonth);

        data.put("gridListData", daliySummaryListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 지사별 월정산 요역 리스트 호출API
    public ResponseEntity<Map<String, Object>> headMonthlySummaryList(String filterYearMonth) {
        log.info("headMonthlySummaryList 호출");

        log.info("filterYearMonth  : " + filterYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<MonthlyHeadSummaryListDto> monthlyHeadSummaryListDtos = monthlySummaryRepository.findByHeadMonthlySummaryList(filterYearMonth);

        data.put("gridListData", monthlyHeadSummaryListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사 월정산 요역 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchMonthlySummaryList(String filterYearMonth, HttpServletRequest request) {
        log.info("branchMonthlySummaryList 호출");

        log.info("filterYearMonth  : " + filterYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<MonthlyBranchSummaryListDto> monthlyBranchSummaryListDtos = monthlySummaryRepository.findByBranchMonthlySummaryList(filterYearMonth, brCode);

        data.put("gridListData", monthlyBranchSummaryListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    // 본사 가맹점별 월정산 요역 리스트 호출API
    public ResponseEntity<Map<String, Object>> monthlySummaryList(String filterYearMonth, HttpServletRequest request) {
        log.info("headFranchiseMonthlySummaryList 호출");

        log.info("filterYearMonth  : " + filterYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String frCode = "";
        if(request != null){
            // 클레임데이터 가져오기
            Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
            frCode = (String) claims.get("frCode");
        }

        List<MonthlyFranchiseSummaryListDto> monthlyFranchiseSummaryListDtos = monthlySummaryRepository.findByFranchiseMonthlySummaryList(filterYearMonth, frCode);

        data.put("gridListData", monthlyFranchiseSummaryListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 지사 월정산입금 리스트 호출API
    public ResponseEntity<Map<String, Object>> receiptMonthlyList(Long branchId, String filterFromYearMonth, String filterToYearMonth) {
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

    // 본사 지사별 월정산 입금현황 호출API
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

    // 본사, 지사 가맹점별 일정산 입금현황 호출API
    public ResponseEntity<Map<String, Object>> franchiseDailyStatusList(String filterYearMonth, HttpServletRequest request) {
        log.info("franchiseDailyStatusList 호출");

        log.info("filterYearMonth  : " + filterYearMonth);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String brCode = "";
        if(request != null){
            // 클레임데이터 가져오기
            Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
            brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
            log.info("현재 접속한 지사 코드 : "+brCode);
        }
        List<ReceiptDailySummaryListDto> receiptDailySummaryListDtos = daliySummaryRepository.findByReceiptDailySummaryList(filterYearMonth, brCode);

        int inamtcnt; // 미입금 카운트
        for(ReceiptDailySummaryListDto receiptDailySummaryListDto : receiptDailySummaryListDtos){
            inamtcnt = 0;
            if(receiptDailySummaryListDto.getAmt01().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt02().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt03().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt04().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt05().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt06().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt07().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt08().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt09().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt10().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt11().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt12().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt13().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt14().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt15().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt16().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt17().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt18().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt19().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt20().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt21().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt22().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt23().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt24().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt25().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt26().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt27().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt28().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt29().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt30().equals("미입금")){
                inamtcnt++;
            }
            if(receiptDailySummaryListDto.getAmt31().equals("미입금")){
                inamtcnt++;
            }
            if(inamtcnt >= 3){
                receiptDailySummaryListDto.setInamtcnt(String.valueOf(inamtcnt));
            }
        }

        data.put("gridListData", receiptDailySummaryListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 일정산 입금 리스트 호출API
    public ResponseEntity<Map<String, Object>> branchReceiptDailyList(Long franchiseId, String filterFromDt, String filterToDt) {
        log.info("branchReceiptDailyList 호출");

        log.info("franchiseId  : " + franchiseId);
        log.info("filterFromDt  : " + filterFromDt);
        log.info("filterToDt  : " + filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ReceiptDailyListDto> receiptDailyListDtos = daliySummaryRepository.findByBranchReceiptDailySummaryList(franchiseId, filterFromDt, filterToDt);
        data.put("gridListData", receiptDailyListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사 일정산 입금 저장 호출API
    public ResponseEntity<Map<String, Object>> branchDailySummarySave(HttpServletRequest request, String hsYyyymmdd, String frCode, String hrReceiptYyyymmdd, Integer hrReceiptSaleAmt, Integer hrReceiptRoyaltyAmt) {
        log.info("branchDailySummarySave 호출");

        log.info("hsYyyymmdd  : " + hsYyyymmdd);
        log.info("frCode  : " + frCode);
        log.info("hrReceiptYyyymmdd  : " + hrReceiptYyyymmdd);
        log.info("hrReceiptSaleAmt  : " + hrReceiptSaleAmt);
        log.info("hrReceiptRoyaltyAmt  : " + hrReceiptRoyaltyAmt);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        log.info("현재 접속한 지사 코드 : "+brCode);

        ReceiptDaily receiptDaily = new ReceiptDaily();
        if(!hsYyyymmdd.equals("") && !frCode.equals("")){
            receiptDaily.setHsYyyymmdd(hsYyyymmdd);
            receiptDaily.setFrCode(frCode);
            receiptDaily.setBrCode(brCode);
            receiptDaily.setHrReceiptYyyymmdd(hrReceiptYyyymmdd);
            receiptDaily.setHrReceiptSaleAmt(hrReceiptSaleAmt);
            receiptDaily.setHrReceiptRoyaltyAmt(hrReceiptRoyaltyAmt);
            receiptDaily.setInsertDateTime(LocalDateTime.now());

            receiptDailyRepository.save(receiptDaily);
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "일정산일자가 "+ResponseErrorCode.TP030.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
        }

        return ResponseEntity.ok(res.success());
    }

}
