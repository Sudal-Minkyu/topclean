package com.broadwave.toppos.Account;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-10-09
 * Time :
 * Remark : Toppos Account RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/account") //  ( 권한 : 어드민, 본사일반 )
public class AccountRestController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    TokenProvider tokenProvider;

    @Autowired
    public AccountRestController(TokenProvider tokenProvider, ModelMapper modelMapper, AccountService accountService) {
        this.modelMapper = modelMapper;
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
    }

    // 사용자등록 API
    @PostMapping("save")
    public ResponseEntity<Map<String,Object>> accountSave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();

        Account account = modelMapper.map(accountMapperDto, Account.class);

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("아이디 : "+login_id);
        Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Authorization"));
        String role = authentication.getAuthorities().toString();
        log.info("권한 : "+role);

        String currentuserid = request.getHeader("insert_id");
        log.info("currentuserid : "+currentuserid);

        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
        if( optionalAccount.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP001.getCode(), ResponseErrorCode.TP001.getDesc(),null,null));
        }

//        //신규일때
//        if (accountMapperDto.getMode().equals("N")) {
//            //userid 중복체크
//            if (optionalAccount.isPresent()) {
//                log.info("사용자 저장 실패(사용자아이디중복) 사용자아이디 : '" + account.getUserid() + "'");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP001.getCode(), ResponseErrorCode.TP001.getDesc(),null,null));
//            }
//            account.setInsert_id(currentuserid);
//            account.setInsertDateTime(LocalDateTime.now());
//        }else{
//            //수정일때
//            if(optionalAccount.isEmpty()){
//                log.info("사용자 정보 수정실패 사용자아이디 : '" + account.getUserid() + "'");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.NDE006.getCode(), ResponseErrorCode.NDE006.getDesc(),null,null));
//            }else{
//                account.setId(optionalAccount.get().getId());
//                account.setInsert_id(optionalAccount.get().getInsert_id());
//                account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
//            }
//            account.setModify_id(currentuserid);
//            account.setModifyDateTime(LocalDateTime.now());
//        }

        Account accountSave =  accountService.save(account);
        log.info("사용자 저장 성공 : id '" + accountSave.getUserid() + "'");
        return ResponseEntity.ok(res.success());

    }

    // AccountList API
    @GetMapping("list")
    public ResponseEntity<Map<String,Object>> accountList(){
//        log.info("AccountList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> accountListData = new ArrayList<>(); // 유지보수 개입시 차트데이터
        HashMap<String,Object> accountInfo;

        List<AccountListDto> accounts = accountService.findAllByAccountList();

        for (AccountListDto account : accounts) {

            accountInfo = new HashMap<>();

            accountInfo.put("userid", account.getUserid());
            accountInfo.put("username", account.getUsername());
            accountInfo.put("role", account.getRole());
            accountListData.add(accountInfo);

        }

        log.info("사용자리스트 : "+accountListData);
        data.put("accountListData",accountListData);


        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
















}
