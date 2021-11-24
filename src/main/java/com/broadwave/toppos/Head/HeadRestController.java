package com.broadwave.toppos.Head;

import com.broadwave.toppos.Account.*;
import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchMapperDto;
import com.broadwave.toppos.Head.Franohise.FranchisInfoDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.Franohise.FranchiseListDto;
import com.broadwave.toppos.Head.Franohise.FranchiseMapperDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupSet;
import com.broadwave.toppos.Head.Item.Group.B.*;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemSet;
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
        log.info("s_userid : "+s_userid);

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


        FranchisInfoDto franchisInfoDto = headService.findByFranchiseInfo(frCode);
//        log.info("가맹점정보 호출 가맹점 코드("+frCode+") : "+franchisInfoDto);
        data.put("franchiseInfoData",franchisInfoDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점의 대한 지사배치 등록 API
    @PostMapping("franchiseAssignment")
    public ResponseEntity<Map<String,Object>> franchiseAssignment(@RequestParam(value="frCode", defaultValue="") String frCode,
                                                                    @RequestParam(value="brCode", defaultValue="") String brCode,
                                                                    @RequestParam(value="bot_brAssignState", defaultValue="") String bot_brAssignState,
                                                                    HttpServletRequest request){
        log.info("franchiseAssignment 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);

        Optional<Franchise> optionalFranohise  =  headService.findByFrCode(frCode);
        Optional<Branch> optionalBranch =  headService.findByBrCode(brCode);
        if(optionalFranohise.isPresent() && optionalBranch.isPresent()){
            Franchise franchise = new Franchise();

            franchise.setId(optionalFranohise.get().getId());
            franchise.setFrCode(optionalFranohise.get().getFrCode());
            franchise.setFrName(optionalFranohise.get().getFrName());
            franchise.setFrContractDt(optionalFranohise.get().getFrContractDt());
            franchise.setFrContractFromDt(optionalFranohise.get().getFrContractFromDt());
            franchise.setFrContractToDt(optionalFranohise.get().getFrContractToDt());
            franchise.setFrContractState(optionalFranohise.get().getFrContractState());
            franchise.setFrPriceGrade(optionalFranohise.get().getFrPriceGrade());
            franchise.setFrRemark(optionalFranohise.get().getFrRemark());

            franchise.setBrId(optionalBranch.get());
            franchise.setBrCode(optionalBranch.get().getBrCode());
            franchise.setBrAssignState(bot_brAssignState);

            franchise.setInsert_id(optionalFranohise.get().getInsert_id());
            franchise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());

            franchise.setModify_id(login_id);
            franchise.setModifyDateTime(LocalDateTime.now());

            headService.franchiseSave(franchise);
        }else{
            log.info("정보가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), ResponseErrorCode.TP005.getDesc(),ResponseErrorCode.TP006.getCode(), ResponseErrorCode.TP006.getDesc()));
        }

        return ResponseEntity.ok(res.success());
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

        log.info("추가 리스트 : "+addList);
        log.info("수정 리스트 : "+updateList);
        log.info("삭제 리스트 : "+deleteList);

        // 저장로직 실행 : 데이터베이스에 같은 코드가 존재하면 리턴처리한다.
        for (ItemGroupDto itemGroupDto : addList) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
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
            log.info("삭제할 대분류의 코드 : "+itemGroupDto.getBgItemGroupcode());
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if(optionalItemGroup.isPresent()) {
                headService.findByItemGroupDelete(optionalItemGroup.get());
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 코드 : " + itemGroupDto.getBgItemGroupcode()));
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

            itemGroupListData.add(itemGroupInfo);
        }

//        log.info("상품그룹 대분류 리스트 : "+itemGroupListData);
        data.put("gridListData",itemGroupListData);

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
                ItemGroupSInfo itemGroupSInfo = headService.findByBsItemGroupcodeS(updateList.get(0).getBgItemGroupcode(), updateList.get(0).getBsItemGroupcodeS());
                if(itemGroupSInfo != null) {
                    log.info("수정 할 중분류 코드 : "+itemGroupSInfo.getBsItemGroupcodeS());
                    for (ItemGroupSDto itemGroupSDto : updateList) {
                        ItemGroupS itemGroupS = new ItemGroupS();
                        itemGroupS.setBsItemGroupcodeS(itemGroupSInfo.getBsItemGroupcodeS());
                        itemGroupS.setBgItemGroupcode(optionalItemGroup.get());
                        itemGroupS.setBsName(itemGroupSDto.getBsName());
                        itemGroupS.setBsRemark(itemGroupSDto.getBsRemark());
                        itemGroupS.setInsert_id(itemGroupSInfo.getInsert_id());
                        itemGroupS.setInsertDateTime(itemGroupSInfo.getInsertDateTime());
                        itemGroupS.setModify_id(login_id);
                        itemGroupS.setModifyDateTime(LocalDateTime.now());
                        log.info("itemGroupS : " +itemGroupS);
                        headService.itemGroupSSave(itemGroupS);
                    }
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 중분류 "+ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 대분류, 중분류 코드 : " + updateList.get(0).getBgItemGroupcode()+", "+updateList.get(0).getBsItemGroupcodeS()));
                }
            }
        }

        // 중분류 삭제로직 실행 : 데이터베이스에 코드사용중인 코드가 존재하면 리턴처리한다. , 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        if(deleteList.size()!=0){
            for (ItemGroupSDto itemGroupSDto : deleteList) {
                ItemGroupS itemGroupS = headService.findByItemGroupcodeS(itemGroupSDto.getBgItemGroupcode(), itemGroupSDto.getBsItemGroupcodeS());
                if(itemGroupS != null) {
//                    log.info("삭제할 대상 : "+itemGroupS.getBsName());
                    log.info("삭제할 중분류의 코드 : "+itemGroupS.getBsItemGroupcodeS());
//                    log.info("삭제할 중분류 명칭 코드 : "+itemGroupS.getBsName());
                    headService.findByItemGroupSDelete(itemGroupS);
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 대분류, 중분류 코드 : " + itemGroupSDto.getBgItemGroupcode() + itemGroupSDto.getBgItemGroupcode()));
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

//        log.info("추가 리스트 : "+addList);
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
                        Optional<Item> optionalItem = headService.findByBiItemcode(addList.get(0).getBiItemcode());
                        if (optionalItem.isPresent()) {
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), "문자", "상품코드 : "+addList.get(0).getBiItemcode()));
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
                            item.setBiRemark(itemDto.getBiRemark());
                            item.setInsert_id(login_id);
                            item.setInsertDateTime(LocalDateTime.now());
                            log.info("item : " +item);
                            headService.itemSave(item);
                        }
                    }
                }
            }
        }

        // 상품소재 수정 시작.
        if(updateList.size()!=0){
            for (ItemDto itemDto : updateList) {
                Optional<Item> itemOptional = headService.findByBiItemcode(itemDto.getBgItemGroupcode());
                if (!itemOptional.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 상품소재 " + ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : " + itemDto.getBiItemcode()));
                } else {
                    log.info("수정 할 상품소재 코드 : " + itemOptional.get().getBiItemcode());
                    Item item = new Item();
                    item.setBsItemGroupcodeS(itemOptional.get().getBsItemGroupcodeS());
                    item.setBgItemGroupcode(itemOptional.get().getBgItemGroupcode());
                    item.setBiItemcode(itemOptional.get().getBiItemcode());
                    item.setBiItemSequence(itemOptional.get().getBiItemSequence());
                    item.setBiName(itemDto.getBiName());
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
                Optional<Item> itemOptional = headService.findByBiItemcode(itemDto.getBgItemGroupcode());
                if(itemOptional.isPresent()) {
                    log.info("삭제할 상품소재 코드 : "+itemOptional.get().getBiItemcode());
                    headService.findByItemDelete(itemOptional.get());
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : "+itemDto.getBiItemcode()));
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
            itemInfo.put("biRemark", itemListDto.getBiRemark());

            itemListData.add(itemInfo);
        }

        log.info("상품그룹 상품소재 리스트 : "+itemListData);
        data.put("gridListData",itemListData);


        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


}
