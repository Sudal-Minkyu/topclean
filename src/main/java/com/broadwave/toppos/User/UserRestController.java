package com.broadwave.toppos.User;

import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.Addprocess.AddprocessDto;
import com.broadwave.toppos.Head.Franohise.FranchisInfoDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Head.Item.Group.A.UserItemGroupSortDto;
import com.broadwave.toppos.Head.Item.Group.B.UserItemGroupSListDto;
import com.broadwave.toppos.Head.Item.Price.UserItemPriceSortDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendar;
import com.broadwave.toppos.Manager.ManagerService;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerInfoDto;
import com.broadwave.toppos.User.Customer.CustomerListDto;
import com.broadwave.toppos.User.Customer.CustomerMapperDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestMapperDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-11-16
 * Time :
 * Remark : Toppos User RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/user") //  ( 권한 : 가맹점 )
public class UserRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;
    private final HeadService headService;
    private final ManagerService managerService;

    @Autowired
    public UserRestController(UserService userService, TokenProvider tokenProvider, ModelMapper modelMapper, HeadService headService, ManagerService managerService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.headService = headService;
        this.managerService = managerService;
    }

    // 고객 등록 API
    @PostMapping("customerSave")
    public ResponseEntity<Map<String,Object>> customerSave(@ModelAttribute CustomerMapperDto customerMapperDto, HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();

        Customer customer = modelMapper.map(customerMapperDto, Customer.class);
        log.info("customer : "+customer);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        if(frCode == null || frCode.equals("")){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP007.getCode(), ResponseErrorCode.TP007.getDesc(),ResponseErrorCode.TP008.getCode(), ResponseErrorCode.TP008.getDesc()));
        }

        if(customerMapperDto.getBcId() != null){
            log.info("고객 정보를 수정합니다.");
            log.info("customerMapperDto.getBcId() : "+customerMapperDto.getBcId());
            Optional<Customer> optionalCustomerById= userService.findByBcId(customer.getBcId());
            if(!optionalCustomerById.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 "+ResponseErrorCode.TP005.getDesc(), null, null));
            }else{
                Optional<Customer> optionalCustomerByHp= userService.findByBcHp(customer.getBcHp());
                if(optionalCustomerByHp.isPresent()){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP014.getCode(), ResponseErrorCode.TP014.getDesc(), null, null));
                }else{
                    // 수정일때
                    customer.setFrCode(optionalCustomerById.get().getFrCode());
                    customer.setBcMessageAgreeDt(LocalDateTime.now());
                    if(customer.getBcQuitYn().equals("Y")){
                        customer.setBcQuitDate(LocalDateTime.now());
                    }
                    customer.setBcLastRequestDt(optionalCustomerById.get().getBcLastRequestDt());
                    customer.setInsert_id(optionalCustomerById.get().getInsert_id());
                    customer.setInsertDateTime(optionalCustomerById.get().getInsertDateTime());
                    customer.setModify_id(login_id);
                    customer.setModifyDateTime(LocalDateTime.now());

                    Customer customerSave =  userService.customerSave(customer);
                    log.info("고객 수정 성공 : 핸드폰 번호 '" + customerSave.getBcHp() +"'");
                }
            }
        }else{
            log.info("신규 고객 입니다.");
            Optional<Customer> optionalCustomerByHp= userService.findByBcHp(customer.getBcHp());
            if(optionalCustomerByHp.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP014.getCode(), ResponseErrorCode.TP014.getDesc(), null, null));
            }else{
                // 신규일때
                customer.setFrCode(frCode);
                customer.setBcQuitYn("N");
                customer.setInsert_id(login_id);
                customer.setInsertDateTime(LocalDateTime.now());
                customer.setBcMessageAgreeDt(LocalDateTime.now());

                Customer customerSave =  userService.customerSave(customer);
                log.info("고객 신규 저장 성공 : 핸드폰 번호 '" + customerSave.getBcHp() +"'");
            }
        }

//        log.info("customer : "+customer);
        return ResponseEntity.ok(res.success());
    }

    // 고객 정보 호출 API (현재 로그인한 가맹점의 대한 고객만 호출한다.)
    @GetMapping("customerInfo")
    public ResponseEntity<Map<String,Object>> customerInfo(HttpServletRequest request,
                                                           @RequestParam(value="searchType", defaultValue="") String searchType,
                                                           @RequestParam(value="searchString", defaultValue="") String searchString){
        log.info("customerInfo 호출");
        log.info("searchType :"+searchType);
        log.info("searchString :"+searchString);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> customerListData = new ArrayList<>();
        HashMap<String,Object> customerListInfo;

        List<CustomerInfoDto> customerInfoListDto = userService.findByCustomerInfo(frCode, searchType, searchString);
        log.info("customerInfoListDto : "+customerInfoListDto);

        for (CustomerInfoDto customerInfoDto: customerInfoListDto) {

            customerListInfo = new HashMap<>();

            customerListInfo.put("bcId", customerInfoDto.getBcId());
            customerListInfo.put("bcName", customerInfoDto.getBcName());
            customerListInfo.put("bcHp", customerInfoDto.getBcHp());
            customerListInfo.put("bcGrade", customerInfoDto.getBcGrade());
            customerListInfo.put("bcValuation", customerInfoDto.getBcValuation());
            customerListInfo.put("bcRemark", customerInfoDto.getBcRemark());
            customerListInfo.put("bcAddress", customerInfoDto.getBcAddress());
            customerListInfo.put("bcLastRequestDt", customerInfoDto.getBcLastRequestDt());

            customerListData.add(customerListInfo);
        }

        log.info("가맹점코드 : "+frCode+"의 고객 리스트 : "+customerListData);
        data.put("gridListData",customerListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    // 고객 리스트 API(현재 로그인한 가맹점의 대한 고객리스트만 호출한다.)
    @GetMapping("customerList")
    public ResponseEntity<Map<String,Object>> customerList(HttpServletRequest request,
                                                           @RequestParam(value="searchType", defaultValue="") String searchType,
                                                           @RequestParam(value="searchString", defaultValue="") String searchString){
        log.info("customerList 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> customerListData = new ArrayList<>();
        HashMap<String,Object> customerListInfo;

        List<CustomerListDto> customerListDtos = userService.findByCustomerList(frCode, searchType, searchString);
//        log.info("customerListDtos : "+customerListDtos);
        for (CustomerListDto customerListDto: customerListDtos) {

            customerListInfo = new HashMap<>();

            customerListInfo.put("bcId", customerListDto.getBcId());
            customerListInfo.put("bcName", customerListDto.getBcName());
            customerListInfo.put("bcHp", customerListDto.getBcHp());
            customerListInfo.put("bcSex", customerListDto.getBcSex());
            customerListInfo.put("bcAddress", customerListDto.getBcAddress());
            customerListInfo.put("bcBirthday", customerListDto.getBcBirthday());
            customerListInfo.put("bcAge", customerListDto.getBcAge());
            customerListInfo.put("bcGrade", customerListDto.getBcGrade());
            customerListInfo.put("bcValuation", customerListDto.getBcValuation());
            customerListInfo.put("bcMessageAgree", customerListDto.getBcMessageAgree());
            customerListInfo.put("bcAgreeType", customerListDto.getBcAgreeType());
            customerListInfo.put("bcSignImage", customerListDto.getBcSignImage());
            customerListInfo.put("bcRemark", customerListDto.getBcRemark());
            customerListInfo.put("bcQuitYn", customerListDto.getBcQuitYn());
            customerListInfo.put("bcQuitDate", customerListDto.getBcQuitDate());
            customerListInfo.put("insertDateTime", customerListDto.getInsertDateTime());

//            if(branch.getBrContractState().equals("01")){
//                branchsetInfo.put("brContractStateValue","진행중");
//            }else{
//                branchsetInfo.put("brContractStateValue","계약완료");
//            }

            customerListData.add(customerListInfo);

        }

        log.info("가맹점코드 : "+frCode+"의 고객 리스트 : "+customerListData);
        data.put("gridListData",customerListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 현재 로그인한 가맹점 정보 가져오기
    @GetMapping("franchiseInfo")
    public ResponseEntity<Map<String,Object>> franchiseInfo(HttpServletRequest request){
        log.info("franchiseInfo 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        UserIndexDto userIndexDto = userService.findByUserInfo(login_id, frCode);
//        log.info("userIndexDto : "+userIndexDto);

        data.put("userIndexDto",userIndexDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 접수페이지 진입시 기본적으롤 받는 데이터 API (대분류 목록리스트)
    @GetMapping("itemGroupAndPriceList")
    public ResponseEntity<Map<String,Object>> itemgroupList(HttpServletRequest request){
        log.info("itemgroupList 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
//        log.info("소속된 지사 코드 : "+frbrCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        FranchisInfoDto franchisInfoDto = headService.findByFranchiseInfo(frCode);
        Long frEstimateDuration = Long.parseLong(String.valueOf(franchisInfoDto.getFrEstimateDuration()+1));

        // 현재 가맹점의 대분류 리스트 가져오기 + 가맹점이 등록한 대분류 순서 테이블 leftjoin
        List<UserItemGroupSortDto> userItemGroupSortData = headService.findByUserItemGroupSortDtoList(frCode);
        log.info("userItemGroupSortData : "+userItemGroupSortData);
        log.info("userItemGroupSortData 사이즈 : "+userItemGroupSortData.size());
        data.put("userItemGroupSortData",userItemGroupSortData);


        // 현재 날짜 받아오기
        LocalDateTime  localDateTime = LocalDateTime.now();
        String nowDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("현재 날짜 yyyymmdd : "+nowDate);
        // 현재 가맹점의 가격 리스트 가져오기 + 가맹점이 등록한 상품 순서 테이블 leftjoin
        List<UserItemPriceSortDto> userItemPriceSortData = headService.findByUserItemPriceSortList(frCode, nowDate);
        log.info("userItemPriceSortData : "+userItemPriceSortData);
        log.info("userItemPriceSortData 사이즈 : "+userItemPriceSortData.size());
        data.put("userItemPriceSortData",userItemPriceSortData);


        // 중분류 리스트 가져오기
        List<UserItemGroupSListDto> userItemGroupSListData = headService.findByUserItemGroupSList();
        log.info("userItemGroupSListData : "+userItemGroupSListData);
        log.info("userItemGroupSListData 사이즈 : "+userItemGroupSListData.size());
        data.put("userItemGroupSListData",userItemGroupSListData);


        // 가격셋팅 테이블 리스트 ex) 고급할인율, 명품할인율 등..
        AddCostDto addCostDto = headService.findByAddCost();
        log.info("addCostDto : "+addCostDto);
        data.put("addCostData",addCostDto);


        // 수선 항목 리스트 데이터 가져오기
        List<AddprocessDto> repairListData = headService.findByAddProcess(frCode, "1");
        log.info("repairListData : "+repairListData);
        log.info("repairListData 사이즈 : "+repairListData.size());
        data.put("repairListData",repairListData);


        // 추가요금 항목 리스트 데이터 가져오기
        List<AddprocessDto> addAmountData = headService.findByAddProcess(frCode, "2");
        log.info("addAmountData : "+addAmountData);
        log.info("addAmountData 사이즈 : "+addAmountData.size());
        data.put("addAmountData",addAmountData);


        Optional<BranchCalendar> optionalBranchCalendar = managerService.branchCalendarInfo(franchisInfoDto.getBrCode(), nowDate);
        if(optionalBranchCalendar.isPresent()){
            // 태그번호, 출고예정일 데이터
            List<EtcDataDto> etcData = userService.findByEtc(frEstimateDuration, frCode, nowDate);
            log.info("etcData : "+etcData.get(franchisInfoDto.getFrEstimateDuration()));
            data.put("etcData",etcData.get(franchisInfoDto.getFrEstimateDuration()));
        }else{
            Calendar cal= Calendar.getInstance();
            cal.add(Calendar.DATE, 3); // 3일 후
            Date currentTime=cal.getTime();
            SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
            String resultDate =formatter.format(currentTime);

            log.info("지사 휴무일 데이터가 존재하지 않음 -> 3일 후 출고예정일 날짜 : "+resultDate);
            EtcDataDto etcData = new EtcDataDto();
            etcData.setFdTag(franchisInfoDto.getFrLastTagno());
            etcData.setFrEstimateDate(resultDate);
            log.info("etcData : "+etcData);
            data.put("etcData",etcData);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    // 접수페이지 가맹점 세탁접수 API
    @PostMapping("requestSave")
    public ResponseEntity<Map<String,Object>> requestSave(@RequestBody RequestDetailSet requestDetailSet, HttpServletRequest request){
        log.info("requestSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 현재 날짜 받아오기
        LocalDateTime  localDateTime = LocalDateTime.now();
        String nowDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("현재 날짜 yyyymmdd : "+nowDate);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드

        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        RequestMapperDto etcData = requestDetailSet.getEtc(); // etc 데이터 얻기

        ArrayList<RequestDetailDto> addList = requestDetailSet.getAdd(); // 추가 리스트 얻기
        ArrayList<RequestDetailDto> updateList = requestDetailSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<RequestDetailDto> deleteList = requestDetailSet.getDelete(); // 제거 리스트 얻기

        log.info("ECT 데이터 : "+etcData);
        log.info("추가 리스트 : "+addList);
        log.info("수정 리스트 : "+updateList);
        log.info("삭제 리스트 : "+deleteList);
        log.info("추가 사이즈 : "+addList.size());
        log.info("수정 사이즈 : "+updateList.size());
        log.info("삭제 사이즈 : "+deleteList.size());

        // 현재 고객을 받아오기
        Optional<Customer> optionalCustomer = userService.findByBcHp(etcData.getBcHp());
        if(!optionalCustomer.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP018.getCode(), ResponseErrorCode.TP018.getDesc(), "문자", "고객번호 : "+etcData.getBcHp()));
        }else{

            Request requestSave;
            if(etcData.getFrNo() != null){
                log.info("접수마스터 테이블 수정합니다. 접수코드 : "+etcData.getFrNo());
                Optional<Request> optionalRequest = userService.findByRequest(etcData.getFrNo());
                if(!optionalRequest.isPresent()){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "접수 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "접수코드 : "+etcData.getFrNo()));
                }else{
                    optionalRequest.get().setFrTotalAmount(etcData.getFrTotalAmount());
                    optionalRequest.get().setFrDiscountAmount(etcData.getFrDiscountAmount());
                    optionalRequest.get().setFrNormalAmount(etcData.getFrNormalAmount());
                    optionalRequest.get().setFrQty(addList.size()+updateList.size());
                    optionalRequest.get().setFrYyyymmdd(nowDate);
                    optionalRequest.get().setModity_id(login_id);
                    optionalRequest.get().setModity_date(LocalDateTime.now());
                    requestSave = optionalRequest.get();
                }
            }else{
                log.info("접수마스터 테이블 신규 저장합니다.");

                requestSave = modelMapper.map(etcData, Request.class);

                requestSave.setBcId(optionalCustomer.get());
                requestSave.setBcCode(frbrCode);
                requestSave.setFrCode(frCode);
                requestSave.setFrYyyymmdd(nowDate);

                requestSave.setFrQty(addList.size()+updateList.size());
                requestSave.setFrRefBoxCode(null); // 무인보관함 연계시 무인보관함 접수번호 : 일단 무조건 NULL
                requestSave.setFr_insert_id(login_id);
                requestSave.setFr_insert_date(LocalDateTime.now());

                log.info("접수마스터 테이블 저장 or 수정 : "+requestSave);
            }

            log.info("etcData.getCheckNum() : "+etcData.getCheckNum());
            if(etcData.getCheckNum().equals("1")){
                requestSave.setFrUncollectYn("Y");
                requestSave.setFrConfirmYn("N");
            }else{
                requestSave.setFrUncollectYn("N");
                requestSave.setFrConfirmYn("N");
            }

            String lastTagNo = null; // 마지막 태그번호
            List<RequestDetail> requestDetailList = new ArrayList<>(); // 세부테이블 객체 리스트
            // 접수 세부 테이블 저장
            if(addList.size()!=0){
                for (RequestDetailDto requestDetailDto : addList) {
                    log.info("RequestDetailDto : "+requestDetailDto);
                    RequestDetail requestDetail = modelMapper.map(requestDetailDto, RequestDetail.class);

                    requestDetail.setBiItemcode(requestDetailDto.getBiItemcode());
                    requestDetail.setFdState("S1");
                    requestDetail.setFdStateDt(LocalDateTime.now());
                    requestDetail.setFdCancel("N");
                    requestDetail.setFdTotAmt(requestDetailDto.getFdRequestAmt());
                    requestDetail.setFdEstimateDt(requestDetailDto.getFrEstimateDate());
                    requestDetail.setInsert_id(login_id);
                    requestDetail.setInsert_date(LocalDateTime.now());
                    lastTagNo = requestDetailDto.getFdTag();
                    requestDetailList.add(requestDetail);
                }
            }
            // 접수 세부 테이블 업데이트
            if(updateList.size()!=0){
                for (RequestDetailDto requestDetailDto : updateList) {
                    log.info("수정로직 FrNo : "+etcData.getFrNo());
                    log.info("수정로직 FdTag : "+requestDetailDto.getFdTag());
                    Optional<RequestDetail> optionalRequestDetail = userService.findByRequestDetail(etcData.getFrNo(), requestDetailDto.getFdTag());
                    if(!optionalRequestDetail.isPresent()){
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "택번호 : "+requestDetailDto.getFdTag()));
                    }else{
                        optionalRequestDetail.get().setBiItemcode(requestDetailDto.getBiItemcode());
                        optionalRequestDetail.get().setFdColor(requestDetailDto.getFdColor());
                        optionalRequestDetail.get().setFdPattern(requestDetailDto.getFdPattern());
                        optionalRequestDetail.get().setFdPriceGrade(requestDetailDto.getFdPriceGrade());

                        optionalRequestDetail.get().setFdOriginAmt(requestDetailDto.getFdOriginAmt());
                        optionalRequestDetail.get().setFdNormalAmt(requestDetailDto.getFdNormalAmt());
                        optionalRequestDetail.get().setFdRepairRemark(requestDetailDto.getFdRepairRemark());
                        optionalRequestDetail.get().setFdRepairAmt(requestDetailDto.getFdRepairAmt());

                        optionalRequestDetail.get().setFdAdd1Remark(requestDetailDto.getFdAdd1Remark());
                        optionalRequestDetail.get().setFdAdd1SpecialYn(requestDetailDto.getFdAdd1SpecialYn());
                        optionalRequestDetail.get().setFdAdd1Amt(requestDetailDto.getFdAdd1Amt());

                        optionalRequestDetail.get().setFdPressed(requestDetailDto.getFdPressed());
                        optionalRequestDetail.get().setFdWhitening(requestDetailDto.getFdWhitening());
                        optionalRequestDetail.get().setFdPollution(requestDetailDto.getFdPollution());
                        optionalRequestDetail.get().setFdPollutionLevel(requestDetailDto.getFdPollutionLevel());
                        optionalRequestDetail.get().setFdStarch(requestDetailDto.getFdStarch());
                        optionalRequestDetail.get().setFdWaterRepellent(requestDetailDto.getFdWaterRepellent());

                        optionalRequestDetail.get().setFdDiscountGrade(requestDetailDto.getFdDiscountGrade());
                        optionalRequestDetail.get().setFdDiscountAmt(requestDetailDto.getFdDiscountAmt());
                        optionalRequestDetail.get().setFdQty(requestDetailDto.getFdQty());

                        optionalRequestDetail.get().setFdRequestAmt(requestDetailDto.getFdRequestAmt());
                        optionalRequestDetail.get().setFdRetryYn(requestDetailDto.getFdRetryYn());

                        optionalRequestDetail.get().setFdRemark(requestDetailDto.getFdRemark());
                        optionalRequestDetail.get().setFdEstimateDt(requestDetailDto.getFrEstimateDate());

                        optionalRequestDetail.get().setModity_id(login_id);
                        optionalRequestDetail.get().setModity_date(LocalDateTime.now());
//                        RequestDetail requestDetail = optionalRequestDetail.get();
                        requestDetailList.add(optionalRequestDetail.get());
                    }
                }
            }
            log.info("requestDetailList : "+requestDetailList);

            // 현재 접수한 고객의 대한 마지막방문일자 업데이트
            optionalCustomer.get().setBcLastRequestDt(nowDate);
            Customer customer = optionalCustomer.get();

            Request requestSaveO = userService.requestAndDetailSave(requestSave, requestDetailList, customer);
            data.put("frNo",requestSaveO.getFrNo());

            // 모두 저장되면 최종 택번호 업데이트
            Optional<Franchise> optionalFranchise = headService.findByFrCode(frCode); // 가맹점
            log.info("마지막 택번호 : "+lastTagNo);
            if(optionalFranchise.isPresent()){
                if(addList.size()==0){
                    lastTagNo = optionalFranchise.get().getFrLastTagno();
                }
                optionalFranchise.get().setFrLastTagno(lastTagNo);

                headService.franchiseSave(optionalFranchise.get());
                log.info(optionalFranchise.get().getFrName()+" 가맹점 택번호 업데이트 완료 : "+lastTagNo);
            }

        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 접수페이지 임시저장 내역 리스트 호출 APi
    @GetMapping("tempRequestList")
    public ResponseEntity<Map<String,Object>> tempRequestList(HttpServletRequest request){
        log.info("tempRequestList 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> requestListData = new ArrayList<>();
        HashMap<String,Object> requestListInfo;

        List<RequestListDto> requestListDtos = userService.findByRequestTempList(frCode);
        for (RequestListDto requestListDto: requestListDtos) {

            requestListInfo = new HashMap<>();

            requestListInfo.put("frNo", requestListDto.getFrNo());
            requestListInfo.put("frInsertDate", requestListDto.getFrInsertDate());
            requestListInfo.put("bcName", requestListDto.getBcName());
            requestListInfo.put("bcHp", requestListDto.getBcHp());

            requestListData.add(requestListInfo);
        }

        data.put("gridListData",requestListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 접수페이지 임시저장 세부내역 리스트 호출 APi
    @GetMapping("tempRequestDetailList")
    public ResponseEntity<Map<String,Object>> tempRequestDetailList(@RequestParam(value="frNo", defaultValue="") String frNo){
        log.info("tempRequestDetailList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접수했던 고객의 정보 호출
        Optional<Request> optionalRequest = userService.findByRequest(frNo);
        if(!optionalRequest.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "접수"+ResponseErrorCode.TP009.getDesc(), "문자", "접수 : "+frNo));
        }else{

            // 다시 고객정보 호출해준다.
            CustomerInfoDto customerInfoDto = new CustomerInfoDto();
            customerInfoDto.setBcId(optionalRequest.get().getId());
            customerInfoDto.setBcHp(optionalRequest.get().getBcId().getBcHp());
            customerInfoDto.setBcName(optionalRequest.get().getBcId().getBcName());
            customerInfoDto.setBcAddress(optionalRequest.get().getBcId().getBcAddress());
            customerInfoDto.setBcGrade(optionalRequest.get().getBcId().getBcGrade());
            customerInfoDto.setBcValuation(optionalRequest.get().getBcId().getBcValuation());
            customerInfoDto.setBcRemark(optionalRequest.get().getBcId().getBcRemark());
            if(optionalRequest.get().getBcId().getBcLastRequestDt() != null){
                String bcLastRequsetDt = optionalRequest.get().getBcId().getBcLastRequestDt();
                StringBuilder getBcLastRequsetDt = new StringBuilder(bcLastRequsetDt);
                getBcLastRequsetDt.insert(4,'-');
                getBcLastRequsetDt.insert(7,'-');
                customerInfoDto.setBcLastRequestDt(getBcLastRequsetDt.toString());
            }
            data.put("gridListData",customerInfoDto);

            // 임시저장의 접수 세무테이블 리스트 호출
            List<RequestDetailDto> requestDetailList = userService.findByRequestTempDetailList(frNo);
            data.put("requestDetailList",requestDetailList);
            data.put("gridListData",customerInfoDto);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }







}
