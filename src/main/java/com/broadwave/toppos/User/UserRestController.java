package com.broadwave.toppos.User;

import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Head.Item.Group.A.UserItemGroupSortDto;
import com.broadwave.toppos.Head.Item.Group.B.UserItemGroupSListDto;
import com.broadwave.toppos.Head.Item.Price.UserItemPriceSortDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerInfoDto;
import com.broadwave.toppos.User.Customer.CustomerListDto;
import com.broadwave.toppos.User.Customer.CustomerMapperDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    public UserRestController(UserService userService, TokenProvider tokenProvider, ModelMapper modelMapper, HeadService headService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.headService = headService;
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
                    customer.setBcLastRequsetDt(optionalCustomerById.get().getBcLastRequsetDt());
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
            customerListInfo.put("bcLastRequsetDt", customerInfoDto.getBcLastRequsetDt());

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




        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
















}
