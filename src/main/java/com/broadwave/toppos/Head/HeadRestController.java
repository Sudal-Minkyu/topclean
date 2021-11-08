package com.broadwave.toppos.Head;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Head.Franohise.Franohise;
import com.broadwave.toppos.Head.Franohise.FranohiseMapperDto;
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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos Head RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/head") //  ( 권한 : 어드민, 본사일반 )
public class HeadRestController {

    private final HeadService headService;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    @Autowired
    public HeadRestController(TokenProvider tokenProvider, ModelMapper modelMapper, HeadService headService) {
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.headService = headService;
    }

    // 가맹점코드 중복확인 API
    @GetMapping("franohiseOverlap")
    public ResponseEntity<Map<String,Object>> franohiseOverlap(@RequestParam(value="frCode", defaultValue="") String frCode, HttpServletRequest request){

        log.info("가맹점 코드 중복확인");

        AjaxResponse res = new AjaxResponse();

        Optional<Franohise> franohiseOptional =  headService.findByFrCode(frCode);
        if(franohiseOptional.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(),null,null));
        }else{
            log.info("중복확인 완료");
        }

        return ResponseEntity.ok(res.success());

    }

    
    // 지점코드 중복확인 API


    // 가맹점등록 API
    @PostMapping("franohiseSave")
    public ResponseEntity<Map<String,Object>> franohiseSave(@ModelAttribute FranohiseMapperDto franohiseMapperDto, HttpServletRequest request){

        log.info("가맹점등록");

        AjaxResponse res = new AjaxResponse();

        Franohise franohise = modelMapper.map(franohiseMapperDto, Franohise.class);

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Franohise> optionalFranohise  =  headService.findByFrCode(franohiseMapperDto.getFrCode());
        if( optionalFranohise.isPresent()){
            log.info("널이 아닙니다 : 업데이트");

            franohise.setId(optionalFranohise.get().getId());
            franohise.setModify_id(login_id);
            franohise.setModifyDateTime(LocalDateTime.now());
            franohise.setInsert_id(optionalFranohise.get().getInsert_id());
            franohise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());
        }else{
            log.info("널입니다. : 신규생성");

            franohise.setBrId(null);
            franohise.setBrCode(null);
            franohise.setBrAssignState("01");
            franohise.setInsert_id(login_id);
            franohise.setInsertDateTime(LocalDateTime.now());
        }

        Franohise franohiseSave =  headService.franohiseSave(franohise);
        log.info("가맹점 저장 성공 : id '" + franohiseSave.getFrCode() + "'");
        return ResponseEntity.ok(res.success());

    }

//    // AccountList API
//    @GetMapping("list")
//    public ResponseEntity<Map<String,Object>> accountList(){
////        log.info("AccountList 호출");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        List<HashMap<String,Object>> accountListData = new ArrayList<>(); // 유지보수 개입시 차트데이터
//        HashMap<String,Object> accountInfo;
//
//        List<AccountListDto> accounts = accountService.findAllByAccountList();
//
//        for (AccountListDto account : accounts) {
//
//            accountInfo = new HashMap<>();
//
//            accountInfo.put("userid", account.getUserid());
//            accountInfo.put("username", account.getUsername());
//            accountInfo.put("role", account.getRole());
//            accountListData.add(accountInfo);
//
//        }
//
//        log.info("사용자리스트 : "+accountListData);
//        data.put("accountListData",accountListData);
//
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }
















}
