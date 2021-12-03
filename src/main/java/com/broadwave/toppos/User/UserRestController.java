package com.broadwave.toppos.User;

import com.broadwave.toppos.Head.HeadService;
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

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        if(frCode == null || frCode.equals("")){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP007.getCode(), ResponseErrorCode.TP007.getDesc(),ResponseErrorCode.TP008.getCode(), ResponseErrorCode.TP008.getDesc()));
        }

        Optional<Customer> optionalCustomer = userService.findByBcHp(customer.getBcHp());
        if(optionalCustomer.isPresent()){
            log.info("고객 정보를 수정합니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP014.getCode(), ResponseErrorCode.TP014.getDesc(), null, null));
            // 수정일때
//            account.setId(optionalAccount.get().getId());
//            account.setUserid(optionalAccount.get().getUserid());
//            account.setPassword(optionalAccount.get().getPassword());
//            account.setInsert_id(optionalAccount.get().getInsert_id());
//            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
//            account.setModify_id(login_id);
//            account.setModifyDateTime(LocalDateTime.now());
//
//            Account accountSave =  accountService.updateAccount(account);
//            log.info("고객 업데이트 저장 성공 : id '" + accountSave.getUserid() + "'");
        }else{
            log.info("신규 고객 입니다.");
            // 신규일때
            customer.setFrCode(frCode);
            customer.setBcQuitYn("N");
            customer.setInsert_id(login_id);
            customer.setInsertDateTime(LocalDateTime.now());
            customer.setBcMessageAgreeDt(LocalDateTime.now());
            log.info("고객저장정보 : "+customer);
            Customer customerSave =  userService.customerSave(customer);
            log.info("고객 신규 저장 성공 : 핸드폰 번호 '" + customerSave.getBcHp() +"'");
        }

        return ResponseEntity.ok(res.success());
    }

    // 고객 정보 호출 API (현재 로그인한 가맹점의 대한 고객만 호출한다.)
    @GetMapping("customerInfo")
    public ResponseEntity<Map<String,Object>> customerInfo(HttpServletRequest request,
                                                           @RequestParam(value="searchType", defaultValue="") String searchType,
                                                           @RequestParam(value="searchString", defaultValue="") String searchString){
        log.info("customerInfo 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        CustomerInfoDto customerInfoDto = userService.findByCustomerInfo(frCode, searchType, searchString);
        data.put("customerInfoDto",customerInfoDto);

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
        HashMap<String,Object> customerInfo;

        List<CustomerListDto> customerListDtos = userService.findByCustomerList(frCode, searchType, searchString);
//        log.info("customerListDtos : "+customerListDtos);
        for (CustomerListDto customerListDto: customerListDtos) {

            customerInfo = new HashMap<>();

            customerInfo.put("bcName", customerListDto.getBcName());
            customerInfo.put("bcHp", customerListDto.getBcHp());
            customerInfo.put("bcSex", customerListDto.getBcSex());
            customerInfo.put("bcAddress", customerListDto.getBcAddress());
            customerInfo.put("bcBirthday", customerListDto.getBcBirthday());
            customerInfo.put("bcAge", customerListDto.getBcAge());
            customerInfo.put("bcGrade", customerListDto.getBcGrade());
            customerInfo.put("bcValuation", customerListDto.getBcValuation());
            customerInfo.put("bcMessageAgree", customerListDto.getBcMessageAgree());
            customerInfo.put("bcAgreeType", customerListDto.getBcAgreeType());
            customerInfo.put("bcSignImage", customerListDto.getBcSignImage());
            customerInfo.put("bcRemark", customerListDto.getBcRemark());
            customerInfo.put("bcQuitYn", customerListDto.getBcQuitYn());
            customerInfo.put("bcQuitDate", customerListDto.getBcQuitDate());
            customerInfo.put("insertDateTime", customerListDto.getInsertDateTime());

//            if(branch.getBrContractState().equals("01")){
//                branchsetInfo.put("brContractStateValue","진행중");
//            }else{
//                branchsetInfo.put("brContractStateValue","계약완료");
//            }

            customerListData.add(customerInfo);

        }

        log.info("가맹점코드 : "+frCode+"의 고객 리스트 : "+customerListData);
        data.put("gridListData",customerListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

























}
