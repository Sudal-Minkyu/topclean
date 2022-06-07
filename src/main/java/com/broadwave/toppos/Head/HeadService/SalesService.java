package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Sales.Dtos.*;
import com.broadwave.toppos.Head.Sales.SalesRepositoryCustom;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerAgeRateDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerGenderRateDto;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final CustomerRepository customerRepository;

    @Autowired
    public SalesService(SalesRepositoryCustom salesRepositoryCustom, CustomerRepository customerRepository) {
        this.salesRepositoryCustom = salesRepositoryCustom;
        this.customerRepository = customerRepository;
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

    // 본사 - 지사,가맹점 품목별 매출 현황 데이터 호출 API
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

    // 본사 - 지사,가맹점 세부품목별 매출 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headItemSaleDetailStatus(String bgCode, String brId, String frId, String filterYear) {
        log.info("headItemSaleDetailStatus 호출");

        log.info("bgCode  : " + bgCode);
        log.info("brId  : " + brId);
        log.info("frId  : " + frId);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemSaleDetailStatusDto> itemSaleDetailStatus = salesRepositoryCustom.findByItemSaleDetailStatus(bgCode, brId, frId, filterYear);

        data.put("gridListData", itemSaleDetailStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 월간 접수 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headMonthlyReceiptList(String filterYear) {
        log.info("headMonthlyReceiptList 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ReceiptMonthlyStatusDto> monthlyReceiptStatus = salesRepositoryCustom.findByMonthlyReceiptList(filterYear);

        data.put("gridListData", monthlyReceiptStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사 접수 순위 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headBranchReceiptList(String filterYear) {
        log.info("headBranchReceiptList 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ReceiptBranchRankDto> branchReceiptList = salesRepositoryCustom.findByBranchReceiptRank(filterYear);

        data.put("gridListData", branchReceiptList);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 가맹점 접수 순위 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headFranchiseReceiptList(String brCode, String filterYear) {
        log.info("headFranchiseReceiptList 호출");

        log.info("brCode  : " + brCode);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ReceiptFranchiseRankDto> franchiseReceiptList = salesRepositoryCustom.findByFranchiseReceiptRank(brCode, filterYear);

        data.put("gridListData", franchiseReceiptList);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사,가맹점 품목별 접수 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headItemReceiptStatus(String brId, String frId, String filterYear) {
        log.info("headItemReceiptStatus 호출");

        log.info("brId  : " + brId);
        log.info("frId  : " + frId);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemReceiptStatusDto> itemReceiptStatus = salesRepositoryCustom.findByItemReceiptStatus(brId, frId, filterYear);

        data.put("gridListData", itemReceiptStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사,가맹점 세부품목별 접수 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headItemReceiptDetailStatus(String bgCode, String brId, String frId, String filterYear) {
        log.info("headItemReceiptDetailStatus 호출");

        log.info("bgCode  : " + bgCode);
        log.info("brId  : " + brId);
        log.info("frId  : " + frId);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemReceiptDetailStatusDto> itemReceiptDetailStatus = salesRepositoryCustom.findByItemReceiptDetailStatus(bgCode, brId, frId, filterYear);

        data.put("gridListData", itemReceiptDetailStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사별 객단가 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headCustomTransactionStatus(String filterYear) {
        log.info("headCustomTransactionStatus 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CustomTransactionStatusDto> customTransactionStatus = salesRepositoryCustom.findByCustomTransactionStatus(filterYear);
        CustomTransactionTotalDto customTransactionTotalStatus = salesRepositoryCustom.findByCustomTransactionTotalStatus(filterYear);

        data.put("gridListData", customTransactionStatus);
        data.put("TotalData", customTransactionTotalStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 가맹점별 객단가 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headCustomTransactionDetailStatus(String brCode, String filterYear) {
        log.info("headCustomTransactionDetailStatus 호출");

        log.info("brCode  : " + brCode);
        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CustomTransactionDetailStatusDto> customTransactionDetailStatus = salesRepositoryCustom.findByCustomTransactionDetailStatus(brCode, filterYear);

        data.put("gridListData", customTransactionDetailStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 월별 단가 추이 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headMonthlyPriceStatus(String filterYear) {
        log.info("headMonthlyPriceStatus 호출");

        log.info("filterYear  : " + filterYear);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CustomTransactionMonthlyDto> customTransactionMonthlyStatus = salesRepositoryCustom.findByCustomTransactionMonthlyStatus(filterYear);

        // 출력데이터 월별명 변경
        customTransactionMonthlyStatus.stream()
                .map(m -> {
                    if (m.getMonth().startsWith("0")) {
                        m.setMonth(m.getMonth().substring(1) + "월");
                    } else {
                        m.setMonth(m.getMonth() + "월");
                    }
                    return m;
                }).collect(Collectors.toList());

        data.put("ListData", customTransactionMonthlyStatus);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사,가맹점별 성별 비중 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headCustomerGenderRateStatus(Long brId, Long frId) {
        log.info("headCustomerGenderRateStatus 호출");

        log.info("brId  : " + brId);
        log.info("frId  : " + frId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CustomerGenderRateDto> customerGenderRate = customerRepository.findByCustomerGenderRate(brId, frId);

        // 0 -> 여자, 1 -> 남자로 변경
        customerGenderRate.stream()
                .map(g -> {
                    if (g.getGender().equals("0")) {
                        g.setGender("여자");
                    } else {
                        g.setGender("남자");
                    }
                    return g;
                }).collect(Collectors.toList());

        data.put("listData", customerGenderRate);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 - 지사,가맹점별 나이 비중 현황 데이터 호출 API
    public ResponseEntity<Map<String, Object>> headCustomerAgeRateStatus(Long brId, Long frId) {
        log.info("headCustomerAgeRateStatus 호출");

        log.info("brId  : " + brId);
        log.info("frId  : " + frId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CustomerAgeRateDto> customerAgeRate = customerRepository.findByCustomerAgeRate(brId, frId);

        ArrayList<CustomerAgeRateDto> customerAgeRateDtos = new ArrayList<>(); // 출력할 DTO 생성
        CustomerAgeRateDto customerAgeOver60 = new CustomerAgeRateDto(); // 60대 이상 인스턴스 생성

        long rate = 0;
        for (CustomerAgeRateDto a : customerAgeRate) {
            if (!a.getAge().equals("")) {
                int i = Integer.parseInt(a.getAge());
                if (i >= 60) {
                    rate += a.getRate(); // 60대 이상부터는 더하기
                    customerAgeOver60.setAge("60대 이상");
                    customerAgeOver60.setRate(rate);

                } else {
                    // 10 ~ 50대
                    CustomerAgeRateDto customerAgeRateDto = new CustomerAgeRateDto();
                    customerAgeRateDto.setAge(a.getAge() + "대");
                    customerAgeRateDto.setRate(a.getRate());
                    customerAgeRateDtos.add(customerAgeRateDto);
                }
            } else {
                // 연령 미선택자
                CustomerAgeRateDto customerAgeRateDto = new CustomerAgeRateDto();
                customerAgeRateDto.setAge("미선택");
                customerAgeRateDto.setRate(a.getRate());
                customerAgeRateDtos.add(customerAgeRateDto);
            }
        }
        if (customerAgeOver60.getAge() != null) {
            customerAgeRateDtos.add(customerAgeOver60);
        }


        data.put("listData", customerAgeRateDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
}
