package com.broadwave.toppos.Head;

import com.broadwave.toppos.Account.*;
import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchMapperDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.Franohise.FranchiseListDto;
import com.broadwave.toppos.Head.Franohise.FranchiseMapperDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
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
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos Head RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/head") //  ( 권한 : 어드민, 본사일반 )
public class HeadRestController {

    private final AccountService accountService;
    private final HeadService headService;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    @Autowired
    public HeadRestController(AccountService accountService, TokenProvider tokenProvider, ModelMapper modelMapper, HeadService headService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.headService = headService;
    }

    // 사용자 등록 API
    @PostMapping("accountSave")
    public ResponseEntity<Map<String,Object>> accountSave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();

        Account account = modelMapper.map(accountMapperDto, Account.class);

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("아이디 : "+login_id);

        log.info("account : "+account);
        log.info("role : "+account.getRole());
        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
        if( optionalAccount.isPresent()){
            // 수정일때
            account.setId(optionalAccount.get().getId());
            account.setPassword(optionalAccount.get().getPassword());
            account.setInsert_id(login_id);
            account.setInsertDateTime(LocalDateTime.now());
        }else{
            // 신규일때
            account.setModify_id(login_id);
            account.setModifyDateTime(LocalDateTime.now());
        }

        Account accountSave =  accountService.save(account);
        log.info("사용자 저장 성공 : id '" + accountSave.getUserid() + "'");
        return ResponseEntity.ok(res.success());

    }

    // 지사 등록 API
    @PostMapping("branchSave")
    public ResponseEntity<Map<String,Object>> branchSave(@ModelAttribute BranchMapperDto branohMapperDto, HttpServletRequest request){

        log.info("지사등록");

        AjaxResponse res = new AjaxResponse();

        Branch branoh = modelMapper.map(branohMapperDto, Branch.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Branch> optionalBranoh  =  headService.findByBrCode(branohMapperDto.getBrCode());
        if( optionalBranoh.isPresent()){
//            log.info("널이 아닙니다 : 업데이트");
            branoh.setId(optionalBranoh.get().getId());

            branoh.setModify_id(login_id);
            branoh.setModifyDateTime(LocalDateTime.now());
            branoh.setInsert_id(optionalBranoh.get().getInsert_id());
            branoh.setInsertDateTime(optionalBranoh.get().getInsertDateTime());
        }else{
//            log.info("널입니다. : 신규생성");
            branoh.setInsert_id(login_id);
            branoh.setInsertDateTime(LocalDateTime.now());
        }

        Branch branohSave =  headService.branchSave(branoh);
        log.info("지사 저장 성공 : id '" + branohSave.getBrCode() + "'");
        return ResponseEntity.ok(res.success());

    }

    // 가맹점 등록 API
    @PostMapping("franchiseSave")
    public ResponseEntity<Map<String,Object>> franchiseSave(@ModelAttribute FranchiseMapperDto franchiseMapperDto, HttpServletRequest request){

        log.info("가맹점등록");

        AjaxResponse res = new AjaxResponse();

        Franchise franchise = modelMapper.map(franchiseMapperDto, Franchise.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Franchise> optionalFranohise  =  headService.findByFrCode(franchiseMapperDto.getFrCode());
        if( optionalFranohise.isPresent()){
//            log.info("널이 아닙니다 : 업데이트");

            franchise.setId(optionalFranohise.get().getId());

            franchise.setBrId(optionalFranohise.get().getBrId());
            franchise.setBrCode(optionalFranohise.get().getBrCode());
            franchise.setBrAssignState(optionalFranohise.get().getBrAssignState());

            franchise.setModify_id(login_id);
            franchise.setModifyDateTime(LocalDateTime.now());
            franchise.setInsert_id(optionalFranohise.get().getInsert_id());
            franchise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());
        }else{
//            log.info("널입니다. : 신규생성");

            franchise.setBrId(null);
            franchise.setBrCode(null);
            franchise.setBrAssignState("01");
            franchise.setInsert_id(login_id);
            franchise.setInsertDateTime(LocalDateTime.now());
        }

        Franchise franchiseSave =  headService.franchiseSave(franchise);
        log.info("가맹점 저장 성공 : id '" + franchiseSave.getFrCode() + "'");
        return ResponseEntity.ok(res.success());
    }

    // 지사 리스트 API
    @GetMapping("branchList")
    public ResponseEntity<Map<String,Object>> branchList(@RequestParam(value="brName", defaultValue="") String brName,
                                                         @RequestParam(value="brCode", defaultValue="") String brCode,
                                                         @RequestParam(value="brContractState", defaultValue="") String brContractState){
        log.info("branohList 호출");

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> branchListData = new ArrayList<>();
        HashMap<String,Object> branchsetInfo;

        List<BranchListDto> branchListDtos = headService.findByBranchList(brName, brCode, brContractState);
//        log.info("branohListDtos : "+branohListDtos);

        for (BranchListDto branch: branchListDtos) {

            branchsetInfo = new HashMap<>();

            branchsetInfo.put("brCode", branch.getBrCode());
            branchsetInfo.put("brName", branch.getBrName());
            branchsetInfo.put("brContractDt", branch.getBrContractDt());
            branchsetInfo.put("brContractFromDt",branch.getBrContractFromDt());
            branchsetInfo.put("brContractToDt", branch.getBrContractToDt());
            branchsetInfo.put("brContractState", branch.getBrContractState());
            if(branch.getBrContractState().equals("01")){
                branchsetInfo.put("brContractStateValue","진행중");
            }else{
                branchsetInfo.put("brContractStateValue","계약완료");
            }
            branchsetInfo.put("brCarculateRateHq", branch.getBrCarculateRateHq());
            branchsetInfo.put("brCarculateRateBr", branch.getBrCarculateRateBr());
            branchsetInfo.put("brCarculateRateFr", branch.getBrCarculateRateFr());
            branchsetInfo.put("brRemark", branch.getBrRemark());
            branchListData.add(branchsetInfo);

        }

        log.info("지사 리스트 : "+branchListData);
        data.put("gridListData",branchListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 리스트 API
    @GetMapping("franchiseList")
    public ResponseEntity<Map<String,Object>> franchiseList(@RequestParam(value="brAssignState", defaultValue="") String brAssignState,
                                                                                           @RequestParam(value="frName", defaultValue="") String frName,
                                                                                           @RequestParam(value="frCode", defaultValue="") String frCode,
                                                                                           @RequestParam(value="frContractState", defaultValue="") String frContractState){
        log.info("franohiseList 호출");

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> franohiseListData = new ArrayList<>();
        HashMap<String,Object> franohisetInfo;

        List<FranchiseListDto> franchiseListDtos = headService.findByFranchiseList(brAssignState, frName, frCode, frContractState);
//        log.info("franohiseListDtos : "+franohiseListDtos);

        for (FranchiseListDto franohise : franchiseListDtos) {

            franohisetInfo = new HashMap<>();

            franohisetInfo.put("frCode", franohise.getFrCode());
            franohisetInfo.put("frName", franohise.getFrName());
            franohisetInfo.put("frContractDt", franohise.getFrContractDt());
            franohisetInfo.put("frContractFromDt", franohise.getFrContractFromDt());
            franohisetInfo.put("frContractToDt", franohise.getFrContractToDt());
            franohisetInfo.put("frContractState", franohise.getFrContractState());
            if(franohise.getFrContractState().equals("01")){
                franohisetInfo.put("frContractStateValue","진행중");
            }else{
                franohisetInfo.put("frContractStateValue","계약완료");
            }
            franohisetInfo.put("frRemark", franohise.getFrRemark());
            franohisetInfo.put("brName", franohise.getBrName());
            franohiseListData.add(franohisetInfo);

        }

        log.info("가맹점리스트 : "+franohiseListData);
        data.put("gridListData",franohiseListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 사용자 리스트 API
    @GetMapping("accountList")
    public ResponseEntity<Map<String,Object>> accountList(@RequestParam(value="s_userid", defaultValue="") String s_userid,
                                                                                        @RequestParam(value="s_username", defaultValue="") String s_username,
                                                                                        @RequestParam(value="s_role", defaultValue="") String s_role,
                                                                                        @RequestParam(value="s_frCode", defaultValue="") String s_frCode,
                                                                                        @RequestParam(value="s_brCode", defaultValue="") String s_brCode){
        log.info("accountList 호출");

        AccountRole role = null;

        if (!s_role.equals("")){
            role = AccountRole.valueOf(s_role);
        }

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> accountListData = new ArrayList<>();
        HashMap<String,Object> accounttInfo;

        List<AccountListDto> accountListDtos = accountService.findByAccountList(s_userid, s_username, role, s_frCode, s_brCode);
        log.info("accountListDtos : "+accountListDtos);

        for (AccountListDto account : accountListDtos) {

            accounttInfo = new HashMap<>();

            accounttInfo.put("userid", account.getUserid());
            accounttInfo.put("role", account.getRole());
            accounttInfo.put("username", account.getUsername());
            accounttInfo.put("usertel", account.getUsertel());
            accounttInfo.put("useremail", account.getUseremail());
            if(account.getBrCode().equals("no")){
                accounttInfo.put("brCode","해당안됨");
            }else{
                accounttInfo.put("brCode",account.getBrCode());
            }
            if(account.getFrCode().equals("not")){
                accounttInfo.put("frCode","해당안됨");
            }else{
                accounttInfo.put("frCode",account.getFrCode());
            }
            accounttInfo.put("userremark", account.getUserremark());
            accountListData.add(accounttInfo);

        }

        log.info("사용자리스트 : "+accountListData);
        data.put("gridListData",accountListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }








    // 유저아이디 중복확인 API
    @GetMapping("useridOverlap")
    public ResponseEntity<Map<String,Object>> useridOverlap(@RequestParam(value="userid", defaultValue="") String userid){

        log.info("가맹점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        if(userid.equals("")){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP004.getCode(), ResponseErrorCode.TP004.getDesc(),null,null));
        }

        Optional<Account> accountOptional =  accountService.findByUserid(userid);
        if(accountOptional.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP001.getCode(), ResponseErrorCode.TP001.getDesc(),null,null));
        }else{
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }

    // 가맹점코드 중복확인 API
    @GetMapping("franchiseOverlap")
    public ResponseEntity<Map<String,Object>> franchiseOverlap(@RequestParam(value="frCode", defaultValue="") String frCode){

        log.info("가맹점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        Optional<Franchise> franohiseOptional =  headService.findByFrCode(frCode);
        if(franohiseOptional.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(),null,null));
        }else{
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }

    // 지점코드 중복확인 API
    @GetMapping("branchOverlap")
    public ResponseEntity<Map<String,Object>> branchOverlap(@RequestParam(value="brCode", defaultValue="") String brCode){

        log.info("지점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        Optional<Branch> branohOptional =  headService.findByBrCode(brCode);
        if(branohOptional.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(),null,null));
        }else{
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }


}
