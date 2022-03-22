package com.broadwave.toppos.User;

import com.broadwave.toppos.Account.AccountPasswordDto;
import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseInfoDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseUserDto;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Head.HeadService.NoticeService;
import com.broadwave.toppos.Head.Item.Group.A.UserItemGroupSortDto;
import com.broadwave.toppos.Head.Item.Group.B.UserItemGroupSListDto;
import com.broadwave.toppos.Head.Item.ItemDtos.UserItemPriceSortDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendar;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarListDto;
import com.broadwave.toppos.Manager.ManagerService.CalendarService;
import com.broadwave.toppos.Manager.ManagerService.TagGalleryService;
import com.broadwave.toppos.Manager.ManagerService.TagNoticeService;
import com.broadwave.toppos.User.Addprocess.AddprocessDtos.AddprocessDto;
import com.broadwave.toppos.User.Addprocess.AddprocessSet;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerInfoDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerListDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerMapperDto;
import com.broadwave.toppos.User.GroupSort.GroupSortSet;
import com.broadwave.toppos.User.ItemSort.ItemSortSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentDtos.PaymentUncollectSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.PaymentSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMainListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMapperDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.PhotoDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailUpdateDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailSet;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestHistoryListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestTempDto;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoney;
import com.broadwave.toppos.User.UserDtos.EtcDataDto;
import com.broadwave.toppos.User.UserDtos.UserIndexDto;
import com.broadwave.toppos.User.UserService.*;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

    @Value("${toppos.aws.s3.bucket.url}")
    private String AWSBUCKETURL;

    private final AWSS3Service awss3Service; // AWS S3 서비스
    private final UserService userService; // 가맹점 통합 서비스
    private final ReceiptService receiptService; // 가맹점 접수페이지 전용 서비스
    private final ReceiptStateService receiptStateService; // 가맹점 접수건 현재상태 변화 서비스
    private final SortService sortService; // 가맹점 상품정렬 서비스
    private final InfoService infoService; // 나의정보관리 서비스
    private final InspectService inspectService; // 통합조회 서비스
    private final UncollectService uncollectService; // 미수관리 서비스
    private final BusinessdayService businessdayService; // 일일영업일보 서비스
    private final TagNoticeService tagNoticeService; // 택분실 게시판 서비스
    private final TagGalleryService tagGalleryService; // NEW 택분실게시판 서비스
    private final NoticeService noticeService; // 공지사항 게시판 서비스
    private final CalendarService calendarService; // 휴무일 서비스

    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;
    private final HeadService headService;

    @Autowired
    public UserRestController(AWSS3Service awss3Service, UserService userService, ReceiptService receiptService, SortService sortService, InfoService infoService, InspectService inspectService,
                              TokenProvider tokenProvider, ModelMapper modelMapper, HeadService headService, CalendarService calendarService, ReceiptStateService receiptStateService,
                              UncollectService uncollectService, BusinessdayService businessdayService, TagNoticeService tagNoticeService, TagGalleryService tagGalleryService, NoticeService noticeService) {
        this.awss3Service = awss3Service;
        this.userService = userService;
        this.receiptService = receiptService;
        this.sortService = sortService;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.infoService = infoService;
        this.inspectService = inspectService;
        this.receiptStateService = receiptStateService;
        this.headService = headService;
        this.uncollectService = uncollectService;
        this.calendarService = calendarService;
        this.businessdayService = businessdayService;
        this.tagNoticeService = tagNoticeService;
        this.tagGalleryService = tagGalleryService;
        this.noticeService = noticeService;
    }

//@@@@@@@@@@@@@@@@@@@@@ 가맹점 메인화면 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // 현재 로그인한 가맹점 정보 가져오기
        @GetMapping("franchiseInfo")
        @ApiOperation(value = "가맹정 점보조회" , notes = "현재 로그인한 가맹점정보를 가져온다.")
        @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
        public ResponseEntity<Map<String,Object>> franchiseInfo(@RequestParam(value="date", defaultValue="") String date, HttpServletRequest request){
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

//        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.info("조회날짜 "+date);

            UserIndexDto userIndexDto = userService.findByUserInfo(login_id, frCode);
            List<BranchCalendarListDto> branchCalendarListDtos = calendarService.branchCalendarSlidingDtoList(frbrCode, date);
            List<InspeotMainListDto> inspeotMainListDtos = inspectService.findByInspeotB1(frbrCode, 3, frCode);

            List<String> calendar = new ArrayList<>();
            if(branchCalendarListDtos.size()!=0){
                for(BranchCalendarListDto branchCalendarListDto : branchCalendarListDtos){
                    if(branchCalendarListDto.getBcDayoffYn().equals("Y")){
                        int year = Integer.parseInt(branchCalendarListDto.getBcDate().substring(0,4));
                        int month = Integer.parseInt(branchCalendarListDto.getBcDate().substring(4,6));
                        int day = Integer.parseInt(branchCalendarListDto.getBcDate().substring(6,8));
                        LocalDate weekDate = LocalDate.of(year, month, day);
                        DayOfWeek dayOfWeek = weekDate.getDayOfWeek();
                        int dayOfWeekNumber = dayOfWeek.getValue();
                        if(dayOfWeekNumber != 7){
                            calendar.add(branchCalendarListDto.getBcDate());
                        }
                    }
                }
            }

            List<HashMap<String,Object>> userIndexData = new ArrayList<>();
            HashMap<String,Object> userIndexInfo;
            if(userIndexDto != null){
                userIndexInfo = new HashMap<>();
                userIndexInfo.put("username", userIndexDto.getUsername());
                userIndexInfo.put("usertel", userIndexDto.getUsertel());
                userIndexInfo.put("brName", userIndexDto.getBrName());
                userIndexInfo.put("frName", userIndexDto.getFrName());
                userIndexInfo.put("slidingText", calendar);
                userIndexData.add(userIndexInfo);
            }

            List<RequestHistoryListDto> requestHistoryListDtos = receiptService.findByRequestHistory(frCode, date);

            data.put("userIndexDto",userIndexData);
            data.put("requestHistoryList",requestHistoryListDtos);
            data.put("inspeotList",inspeotMainListDtos);

            return ResponseEntity.ok(res.dataSendSuccess(data));
        }



//@@@@@@@@@@@@@@@@@@@@@ 가맹점 고객조회 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 고객 등록 API
    @PostMapping("customerSave")
    @ApiOperation(value = "고객등록 API" , notes = "현재 로그인한 가맹점의 고객을 등록한다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
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
                customer.setBcId(optionalCustomerById.get().getBcId());

                // 수정일때
                customer.setFrCode(optionalCustomerById.get().getFrCode());
                customer.setBcMessageAgreeDt(LocalDateTime.now());
                if(customer.getBcQuitYn().equals("Y")){
                    customer.setBcQuitDate(LocalDateTime.now());
                }
                customer.setBcSignImage(optionalCustomerById.get().getBcSignImage());
                customer.setBcLastRequestDt(optionalCustomerById.get().getBcLastRequestDt());
                customer.setInsert_id(optionalCustomerById.get().getInsert_id());
                customer.setInsertDateTime(optionalCustomerById.get().getInsertDateTime());
                customer.setModify_id(login_id);
                customer.setModifyDateTime(LocalDateTime.now());

                Customer customerSave =  userService.customerSave(customer);
                log.info("고객 수정 성공 : 고객명 '" + customerSave.getBcName() +"'");
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
    @ApiOperation(value = "고객 정보 호출 API" , notes = "고객의 대한 정보를 가져온다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> customerInfo(HttpServletRequest request,
                                                           @RequestParam(value="searchType", defaultValue="") String searchType,
                                                           @RequestParam(value="searchString", defaultValue="") String searchString){
        log.info("customerInfo 호출");
//        log.info("searchType :"+searchType);
//        log.info("searchString :"+searchString);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<Long> customerIdList = new ArrayList<>();
        List<HashMap<String,Object>> customerListData = new ArrayList<>();
        HashMap<String,Object> customerListInfo;

        List<CustomerInfoDto> customerInfoListDto = userService.findByCustomerInfo(frCode, searchType, searchString);
        log.info("customerInfoListDto : "+customerInfoListDto);

        // 임시저장한 내역이 존재하는지
        RequestTempDto requestTemp = receiptService.findByRequestTemp(frCode);
        log.info("requestTemp : "+requestTemp);
        for (CustomerInfoDto customerInfoDto: customerInfoListDto) {
            customerListInfo = new HashMap<>();
            customerIdList.add(customerInfoDto.getBcId());
            // 개인정보
            customerListInfo.put("bcId", customerInfoDto.getBcId());
            customerListInfo.put("bcName", customerInfoDto.getBcName());
            customerListInfo.put("bcHp", customerInfoDto.getBcHp());
            customerListInfo.put("bcGrade", customerInfoDto.getBcGrade());
            customerListInfo.put("bcValuation", customerInfoDto.getBcValuation());
            customerListInfo.put("bcRemark", customerInfoDto.getBcRemark());
            customerListInfo.put("bcAddress", customerInfoDto.getBcAddress());
            customerListInfo.put("bcLastRequestDt", customerInfoDto.getBcLastRequestDt());
            customerListInfo.put("beforeUncollectMoney", 0);
            customerListInfo.put("saveMoney", 0);
            if(requestTemp != null){
                customerListInfo.put("tempSaveFrNo", requestTemp.getFrNo());
            }else{
                customerListInfo.put("tempSaveFrNo", null);
            }
            customerListData.add(customerListInfo);
        }

        if(customerListData.size() != 0) {
            List<HashMap<String,Object>> customerListDataGet = receiptService.findByUnCollectAndSaveMoney(customerListData, customerIdList, "2");
            data.put("gridListData",customerListDataGet);
        }else{
            data.put("gridListData",customerListData);
        }


        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 고객 리스트 API(현재 로그인한 가맹점의 대한 고객리스트만 호출한다.)
    @GetMapping("customerList")
    @ApiOperation(value = "고객 리스트 API" , notes = "현재 로그인한 가맹점의 고객리스트를 가져온다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
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

        List<Long> customerIdList = new ArrayList<>();
        List<HashMap<String,Object>> customerListData = new ArrayList<>();
        HashMap<String,Object> customerListInfo;

        List<CustomerListDto> customerListDtos = userService.findByCustomerList(frCode, searchType, searchString);
//        log.info("customerListDtos : "+customerListDtos);
        for (CustomerListDto customerListDto: customerListDtos) {

            customerListInfo = new HashMap<>();

            customerIdList.add(customerListDto.getBcId());

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
            customerListInfo.put("beforeUncollectMoney", 0);
            customerListInfo.put("saveMoney", 0);
            customerListInfo.put("insertDateTime", customerListDto.getInsertDateTime());

            customerListData.add(customerListInfo);
        }

        if(customerListData.size() != 0) {
            List<HashMap<String,Object>> customerListDataGet = receiptService.findByUnCollectAndSaveMoney(customerListData, customerIdList,"2");
            data.put("gridListData",customerListDataGet);
        }else{
            data.put("gridListData",customerListData);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 고객 적립금조정 API(현재 로그인한 가맹점의 대한 고객리스트만 호출한다.)
    @PostMapping("customerSaveMoneyControl")
    @ApiOperation(value = "고객 적립금조정 API" , notes = "고객의 적립금을 조정한다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> customerSaveMoneyControl(@RequestParam(value="bcId", defaultValue="") Long bcId,
                                                                       @RequestParam(value="controlMoney", defaultValue="") Integer controlMoney, HttpServletRequest request){
        log.info("customerSaveMoneyControl 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<Customer> optionalCustomer = userService.findByBcId(bcId);
        if(!optionalCustomer.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "고객 "+ResponseErrorCode.TP022.getDesc(), "문자", "재조회후 입력해주시길 바랍니다."));
        }else{
            Integer saveMoney = receiptService.findBySaveMoney(optionalCustomer.get());
//            log.info("현재 고객님의 적립금 : "+saveMoney);
//            log.info("조정할 고객님의 적립금 : "+controlMoney);
            data.put("saveMoney",saveMoney+controlMoney);

            SaveMoney saveMoneySave = new SaveMoney();
            saveMoneySave.setBcId(optionalCustomer.get());
            saveMoneySave.setFsYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            saveMoneySave.setFsType("1");
            saveMoneySave.setFsClose("N");
            saveMoneySave.setFsAmt(controlMoney);
            saveMoneySave.setInsert_id(login_id);
            saveMoneySave.setInsert_date(LocalDateTime.now());
            userService.saveMoneySave(saveMoneySave);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }



//@@@@@@@@@@@@@@@@@@@@@ 가맹점 세탁접수 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 택번호 변경시 비밀번호 입력후 확인 API
    @GetMapping("franchiseCheck")
    @ApiOperation(value = "가맹정 택번호 변경" , notes = "비밀번호를 가져와 확인한다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> franchiseCheck(@RequestParam(value="password", defaultValue="") String password, HttpServletRequest request){
        return infoService.franchiseCheck(password, request);
    }

    // 접수페이지 진입시 기본적으롤 받는 데이터 API (대분류 목록리스트)
    @GetMapping("itemGroupAndPriceList")
    @ApiOperation(value = "접수페이지 진입시 받아오는 API" , notes = "기본 데이터를 받는다.")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> itemGroupAndPriceList(HttpServletRequest request){
        log.info("itemGroupAndPriceList 호출");

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        log.info("금일날짜 : "+nowDate);

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

        FranchiseInfoDto franchiseInfoDto = headService.findByFranchiseInfo(frCode);
        Long frEstimateDuration = Long.parseLong(String.valueOf(franchiseInfoDto.getFrEstimateDuration()+1));

        // 현재 가맹점의 대분류 리스트 가져오기 + 가맹점이 등록한 대분류 순서 테이블 leftjoin
        List<UserItemGroupSortDto> userItemGroupSortData = headService.findByUserItemGroupSortDtoList(frCode);
        log.info("userItemGroupSortData : "+userItemGroupSortData);
        log.info("userItemGroupSortData 사이즈 : "+userItemGroupSortData.size());
        data.put("userItemGroupSortData",userItemGroupSortData);

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
        List<AddprocessDto> repairListData = userService.findByAddProcessDtoList(frCode, "1");
        log.info("repairListData : "+repairListData);
        log.info("repairListData 사이즈 : "+repairListData.size());
        data.put("repairListData",repairListData);


        // 추가요금 항목 리스트 데이터 가져오기
        List<AddprocessDto> addAmountData = userService.findByAddProcessDtoList(frCode, "2");
        log.info("addAmountData : "+addAmountData);
        log.info("addAmountData 사이즈 : "+addAmountData.size());
        data.put("addAmountData",addAmountData);


        Optional<BranchCalendar> optionalBranchCalendar = calendarService.branchCalendarInfo(franchiseInfoDto.getBrCode(), nowDate);
        if(optionalBranchCalendar.isPresent()){
            // 태그번호, 출고예정일 데이터
            List<EtcDataDto> etcData = receiptService.findByEtc(frEstimateDuration, frCode, nowDate);
            log.info("etcData : "+etcData.get(franchiseInfoDto.getFrEstimateDuration()));
            data.put("etcData",etcData.get(franchiseInfoDto.getFrEstimateDuration()));
        }else{
            Calendar cal= Calendar.getInstance();
            cal.add(Calendar.DATE, 3); // 3일 후
            Date currentTime=cal.getTime();
            SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMdd");
            String resultDate =formatter.format(currentTime);

            log.info("지사 휴무일 데이터가 존재하지 않음 -> 3일 후 출고예정일 날짜 : "+resultDate);
            EtcDataDto etcData = new EtcDataDto();
            etcData.setFrCode(franchiseInfoDto.getFrCode());
            etcData.setFrName(franchiseInfoDto.getFrName());
            etcData.setFdTag(franchiseInfoDto.getFrLastTagno());
            etcData.setFrEstimateDate(resultDate);
            etcData.setFrBusinessNo(franchiseInfoDto.getFrBusinessNo());
            etcData.setFrRpreName(franchiseInfoDto.getFrBusinessNo());
            etcData.setFrTelNo(franchiseInfoDto.getFrBusinessNo());
            etcData.setFdTag(franchiseInfoDto.getFrLastTagno());
            etcData.setFrMultiscreenYn(franchiseInfoDto.getFrMultiscreenYn());

            log.info("etcData : "+etcData);
            data.put("etcData",etcData);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 접수페이지 가맹점 세탁접수 API
    @PostMapping("requestSave")
    public ResponseEntity<Map<String,Object>> requestSave(@RequestBody RequestDetailSet requestDetailSet, HttpServletRequest request){
        return receiptService.requestSave(requestDetailSet, request);
    }

    // 접수페이지 접수완료시 카톡메세지
    @PostMapping("requestReceiptMessage")
    @ApiOperation(value = "가맹점 접수완료 메세지" , notes = "세탁접수를 완료하면 메세지보낼 테이블에 Insert 한다")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> requestReceiptMessage(HttpServletRequest request, @RequestParam(value="frNo", defaultValue="") String frNo,
                                                                    @RequestParam(value="locationHost", defaultValue="") String locationHost) {
        return receiptService.requestReceiptMessage(frNo, locationHost,request);
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

        List<RequestListDto> requestListDtos = receiptService.findByRequestTempList(frCode);
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
    public ResponseEntity<Map<String,Object>> tempRequestDetailList(HttpServletRequest request, @RequestParam(value="frNo", defaultValue="") String frNo){
        log.info("tempRequestDetailList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<Long> customerIdList = new ArrayList<>();
        List<HashMap<String,Object>> customerListData = new ArrayList<>();
        HashMap<String,Object> customerListInfo;

        List<HashMap<String,Object>> requestDetailListData = new ArrayList<>();
        HashMap<String,Object> requestDetailInfo;

        // 접수했던 고객의 정보 호출
        Optional<Request> optionalRequest = receiptService.findByRequest(frNo, "N", frCode);
        if(!optionalRequest.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "접수"+ResponseErrorCode.TP009.getDesc(), "문자", "접수 : "+frNo));
        }else{
            // 다시 고객정보 호출해준다.
            customerListInfo = new HashMap<>();
            customerIdList.add(optionalRequest.get().getBcId().getBcId());
            customerListInfo.put("bcId", optionalRequest.get().getBcId().getBcId());
            customerListInfo.put("bcName", optionalRequest.get().getBcId().getBcName());
            customerListInfo.put("bcHp", optionalRequest.get().getBcId().getBcHp());
            customerListInfo.put("bcAddress", optionalRequest.get().getBcId().getBcAddress());
            customerListInfo.put("bcGrade", optionalRequest.get().getBcId().getBcGrade());
            customerListInfo.put("bcValuation", optionalRequest.get().getBcId().getBcValuation());
            customerListInfo.put("bcRemark", optionalRequest.get().getBcId().getBcRemark());
            customerListInfo.put("bcLastRequestDt", optionalRequest.get().getBcId().getBcLastRequestDt());
            customerListInfo.put("beforeUncollectMoney", 0);
            customerListInfo.put("saveMoney", 0);
            customerListData.add(customerListInfo);

            List<HashMap<String,Object>> customerListDataGet = receiptService.findByUnCollectAndSaveMoney(customerListData, customerIdList,"2");
            data.put("gridListData",customerListDataGet);

            // 임시저장의 접수 세무테이블 리스트 호출
            List<RequestDetailDto> requestDetailList = receiptService.findByRequestTempDetailList(frNo);
            for(RequestDetailDto requestDetailDto : requestDetailList){
                requestDetailInfo = new HashMap<>();

                requestDetailInfo.put("biItemcode", requestDetailDto.getBiItemcode());
                requestDetailInfo.put("fdTag", requestDetailDto.getFdTag());
                requestDetailInfo.put("fdColor", requestDetailDto.getFdColor());
                requestDetailInfo.put("fdPattern", requestDetailDto.getFdPattern());
                requestDetailInfo.put("fdPriceGrade", requestDetailDto.getFdPriceGrade());

                requestDetailInfo.put("fdOriginAmt", requestDetailDto.getFdOriginAmt());
                requestDetailInfo.put("fdNormalAmt", requestDetailDto.getFdNormalAmt());
                requestDetailInfo.put("fdRepairRemark", requestDetailDto.getFdRepairRemark());
                requestDetailInfo.put("fdRepairAmt", requestDetailDto.getFdRepairAmt());

                requestDetailInfo.put("fdAdd1Remark", requestDetailDto.getFdAdd1Remark());
                requestDetailInfo.put("fdSpecialYn", requestDetailDto.getFdSpecialYn());
                requestDetailInfo.put("fdAdd1Amt", requestDetailDto.getFdAdd1Amt());

                requestDetailInfo.put("fdPressed", requestDetailDto.getFdPressed());
                requestDetailInfo.put("fdWhitening", requestDetailDto.getFdWhitening());
                requestDetailInfo.put("fdPollution", requestDetailDto.getFdPollution());
                requestDetailInfo.put("fdPollutionLevel", requestDetailDto.getFdPollutionLevel());

                requestDetailInfo.put("fdPollutionLocFcn", requestDetailDto.getFdPollutionLocFcn());
                requestDetailInfo.put("fdPollutionLocFcs", requestDetailDto.getFdPollutionLocFcs());
                requestDetailInfo.put("fdPollutionLocFcb", requestDetailDto.getFdPollutionLocFcb());
                requestDetailInfo.put("fdPollutionLocFlh", requestDetailDto.getFdPollutionLocFlh());
                requestDetailInfo.put("fdPollutionLocFrh", requestDetailDto.getFdPollutionLocFrh());
                requestDetailInfo.put("fdPollutionLocFlf", requestDetailDto.getFdPollutionLocFlf());
                requestDetailInfo.put("fdPollutionLocFrf", requestDetailDto.getFdPollutionLocFrf());

                requestDetailInfo.put("fdPollutionLocBcn", requestDetailDto.getFdPollutionLocBcn());
                requestDetailInfo.put("fdPollutionLocBcs", requestDetailDto.getFdPollutionLocBcs());
                requestDetailInfo.put("fdPollutionLocBcb", requestDetailDto.getFdPollutionLocBcb());
                requestDetailInfo.put("fdPollutionLocBrh", requestDetailDto.getFdPollutionLocBrh());
                requestDetailInfo.put("fdPollutionLocBlh", requestDetailDto.getFdPollutionLocBlh());
                requestDetailInfo.put("fdPollutionLocBrf", requestDetailDto.getFdPollutionLocBrf());
                requestDetailInfo.put("fdPollutionLocBlf", requestDetailDto.getFdPollutionLocBlf());

                requestDetailInfo.put("fdStarch", requestDetailDto.getFdStarch());
                requestDetailInfo.put("fdWaterRepellent", requestDetailDto.getFdWaterRepellent());

                requestDetailInfo.put("fdDiscountGrade", requestDetailDto.getFdDiscountGrade());
                requestDetailInfo.put("fdDiscountAmt", requestDetailDto.getFdDiscountAmt());
                requestDetailInfo.put("fdQty", requestDetailDto.getFdQty());

                requestDetailInfo.put("fdRequestAmt", requestDetailDto.getFdRequestAmt());

                requestDetailInfo.put("fdRetryYn", requestDetailDto.getFdRetryYn());
                requestDetailInfo.put("fdUrgentYn", requestDetailDto.getFdUrgentYn());

                requestDetailInfo.put("fdRemark", requestDetailDto.getFdRemark());
                requestDetailInfo.put("frEstimateDate", requestDetailDto.getFrEstimateDate());

                requestDetailInfo.put("bgName", requestDetailDto.getBgName());
                requestDetailInfo.put("bsName", requestDetailDto.getBsName());
                requestDetailInfo.put("biName", requestDetailDto.getBiName());

                requestDetailInfo.put("fdAgreeType", requestDetailDto.getFdAgreeType());
                requestDetailInfo.put("fdSignImage", requestDetailDto.getFdSignImage());

                List<PhotoDto> photoDtoList = receiptService.findByPhotoDto(requestDetailDto.getId());
                requestDetailInfo.put("photoList", photoDtoList);

                requestDetailListData.add(requestDetailInfo);
            }
            data.put("requestDetailList",requestDetailListData);

        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 접수페이지 임시저장의 마스터 테이블+세부 테이블 삭제
    @PostMapping("tempRequestDetailDelete")
    public ResponseEntity<Map<String,Object>> tempRequestDetailDelete(HttpServletRequest request, @RequestParam(value="frNo", defaultValue="") String frNo){
        log.info("tempRequestDetailDelete 호출");
        // 임시저장 글 삭제 서비스 실행
        return receiptService.requestDelete(request, frNo);
    }

    // 접수페이지 가맹점 세탁접수 결제 API
    @PostMapping("requestPayment")
    public ResponseEntity<Map<String,Object>> requestPayment(@RequestBody PaymentSet paymentSet, HttpServletRequest request){
        return receiptService.requestPayment(paymentSet, request);
    }

    // 접수 사진촬영 API
    @PostMapping("takePicture")
    public ResponseEntity<Map<String,Object>> takePicture(MultipartHttpServletRequest multi) throws IOException {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        //파일저장
        Iterator<String> files = multi.getFileNames();
        String uploadFile = files.next();
        MultipartFile mFile = multi.getFile(uploadFile);
//        log.info("mFile : "+mFile);

        assert mFile != null;
        if(!mFile.isEmpty()) {
            // 파일 중복명 처리
            String genId = UUID.randomUUID().toString().replace("-", "");
//            log.info("genId : "+genId);

            // S3에 저장 할 파일주소
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
            String filePath = "/toppos-franchise/" + date.format(new Date());
//            log.info("filePath : "+filePath);
            String storedFileName = genId + ".png";
//            log.info("storedFileName : "+storedFileName);
            String ffFilename = awss3Service.imageFileUpload(mFile, storedFileName, filePath);
            data.put("ffPath",AWSBUCKETURL+filePath+"/");
            data.put("ffFilename",ffFilename);
        }else{
            log.info("사진파일을 못불러왔습니다.");
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 접수페이지 영수증 출력 API
    @GetMapping("requestPaymentPaper")
    public ResponseEntity<Map<String,Object>> requestPaymentPaper(HttpServletRequest request, @RequestParam(value="frNo", defaultValue="") String frNo, @RequestParam(value="frId", defaultValue="") Long frId){
        return receiptService.requestPaymentPaper(request, frNo, frId);
    }



    //@@@@@@@@@@@@@@@@@@@@@ 가맹점 대분류, 상품 정렬관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 현재 가맹점의 대분류 리스트 가져오기
    @GetMapping("franchiseItemGroupList")
    public ResponseEntity<Map<String,Object>> franchiseItemGroupList(HttpServletRequest request){
        log.info("franchiseItemGroupList 호출");
        return sortService.findByGroupSort(request);
    }

    // 현재 가맹점의 대분류 순서 업데이트
    @PostMapping("franchiseItemGroupUpdate")
    public ResponseEntity<Map<String,Object>> franchiseItemGroupUpdate(@RequestBody GroupSortSet groupSortSet, HttpServletRequest request){
        return sortService.findByGroupSortUpdate(groupSortSet, request);
    }

    // 현재 가맹점의 중분류 리스트 가져오기
    @PostMapping("franchiseItemSortList")
    public ResponseEntity<Map<String,Object>> franchiseItemSortList(@RequestParam(value="filterCode", defaultValue="") String filterCode,
                                                                                                               @RequestParam(value="filterName", defaultValue="") String filterName){
        return sortService.franchiseItemSortList(filterCode, filterName);
    }

    // 현재 가맹점의 상품순서 리스트 가져오기
    @PostMapping("franchiseItemList")
    public ResponseEntity<Map<String,Object>> franchiseItemList(@RequestParam(value="bgItemGroupcode", defaultValue="") String bgItemGroupcode,
                                                                    @RequestParam(value="bsItemGroupcodeS", defaultValue="") String bsItemGroupcodeS, HttpServletRequest request){

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        log.info("금일날짜 : "+nowDate);

        String biItemMgroup = bgItemGroupcode+bsItemGroupcodeS;
        return sortService.franchiseItemList(biItemMgroup, request, nowDate);
    }

    // 현재 가맹점의 상품순서 업데이트
    @PostMapping("franchiseItemSortUpdate")
    public ResponseEntity<Map<String,Object>> franchiseItemSortUpdate(@RequestBody ItemSortSet itemSortSet, HttpServletRequest request){
        return sortService.findByItemSortUpdate(itemSortSet, request);
    }

//@@@@@@@@@@@@@@@@@@@@@ 나의 정보관리 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    // 현재 가맹점의 정보 호출하기
    @GetMapping("myInfo")
    public ResponseEntity<Map<String,Object>> myInfo(HttpServletRequest request){
        return infoService.myInfo(request);
    }

    // 가맹점 나의정보 수정 API
    @PostMapping("franchiseMyInfoSave")
    public ResponseEntity<Map<String,Object>> franchiseMyInfoSave(@ModelAttribute FranchiseUserDto franchiseUserDto, HttpServletRequest request){
//        log.info("franchisUserDto : "+franchisUserDto);
        return infoService.franchiseMyInfoSave(franchiseUserDto, request);
    }

    // 가맹점 비밀번호 수정 API
    @PostMapping("franchisePassword")
    public ResponseEntity<Map<String,Object>> franchisePassword(@ModelAttribute AccountPasswordDto accountPasswordDto, HttpServletRequest request){
        return infoService.franchisePassword(accountPasswordDto, request);
    }

    // 현재 가맹점의 각각 상용구,수선항목, 추가항목의 대한 리스트 호출
    @GetMapping("franchiseAddProcessList")
    public ResponseEntity<Map<String,Object>> franchiseAddProcessList(@RequestParam(value="baType", defaultValue="") String baType, HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<AddprocessDto> addprocessDtoList = userService.findByAddProcessDtoList(frCode, baType);

        if(baType.equals("1")){
            data.put("repairListData",addprocessDtoList); // 수선항목
        }else if(baType.equals("2")){
            data.put("addAmountData",addprocessDtoList); // 추가항목
        }else{
            data.put("keyWordData",addprocessDtoList); // 상용구
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 수선항목,추가항목,상용구 - 저장&수정&삭제
    @PostMapping("franchiseAddProcess")
    public ResponseEntity<Map<String,Object>> franchiseAddProcess(@RequestBody AddprocessSet addprocessSet, HttpServletRequest request){
        return infoService.franchiseAddProcess(addprocessSet, request);
    }


//@@@@@@@@@@@@@@@@@@@@@ 가맹점 통합조회 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  통합조회용 - 접수세부 테이블
    @GetMapping("franchiseRequestDetailSearch")
    public ResponseEntity<Map<String,Object>> franchiseRequestDetailSearch(@RequestParam(value="bcId", defaultValue="") Long bcId,
                                                                                                                   @RequestParam(value="searchTag", defaultValue="") String searchTag,
                                                                                                                   @RequestParam(value="filterCondition", defaultValue="") String filterCondition,
                                                                                                                   @RequestParam(value="filterFromDt", defaultValue="") String filterFromDt,
                                                                                                                   @RequestParam(value="filterToDt", defaultValue="") String filterToDt,
                                                                                                                   HttpServletRequest request){
        if(filterFromDt.equals("")){
            filterFromDt = "00000101";
        }
        if(filterToDt.equals("")){
            filterToDt = "99991230";
        }

        return inspectService.franchiseRequestDetailSearch(bcId, searchTag, filterCondition, filterFromDt, filterToDt, request);
    }

    //  통합조회용 - 접수 세부테이블 수정
    @PostMapping("franchiseRequestDetailUpdate")
    public ResponseEntity<Map<String,Object>> franchiseRequestDetailUpdate(@RequestBody RequestDetailUpdateDto requestDetailUpdateDto, HttpServletRequest request){
        return inspectService.franchiseRequestDetailUpdate(requestDetailUpdateDto, request);
    }

    //  통합조회용 - 결제취소 전 결제내역리스트 요청
    @GetMapping("franchiseDetailCencelDataList")
    public ResponseEntity<Map<String,Object>> franchiseDetailCencelDataList(@RequestParam(value="frId", defaultValue="") Long frId, HttpServletRequest request){
        return inspectService.franchiseDetailCencelDataList(frId, request);
    }

    //  통합조회용 - 결제취소/적립금전환 요청
    @PostMapping("franchiseRequestDetailCencel")
    @ApiOperation(value = "통합조회용" , notes = "결제취소/적립금전환 요청")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> franchiseRequestDetailCencel(@RequestParam(value="fpId", defaultValue="") Long fpId,
                                                                                                                @RequestParam(value="type", defaultValue="") String type, HttpServletRequest request){
        return inspectService.franchiseRequestDetailCencel(fpId, type, request);
    }

    // 검품 등록 API -> 지사도 사용
    @PostMapping("franchiseInspectionSave")
    public ResponseEntity<Map<String,Object>> franchiseInspectionSave(@ModelAttribute InspeotMapperDto inspeotMapperDto, MultipartHttpServletRequest multi) throws IOException {
        return inspectService.InspectionSave(inspeotMapperDto, multi, AWSBUCKETURL);
    }

    //  통합조회용 - 등록 검품 삭제
    @PostMapping("franchiseInspectionDelete")
    public ResponseEntity<Map<String,Object>> franchiseInspectionDelete(@RequestBody InspeotSet inspeotSet){
        return inspectService.InspectionDelete(inspeotSet);
    }

    //  통합조회용 - 검품 리스트 요청
    @GetMapping("franchiseInspectionList")
    @ApiOperation(value = "통합조회용" , notes = "검품 리스트 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> franchiseInspectionList(@RequestParam(value="fdId", defaultValue="") Long fdId,
                                                                           @RequestParam(value="type", defaultValue="") String type){
        return inspectService.franchiseInspectionList(fdId, type);
    }

    //  통합조회용 - 접수 취소
    @PostMapping("franchiseReceiptCancel")
    public ResponseEntity<Map<String,Object>> franchiseReceiptCancel(@RequestParam(value="fdId", defaultValue="") Long fdId, HttpServletRequest request){
        return inspectService.franchiseReceiptCancel(fdId, request);
    }

    //  통합조회용 - 인도 취소
    @PostMapping("franchiseLeadCancel")
    public ResponseEntity<Map<String,Object>> franchiseLeadCancel(@RequestParam(value="fdId", defaultValue="") Long fdId, HttpServletRequest request){
        return inspectService.franchiseLeadCancel(fdId, request);
    }

    //  통합조회용 - 검품 고객 수락/거부
    @PostMapping("franchiseInspectionYn")
    @ApiOperation(value = "통합조회용" , notes = "검품 고객 수락/거부를 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> franchiseInspectionYn(@RequestParam(value="fiId", defaultValue="") Long fiId,
                                                                    @RequestParam(value="type", defaultValue="") String type,
                                                                    @RequestParam(value="fiAddAmt", defaultValue="") Integer fiAddAmt,
                                                                    HttpServletRequest request){
        return inspectService.franchiseInspectionYn(fiId, type, fiAddAmt, request);
    }


//@@@@@@@@@@@@@@@@@@@@@ 검품이력 조회 및 메세지 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  검품이력 조회 및 메세지 - 리스트호출 테이블
    @GetMapping("inspectList")
    public ResponseEntity<Map<String,Object>> inspectList(
            @RequestParam(value="bcId", defaultValue="") Long bcId,
            @RequestParam(value="searchTag", defaultValue="") String searchTag,
            @RequestParam(value="filterFromDt", defaultValue="") String filterFromDt,
            @RequestParam(value="filterToDt", defaultValue="") String filterToDt,
            HttpServletRequest request){
        if(filterFromDt.equals("")){
            filterFromDt = "00000101";
        }
        if(filterToDt.equals("")){
            filterToDt = "99991230";
        }

        return inspectService.inspectList(bcId, searchTag, filterFromDt, filterToDt, request);
    }

    //  검품이력 조회 및 메세지 - 카카오 메세지 보내기
    @PostMapping("franchiseInspectionMessageSend")
    public ResponseEntity<Map<String,Object>> franchiseInspectionMessageSend(@RequestParam(value="bcId", defaultValue="") Long bcId, @RequestParam(value="fiId", defaultValue="") Long fiId,
                                                                             @RequestParam(value="fmMessage", defaultValue="") String fmMessage,
                                                                             @RequestParam(value="isIncludeImg", defaultValue="") String isIncludeImg,
                                                                             HttpServletRequest request){
        return inspectService.franchiseInspectionMessageSend(bcId, fiId, fmMessage, isIncludeImg, request);
    }



    //@@@@@@@@@@@@@@@@@@@@@ 가맹점 수기마감, 가맹점입고, 지사반송, 가맹점강제입고, 세탁인도 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  접수테이블의 상태 변화 API - 수기마감페이지, 가맹점입고 페이지, 지사반송건전송 페이지, 세탁인도 페이지 공용함수
    @PostMapping("franchiseStateChange")
    public ResponseEntity<Map<String,Object>> franchiseStateChange(@RequestParam(value="fdIdList", defaultValue="") List<Long> fdIdList,
                                                                   @RequestParam(value="stateType", defaultValue="") String stateType, HttpServletRequest request){
        return receiptStateService.franchiseStateChange(fdIdList, stateType, request);
    }

    //  수기마감 - 세부테이블 접수상태 리스트 + 검품이 존재할시 상태가 고객수락일 경우에만 호출
    @GetMapping("franchiseReceiptCloseList")
    public ResponseEntity<Map<String,Object>> franchiseReceiptCloseList(HttpServletRequest request){
        return receiptStateService.franchiseReceiptCloseList(request);
    }

    //  가맹점입고 - 세부테이블 지사출고상태 리스트
    @GetMapping("franchiseReceiptFranchiseInList")
    public ResponseEntity<Map<String,Object>> franchiseReceiptFranchiseInList(HttpServletRequest request){
        return receiptStateService.franchiseReceiptFranchiseInList(request);
    }

//    //  지사반송 - 세부테이블 지사반송상태 리스트
//    @GetMapping("franchiseReceiptReturnList")
//    public ResponseEntity<Map<String,Object>> franchiseReceiptReturnList(HttpServletRequest request){
//        return receiptStateService.franchiseReceiptReturnList(request);
//    }

    //  가맹점입고취소 - 세부테이블 가맹점입고상태 리스트
    @GetMapping("franchiseReceiptFranchiseInCancelList")
    public ResponseEntity<Map<String,Object>> franchiseReceiptFranchiseInCancelList(@RequestParam("filterFromDt")String filterFromDt,
                                                                                    @RequestParam("filterToDt")String filterToDt, HttpServletRequest request){
        return receiptStateService.franchiseReceiptFranchiseInCancelList(filterFromDt, filterToDt, request);
    }

    //  가맹점입고취소 상태 변화 API
    @PostMapping("franchiseInCancelChange")
    public ResponseEntity<Map<String,Object>> franchiseInCancelChange(@RequestParam(value="fdIdList", defaultValue="") List<Long> fdIdList, HttpServletRequest request){
        return receiptStateService.franchiseInCancelChange(fdIdList, request);
    }

    //  가맹점강제입고 - 세부테이블 강제출고상태 리스트
    @GetMapping("franchiseReceiptForceList")
    public ResponseEntity<Map<String,Object>> franchiseReceiptForceList(@RequestParam(value="bcId", defaultValue="") Long bcId, @RequestParam(value="fdTag", defaultValue="") String fdTag, HttpServletRequest request){
        return receiptStateService.franchiseReceiptForceList(bcId, fdTag, request);
    }

    //  세탁인도 - 세부테이블 S5-가맹점입고, S8- 강제입고 상태 리스트, S3 - 강제출고입고 상태(22/03/07 추가)
    @GetMapping("franchiseReceiptDeliveryList")
    public ResponseEntity<Map<String,Object>> franchiseReceiptDeliveryList(@RequestParam(value="bcId", defaultValue="") Long bcId, HttpServletRequest request){
        return receiptStateService.franchiseReceiptDeliveryList(bcId, request);
    }



//@@@@@@@@@@@@@@@@@@@@@ 가맹점 미수관리 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 미수관리페이지 - 고객검색 리스트 호출
    @GetMapping("franchiseUncollectCustomerList")
    public ResponseEntity<Map<String,Object>>  franchiseUncollectCustomerList(HttpServletRequest request,
                                                           @RequestParam(value="searchType", defaultValue="") String searchType,
                                                           @RequestParam(value="searchText", defaultValue="") String searchText){
        return uncollectService.franchiseUncollectCustomerList(searchType, searchText, request);
    }

    // 미수관리페이지 - 해당고객의 세탁접수 미수금 리스트 호출
    @GetMapping("franchiseUncollectRequestList")
    public ResponseEntity<Map<String,Object>>  franchiseUncollectRequestList(HttpServletRequest request,
                                                                              @RequestParam(value="bcId", defaultValue="") Long bcId){
        return uncollectService.franchiseUncollectRequestList(bcId, request);
    }

    // 미수관리페이지 - 해당고객의 접수세부테이블 미수금 리스트 호출
    @GetMapping("franchiseUncollectRequestDetailList")
    public ResponseEntity<Map<String,Object>>  franchiseUncollectRequestDetailList(HttpServletRequest request,
                                                                             @RequestParam(value="frId", defaultValue="") Long frId){
        return uncollectService.franchiseUncollectRequestDetailList(frId, request);
    }

    // 미수관리페이지 - 선택한 미수금 결제할 접수리스트 호출
    @GetMapping("franchiseUncollectPayRequestList")
    public ResponseEntity<Map<String,Object>> franchiseUncollectPayRequestList(@RequestParam(value="frIdList", defaultValue="") List<Long> frIdList,
                                                                               @RequestParam(value="bcId", defaultValue="") Long bcId, HttpServletRequest request){
        return uncollectService.franchiseUncollectPayRequestList(frIdList, bcId, request);
    }

    // 미수관리페이지 - 선택한 미수금 접수테이블 결제
    @PostMapping("franchiseUncollectPay")
    public ResponseEntity<Map<String,Object>> franchiseUncollectPay(@RequestBody PaymentUncollectSet paymentUncollectSet, HttpServletRequest request) {
        return uncollectService.franchiseUncollectPay(paymentUncollectSet, request);
    }



//@@@@@@@@@@@@@@@@@@@@@ 일일 영업일보 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  일일영업일보 - 리스트호출 테이블
    @GetMapping("businessdayList")
    public ResponseEntity<Map<String,Object>> businessdayList(
                                                                            @RequestParam(value="filterFromDt", defaultValue="") String filterFromDt,
                                                                            @RequestParam(value="filterToDt", defaultValue="") String filterToDt,
                                                                            HttpServletRequest request){
        if(filterFromDt.equals("")){
            filterFromDt = "00000101";
        }
        if(filterToDt.equals("")){
            filterToDt = "99991230";
        }

        return businessdayService.businessdayList(filterFromDt, filterToDt, request);
    }


//@@@@@@@@@@@@@@@@@@@@@ 공지사항게시판, 택분실게시판 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//  택분실게시판 - 리스트 호출
    @PostMapping("lostNoticeList")
    public ResponseEntity<Map<String,Object>> lostNoticeList(
                                                            @RequestParam("searchString")String searchString,
                                                            @RequestParam("filterFromDt")String filterFromDt,
                                                            @RequestParam("filterToDt")String filterToDt,
                                                            Pageable pageable, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime fromDt = null;
        if(!filterFromDt.equals("")){
            filterFromDt = filterFromDt+" 00:00:00.000";
            fromDt = LocalDateTime.parse(filterFromDt, formatter);
//            log.info("fromDt :"+fromDt);
        }

        LocalDateTime toDt = null;
        if(!filterToDt.equals("")){
            filterToDt = filterToDt+" 23:59:59.999";
            toDt = LocalDateTime.parse(filterToDt, formatter);
//            log.info("toDt :"+toDt);
        }

        return tagNoticeService.lostNoticeList(searchString, fromDt, toDt, pageable, request, "2");
    }

    //  택분실게시판 - 글보기
    @GetMapping("lostNoticeView")
    public ResponseEntity<Map<String,Object>> lostNoticeView(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeView(htId, request, "2");
    }

    //  택분실게시판 - 댓글 리스트 호출
    @GetMapping("lostNoticeCommentList")
    public ResponseEntity<Map<String,Object>> lostNoticeCommentList(@RequestParam("htId") Long htId, HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentList(htId, request);
    }

    //  택분실게시판 - 댓글 작성 and 수정
    @ApiOperation(value = "택분실게시판" , notes = "게시판의 댓글작성 및 수정을 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @PostMapping("lostNoticeCommentSave")
    public ResponseEntity<Map<String,Object>> lostNoticeCommentSave(@RequestParam("hcId") Long hcId, @RequestParam("htId") Long htId, @RequestParam("type") String type,
                                                                                                            @RequestParam("comment") String comment, @RequestParam("preId") Long preId,
                                                                                                            HttpServletRequest request) {
        return tagNoticeService.lostNoticeCommentSave(hcId, htId, type, comment, preId, request);
    }

//@@@@@@@@@@@@@@@@@@@@@ NEW 택분실게시판 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  NEW 택분실게시판 - 리스트 호출
    @ApiOperation(value = "택분실 조회" , notes = "가맹점이 택분실 리스트 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @GetMapping("tagGalleryList")
    public ResponseEntity<Map<String,Object>> tagGalleryList(@RequestParam("type")String searchString, @RequestParam("filterFromDt")String filterFromDt,
                                                             @RequestParam("filterToDt")String filterToDt, HttpServletRequest request) {
        return tagGalleryService.tagGalleryList(searchString, filterFromDt, filterToDt, request, "2");
    }

    //  NEW 택분실게시판 - 상세보기 호출
    @ApiOperation(value = "택분실 상세보기" , notes = "가맹점이 택분실 상세보기 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @GetMapping("tagGalleryDetail")
    public ResponseEntity<Map<String,Object>> tagGalleryDetail(@RequestParam("btId")Long btId, HttpServletRequest request) {
        return tagGalleryService.tagGalleryDetail(btId, request, "2");
    }

    //  NEW 택분실게시판 - 가맹점 체크 호출
    @ApiOperation(value = "택분실 가맹점 체크" , notes = "가맹점이 택분실 체크 요청한다 ")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    @PostMapping("tagGalleryCheck")
    public ResponseEntity<Map<String,Object>> tagGalleryCheck(@RequestParam("btId")Long btId, HttpServletRequest request, @RequestParam("type")String type) {
        return tagGalleryService.tagGalleryCheck(btId, type, request);
    }

// @@@@@@@@@@@@@@@@@@@ 공지사항 게시판 API @@@@@@@@@@@@@@@@@@@@@@@@@@
    // 공지사항 게시판 - 리스트 호출
    @PostMapping("noticeList")
    public ResponseEntity<Map<String,Object>> noticeList(@RequestParam("searchString")String searchString, @RequestParam("filterFromDt")String filterFromDt,
                                                         @RequestParam("filterToDt")String filterToDt,
                                                         Pageable pageable, HttpServletRequest request) {
        return noticeService.noticeList(searchString, filterFromDt.replaceAll("-",""), filterToDt.replaceAll("-",""), pageable, "2");
    }

    //  공지사항 게시판 - 글보기
    @GetMapping("noticeView")
    public ResponseEntity<Map<String,Object>> noticeView(@RequestParam("hnId") Long hnId) {
        return noticeService.noticeView(hnId, "2");
    }












}
