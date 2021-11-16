package com.broadwave.toppos.User;

import com.broadwave.toppos.Account.*;
import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchMapperDto;
import com.broadwave.toppos.Head.Franohise.FranchisInfoDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.Franohise.FranchiseListDto;
import com.broadwave.toppos.Head.Franohise.FranchiseMapperDto;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerMapperDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
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

    @Autowired
    public UserRestController(UserService userService, TokenProvider tokenProvider, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
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
        if( optionalCustomer.isPresent()){
            log.info("수정합니다.");
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
//            log.info("사용자 업데이트 저장 성공 : id '" + accountSave.getUserid() + "'");
        }else{
            log.info("신규입니다.");
            // 신규일때
            customer.setFrCode(frCode);
            customer.setModify_id(login_id);
            customer.setModifyDateTime(LocalDateTime.now());

            Customer customerSave =  userService.customerSave(customer);
            log.info("고객 신규 저장 성공 : 핸드폰 번호 '" + customerSave.getBcHp() +"'");
        }

//        log.info("customer : "+customer);

        return ResponseEntity.ok(res.success());

    }

}
