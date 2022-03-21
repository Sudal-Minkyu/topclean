package com.broadwave.toppos.Head;

import com.broadwave.toppos.Account.*;
import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchMapperDto;
import com.broadwave.toppos.Head.Franchise.*;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseInfoDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseMapperDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseSearchDto;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Head.HeadService.NoticeService;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupSet;
import com.broadwave.toppos.Head.Item.Group.B.*;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemSet;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.*;
import com.broadwave.toppos.Head.Item.Price.ItemPrice;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestSearchDto;
import com.broadwave.toppos.User.UserService.ReceiptService;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final HeadService headService; // 본사 공용서비스
    private final NoticeService noticeService; // 공지사항 서비스
    private final ModelMapper modelMapper;
    private final ReceiptService receiptService;

    @Autowired
    public HeadRestController(AccountService accountService, NoticeService noticeService, ModelMapper modelMapper, HeadService headService, ReceiptService receiptService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.noticeService = noticeService;
        this.headService = headService;
        this.receiptService = receiptService;
    }

    // 사용자 등록 API
    @PostMapping("accountSave")
    public ResponseEntity<Map<String,Object>> accountSave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();

        Account account = modelMapper.map(accountMapperDto, Account.class);

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("접속한 아이디 : "+login_id);

//        log.info("account : "+account);
//        log.info("role : "+account.getRole());
        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
        if(optionalAccount.isPresent()){
            log.info("수정합니다.");
            // 수정일때
            account.setId(optionalAccount.get().getId());
            account.setUserid(optionalAccount.get().getUserid());
            account.setPassword(optionalAccount.get().getPassword());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setModify_id(login_id);
            account.setModifyDateTime(LocalDateTime.now());

            Account accountSave =  accountService.updateAccount(account);
            log.info("사용자 업데이트 저장 성공 : id '" + accountSave.getUserid() + "'");
        }else{
            log.info("신규입니다.");
            // 신규일때
            account.setInsert_id(login_id);
            account.setInsertDateTime(LocalDateTime.now());

            Account accountSave =  accountService.save(account);
            log.info("사용자 신규 저장 성공 : id '" + accountSave.getUserid() + "'");
        }
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
        return headService.franchiseSave(franchiseMapperDto, request);
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
            accounttInfo.put("roleCode", account.getRole().getCode());
            accounttInfo.put("role", account.getRole().getDesc());
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
        log.info("branchListDtos : "+branchListDtos);

        for (BranchListDto branch: branchListDtos) {

            branchsetInfo = new HashMap<>();

            branchsetInfo.put("brCode", branch.getBrCode());
            branchsetInfo.put("brName", branch.getBrName());
            branchsetInfo.put("brTelNo", branch.getBrTelNo());
            branchsetInfo.put("brContractDt", branch.getBrContractDt());
            branchsetInfo.put("brContractFromDt",branch.getBrContractFromDt());
            branchsetInfo.put("brContractToDt", branch.getBrContractToDt());
            branchsetInfo.put("brContractState", branch.getBrContractState());
            if(branch.getBrContractState().equals("01")){
                branchsetInfo.put("brContractStateValue","진행중");
            }else{
                branchsetInfo.put("brContractStateValue","계약완료");
            }
            branchsetInfo.put("brCaculateRateBr", branch.getBrCaculateRateBr());
            branchsetInfo.put("brCaculateRateFr", branch.getBrCaculateRateFr());
            branchsetInfo.put("brRoyaltyRateBr", branch.getBrRoyaltyRateBr());
            branchsetInfo.put("brRoyaltyRateFr", branch.getBrRoyaltyRateFr());

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

        List<FranchiseListDto> franchiseListDtos = headService.findByFranchiseList("",brAssignState, frName, frCode, frContractState);
//        log.info("franohiseListDtos : "+franohiseListDtos);

        for (FranchiseListDto franohise : franchiseListDtos) {

            franohisetInfo = new HashMap<>();

            franohisetInfo.put("frCode", franohise.getFrCode());
            franohisetInfo.put("frName", franohise.getFrName());
            franohisetInfo.put("frRefCode", franohise.getFrRefCode());
            franohisetInfo.put("frContractDt", franohise.getFrContractDt());
            franohisetInfo.put("frContractFromDt", franohise.getFrContractFromDt());
            franohisetInfo.put("frContractToDt", franohise.getFrContractToDt());
            franohisetInfo.put("frContractState", franohise.getFrContractState());
            franohisetInfo.put("brAssignState", franohise.getBrAssignState());
            if(franohise.getBrAssignState().equals("01")){
                franohisetInfo.put("brAssignStateValue","진행중");
            }else{
                franohisetInfo.put("brAssignStateValue","계약완료");
            }
            if(franohise.getFrContractState().equals("01")){
                franohisetInfo.put("frContractStateValue","진행중");
            }else{
                franohisetInfo.put("frContractStateValue","계약완료");
            }
            franohisetInfo.put("frPriceGrade", franohise.getFrPriceGrade());
            franohisetInfo.put("frTagNo", franohise.getFrTagNo());
            franohisetInfo.put("frEstimateDuration", franohise.getFrEstimateDuration());
            franohisetInfo.put("frRemark", franohise.getFrRemark());
            franohisetInfo.put("brName", franohise.getBrName());
            franohisetInfo.put("frBusinessNo", franohise.getFrBusinessNo());
            franohisetInfo.put("frRpreName", franohise.getFrRpreName());
            franohisetInfo.put("frTelNo", franohise.getFrTelNo());

            franohisetInfo.put("frCaculateRateBr", franohise.getFrCaculateRateBr());
            franohisetInfo.put("frCaculateRateFr", franohise.getFrCaculateRateFr());
            franohisetInfo.put("frRoyaltyRateBr", franohise.getFrRoyaltyRateBr());
            franohisetInfo.put("frRoyaltyRateFr", franohise.getFrRoyaltyRateFr());

            franohisetInfo.put("frPostNo", franohise.getFrPostNo());
            franohisetInfo.put("frAddress", franohise.getFrAddress());
            franohisetInfo.put("frAddressDetail", franohise.getFrAddressDetail());


            franohiseListData.add(franohisetInfo);

        }

        log.info("가맹점리스트 : "+franohiseListData);
        data.put("gridListData",franohiseListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 사용자 삭제 API
    @PostMapping("accountDelete")
    public ResponseEntity<Map<String,Object>> accountDelete(@RequestParam(value="userid", defaultValue="") String userid) {
        log.info("accountDelete 호출");
        log.info("삭제 할 USER ID : " + userid);

        AjaxResponse res = new AjaxResponse();

        Optional<Account> optionalAccount = accountService.findByUserid(userid);
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 "+ResponseErrorCode.TP005.getDesc(), "문자", "유저 아이디 : "+userid));
        } else {
            log.info(userid+" 사용자 삭제완료");
            accountService.findByAccountDelete(optionalAccount.get());
        }

        return ResponseEntity.ok(res.success());
    }

    // 지사 삭제 API
    @PostMapping("branchDelete")
    public ResponseEntity<Map<String,Object>> branchDelete(@RequestParam(value="brCode", defaultValue="") String brCode) {
        log.info("branchDelete 호출");
        log.info("요청된 지사코드 : " + brCode);

        AjaxResponse res = new AjaxResponse();

        Optional<Branch> optionalBranch = headService.findByBrCode(brCode);
        if (!optionalBranch.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 "+ResponseErrorCode.TP005.getDesc(), "문자", "지사코드(2자리) : "+brCode));
        } else {
            log.info("지사코드 : "+optionalBranch.get().getBrCode());
            List<FranchiseSearchDto> franchise = headService.findByFranchiseBrcode(optionalBranch.get().getBrCode());
            if(franchise.size() == 0){
                log.info(optionalBranch.get().getBrCode()+" 지사삭제 진행");
                headService.findByBranchDelete(optionalBranch.get());
            }else{
                return ResponseEntity.ok(res.fail("문자", "해당 지사에 배정된 가맹점이 존재합니다.", "문자", "지사코드(2자리) : "+optionalBranch.get().getBrCode()));
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 가맹점 삭제 API
    @PostMapping("franchiseDelete")
    public ResponseEntity<Map<String,Object>> franchiseDelete(@RequestParam(value="frCode", defaultValue="") String frCode) {
        log.info("franchiseDelete 호출");
        log.info("요청된 가맹점코드 : " + frCode);

        AjaxResponse res = new AjaxResponse();

        Optional<Franchise> optionalFranchise = headService.findByFrCode(frCode);
        if (!optionalFranchise.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 "+ResponseErrorCode.TP005.getDesc(), "문자", "가맹점코드(3자리)  : "+frCode));
        } else {
            log.info("가맹점코드 : "+optionalFranchise.get().getFrCode());
            List<RequestSearchDto> request = receiptService.findByRequestFrCode(optionalFranchise.get().getFrCode());
            if(request.size() == 0){
                log.info(optionalFranchise.get().getFrCode()+" 가맹점삭제 진행");
                headService.findByFranchiseDelete(optionalFranchise.get());
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), "해당 가맹점의 "+ResponseErrorCode.TP011.getDesc(), "문자", "가맹점코드(3자리)  : "+frCode));
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 해당 지사에 배정된 가맹점 리스트 호출
    @GetMapping("branchAssignList")
    public ResponseEntity<Map<String,Object>> branchAssignList(@RequestParam(value="brCode", defaultValue="") String brCode){
        log.info("branchAssignList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> franohiseListData = new ArrayList<>();
        HashMap<String,Object> franohisetInfo;

        List<FranchiseListDto> franchiseListDtos = headService.findByFranchiseList(brCode,"", "", "", "");
//        log.info("franchiseListDtos : "+franchiseListDtos);

        for (FranchiseListDto franohise : franchiseListDtos) {

            franohisetInfo = new HashMap<>();
            franohisetInfo.put("frCode", franohise.getFrCode());
            franohisetInfo.put("frRefCode", franohise.getFrRefCode());
            franohisetInfo.put("frName", franohise.getFrName());
            franohisetInfo.put("frContractDt", franohise.getFrContractDt());
            franohisetInfo.put("frContractFromDt", franohise.getFrContractFromDt());
            franohisetInfo.put("frContractToDt", franohise.getFrContractToDt());
            franohisetInfo.put("frContractState", franohise.getFrContractState());
            franohisetInfo.put("brAssignState", franohise.getBrAssignState());
            if(franohise.getBrAssignState().equals("01")){
                franohisetInfo.put("brAssignStateValue","진행중");
            }else{
                franohisetInfo.put("brAssignStateValue","계약완료");
            }
            if(franohise.getFrContractState().equals("01")){
                franohisetInfo.put("frContractStateValue","진행중");
            }else{
                franohisetInfo.put("frContractStateValue","계약완료");
            }
            franohisetInfo.put("frPriceGrade", franohise.getFrPriceGrade());
            franohisetInfo.put("frTagNo", franohise.getFrTagNo());
            franohisetInfo.put("frRemark", franohise.getFrRemark());
            franohisetInfo.put("brName", franohise.getBrName());
            franohiseListData.add(franohisetInfo);
        }

        log.info("해당 지사에 배정된 가맹점 리스트 지사코드("+brCode+") : "+franohiseListData);
        data.put("gridListData",franohiseListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 해당 가맹점 선택시 가맹점 정보 호출하기
    @GetMapping("franchiseInfo")
    public ResponseEntity<Map<String,Object>> franchiseInfo(@RequestParam(value="frCode", defaultValue="") String frCode){
        log.info("franchiseInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        FranchiseInfoDto franchiseInfoDto = headService.findByFranchiseInfo(frCode);
        log.info("franchiseInfoDto : "+franchiseInfoDto);
        data.put("franchiseInfoData", franchiseInfoDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점의 대한 지사배치 등록 API
    @PostMapping("franchiseAssignment")
    public ResponseEntity<Map<String,Object>> franchiseAssignment(@RequestParam(value="frCode", defaultValue="") String frCode,
                                                                    @RequestParam(value="brCode", defaultValue="") String brCode,
                                                                    @RequestParam(value="bot_brAssignState", defaultValue="") String bot_brAssignState,
                                                                    @RequestParam(value="bot_frCarculateRateBr", defaultValue="") Double bot_frCarculateRateBr,
                                                                    @RequestParam(value="bot_frCarculateRateFr", defaultValue="") Double bot_frCarculateRateFr,
                                                                    @RequestParam(value="bot_frRoyaltyRateBr", defaultValue="") Double bot_frRoyaltyRateBr,
                                                                    @RequestParam(value="bot_frRoyaltyRateFr", defaultValue="") Double bot_frRoyaltyRateFr,
                                                                    HttpServletRequest request){
        return headService.franchiseAssignment(frCode, brCode, bot_brAssignState, bot_frCarculateRateBr, bot_frCarculateRateFr, bot_frRoyaltyRateBr, bot_frRoyaltyRateFr, request);
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

    // 상품그룹 대분류 호출 API
    @PostMapping("itemGroupA")
    public ResponseEntity<Map<String,Object>> itemGroupA(@RequestBody ItemGroupSet itemGroupSet, HttpServletRequest request){
        log.info("itemGroupA 호출");
        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 로그인한 아이디 : "+login_id);

        ArrayList<ItemGroupDto> addList = itemGroupSet.getAdd(); // 추가 리스트 얻기
        ArrayList<ItemGroupDto> updateList = itemGroupSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<ItemGroupDto> deleteList = itemGroupSet.getDelete(); // 제거 리스트 얻기

//        log.info("추가 리스트 : "+addList);
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        // 저장로직 실행 : 데이터베이스에 같은 코드가 존재하면 리턴처리한다.
        for (ItemGroupDto itemGroupDto : addList) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if (optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), "문자", "존재하는 코드 : " + itemGroupDto.getBgItemGroupcode()));
            }
        }
        // 저장 시작.
        for (ItemGroupDto itemGroupDto : addList) {
            log.info("같은 코드 존재하지 않음 신규생성");
            ItemGroup itemGroup = modelMapper.map(itemGroupDto, ItemGroup.class);
            itemGroup.setInsert_id(login_id);
            itemGroup.setInsertDateTime(LocalDateTime.now());
//            log.info("itemGroup : " +itemGroup);
            headService.itemGroupSave(itemGroup);
        }


        // 수정로직 실행 : 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        for (ItemGroupDto itemGroupDto : updateList) {
            log.info("수정할 대분류의 코드 : "+itemGroupDto.getBgItemGroupcode());
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if(optionalItemGroup.isPresent()) {
                ItemGroup itemGroup = new ItemGroup();
                itemGroup.setBgItemGroupcode(optionalItemGroup.get().getBgItemGroupcode());
                itemGroup.setBgName(itemGroupDto.getBgName());
                itemGroup.setBgRemark(itemGroupDto.getBgRemark());
                itemGroup.setBgIconFilename(itemGroupDto.getBgIconFilename());
                itemGroup.setBgUseYn(itemGroupDto.getBgUseYn());
                itemGroup.setInsert_id(optionalItemGroup.get().getInsert_id());
                itemGroup.setInsertDateTime(optionalItemGroup.get().getInsertDateTime());
                itemGroup.setModify_id(login_id);
                itemGroup.setModifyDateTime(LocalDateTime.now());
                headService.itemGroupSave(itemGroup);
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 코드 : " + itemGroupDto.getBgItemGroupcode()));
            }
        }


        // 삭제로직 실행 : 데이터베이스에 코드사용중인 코드가 존재하면 리턴처리한다. , 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        for (ItemGroupDto itemGroupDto : deleteList) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 " + ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 코드 : " + itemGroupDto.getBgItemGroupcode()));
            }else {
                log.info("삭제할 대분류의 코드 : " + optionalItemGroup.get().getBgItemGroupcode());
                List<ItemGroupSListDto> itemGroupSListDtos = headService.findByItemGroupSList(optionalItemGroup.get());
//                log.info("itemGroupSListDtos : " + itemGroupSListDtos);
                if (itemGroupSListDtos.size() == 0) {
                    headService.findByItemGroupDelete(optionalItemGroup.get());
                } else {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), ResponseErrorCode.TP011.getDesc(), null, null));
                }
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 상품그룹 대분류 리스트 호출 API
    @GetMapping("itemGroupAList")
    public ResponseEntity<Map<String,Object>> itemGroupAList(){
        log.info("itemGroupAList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> itemGroupListData = new ArrayList<>();
        HashMap<String,Object> itemGroupInfo;

        List<ItemGroupDto> itemGroupListDtos = headService.findByItemGroupList();
        log.info("itemGroupListDtos : "+itemGroupListDtos);

        for (ItemGroupDto itemGroupDto : itemGroupListDtos) {

            itemGroupInfo = new HashMap<>();

            itemGroupInfo.put("bgItemGroupcode", itemGroupDto.getBgItemGroupcode());
            itemGroupInfo.put("bgName", itemGroupDto.getBgName());
            itemGroupInfo.put("bgRemark", itemGroupDto.getBgRemark());
            itemGroupInfo.put("bgIconFilename", itemGroupDto.getBgIconFilename());
            itemGroupInfo.put("bgUseYn", itemGroupDto.getBgUseYn());

            itemGroupListData.add(itemGroupInfo);
        }

//        log.info("상품그룹 대분류 리스트 : "+itemGroupListData);
        data.put("gridListData",itemGroupListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 대분류 명칭리스트 호출 API
    @GetMapping("itemGroupNameList")
    public ResponseEntity<Map<String,Object>> itemGroupNameList(){
        log.info("itemGroupNameList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemGroupNameListDto> itemGroupNameListDtos = headService.findByItemGroupName();
        log.info("itemGroupNameListDtos : "+itemGroupNameListDtos);
        data.put("itemGroupNameList",itemGroupNameListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 중분류 호출 API
    @PostMapping("itemGroupB")
    public ResponseEntity<Map<String,Object>> itemGroupB(@RequestBody ItemGroupSSet itemGroupSSet, HttpServletRequest request){
        log.info("itemGroupB 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 로그인한 아이디 : "+login_id);

        ArrayList<ItemGroupSDto> addList = itemGroupSSet.getAdd(); // 추가 리스트 얻기
        ArrayList<ItemGroupSDto> updateList = itemGroupSSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<ItemGroupSDto> deleteList = itemGroupSSet.getDelete(); // 제거 리스트 얻기

//        log.info("추가 리스트 : "+addList);
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        // 중분류 저장 시작.
        if(addList.size()!=0){
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(addList.get(0).getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "저장 할 대분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
            }else{
                for (ItemGroupSDto itemGroupSDto : addList) {
//                    log.info("같은 코드 존재하지 않음 신규생성");
                    log.info("itemGroupSDto : "+itemGroupSDto.getBsItemGroupcodeS());
                    ItemGroupS itemGroupS = modelMapper.map(itemGroupSDto, ItemGroupS.class);
                    itemGroupS.setBgItemGroupcode(optionalItemGroup.get());
                    itemGroupS.setInsert_id(login_id);
                    itemGroupS.setInsertDateTime(LocalDateTime.now());
//                    log.info("itemGroupS : " +itemGroupS);
                    headService.itemGroupSSave(itemGroupS);
                }
            }
        }

        // 중분류 수정 시작.
        if(updateList.size()!=0){
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(updateList.get(0).getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 대분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
            }else{
                for (ItemGroupSDto itemGroupSDto : updateList) {
                    ItemGroupSInfo itemGroupSInfo = headService.findByBsItemGroupcodeS(itemGroupSDto.getBgItemGroupcode(), itemGroupSDto.getBsItemGroupcodeS());
                    if(itemGroupSInfo != null) {
                        log.info("수정 할 중분류 코드 : "+itemGroupSInfo.getBsItemGroupcodeS());
                        ItemGroupS itemGroupS = new ItemGroupS();
                        itemGroupS.setBsItemGroupcodeS(itemGroupSInfo.getBsItemGroupcodeS());
                        itemGroupS.setBgItemGroupcode(optionalItemGroup.get());
                        itemGroupS.setBsName(itemGroupSDto.getBsName());
                        itemGroupS.setBsUseYn(itemGroupSDto.getBsUseYn());
                        itemGroupS.setBsRemark(itemGroupSDto.getBsRemark());
                        itemGroupS.setInsert_id(itemGroupSInfo.getInsert_id());
                        itemGroupS.setInsertDateTime(itemGroupSInfo.getInsertDateTime());
                        itemGroupS.setModify_id(login_id);
                        itemGroupS.setModifyDateTime(LocalDateTime.now());
                        log.info("itemGroupS : " +itemGroupS);
                        headService.itemGroupSSave(itemGroupS);
                    }else {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 중분류 " + ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 대분류, 중분류 코드 : " + itemGroupSDto.getBgItemGroupcode() + ", " + itemGroupSDto.getBsItemGroupcodeS()));
                    }
                }
            }
        }

        // 중분류 삭제로직 실행 : 데이터베이스에 코드사용중인 코드가 존재하면 리턴처리한다. , 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        if(deleteList.size()!=0){

            Optional<Item> item = headService.findByBiItem(deleteList.get(0).getBgItemGroupcode(), deleteList.get(0).getBsItemGroupcodeS());
            if(item.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), ResponseErrorCode.TP011.getDesc(), "문자", "상품코드 : "+item.get().getBiItemcode()));
            }else{
                for (ItemGroupSDto itemGroupSDto : deleteList) {
                    ItemGroupS itemGroupS = headService.findByItemGroupcodeS(itemGroupSDto.getBgItemGroupcode(), itemGroupSDto.getBsItemGroupcodeS());
                    if(itemGroupS != null) {
//                    log.info("삭제할 대상 : "+itemGroupS.getBsName());
                        log.info("삭제할 중분류의 코드 : "+itemGroupS.getBsItemGroupcodeS());
                        headService.findByItemGroupSDelete(itemGroupS);
                    }else{
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 대분류, 중분류 코드 : " + itemGroupSDto.getBgItemGroupcode() + itemGroupSDto.getBgItemGroupcode()));
                    }
                }
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 상품그룹 중분류 리스트 호출 API
    @GetMapping("itemGroupBList")
    public ResponseEntity<Map<String,Object>> itemGroupBList(@RequestParam(value="bgItemGroupcode", defaultValue="") String bgItemGroupcode){
        log.info("itemGroupBList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> itemGroupSListData = new ArrayList<>();
        HashMap<String,Object> itemGroupSInfo;

        Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(bgItemGroupcode);
        if (!optionalItemGroup.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "조회 할 "+ResponseErrorCode.TP003.getDesc(), null, null));
        }else{
            List<ItemGroupSListDto> itemGroupSListDtos = headService.findByItemGroupSList(optionalItemGroup.get());
//            log.info("itemGroupSListDtos : "+itemGroupSListDtos);
            for (ItemGroupSListDto itemGroupSListDto : itemGroupSListDtos) {

                itemGroupSInfo = new HashMap<>();

                itemGroupSInfo.put("bgItemGroupcode", itemGroupSListDto.getBgItemGroupcode());
                itemGroupSInfo.put("bsItemGroupcodeS", itemGroupSListDto.getBsItemGroupcodeS());
                itemGroupSInfo.put("bgName", itemGroupSListDto.getBgName());
                itemGroupSInfo.put("bsName", itemGroupSListDto.getBsName());
                itemGroupSInfo.put("bsRemark", itemGroupSListDto.getBsRemark());
                itemGroupSInfo.put("bsUseYn", itemGroupSListDto.getBsUseYn());

                itemGroupSListData.add(itemGroupSInfo);
            }

//        log.info("상품그룹 중분류 리스트 : "+itemGroupSListData);
            data.put("gridListData",itemGroupSListData);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 상품소재 호출 API
    @PostMapping("itemGroupC")
    public ResponseEntity<Map<String,Object>> itemGroupC(@RequestBody ItemSet itemSet, HttpServletRequest request){
        log.info("itemGroupC 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 로그인한 아이디 : "+login_id);

        ArrayList<ItemDto> addList = itemSet.getAdd(); // 추가 리스트 얻기
        ArrayList<ItemDto> updateList = itemSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<ItemDto> deleteList = itemSet.getDelete(); // 제거 리스트 얻기

//        log.info("추가 리스트 : "+addList.get(0).getBgItemGroupcode());
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        // 상품소재 저장 시작.
        if(addList.size()!=0){
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(addList.get(0).getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "저장 할 상품소재의 대분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
            }else{
                ItemGroupS itemGroupS = headService.findByItemGroupcodeS(addList.get(0).getBgItemGroupcode(), addList.get(0).getBsItemGroupcodeS());
                if (itemGroupS == null) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "저장 할 상품소재의 중분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
                }else{
                    for (ItemDto itemDto : addList) {
                        Optional<Item> optionalItem = headService.findByBiItemcode(itemDto.getBiItemcode());
                        if (optionalItem.isPresent()) {
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), "문자", "상품코드 : "+itemDto.getBiItemcode()));
                        }else{
                            log.info("같은 코드 존재하지 않음 신규생성");
//                            log.info("itemDto.getBgItemGroupcode : "+itemDto.getBgItemGroupcode());
//                            log.info("itemDto.getBsItemGroupcodeS : "+itemDto.getBsItemGroupcodeS());
//                            log.info("itemDto.getBiItemSequence : "+itemDto.getBiItemSequence());
                            Item item = new Item();
                            item.setBiItemcode(itemDto.getBiItemcode());
                            item.setBgItemGroupcode(itemDto.getBgItemGroupcode());
                            item.setBsItemGroupcodeS(itemDto.getBsItemGroupcodeS());
                            item.setBiItemSequence(itemDto.getBiItemSequence());
                            item.setBiName(itemDto.getBiName());
                            item.setBiUseYn(itemDto.getBiUseYn());
                            item.setBiRemark(itemDto.getBiRemark());
                            item.setInsert_id(login_id);
                            item.setInsertDateTime(LocalDateTime.now());
//                            log.info("item : " +item);
                            headService.itemSave(item);
                        }
                    }
                }
            }
        }

        // 상품소재 수정 시작.
        if(updateList.size()!=0){
            for (ItemDto itemDto : updateList) {
                Optional<Item> itemOptional = headService.findByBiItemcode(itemDto.getBiItemcode());
                if (!itemOptional.isPresent()) {
                    log.info("존재하지 않은 상품소재 코드 : " +itemDto.getBiItemcode());
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 상품소재 " + ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : " + itemDto.getBiItemcode()));
                } else {
                    log.info("수정 할 상품소재 코드 : " + itemOptional.get().getBiItemcode());
                    Item item = new Item();
                    item.setBsItemGroupcodeS(itemOptional.get().getBsItemGroupcodeS());
                    item.setBgItemGroupcode(itemOptional.get().getBgItemGroupcode());
                    item.setBiItemcode(itemOptional.get().getBiItemcode());
                    item.setBiItemSequence(itemOptional.get().getBiItemSequence());
                    item.setBiName(itemDto.getBiName());
                    item.setBiUseYn(itemDto.getBiUseYn());
                    item.setBiRemark(itemDto.getBiRemark());
                    item.setInsert_id(itemOptional.get().getInsert_id());
                    item.setInsertDateTime(itemOptional.get().getInsertDateTime());
                    item.setModify_id(login_id);
                    item.setModifyDateTime(LocalDateTime.now());
//                    log.info("item : " + item);
                    headService.itemSave(item);
                }
            }
        }

        // 상품소재 삭제로직 실행 : 데이터베이스에 코드사용중인 코드가 존재하면 리턴처리한다. , 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        if(deleteList.size()!=0){
            for (ItemDto itemDto : deleteList) {
                Optional<ItemPrice> itemPrice = headService.findByItemPriceByBiItemcode(itemDto.getBiItemcode());
                if(itemPrice.isPresent()){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), ResponseErrorCode.TP011.getDesc(), "문자", "상품코드 : "+itemPrice.get().getBiItemcode()));
                }else{
                    Optional<Item> itemOptional = headService.findByBiItemcode(itemDto.getBiItemcode());
                    if(itemOptional.isPresent()) {
                        log.info("삭제할 상품소재 코드 : "+itemOptional.get().getBiItemcode());
                        headService.findByItemDelete(itemOptional.get());
                    }else{
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : "+itemDto.getBiItemcode()));
                    }
                }
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 상품그룹 상품소재 리스트 호출 API
    @GetMapping("itemGroupCList")
    public ResponseEntity<Map<String,Object>> itemGroupCList(@RequestParam(value="bgItemGroupcode", defaultValue="") String bgItemGroupcode,
                                                                                               @RequestParam(value="bsItemGroupcodeS", defaultValue="") String bsItemGroupcodeS){
        log.info("itemGroupCList 호출");

//        log.info("bgItemGroupcode : "+bgItemGroupcode);
//        log.info("bsItemGroupcodeS : "+bsItemGroupcodeS);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> itemListData = new ArrayList<>();
        HashMap<String,Object> itemInfo;

        List<ItemListDto> itemListDtos = headService.findByItemList(bgItemGroupcode, bsItemGroupcodeS);
//        log.info("itemListDtos : "+itemListDtos);
        for (ItemListDto itemListDto : itemListDtos) {

            itemInfo = new HashMap<>();

            itemInfo.put("bgItemGroupcode", itemListDto.getBgItemGroupcode());
            itemInfo.put("bgName", itemListDto.getBgName());
            itemInfo.put("bsItemGroupcodeS", itemListDto.getBsItemGroupcodeS());
            itemInfo.put("bsName", itemListDto.getBsName());
            itemInfo.put("biItemcode", itemListDto.getBiItemcode());
            itemInfo.put("biItemSequence", itemListDto.getBiItemSequence());
            itemInfo.put("biName", itemListDto.getBiName());
            itemInfo.put("biUseYn", itemListDto.getBiUseYn());
            itemInfo.put("biRemark", itemListDto.getBiRemark());

            itemListData.add(itemInfo);
        }

        log.info("상품그룹 상품소재 리스트 : "+itemListData);
        log.info("상품그룹 상품소재 리스트 사이즈 : "+itemListData.size());
        data.put("gridListData",itemListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 가격페이지 호출 API
    @PostMapping("itemPrice")
    public ResponseEntity<Map<String,Object>> itemPrice(@RequestParam("priceUpload") MultipartFile priceUpload, @RequestParam("setDt") String setDt, HttpServletRequest request) throws Exception {
        log.info("itemPrice 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 로그인한 아이디 : "+login_id);

        String extension = FilenameUtils.getExtension(priceUpload.getOriginalFilename());
//        log.info("확장자 : " + extension);

        // 확장자가 엑셀이 맞는지 확인
        Workbook workbook;
        assert extension != null;
        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(priceUpload.getInputStream());  // -> .xlsx
        } else if(extension.equals("xls")) {
            workbook = new HSSFWorkbook(priceUpload.getInputStream());  // -> .xls
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP012.getCode(), ResponseErrorCode.TP012.getDesc(), null, null));
        }

        log.info("시작일 : "+setDt);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date setDate = formatter.parse(setDt);
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(setDate);
        cal.add(Calendar.DATE, -1); // 하루전으로 셋팅
        String closeDate = formatter.format(cal.getTime());
        log.info("종료임 : "+closeDate);

        String setDtReplace = setDt.replaceAll("-","");
        String closeDtReplace = closeDate.replaceAll("-","");
        log.info("setDtReplace : "+setDtReplace);

        Sheet worksheet = workbook.getSheetAt(0); // 첫번째 시트
        try {
            Row rowCheck = worksheet.getRow(0);
            Object cellDataCheck = rowCheck.getCell(0);
//            log.info("cellDataCheck : " + cellDataCheck.toString());
            if (!cellDataCheck.toString().equals("상품코드")) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP013.getCode(), ResponseErrorCode.TP013.getDesc(), null, null));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP013.getCode(), ResponseErrorCode.TP013.getDesc(), null, null));
        }


        int numOfRows = worksheet.getPhysicalNumberOfRows();
        log.info("데이터 총 길이 : "+numOfRows);

        ArrayList<ItemPrice> itemPriceSaveArrayList = new ArrayList<>();
        ArrayList<ItemPrice> itemPriceUpdateArrayList = new ArrayList<>();

        ArrayList<Object> excelList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        for(int i=1; i<worksheet.getPhysicalNumberOfRows(); i++){
            ItemPrice itemPrice = new ItemPrice();
            for (int j = 0; j < 12; j++) {
                Row row = worksheet.getRow(i);
                Cell cellData = row.getCell(j);
                CellType ct = cellData.getCellType();

                if (ct == CellType.BLANK) {
                    excelList.add("");
                } else {
                    excelList.add(getStringValue(cellData));
                }

            }

            log.info(i+"번째 excelList : "+excelList);

            Optional<Item> optionalItem = headService.findByBiItemcode(excelList.get(0).toString());
            if(!optionalItem.isPresent()){
                errorList.add(i+"번째 행의 코드가 존재하지 않습니다.");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), i+"번쨰 상품"+ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : "+excelList.get(0).toString()));
            }else{
                ItemPriceDto priceDto = headService.findByItemPrice(excelList.get(0).toString(), setDtReplace);
                if(priceDto != null){
                    errorList.add(i+"번째 행의 중복되는 적용일자가 존재합니다.");
//                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP015.getCode(), i+"번째 행 "+ResponseErrorCode.TP015.getDesc(), "문자", "상품코드 : "+excelList.get(0).toString()));
                }else{
                    log.info(i+"번째 excelList 시작");
                    ItemPriceDto itemPriceDto = headService.findByItemPrice(excelList.get(0).toString(), "99991231");
                    log.info(i+"번째 itemPriceDto : "+itemPriceDto);
                    if(itemPriceDto != null){
                        itemPrice = modelMapper.map(itemPriceDto, ItemPrice.class);
                        itemPrice.setModify_id(login_id);
                        itemPrice.setModifyDateTime(LocalDateTime.now());
                        itemPrice.setCloseDt(closeDtReplace);

                        itemPriceUpdateArrayList.add(itemPrice);
                        itemPrice = new ItemPrice();
                    }
                    itemPrice.setBiItemcode(excelList.get(0).toString());
                    itemPrice.setSetDt(setDtReplace);
                    itemPrice.setCloseDt("99991231");

                    try {
                        itemPrice.setBpBasePrice(Integer.parseInt((String) excelList.get(3)));
                        itemPrice.setBpAddPrice(Integer.parseInt((String) excelList.get(5)));
                        itemPrice.setBpPriceA(Integer.parseInt((String) excelList.get(6)));
                        itemPrice.setBpPriceB(Integer.parseInt((String) excelList.get(7)));
                        itemPrice.setBpPriceC(Integer.parseInt((String) excelList.get(8)));
                        itemPrice.setBpPriceD(Integer.parseInt((String) excelList.get(9)));
                        itemPrice.setBpPriceE(Integer.parseInt((String) excelList.get(10)));
                    } catch (NumberFormatException e){
                        log.info("문자가 들어가있음 : "+e);
                        errorList.add(i+"번째 행 금액에 문자가 들어갔습니다. 숫자만 입력해주세요.");
//                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP016.getCode(), i+"번째 행 "+ResponseErrorCode.TP016.getDesc(), "문자", "상품코드 : "+excelList.get(0).toString()));
                    }

                    itemPrice.setBiRemark(excelList.get(11).toString());

                    itemPrice.setInsert_id(login_id);
                    itemPrice.setInsertDateTime(LocalDateTime.now());
                    log.info(i+"번째 itemPrice : "+itemPrice);

                    itemPriceSaveArrayList.add(itemPrice);
                }
                excelList.clear();
            }
        }

//        log.info("수정 데이터리스트 itemPriceUpdateArrayList : "+itemPriceUpdateArrayList);
//        log.info("신규 데이터리스트 itemPriceSaveArrayList : "+itemPriceSaveArrayList);

        HashMap<String, Object> data = new HashMap<>();
        if(errorList.size() == 0){
            headService.itemPriceSaveAll(itemPriceSaveArrayList, itemPriceUpdateArrayList);
            data.put("errorListData",null);
        }else{
            data.put("errorListData",errorList);
        }
        return ResponseEntity.ok(res.dataSendSuccess(data));

    }

     // 엑셀 : cell의 데이터를 String 또는 Int형으로 변경
    public static String getStringValue(Cell cell) {
        String rtnValue;
        try {
            rtnValue = cell.getStringCellValue();
        } catch(IllegalStateException e) {
            rtnValue = Integer.toString((int)cell.getNumericCellValue());
        }
        return rtnValue;
    }

    // 상품그룹 가격페이지 리스트 호출 API
    @GetMapping("itemPriceList")
    public ResponseEntity<Map<String,Object>> itemPriceList(){
        log.info("itemPriceList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> itemPriceListData = new ArrayList<>();
        HashMap<String,Object> itemPriceInfo;

        List<ItemPriceListDto> itemPriceListDtos = headService.findByItemPriceList();
//        log.info("itemPriceListDtos : "+itemPriceListDtos.size());
        for (ItemPriceListDto itemPriceListDto : itemPriceListDtos) {

            itemPriceInfo = new HashMap<>();

            itemPriceInfo.put("biItemcode", itemPriceListDto.getBiItemcode());
            itemPriceInfo.put("bgName", itemPriceListDto.getBgName());
            itemPriceInfo.put("bsName", itemPriceListDto.getBsName());
            itemPriceInfo.put("biName", itemPriceListDto.getBiName());
            itemPriceInfo.put("setDt", itemPriceListDto.getSetDt());
            itemPriceInfo.put("closeDt", itemPriceListDto.getCloseDt());

            itemPriceInfo.put("bpBasePrice", itemPriceListDto.getBpBasePrice());
            itemPriceInfo.put("bpAddPrice", itemPriceListDto.getBpAddPrice());

            itemPriceInfo.put("bpPriceA", itemPriceListDto.getBpPriceA());
            itemPriceInfo.put("bpPriceB", itemPriceListDto.getBpPriceB());
            itemPriceInfo.put("bpPriceC", itemPriceListDto.getBpPriceC());
            itemPriceInfo.put("bpPriceD", itemPriceListDto.getBpPriceD());
            itemPriceInfo.put("bpPriceE", itemPriceListDto.getBpPriceE());

            itemPriceInfo.put("bpRemark", itemPriceListDto.getBiRemark());

            itemPriceListData.add(itemPriceInfo);
        }

//        log.info("상품 가격 리스트 : "+itemPriceListData);
//        log.info("상품 가격 리스트 사이즈 : "+itemPriceListData.size());
        data.put("gridListData",itemPriceListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 가격페이지 업데이트 및 수정 호출 API
    @PostMapping("itemPriceUpdate")
    public ResponseEntity<Map<String,Object>> itemPriceUpdate(@RequestBody ItemPriceSet itemPriceSet, HttpServletRequest request){
        log.info("itemPriceUpdate 호출");

        AjaxResponse res = new AjaxResponse();

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 접속한 아이디 : "+login_id);

        ArrayList<ItemPriceDto> updateList = itemPriceSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<ItemPriceDto> deleteList = itemPriceSet.getDelete(); // 제거 리스트 얻기

//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        List<ItemPrice> itemPriceList = new ArrayList<>();

        // 상품가격 수정 시작
        if(updateList.size()!=0){
            for (ItemPriceDto itemPriceDto : updateList) {
                Optional<ItemPrice> optionalItemPrice = headService.findByItemPriceOptional(itemPriceDto.getBiItemcode(), itemPriceDto.getSetDt(), itemPriceDto.getCloseDt());
                if (!optionalItemPrice.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 품목" + ResponseErrorCode.TP005.getDesc(), "문자", "상품코드 : " + itemPriceDto.getBiItemcode()));
                } else {
//                    log.info("수정 할 품목 상품코드 : " + optionalItemPrice.get().getBiItemcode());
                    ItemPrice itemPrice = new ItemPrice();

                    itemPrice.setBiItemcode(optionalItemPrice.get().getBiItemcode());
                    itemPrice.setSetDt(optionalItemPrice.get().getSetDt());
                    itemPrice.setCloseDt(optionalItemPrice.get().getCloseDt());

                    itemPrice.setBpBasePrice(itemPriceDto.getBpBasePrice());
                    itemPrice.setBpAddPrice(itemPriceDto.getBpAddPrice());
                    itemPrice.setBpPriceA(itemPriceDto.getBpPriceA());
                    itemPrice.setBpPriceB(itemPriceDto.getBpPriceB());
                    itemPrice.setBpPriceC(itemPriceDto.getBpPriceC());
                    itemPrice.setBpPriceD(itemPriceDto.getBpPriceD());
                    itemPrice.setBpPriceE(itemPriceDto.getBpPriceE());
                    itemPrice.setBiRemark(itemPriceDto.getBiRemark());

                    itemPrice.setInsert_id(optionalItemPrice.get().getInsert_id());
                    itemPrice.setInsertDateTime(optionalItemPrice.get().getInsertDateTime());
                    itemPrice.setModify_id(login_id);
                    itemPrice.setModifyDateTime(LocalDateTime.now());

//                    log.info("itemPrice : " + itemPrice);
                    itemPriceList.add(itemPrice);
                }
            }
        }
//        log.info("수정 itemPriceList : " +itemPriceList);
        if(itemPriceList.size() != 0){
            headService.itemPriceSave(itemPriceList);
            itemPriceList.clear();
        }

        // 상품가격 삭제로직 실행
        if(deleteList.size()!=0){
            for (ItemPriceDto itemPriceDto : deleteList) {
                Optional<ItemPrice> optionalItemPrice = headService.findByItemPriceOptional(itemPriceDto.getBiItemcode(), itemPriceDto.getSetDt(), itemPriceDto.getCloseDt());
                if(optionalItemPrice.isPresent()) {
                    log.info("삭제할 상품소재 코드 : "+optionalItemPrice.get().getBiItemcode());
                    itemPriceList.add(optionalItemPrice.get());
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 "+ResponseErrorCode.TP005.getDesc(), "문자", "상품코드 : "+itemPriceDto.getBiItemcode()));
                }
            }
        }
//        log.info("삭제 itemPriceList : " +itemPriceList);
        if(itemPriceList.size() != 0){
            headService.findByItemPriceDelete(itemPriceList);
            itemPriceList.clear();
        }

        return ResponseEntity.ok(res.success());
    }

    // 가맹점 특정상품가격 호출 API
    @PostMapping("franchisePrice")
    public ResponseEntity<Map<String,Object>> franchisePrice(@RequestBody FranchisePriceSet franchisePriceSet, HttpServletRequest request){
        log.info("franchisePrice 호출");

        AjaxResponse res = new AjaxResponse();

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 접속한 아이디 : "+login_id);

        ArrayList<FranchisePriceDto> addList = franchisePriceSet.getAdd(); // 추가 리스트 얻기
        ArrayList<FranchisePriceDto> updateList = franchisePriceSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<FranchisePriceDto> deleteList = franchisePriceSet.getDelete(); // 제거 리스트 얻기

//        log.info("추가 리스트 : "+addList);
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        List<FranchisePrice> franchisePriceList = new ArrayList<>();
        // 특정가격 적용품목 저장 시작.
        if(addList.size()!=0){
            Optional<Franchise> optionalFranchise = headService.findByFrCode(addList.get(0).getFrCode());
            if(!optionalFranchise.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(),"가맹점 "+ResponseErrorCode.TP009.getDesc(), "문자", "가맹점코드 : "+addList.get(0).getFrCode()));
            }else{
                for (FranchisePriceDto franchisePriceDto : addList) {
                    Optional<Item> optionalItem = headService.findByBiItemcode(franchisePriceDto.getBiItemcode());
                    if (!optionalItem.isPresent()) {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(),"상품"+ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : "+franchisePriceDto.getBiItemcode()));
                    }else{
                        Optional<FranchisePrice> optionalFranchisePrice = headService.findByFranchisePrice(franchisePriceDto.getBiItemcode(), franchisePriceDto.getFrCode());
                        if (optionalFranchisePrice.isPresent()) {
                            log.info("이미 존재하는 특정가격 적용품목");
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP017.getCode(),ResponseErrorCode.TP017.getDesc(), "문자", "상품코드 : "+franchisePriceDto.getBiItemcode()));
                        }else{
                            log.info("특정가격 적용품목 신규생성");
                            FranchisePrice franchisePrice = new FranchisePrice();
                            franchisePrice.setBiItemcode(franchisePriceDto.getBiItemcode());
                            franchisePrice.setFrCode(franchisePriceDto.getFrCode());

                            franchisePrice.setBfPrice(franchisePriceDto.getBfPrice());
                            franchisePrice.setBfRemark(franchisePriceDto.getBfRemark());
                            franchisePrice.setInsert_id(login_id);
                            franchisePrice.setInsertDateTime(LocalDateTime.now());

                            franchisePriceList.add(franchisePrice);
                        }
                    }
                }
            }
        }

//        log.info("저장 franchisePriceList : " +franchisePriceList);
        if(franchisePriceList.size() != 0){
            headService.franchisePriceSave(franchisePriceList);
            franchisePriceList.clear();
        }
        
        // 특정가격 적용품목 수정 시작.
        if(updateList.size()!=0){
            for (FranchisePriceDto franchisePriceDto : updateList) {
                Optional<FranchisePrice> optionalFranchisePrice = headService.findByFranchisePrice(franchisePriceDto.getBiItemcode(), franchisePriceDto.getFrCode());
                if (!optionalFranchisePrice.isPresent()) {
                    log.info("존재하지 않은 상품소재 코드 : " +franchisePriceDto.getBiItemcode());
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 품목" + ResponseErrorCode.TP005.getDesc(), "문자", "상품코드 : " + franchisePriceDto.getBiItemcode()));
                } else {
                    log.info("수정 할 품목 상품코드 : " + optionalFranchisePrice.get().getBiItemcode());
                    FranchisePrice franchisePrice = new FranchisePrice();

                    franchisePrice.setBiItemcode(optionalFranchisePrice.get().getBiItemcode());
                    franchisePrice.setFrCode(optionalFranchisePrice.get().getFrCode());

                    franchisePrice.setBfPrice(franchisePriceDto.getBfPrice());
                    franchisePrice.setBfRemark(franchisePriceDto.getBfRemark());

                    franchisePrice.setInsert_id(optionalFranchisePrice.get().getInsert_id());
                    franchisePrice.setInsertDateTime(optionalFranchisePrice.get().getInsertDateTime());
                    franchisePrice.setModify_id(login_id);
                    franchisePrice.setModifyDateTime(LocalDateTime.now());

//                    log.info("franchisePrice : " + franchisePrice);
                    franchisePriceList.add(franchisePrice);
                }
            }
        }

//        log.info("수정 franchisePriceList : " +franchisePriceList);
        if(franchisePriceList.size() != 0){
            headService.franchisePriceSave(franchisePriceList);
            franchisePriceList.clear();
        }

        // 특정가격 적용품목 삭제로직 실행
        if(deleteList.size()!=0){
            for (FranchisePriceDto franchisePriceDto : deleteList) {
                Optional<FranchisePrice> optionalFranchisePrice = headService.findByFranchisePrice(franchisePriceDto.getBiItemcode(), franchisePriceDto.getFrCode());
                if(optionalFranchisePrice.isPresent()) {
//                    log.info("삭제할 상품소재 코드 : "+optionalFranchisePrice.get().getBiItemcode());
                    franchisePriceList.add(optionalFranchisePrice.get());
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 "+ResponseErrorCode.TP005.getDesc(), "문자", "가맹점코드 : "+franchisePriceDto.getFrCode()+", "+"상품코드 : "+franchisePriceDto.getBiItemcode()));
                }
            }
        }

        if(franchisePriceList.size() != 0){
            headService.findByFranchisePriceDelete(franchisePriceList);
            franchisePriceList.clear();
        }

        return ResponseEntity.ok(res.success());
    }

    // 가맹점 특정상품가격 리스트 호출 API
    @GetMapping("franchisePriceList")
    public ResponseEntity<Map<String,Object>> franchisePriceList(@RequestParam("frCode") String frCode){
        log.info("franchisePriceList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> franchisePriceListData = new ArrayList<>();
        HashMap<String,Object> franchisePriceInfo;

//        log.info("frCode : "+frCode);

        List<FranchisePriceListDto> franchisePriceListDtos = headService.findByFranchisePriceList(frCode);
//        log.info("franchisePriceListDtos : "+franchisePriceListDtos);
        for (FranchisePriceListDto franchisePriceListDto : franchisePriceListDtos) {

            franchisePriceInfo = new HashMap<>();

            franchisePriceInfo.put("biItemcode", franchisePriceListDto.getBiItemcode());
            franchisePriceInfo.put("bgName", franchisePriceListDto.getBgName());
            franchisePriceInfo.put("bsName", franchisePriceListDto.getBsName());
            franchisePriceInfo.put("biName", franchisePriceListDto.getBiName());
            franchisePriceInfo.put("bfPrice", franchisePriceListDto.getBfPrice());
            franchisePriceInfo.put("bfRemark", franchisePriceListDto.getBfRemark());

            franchisePriceListData.add(franchisePriceInfo);
        }

//        log.info("가맹점 특정가격 적용품목 리스트 : "+franchisePriceListData);
        data.put("gridListData",franchisePriceListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가격셋팅 할인율 정보호출 API
    @GetMapping("addCostInfo")
    public ResponseEntity<Map<String,Object>> addCostInfo() {
        return headService.addCostInfo();
    }

    // 가격셋팅 할인율 설정 API
    @PostMapping("addCostUpdate")
    public ResponseEntity<Map<String,Object>> addCostUpdate(@ModelAttribute AddCostDto addCostDto, HttpServletRequest request) {
        return headService.findByAddCostUpdate(addCostDto, request);
    }



// @@@@@@@@@@@@@@@@@@@ 공지사항 게시판 API @@@@@@@@@@@@@@@@@@@@@@@@@@
    // 공지사항 게시판 - 리스트 호출
    @PostMapping("/noticeList")
    public ResponseEntity<Map<String,Object>> noticeList(@RequestParam("searchString")String searchString, @RequestParam("filterFromDt")String filterFromDt,
                                                             @RequestParam("filterToDt")String filterToDt,
                                                             Pageable pageable, HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime fromDt = null;
        if(filterFromDt != null){
            filterFromDt = filterFromDt+" 00:00:00.000";
            fromDt = LocalDateTime.parse(filterFromDt, formatter);
    //            log.info("fromDt :"+fromDt);
        }

        LocalDateTime toDt = null;
        if(filterToDt != null){
            filterToDt = filterToDt+" 23:59:59.999";
            toDt = LocalDateTime.parse(filterToDt, formatter);
    //            log.info("toDt :"+toDt);
        }

        return noticeService.noticeList(searchString, fromDt, toDt, pageable, request, "1");
    }

    //  공지사항 게시판 - 글보기
    @GetMapping("/noticeView")
    public ResponseEntity<Map<String,Object>> noticeView(@RequestParam("hnId") Long hnId, HttpServletRequest request) {
        return noticeService.noticeView(hnId, "1");
    }











}
