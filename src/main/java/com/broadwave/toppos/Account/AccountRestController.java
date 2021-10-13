package com.broadwave.toppos.Account;

import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import com.broadwave.toppos.jwt.exception.ErrorCode;
import com.broadwave.toppos.jwt.exception.UserIdDuplicateException;
import com.broadwave.toppos.jwt.user.SignVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/api/account")
public class AccountRestController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
//    private final LoginlogService loginlogService;

    @Autowired
    public AccountRestController(ModelMapper modelMapper, AccountService accountService, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    // AccountSave API Test
    @PostMapping("/savetest")
    public void saveUser(@RequestBody SignVo signVo){
        Account user = Account.builder()
                .userid(signVo.getUserid())
                .password(signVo.getPassword())
                .username(signVo.getName())
                .role(signVo.getRole())
                .build();
        accountService.save(user);
    }

    // AccountExist API Test
    @GetMapping("/exist_user/{userid}")
    public boolean findUserByEmail(@PathVariable String userid){
        Optional<Account> account = accountService.findByUserid(userid);
        return account.isPresent();
    }

    // AccountSave API
    @PostMapping("save")
    public ResponseEntity<Map<String,Object>> accountSave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();

//        String JWT_AccessToken = request.getHeader("JWT_AccessToken");
//        log.info("JWT_AccessToken : "+JWT_AccessToken);

        Account account = modelMapper.map(accountMapperDto, Account.class);

//        String currentuserid = request.getHeader("insert_id");
//        log.info("currentuserid : "+currentuserid);
        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
        if( optionalAccount.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP001.getCode(), ResponseErrorCode.TP001.getDesc(),null,null));
//            throw new UserIdDuplicateException("이미 존재하는 아이디입니다.", ErrorCode.EMAIL_DUPLICATION);
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

        data.put("accountListData",accountListData);


        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
















}
