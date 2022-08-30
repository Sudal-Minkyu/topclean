package com.broadwave.toppos.Head;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Account.AcountDtos.AccountListDto;
import com.broadwave.toppos.Account.AcountDtos.AccountMapperDto;
import com.broadwave.toppos.Account.AcountDtos.AccountRole;
import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.Branoh.BranchDtos.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchDtos.BranchMapperDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseInfoDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseMapperDto;
import com.broadwave.toppos.Head.HeadService.*;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupSet;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupS;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSInfo;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSListDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSSet;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemSet;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.FranchisePriceSet;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.ItemPriceSet;
import com.broadwave.toppos.Head.Item.Price.ItemPrice;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeMapperDto;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionSetDto;
import com.broadwave.toppos.Manager.HmTemplate.HmTemplateDto;
import com.broadwave.toppos.Manager.ManagerService.CurrentService;
import com.broadwave.toppos.Manager.ManagerService.HmTemplateService;
import com.broadwave.toppos.User.UserService.ReceiptService;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    private final HeadService headService; // 본사 공용서비스
    private final HeadInfoService headInfoService; // 본사 정보호출 서비스
    private final NoticeService noticeService; // 공지사항 서비스
    private final ModelMapper modelMapper;
    private final ReceiptService receiptService;
    private final ItemPriceService itemPriceService; // 상품그룹가격 서비스
    private final SalesService salesService; // 지사의 매출현황 관련 서비스
    private final HmTemplateService hmTemplateService; // 문자메세지 서비스
    private final CurrentService currentService; // 현황페이지 서비스
    private final SummaryService summaryService; // 정산페이지 서비스
    private final PromotionService promotionService; // 행사페이지 서비스

    @Autowired
    public HeadRestController(AccountService accountService, NoticeService noticeService, ModelMapper modelMapper,
                              HeadService headService, HeadInfoService headInfoService, ReceiptService receiptService, ItemPriceService itemPriceService, SalesService salesService,
                              HmTemplateService hmTemplateService, CurrentService currentService, SummaryService summaryService, PromotionService promotionService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.noticeService = noticeService;
        this.headService = headService;
        this.headInfoService = headInfoService;
        this.receiptService = receiptService;
        this.itemPriceService = itemPriceService;
        this.salesService = salesService;
        this.hmTemplateService = hmTemplateService;
        this.currentService = currentService;
        this.summaryService = summaryService;
        this.promotionService = promotionService;
    }

    //@@@@@@@@@@@@@@@@@@@@@ 본사 메인화면 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 현재 로그인한 본사 정보 가져오기
    @GetMapping("headHeaderData")
    @ApiOperation(value = "현재 로그인한 본사 점보조회", notes = "현재 로그인한 본사의 정보를 가져온다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headHeaderData(HttpServletRequest request) {
        return headInfoService.headHeaderData(request);
    }

    // 사용자 등록 API
    @PostMapping("accountSave")
    public ResponseEntity<Map<String, Object>> accountSave(@ModelAttribute AccountMapperDto accountMapperDto, HttpServletRequest request) {
        log.info("accountSave 호출");

        AjaxResponse res = new AjaxResponse();

        Account account = modelMapper.map(accountMapperDto, Account.class);

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("접속한 아이디 : " + login_id);

//        log.info("account : "+account);
//        log.info("role : "+account.getRole());
        Optional<Account> optionalAccount = accountService.findByUserid(account.getUserid());
        if (optionalAccount.isPresent()) {
            log.info("수정합니다.");
            // 수정일때
            account.setId(optionalAccount.get().getId());
            account.setUserid(optionalAccount.get().getUserid());
            account.setPassword(optionalAccount.get().getPassword());
            account.setInsert_id(optionalAccount.get().getInsert_id());
            account.setInsertDateTime(optionalAccount.get().getInsertDateTime());
            account.setModify_id(login_id);
            account.setModifyDateTime(LocalDateTime.now());

            Account accountSave = accountService.saveUpdate(account);
            log.info("사용자 업데이트 저장 성공 : id '" + accountSave.getUserid() + "'");
        } else {
            log.info("신규입니다.");
            // 신규일때
            account.setInsert_id(login_id);
            account.setInsertDateTime(LocalDateTime.now());

            Account accountSave = accountService.save(account);
            log.info("사용자 신규 저장 성공 : id '" + accountSave.getUserid() + "'");
        }
        return ResponseEntity.ok(res.success());

    }

    // 지사 등록 API
    @PostMapping("branchSave")
    public ResponseEntity<Map<String, Object>> branchSave(@ModelAttribute BranchMapperDto branohMapperDto, HttpServletRequest request) {
        return headService.branchSave(branohMapperDto, request);
    }

    // 가맹점 등록 API
    @PostMapping("franchiseSave")
    public ResponseEntity<Map<String, Object>> franchiseSave(@ModelAttribute FranchiseMapperDto franchiseMapperDto, HttpServletRequest request) {
        return headService.franchiseSave(franchiseMapperDto, request);
    }

    // 사용자 리스트 API
    @GetMapping("accountList")
    public ResponseEntity<Map<String, Object>> accountList(@RequestParam(value = "s_userid", defaultValue = "") String s_userid,
                                                           @RequestParam(value = "s_username", defaultValue = "") String s_username,
                                                           @RequestParam(value = "s_role", defaultValue = "") String s_role,
                                                           @RequestParam(value = "s_frCode", defaultValue = "") String s_frCode,
                                                           @RequestParam(value = "s_brCode", defaultValue = "") String s_brCode) {
        log.info("accountList 호출");

        AccountRole role = null;

        if (!s_role.equals("")) {
            role = AccountRole.valueOf(s_role);
        }

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> accountListData = new ArrayList<>();
        HashMap<String, Object> accounttInfo;

        List<AccountListDto> accountListDtos = accountService.findByAccountList(s_userid, s_username, role, s_frCode, s_brCode);
        log.info("accountListDtos : " + accountListDtos);

        for (AccountListDto account : accountListDtos) {

            accounttInfo = new HashMap<>();

            accounttInfo.put("userid", account.getUserid());
            accounttInfo.put("roleCode", account.getRole().getCode());
            accounttInfo.put("role", account.getRole().getDesc());
            accounttInfo.put("username", account.getUsername());
            accounttInfo.put("usertel", account.getUsertel());
            accounttInfo.put("useremail", account.getUseremail());
            if (account.getBrCode().equals("no")) {
                accounttInfo.put("brCode", "해당안됨");
            } else {
                accounttInfo.put("brCode", account.getBrCode());
            }
            if (account.getFrCode().equals("not")) {
                accounttInfo.put("frCode", "해당안됨");
            } else {
                accounttInfo.put("frCode", account.getFrCode());
            }
            accounttInfo.put("userremark", account.getUserremark());
            accountListData.add(accounttInfo);

        }

        log.info("사용자리스트 : " + accountListData);
        data.put("gridListData", accountListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    // 지사 리스트 API
    @GetMapping("branchList")
    public ResponseEntity<Map<String, Object>> branchList(@RequestParam(value = "brName", defaultValue = "") String brName,
                                                          @RequestParam(value = "brCode", defaultValue = "") String brCode,
                                                          @RequestParam(value = "brContractState", defaultValue = "") String brContractState) {
        log.info("BranohList 호출");

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> branchListData = new ArrayList<>();
        HashMap<String, Object> branchsetInfo;

        List<BranchListDto> branchListDtos = headService.findByBranchList(brName, brCode, brContractState);
        log.info("branchListDtos : " + branchListDtos);

        for (BranchListDto branch : branchListDtos) {

            branchsetInfo = new HashMap<>();

            branchsetInfo.put("brCode", branch.getBrCode());
            branchsetInfo.put("brName", branch.getBrName());
            branchsetInfo.put("brTelNo", branch.getBrTelNo());
            branchsetInfo.put("brContractDt", branch.getBrContractDt());
            branchsetInfo.put("brContractFromDt", branch.getBrContractFromDt());
            branchsetInfo.put("brContractToDt", branch.getBrContractToDt());
            branchsetInfo.put("brContractState", branch.getBrContractState());
            if (branch.getBrContractState().equals("01")) {
                branchsetInfo.put("brContractStateValue", "진행중");
            } else {
                branchsetInfo.put("brContractStateValue", "계약완료");
            }
            branchsetInfo.put("brCarculateRateBr", branch.getBrCarculateRateBr());
            branchsetInfo.put("brCarculateRateFr", branch.getBrCarculateRateFr());
            branchsetInfo.put("brRoyaltyRateBr", branch.getBrRoyaltyRateBr());
            branchsetInfo.put("brRoyaltyRateFr", branch.getBrRoyaltyRateFr());

            branchsetInfo.put("brRemark", branch.getBrRemark());
            branchListData.add(branchsetInfo);

        }

        log.info("지사 리스트 : " + branchListData);
        data.put("gridListData", branchListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 리스트 API
    @GetMapping("franchiseList")
    @ApiOperation(value = "본사 가맹점 리스트 호출", notes = "본사가 가맹점 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> franchiseList(@RequestParam(value = "brAssignState", defaultValue = "") String brAssignState,
                                                             @RequestParam(value = "frName", defaultValue = "") String frName,
                                                             @RequestParam(value = "frRefCode", defaultValue = "") String frRefCode,
                                                             @RequestParam(value = "frCode", defaultValue = "") String frCode,
                                                             @RequestParam(value = "frContractState", defaultValue = "") String frContractState) {
        log.info("franohiseList 호출");
        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> franohiseListData = new ArrayList<>();
        HashMap<String, Object> franohisetInfo;

        List<FranchiseListDto> franchiseListDtos = headService.findByFranchiseList("", brAssignState, frName, frCode, frRefCode, frContractState);
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
            if (franohise.getBrAssignState().equals("01")) {
                franohisetInfo.put("brAssignStateValue", "진행중");
            } else {
                franohisetInfo.put("brAssignStateValue", "계약완료");
            }
            if (franohise.getFrContractState().equals("01")) {
                franohisetInfo.put("frContractStateValue", "진행중");
            } else {
                franohisetInfo.put("frContractStateValue", "계약완료");
            }
            franohisetInfo.put("frPriceGrade", franohise.getFrPriceGrade());
            franohisetInfo.put("frTagNo", franohise.getFrTagNo());
            franohisetInfo.put("frTagType", franohise.getFrTagType());
            franohisetInfo.put("frEstimateDuration", franohise.getFrEstimateDuration());
            franohisetInfo.put("frRemark", franohise.getFrRemark());
            franohisetInfo.put("brName", franohise.getBrName());
            franohisetInfo.put("frBusinessNo", franohise.getFrBusinessNo());
            franohisetInfo.put("frRpreName", franohise.getFrRpreName());
            franohisetInfo.put("frTelNo", franohise.getFrTelNo());

            franohisetInfo.put("frPostNo", franohise.getFrPostNo());
            franohisetInfo.put("frAddress", franohise.getFrAddress());
            franohisetInfo.put("frAddressDetail", franohise.getFrAddressDetail());

            franohisetInfo.put("frCarculateRateBr", franohise.getFrCarculateRateBr());
            franohisetInfo.put("frCarculateRateFr", franohise.getFrCarculateRateFr());
            franohisetInfo.put("frRoyaltyRateBr", franohise.getFrRoyaltyRateBr());
            franohisetInfo.put("frRoyaltyRateFr", franohise.getFrRoyaltyRateFr());

            franohisetInfo.put("frUrgentDayYn", franohise.getFrUrgentDayYn());

            franohisetInfo.put("frManualPromotionYn", franohise.getFrManualPromotionYn());
            franohisetInfo.put("frCardTid", franohise.getFrCardTid());

            franohiseListData.add(franohisetInfo);

        }

        log.info("가맹점리스트 : " + franohiseListData);
        data.put("gridListData", franohiseListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 사용자 삭제 API
    @PostMapping("accountDelete")
    public ResponseEntity<Map<String, Object>> accountDelete(@RequestParam(value = "userid", defaultValue = "") String userid) {
        log.info("accountDelete 호출");
        log.info("삭제 할 USER ID : " + userid);

        AjaxResponse res = new AjaxResponse();

        Optional<Account> optionalAccount = accountService.findByUserid(userid);
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 " + ResponseErrorCode.TP005.getDesc(), "문자", "유저 아이디 : " + userid));
        } else {
            log.info(userid + " 사용자 삭제완료");
            accountService.findByAccountDelete(optionalAccount.get());
        }

        return ResponseEntity.ok(res.success());
    }

    // 지사 삭제 API
    @PostMapping("branchDelete")
    public ResponseEntity<Map<String, Object>> branchDelete(@RequestParam(value = "brCode", defaultValue = "") String brCode) {
        return headService.branchDelete(brCode);
    }

    // 가맹점 삭제 API
    @PostMapping("franchiseDelete")
    public ResponseEntity<Map<String, Object>> franchiseDelete(@RequestParam(value = "frCode", defaultValue = "") String frCode) {
        return headService.franchiseDelete(frCode);
    }

    // 해당 지사에 배정된 가맹점 리스트 호출
    @GetMapping("branchAssignList")
    public ResponseEntity<Map<String, Object>> branchAssignList(@RequestParam(value = "brCode", defaultValue = "") String brCode) {
        log.info("branchAssignList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> franohiseListData = new ArrayList<>();
        HashMap<String, Object> franohisetInfo;

        List<FranchiseListDto> franchiseListDtos = headService.findByFranchiseList(brCode, "", "", "", "", "");
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
            if (franohise.getBrAssignState().equals("01")) {
                franohisetInfo.put("brAssignStateValue", "진행중");
            } else {
                franohisetInfo.put("brAssignStateValue", "계약완료");
            }
            if (franohise.getFrContractState().equals("01")) {
                franohisetInfo.put("frContractStateValue", "진행중");
            } else {
                franohisetInfo.put("frContractStateValue", "계약완료");
            }
            franohisetInfo.put("frPriceGrade", franohise.getFrPriceGrade());
            franohisetInfo.put("frTagNo", franohise.getFrTagNo());
            franohisetInfo.put("frRemark", franohise.getFrRemark());
            franohisetInfo.put("brName", franohise.getBrName());

            franohisetInfo.put("frCarculateRateBr", franohise.getFrCarculateRateBr());
            franohisetInfo.put("frCarculateRateFr", franohise.getFrCarculateRateFr());
            franohisetInfo.put("frRoyaltyRateBr", franohise.getFrRoyaltyRateBr());
            franohisetInfo.put("frRoyaltyRateFr", franohise.getFrRoyaltyRateFr());

            franohiseListData.add(franohisetInfo);
        }

        log.info("해당 지사에 배정된 가맹점 리스트 지사코드(" + brCode + ") : " + franohiseListData);
        data.put("gridListData", franohiseListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 해당 가맹점 선택시 가맹점 정보 호출하기
    @GetMapping("franchiseInfo")
    public ResponseEntity<Map<String, Object>> franchiseInfo(@RequestParam(value = "frCode", defaultValue = "") String frCode) {
        log.info("franchiseInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        FranchiseInfoDto franchiseInfoDto = headService.findByFranchiseInfo(frCode);
        log.info("franchiseInfoDto : " + franchiseInfoDto);
        data.put("franchiseInfoData", franchiseInfoDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점의 대한 지사배치 등록 API
    @PostMapping("franchiseAssignment")
    public ResponseEntity<Map<String, Object>> franchiseAssignment(@RequestParam(value = "frCode", defaultValue = "") String frCode,
                                                                   @RequestParam(value = "brCode", defaultValue = "") String brCode,
                                                                   @RequestParam(value = "bot_brAssignState", defaultValue = "") String bot_brAssignState,
                                                                   @RequestParam(value = "bot_frCarculateRateBr", defaultValue = "") Double bot_frCarculateRateBr,
                                                                   @RequestParam(value = "bot_frCarculateRateFr", defaultValue = "") Double bot_frCarculateRateFr,
                                                                   @RequestParam(value = "bot_frRoyaltyRateBr", defaultValue = "") Double bot_frRoyaltyRateBr,
                                                                   @RequestParam(value = "bot_frRoyaltyRateFr", defaultValue = "") Double bot_frRoyaltyRateFr,
                                                                   HttpServletRequest request) {
        return headService.franchiseAssignment(frCode, brCode, bot_brAssignState, bot_frCarculateRateBr, bot_frCarculateRateFr, bot_frRoyaltyRateBr, bot_frRoyaltyRateFr, request);
    }

    // 유저아이디 중복확인 API
    @GetMapping("useridOverlap")
    public ResponseEntity<Map<String, Object>> useridOverlap(@RequestParam(value = "userid", defaultValue = "") String userid) {
        log.info("useridOverlap 호출");

        log.info("유저아이디중복확인");
        AjaxResponse res = new AjaxResponse();

        if (userid.equals("")) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP004.getCode(), ResponseErrorCode.TP004.getDesc(), null, null));
        }

        Optional<Account> accountOptional = accountService.findByUserid(userid);
        if (accountOptional.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP001.getCode(), ResponseErrorCode.TP001.getDesc(), null, null));
        } else {
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }

    // 가맹점코드 중복확인 API
    @GetMapping("franchiseOverlap")
    public ResponseEntity<Map<String, Object>> franchiseOverlap(@RequestParam(value = "frCode", defaultValue = "") String frCode) {
        return headService.franchiseOverlap(frCode);
    }

    // 지점코드 중복확인 API
    @GetMapping("branchOverlap")
    public ResponseEntity<Map<String, Object>> branchOverlap(@RequestParam(value = "brCode", defaultValue = "") String brCode) {
        return headService.branchOverlap(brCode);
    }

    // @@@@@@@@@@@@@@@@@@@@ 상품그룹 관련 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 상품그룹 대분류 호출 API
    @PostMapping("itemGroupA")
    public ResponseEntity<Map<String, Object>> itemGroupA(@RequestBody ItemGroupSet itemGroupSet, HttpServletRequest request) {
        log.info("itemGroupA 호출");
        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 로그인한 아이디 : " + login_id);

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
            log.info("수정할 대분류의 코드 : " + itemGroupDto.getBgItemGroupcode());
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if (optionalItemGroup.isPresent()) {
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
            } else {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 " + ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 코드 : " + itemGroupDto.getBgItemGroupcode()));
            }
        }


        // 삭제로직 실행 : 데이터베이스에 코드사용중인 코드가 존재하면 리턴처리한다. , 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        for (ItemGroupDto itemGroupDto : deleteList) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(itemGroupDto.getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 " + ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 코드 : " + itemGroupDto.getBgItemGroupcode()));
            } else {
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
    public ResponseEntity<Map<String, Object>> itemGroupAList() {
        log.info("itemGroupAList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> itemGroupListData = new ArrayList<>();
        HashMap<String, Object> itemGroupInfo;

        List<ItemGroupDto> itemGroupListDtos = headService.findByItemGroupList();
        log.info("itemGroupListDtos : " + itemGroupListDtos);

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
        data.put("gridListData", itemGroupListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 대분류 명칭리스트 호출 API
    @GetMapping("itemGroupNameList")
    public ResponseEntity<Map<String, Object>> itemGroupNameList() {
        log.info("itemGroupNameList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemGroupNameListDto> itemGroupNameListDtos = headService.findByItemGroupName();
        log.info("itemGroupNameListDtos : " + itemGroupNameListDtos);
        data.put("itemGroupNameList", itemGroupNameListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 중분류 호출 API
    @PostMapping("itemGroupB")
    public ResponseEntity<Map<String, Object>> itemGroupB(@RequestBody ItemGroupSSet itemGroupSSet, HttpServletRequest request) {
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
        if (addList.size() != 0) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(addList.get(0).getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "저장 할 대분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
            } else {
                for (ItemGroupSDto itemGroupSDto : addList) {
//                    log.info("같은 코드 존재하지 않음 신규생성");
                    log.info("itemGroupSDto : " + itemGroupSDto.getBsItemGroupcodeS());
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
        if (updateList.size() != 0) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(updateList.get(0).getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 대분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
            } else {
                for (ItemGroupSDto itemGroupSDto : updateList) {
                    ItemGroupSInfo itemGroupSInfo = headService.findByBsItemGroupcodeS(itemGroupSDto.getBgItemGroupcode(), itemGroupSDto.getBsItemGroupcodeS());
                    if (itemGroupSInfo != null) {
                        log.info("수정 할 중분류 코드 : " + itemGroupSInfo.getBsItemGroupcodeS());
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
                        log.info("itemGroupS : " + itemGroupS);
                        headService.itemGroupSSave(itemGroupS);
                    } else {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 중분류 " + ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 대분류, 중분류 코드 : " + itemGroupSDto.getBgItemGroupcode() + ", " + itemGroupSDto.getBsItemGroupcodeS()));
                    }
                }
            }
        }

        // 중분류 삭제로직 실행 : 데이터베이스에 코드사용중인 코드가 존재하면 리턴처리한다. , 데이터베이스에 코드가 존재하지 않으면 리턴처리한다.
        if (deleteList.size() != 0) {

            Optional<Item> item = headService.findByBiItem(deleteList.get(0).getBgItemGroupcode(), deleteList.get(0).getBsItemGroupcodeS());
            if (item.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), ResponseErrorCode.TP011.getDesc(), "문자", "상품코드 : " + item.get().getBiItemcode()));
            } else {
                for (ItemGroupSDto itemGroupSDto : deleteList) {
                    ItemGroupS itemGroupS = headService.findByItemGroupcodeS(itemGroupSDto.getBgItemGroupcode(), itemGroupSDto.getBsItemGroupcodeS());
                    if (itemGroupS != null) {
//                    log.info("삭제할 대상 : "+itemGroupS.getBsName());
                        log.info("삭제할 중분류의 코드 : " + itemGroupS.getBsItemGroupcodeS());
                        headService.findByItemGroupSDelete(itemGroupS);
                    } else {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 " + ResponseErrorCode.TP009.getDesc(), "문자", "다시 시도해주세요. 대분류, 중분류 코드 : " + itemGroupSDto.getBgItemGroupcode() + itemGroupSDto.getBgItemGroupcode()));
                    }
                }
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 상품그룹 중분류 리스트 호출 API
    @GetMapping("itemGroupBList")
    public ResponseEntity<Map<String, Object>> itemGroupBList(@RequestParam(value = "bgItemGroupcode", defaultValue = "") String bgItemGroupcode) {
        log.info("itemGroupBList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> itemGroupSListData = new ArrayList<>();
        HashMap<String, Object> itemGroupSInfo;

        Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(bgItemGroupcode);
        if (!optionalItemGroup.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "조회 할 " + ResponseErrorCode.TP003.getDesc(), null, null));
        } else {
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
            data.put("gridListData", itemGroupSListData);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 상품소재 호출 API
    @PostMapping("itemGroupC")
    public ResponseEntity<Map<String, Object>> itemGroupC(@RequestBody ItemSet itemSet, HttpServletRequest request) {
        log.info("itemGroupC 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 로그인한 아이디 : " + login_id);

        ArrayList<ItemDto> addList = itemSet.getAdd(); // 추가 리스트 얻기
        ArrayList<ItemDto> updateList = itemSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<ItemDto> deleteList = itemSet.getDelete(); // 제거 리스트 얻기

//        log.info("추가 리스트 : "+addList.get(0).getBgItemGroupcode());
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        // 상품소재 저장 시작.
        if (addList.size() != 0) {
            Optional<ItemGroup> optionalItemGroup = headService.findByBgItemGroupcode(addList.get(0).getBgItemGroupcode());
            if (!optionalItemGroup.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "저장 할 상품소재의 대분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
            } else {
                ItemGroupS itemGroupS = headService.findByItemGroupcodeS(addList.get(0).getBgItemGroupcode(), addList.get(0).getBsItemGroupcodeS());
                if (itemGroupS == null) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "저장 할 상품소재의 중분류 " + ResponseErrorCode.TP009.getDesc(), null, null));
                } else {
                    for (ItemDto itemDto : addList) {
                        Optional<Item> optionalItem = headService.findByBiItemcode(itemDto.getBiItemcode());
                        if (optionalItem.isPresent()) {
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), "문자", "상품코드 : " + itemDto.getBiItemcode()));
                        } else {
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
        if (updateList.size() != 0) {
            for (ItemDto itemDto : updateList) {
                Optional<Item> itemOptional = headService.findByBiItemcode(itemDto.getBiItemcode());
                if (!itemOptional.isPresent()) {
                    log.info("존재하지 않은 상품소재 코드 : " + itemDto.getBiItemcode());
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
        if (deleteList.size() != 0) {
            for (ItemDto itemDto : deleteList) {
                Optional<ItemPrice> itemPrice = headService.findByItemPriceByBiItemcode(itemDto.getBiItemcode());
                if (itemPrice.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), ResponseErrorCode.TP011.getDesc(), "문자", "상품코드 : " + itemPrice.get().getBiItemcode()));
                } else {
                    Optional<Item> itemOptional = headService.findByBiItemcode(itemDto.getBiItemcode());
                    if (itemOptional.isPresent()) {
                        log.info("삭제할 상품소재 코드 : " + itemOptional.get().getBiItemcode());
                        headService.findByItemDelete(itemOptional.get());
                    } else {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "삭제 할 " + ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : " + itemDto.getBiItemcode()));
                    }
                }
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 상품그룹 상품소재 리스트 호출 API
    @GetMapping("itemGroupCList")
    public ResponseEntity<Map<String, Object>> itemGroupCList(@RequestParam(value = "bgItemGroupcode", defaultValue = "") String bgItemGroupcode,
                                                              @RequestParam(value = "bsItemGroupcodeS", defaultValue = "") String bsItemGroupcodeS,
                                                              @RequestParam(value = "biItemcode", defaultValue = "") String biItemcode,
                                                              @RequestParam(value = "biName", defaultValue = "") String biName) {
        log.info("itemGroupCList 호출");

//        log.info("bgItemGroupcode : "+bgItemGroupcode);
//        log.info("bsItemGroupcodeS : "+bsItemGroupcodeS);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> itemListData = new ArrayList<>();
        HashMap<String, Object> itemInfo;

        List<ItemListDto> itemListDtos = headService.findByItemList(bgItemGroupcode, bsItemGroupcodeS, biItemcode, biName);
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

        log.info("상품그룹 상품소재 리스트 : " + itemListData);
        log.info("상품그룹 상품소재 리스트 사이즈 : " + itemListData.size());
        data.put("gridListData", itemListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // @@@@@@@@@@@@@@@@@@@@ 상품 가격관리 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 상품그룹 가격페이지 호출 API
    @PostMapping("itemPrice")
    public ResponseEntity<Map<String, Object>> itemPrice(@RequestParam("priceUpload") MultipartFile priceUpload, @RequestParam("setDt") String setDt, HttpServletRequest request) throws Exception {
        return itemPriceService.itemPrice(priceUpload, setDt, request);
    }

    // 상품그룹 가격페이지 리스트 호출 API
    @PostMapping("itemPriceList")
    public ResponseEntity<Map<String, Object>> itemPriceList(@RequestParam("bgName") String bgName, @RequestParam("biItemcode") String biItemcode,
                                                             @RequestParam("biName") String biName, @RequestParam("setDt") String setDt) {
        return itemPriceService.findByItemPriceList(bgName, biItemcode, biName, setDt);
    }

    // 상품그룹 가격페이지 등록되지 않은 상품 조회리스트 호출 API
    @GetMapping("itemPriceNotList")
    @ApiOperation(value = "상품그룹 가격페이지 등록되지 않은 상품 조회", notes = "가격 등록되지 않은 상품리스트를 보낸다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> itemPriceNotList(@RequestParam("bgName") String bgName, @RequestParam("biItemcode") String biItemcode,
                                                                @RequestParam("biName") String biName, @RequestParam("setDt") String setDt) {
        return itemPriceService.itemPriceNotList(bgName, biItemcode, biName, setDt);
    }

    // 상품그룹 가격페이지 업데이트 및 수정 호출 API
    @PostMapping("itemPriceUpdate")
    public ResponseEntity<Map<String, Object>> itemPriceUpdate(@RequestBody ItemPriceSet itemPriceSet, HttpServletRequest request) {
        return itemPriceService.itemPriceUpdate(itemPriceSet, request);
    }

    // 가맹점 특정상품가격 호출 API
    @PostMapping("franchisePrice")
    public ResponseEntity<Map<String, Object>> franchisePrice(@RequestBody FranchisePriceSet franchisePriceSet, HttpServletRequest request) {
        return itemPriceService.franchisePrice(franchisePriceSet, request);
    }

    // 가맹점 특정상품가격 리스트 호출 API
    @GetMapping("franchisePriceList")
    public ResponseEntity<Map<String, Object>> franchisePriceList(@RequestParam("frCode") String frCode) {
        return itemPriceService.franchisePriceList(frCode);
    }

    // 가격셋팅 할인율 정보호출 API
    @GetMapping("addCostInfo")
    public ResponseEntity<Map<String, Object>> addCostInfo() {
        return headService.addCostInfo();
    }

    // 가격셋팅 할인율 설정 API
    @PostMapping("addCostUpdate")
    public ResponseEntity<Map<String, Object>> addCostUpdate(@ModelAttribute AddCostDto addCostDto, HttpServletRequest request) {
        return headService.findByAddCostUpdate(addCostDto, request);
    }


    // @@@@@@@@@@@@@@@@@@@ 공지사항 게시판 API @@@@@@@@@@@@@@@@@@@@@@@@@@
    // 공지사항 게시판 - 리스트 호출
    @PostMapping("/noticeList")
    public ResponseEntity<Map<String, Object>> noticeList(@RequestParam("hnType") String hnType, @RequestParam("searchString") String searchString, @RequestParam("filterFromDt") String filterFromDt,
                                                          @RequestParam("filterToDt") String filterToDt,
                                                          Pageable pageable) {
        return noticeService.noticeList(hnType, searchString, filterFromDt.replaceAll("-", ""), filterToDt.replaceAll("-", ""), pageable, "1");
    }

    //  공지사항 게시판 - 글보기
    @GetMapping("/noticeView")
    public ResponseEntity<Map<String, Object>> noticeView(@RequestParam("hnId") Long hnId) {
        return noticeService.noticeView(hnId, "1");
    }

    //  공지사항 게시판 - 등록&수정
    @PostMapping("noticeSave")
    @ApiOperation(value = "공지사항 등록및수정", notes = "본사에서 공지사항을 등록하거나 수정한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> noticeSave(@ModelAttribute NoticeMapperDto noticeMapperDto, HttpServletRequest request) throws IOException {
        return noticeService.noticeSave(noticeMapperDto, request, "1");
    }

    //  공지사항 게시판 - 글삭제
    @PostMapping("noticeDelete")
    @ApiOperation(value = "공지사항 글삭제", notes = "본사에서 공지사항 글을 삭제한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> noticeDelete(@RequestParam("hnId") Long hnId) {
        return noticeService.noticeDelete(hnId);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 나의 정보관리 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 현재 본사의 정보 호출하기
    @GetMapping("myInfo")
    public ResponseEntity<Map<String, Object>> headMyInfo(HttpServletRequest request) {
        return headInfoService.headMyInfo(request);
    }

    // 본사 나의정보관리 수정 API
    @PostMapping("headMyInfoSave")
    public ResponseEntity<Map<String, Object>> branchMyInfoSave(@RequestParam(value = "userEmail", defaultValue = "") String userEmail,
                                                                @RequestParam(value = "userTel", defaultValue = "") String userTel,
                                                                @RequestParam(value = "nowPassword", defaultValue = "") String nowPassword,
                                                                @RequestParam(value = "newPassword", defaultValue = "") String newPassword,
                                                                @RequestParam(value = "checkPassword", defaultValue = "") String checkPassword,
                                                                HttpServletRequest request) {
        return headInfoService.headMyInfoSave(userEmail, userTel, nowPassword, newPassword, checkPassword, request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 나의 정보관리 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 지사리스트, 지사소속된 가맹점 리스트 호출 API
    @GetMapping("headBrFrInfoList")
    @ApiOperation(value = "지사와 가맹점 정보호출", notes = "지사와 가맹점 정보를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBrFrInfoList() {
        return headInfoService.headBrFrInfoList();
    }

    //@@@@@@@@@@@@@@@@@@@@@ 접수현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 접수현황 왼쪽 리스트 호출 API
    @GetMapping("headReceiptList")
    @ApiOperation(value = "접수현황 왼쪽 호출", notes = "접수날짜의 따라 왼쪽 접수현황 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                               @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.headReceiptList(branchId, franchiseId, filterFromDt, filterToDt);
    }

    // 본사 접수현황 오른쪽 리스트 호출 API
    @GetMapping("headReceiptSubList")
    @ApiOperation(value = "접수현황 오른쪽 호출", notes = "선택한 접수날짜의 따라 오른쪽 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                  @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.headReceiptSubList(branchId, franchiseId, frYyyymmdd);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 대시보드 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 - 지사 월간매출, 누적매출 그래프 데이터 NativeQuery
    @GetMapping("headBranchMonthlySale")
    @ApiOperation(value = "지사별 월간매출, 누적매출", notes = "지사별 월간매출, 누적매출 그래프 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchMonthlySale(@RequestParam("filterYear") String filterYear) {
        return salesService.headBranchMonthlySale(filterYear);
    }

    // 본사 - 지사 매출순위 데이터 호출 API
    @GetMapping("headBranchRankSale")
    @ApiOperation(value = "지사별 매출 순위", notes = "지사별 매출 순위 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchRankSale(@RequestParam("filterYear") String filterYear) {
        return salesService.headBranchRankSale(filterYear);
    }

    // 본사 - 가맹점 매출순위 데이터 호출 API
    @GetMapping("headFranchiseRankSale")
    @ApiOperation(value = "가맹점별 매출 순위", notes = "가맹점별 매출 순위 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headFranchiseRankSale(@RequestParam("brCode") String brCode, @RequestParam("filterYear") String filterYear) {
        return salesService.headFranchiseRankSale(brCode, filterYear);
    }

    // 본사 - 지사,가맹점 품목별 매출 현황 데이터 호출 API
    @GetMapping("headItemSaleStatus")
    @ApiOperation(value = "품목별 매출 현황", notes = "품목별 매출 현황 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headItemSaleStatus(@RequestParam("branchId") String brId, @RequestParam("franchiseId") String frId, @RequestParam("filterYear") String filterYear) {
        return salesService.headItemSaleStatus(brId, frId, filterYear);
    }

    // 본사 - 지사,가맹점 세부품목별 매출 현황 데이터 호출 API
    @GetMapping("headItemSaleDetailStatus")
    @ApiOperation(value = "세부품목별 매출 현황", notes = "세부품목별 매출 현황 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headItemSaleDetailStatus(@RequestParam("bgCode") String bgCode, @RequestParam("branchId") String brId, @RequestParam("franchiseId") String frId, @RequestParam("filterYear") String filterYear) {
        return salesService.headItemSaleDetailStatus(bgCode, brId, frId, filterYear);
    }

    // 본사 - 월간 접수 현황 데이터 호출 API
    @GetMapping("headMonthlyReceiptList")
    @ApiOperation(value = "월간 접수 현황", notes = "월간 접수 현황 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headMonthlyReceiptList(@RequestParam("filterYear") String filterYear) {
        return salesService.headMonthlyReceiptList(filterYear);
    }

    // 본사 - 지사별 접수 순위 데이터 호출 API
    @GetMapping("headBranchReceiptRank")
    @ApiOperation(value = "지사별 접수 순위", notes = "지사별 접수 순위 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchReceiptList(@RequestParam("filterYear") String filterYear) {
        return salesService.headBranchReceiptList(filterYear);
    }

    // 본사 - 가맹점별 접수 순위 데이터 호출 API
    @GetMapping("headFranchiseReceiptRank")
    @ApiOperation(value = "가맹점별 접수 순위", notes = "가맹점별 접수 순위 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchReceiptList(@RequestParam("brCode") String brCode, @RequestParam("filterYear") String filterYear) {
        return salesService.headFranchiseReceiptList(brCode, filterYear);
    }

    // 본사 - 지사,가맹점 품목별 접수 현황 데이터 호출 API
    @GetMapping("headItemReceiptStatus")
    @ApiOperation(value = "품목별 접수 현황", notes = "품목별 접수 현황 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headItemReceiptStatus(@RequestParam("branchId") String brId, @RequestParam("franchiseId") String frId, @RequestParam("filterYear") String filterYear) {
        return salesService.headItemReceiptStatus(brId, frId, filterYear);
    }

    // 본사 - 지사,가맹점 세부품목별 접수 현황 데이터 호출 API
    @GetMapping("headItemReceiptDetailStatus")
    @ApiOperation(value = "세부품목별 접수 현황", notes = "세부품목별 접수 현황 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headItemReceiptDetailStatus(@RequestParam("bgCode") String bgCode, @RequestParam("branchId") String brId, @RequestParam("franchiseId") String frId, @RequestParam("filterYear") String filterYear) {
        return salesService.headItemReceiptDetailStatus(bgCode, brId, frId, filterYear);
    }

    // 본사 - 지사별 객단가 현황 데이터 호출 API
    @GetMapping("headCustomTransactionStatus")
    @ApiOperation(value = "지사별 객단가 현황", notes = "지사별 객단가 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headCustomTransactionStatus(@RequestParam("filterYear") String filterYear) {
        return salesService.headCustomTransactionStatus(filterYear);
    }

    // 본사 - 가맹점별 객단점 현황 데이터 호출 API
    @GetMapping("headCustomTransactionDetailStatus")
    @ApiOperation(value = "가맹점별 객단가 현황", notes = "가맹점별 객단가 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headCustomTransactionDetailStatus(@RequestParam("brCode") String brCode, @RequestParam("filterYear") String filterYear) {
        return salesService.headCustomTransactionDetailStatus(brCode, filterYear);
    }

    // 본사 - 월별 단가 추이 데이터 호출 API
    @GetMapping("headMonthlyPriceStatus")
    @ApiOperation(value = "월별 단가 추이", notes = "월별 단가 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headMonthlyPriceStatus(@RequestParam("filterYear") String filterYear) {
        return salesService.headMonthlyPriceStatus(filterYear);
    }

    // 본사 - 지사,가맹점별 성별 비중 현황 데이터 호출 API
    @GetMapping("headCustomerGenderRateStatus")
    @ApiOperation(value = "지사,가맹점별 성별 비중 현황", notes = "지사,가맹점별 성별 비중 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headCustomerGenderRateStatus(@RequestParam("branchId") Long brId, @RequestParam("franchiseId") Long frId) {
        return salesService.headCustomerGenderRateStatus(brId, frId);
    }

    // 본사 - 지사,가맹점별 나이 비중 현황 데이터 호출 API
    @GetMapping("headCustomerAgeRateStatus")
    @ApiOperation(value = "지사,가맹점별 나이 비중 현황", notes = "지사,가맹점별 나이 비중 데이터를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headCustomerAgeRateStatus(@RequestParam("branchId") Long brId, @RequestParam("franchiseId") Long frId) {
        return salesService.headCustomerAgeRateStatus(brId, frId);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 문자메세지 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 메세지 보낼 고객 리스트 호출
    @GetMapping("messageCustomerList")
    @ApiOperation(value = "본사의 문자 메시지 고객리스트", notes = "문자를 메시지 보낼 고객리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> messageCustomerList(
            @RequestParam(value = "visitDayRange", defaultValue = "") String visitDayRange,
            @RequestParam(value = "franchiseId", defaultValue = "") Long franchiseId,
            @RequestParam(value = "branchId", defaultValue = "") Long branchId,
            HttpServletRequest request) {
        return hmTemplateService.messageCustomerList(visitDayRange, franchiseId, branchId, request);
    }

    // 문자 메시지 보내기
    @PostMapping("messageSendCustomer")
    @ApiOperation(value = "문자 메시지 보내기", notes = "선택한 고객들에게 문자메세지를 보낸다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> messageSendCustomer(
            @RequestParam(value = "bcIdList", defaultValue = "") List<Long> bcIdList,
            @RequestParam(value = "hmMessage", defaultValue = "") String hmMessage,
            @RequestParam(value = "msgType", defaultValue = "") String msgType,
            @RequestParam(value = "hmSendreqtimeDt", defaultValue = "") String hmSendreqtimeDt,
            HttpServletRequest request) {
        return hmTemplateService.messageSendCustomer("1", bcIdList, hmMessage, hmSendreqtimeDt, msgType, request);
    }

    // 메세지 템플릿 6개 저장
    @PostMapping("templateSave")
    @ApiOperation(value = "본사 메세지 템플릿 6개 저장", notes = "문자 메세지 템플릿을 저장한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> templateSave(@RequestBody List<HmTemplateDto> hmTemplateDtos, HttpServletRequest request) {
        return hmTemplateService.hmTemplateSave("1", hmTemplateDtos, request);
    }

    // 메세지 템플릿 6개 호출
    @GetMapping("templateList")
    @ApiOperation(value = "본사  메세지 템플릿 호출", notes = "문자 메세지 템플릿을 호출한다")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> templateList(HttpServletRequest request) {
        return hmTemplateService.hmTemplateList("1", request);
    }

    //@@@@@@@@@@@@@@@@@@@@@ 본사 입고/출고 관련 현황 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 지사입고현황 - 왼쪽 리스트 호출API
    @GetMapping("headStoreInputList")
    @ApiOperation(value = "본사 지사입고현황 리스트", notes = "본사가 지사입고현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headStoreInputList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.headStoreInputList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 지사입고현황 - 오른쪽 리스트 호출API
    @GetMapping("headStoreInputSubList")
    @ApiOperation(value = "본사 지사입고현황 리스트", notes = "본사가 지사입고현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headStoreInputSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                  @RequestParam("fdS2Dt") String fdS2Dt) {
        return currentService.headStoreInputSubList(branchId, franchiseId, fdS2Dt);
    }

    // 본사 지사출고현황 - 왼쪽 리스트 호출API
    @GetMapping("headReleaseInputList")
    @ApiOperation(value = "본사 지사출고현황 리스트", notes = "본사가 지사출고현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headReleaseInputList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.headReleaseInputList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 지사출고현황 - 오른쪽 리스트 호출API
    @GetMapping("headReleaseInputSubList")
    @ApiOperation(value = "본사 지사출고현황 리스트", notes = "본사가 지사출고현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headReleaseInputSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("fdS4Dt") String fdS4Dt) {
        return currentService.headReleaseInputSubList(branchId, franchiseId, fdS4Dt);
    }

    // 본사 강제출고현황 - 왼쪽 리스트 호출API
    @GetMapping("headForceReleaseInputList")
    @ApiOperation(value = "본사 강제출고현황 리스트", notes = "본사가 강제출고현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headForceReleaseInputList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.headForceReleaseInputList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 강제출고현황 - 오른쪽 리스트 호출API
    @GetMapping("headForceReleaseInputSubList")
    @ApiOperation(value = "본사 강제출고현황 리스트", notes = "본사가 강제출고현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headForceReleaseInputSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("fdS7Dt") String fdS7Dt) {
        return currentService.forceReleaseInputSubList(branchId, franchiseId, fdS7Dt);
    }

    // 본사 강제입고현황 - 왼쪽 리스트 호출API
    @GetMapping("headForceStoreInputList")
    @ApiOperation(value = "본사 강제입고현황 리스트", notes = "본사가 강제입고현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headForceStoreInputList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.forceStoreInputList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 강제입고현황 - 오른쪽 리스트 호출API
    @GetMapping("headForceStoreInputSubList")
    @ApiOperation(value = "본사 강제입고현황 리스트", notes = "본사가 강제입고현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headForceStoreInputSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("fdS8Dt") String fdS8Dt) {
        return currentService.forceStoreInputSubList(branchId, franchiseId, fdS8Dt);
    }

    // 본사 미출고 현황 - 왼쪽 리스트 호출API
    @GetMapping("headNoReleaseInputList")
    @ApiOperation(value = "본사 미출고 현황 리스트", notes = "본사가 미출고 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headNoReleaseInputList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.headNoReleaseInputList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 미출고 현황 - 오른쪽 리스트 호출API
    @GetMapping("headNoReleaseInputSubList")
    @ApiOperation(value = "본사 미출고 현황 리스트", notes = "본사가 미출고 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headNoReleaseInputSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("fdEstimateDt") String fdEstimateDt) {
        return currentService.headNoReleaseInputSubList(branchId, franchiseId, fdEstimateDt);
    }

    // 본사 재세탁입고 현황 - 왼쪽 리스트 호출API
    @GetMapping("headRetryList")
    @ApiOperation(value = "본사 재세탁입고 현황 리스트", notes = "본사가 재세탁입고 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headRetryList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt, HttpServletRequest request) {
        return currentService.retryList(branchId, franchiseId, filterFromDt, filterToDt, request);
    }

    // 본사 재세탁입고 현황 - 오른쪽 리스트 호출API
    @GetMapping("headRetrySubList")
    @ApiOperation(value = "본사 재세탁입고 현황 리스트", notes = "본사가 재세탁입고 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headRetrySubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return currentService.retrySubList(branchId, franchiseId, frYyyymmdd);
    }

//@@@@@@@@@@@@@@@@@@@@@ 택번호조회 페이지 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사  TAG번호 조회하기 위한 지사리스트, 가맹점 리스트 호출 API
    @GetMapping("headTagNoSearch")
    @ApiOperation(value = "TAG번호 조회하기 위한 지사,가맹점 리스트 호출", notes = "TAG번호 조회하기 위한 지사,가맹점 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headTagNoSearch() {
        return headInfoService.headTagNoSearch();
    }

    // 본사 TAG번호 접수리스트 호출 API
    @GetMapping("headTagNoReceiptSearch")
    @ApiOperation(value = "본사 TAG번호 접수리스트 호출", notes = "해당 TAG번호의 대한 접수 리스트를 호출한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headTagNoReceiptSearch(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("tagNo") String tagNo) {
        return receiptService.headTagNoReceiptSearch(branchId, franchiseId, tagNo);
    }

//@@@@@@@@@@@@@@@@@@@@@ 본사 세탁현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 초특급세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("headSpecialQuickReceiptList")
    @ApiOperation(value = "본사 초특급세탁 현황 리스트", notes = "본사가 초특급세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headSpecialQuickReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                           @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "1", "Y", "1");
    }

    // 본사 초특급세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("headSpecialQuickReceiptSubList")
    @ApiOperation(value = "본사 초특급세탁 현황 리스트", notes = "본사가 초특급세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headSpecialQuickReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "1", "Y", "1");
    }

    // 본사 특급세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("headSpecialReceiptList")
    @ApiOperation(value = "본사 특급세탁 현황 리스트", notes = "본사가 특급세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headSpecialReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                      @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "2", "Y", "2");
    }

    // 본사 특급세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("headSpecialReceiptSubList")
    @ApiOperation(value = "본사 특급세탁 현황 리스트", notes = "본사가 특급세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headSpecialReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "2", "Y", "2");
    }

    // 본사 급세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("headQuickReceiptList")
    @ApiOperation(value = "본사 급세탁 현황 리스트", notes = "본사가 급세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headQuickReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                    @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "3", "Y", "3");
    }

    // 본사 급세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("headQuickReceiptSubList")
    @ApiOperation(value = "본사 급세탁 현황 리스트", notes = "본사가 급세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headQuickReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "3", "Y", "3");
    }

    // 본사 일반세탁 입고현황 - 왼쪽 리스트 호출API
    @GetMapping("headNormalReceiptList")
    @ApiOperation(value = "본사 일반세탁 현황 리스트", notes = "본사가 일반세탁 현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headNormalReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.urgentReceiptList(branchId, franchiseId, filterFromDt, filterToDt, "4", "N", "");
    }

    // 본사 일반세탁 입고현황 - 오른쪽 리스트 호출API
    @GetMapping("headNormalReceiptSubList")
    @ApiOperation(value = "본사 일반세탁 현황 리스트", notes = "본사가 일반세탁 현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headNormalReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("frYyyymmdd") String frYyyymmdd) {
        return receiptService.urgentReceiptSubList(branchId, franchiseId, frYyyymmdd, "4", "N", "");
    }

//@@@@@@@@@@@@@@@@@@@@@ 반품현황 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 반품현황 - 왼쪽 리스트 호출API
    @GetMapping("headReturnReceiptList")
    @ApiOperation(value = "본사 반품현황 리스트", notes = "본사가 반품현황 왼쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headReturnReceiptList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("filterFromDt") String filterFromDt, @RequestParam("filterToDt") String filterToDt) {
        return receiptService.returnReceiptList(branchId, franchiseId, filterFromDt, filterToDt);
    }

    // 본사 반품현황 - 오른쪽 리스트 호출API
    @GetMapping("headReturnReceiptSubList")
    @ApiOperation(value = "본사 반품현황 리스트", notes = "본사가 반품현황 오른쪽 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headReturnReceiptSubList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                     @RequestParam("fdS6Dt") String fdS6Dt) {
        return receiptService.returnReceiptSubList(branchId, franchiseId, fdS6Dt);
    }


//@@@@@@@@@@@@@@@@@@@@@ 정산 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 가맹점 일정산 요약 리스트 호출API
    @GetMapping("headFranchiseDaliySummaryList")
    @ApiOperation(value = "본사 가맹점 일정산 리스트", notes = "본사가 가맹점 일일정산 요약 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headFranchiseDaliySummaryList(@RequestParam("franchiseId") Long franchiseId, @RequestParam("filterYearMonth") String filterYearMonth) {
        return summaryService.daliySummaryList(franchiseId, filterYearMonth, null);
    }

    // 본사 지사별 월정산 요약 리스트 호출API
    @GetMapping("headBranchMonthlySummaryList")
    @ApiOperation(value = "본사 지사 월정산 리스트", notes = "본사가 지사 월정산 요약 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchMonthlySummaryList(@RequestParam("filterYearMonth") String filterYearMonth) {
        return summaryService.headMonthlySummaryList(filterYearMonth);
    }

    // 본사 가맹점별 월정산 요약 리스트 호출API
    @GetMapping("headFranchiseMonthlySummaryList")
    @ApiOperation(value = "본사 가맹점 월정산 리스트", notes = "본사가 가맹점 월정산 요약 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headFranchiseMonthlySummaryList(@RequestParam("filterYearMonth") String filterYearMonth) {
        return summaryService.monthlySummaryList(filterYearMonth, null);
    }

    // 본사 지사 월정산 입금현황 호출API
    @GetMapping("headBranchMonthlyStatusList")
    @ApiOperation(value = "본사 지사의 월정산입금 리스트", notes = "본사가 지사의 월정산입금 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchMonthlyStatusList(@RequestParam("filterYear") String filterYear) {
        return summaryService.headBranchMonthlyStatusList(filterYear);
    }

    // 본사 가맹점별 일정산 입금현황 호출API
    @GetMapping("headFranchiseDailyStatusList")
    @ApiOperation(value = "본사 가맹점별 일정산입금 리스트", notes = "본사가 가맹점별 일정산입금 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headFranchiseDailyStatusList(@RequestParam("filterYearMonth") String filterYearMonth) {
        return summaryService.franchiseDailyStatusList(filterYearMonth, null);
    }

//@@@@@@@@@@@@@@@@@@@@@ 월정산 입금 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 지사 월정산 입금 리스트 호출API
    @GetMapping("headBranchReceiptMonthlyList")
    @ApiOperation(value = "본사 지사의 월정산입금 리스트", notes = "본사가 지사의 월정산입금 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchReceiptMonthlyList(@RequestParam("branchId") Long branchId, @RequestParam("filterFromYearMonth") String filterFromYearMonth,
                                                                            @RequestParam("filterToYearMonth") String filterToYearMonth) {
        return summaryService.receiptMonthlyList(branchId, filterFromYearMonth, filterToYearMonth);
    }

    // 본사 지사 월정산 입금 저장 호출API
    @PostMapping("headBranchMonthlySummarySave")
    @ApiOperation(value = "본사 지사 월정산 저장", notes = "본사가 지사 월정산을 저장합니다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headBranchMonthlySummarySave(@RequestParam("hsYyyymm") String hsYyyymm, @RequestParam("brCode") String brCode,
                                                                            @RequestParam("hrReceiptYyyymmdd") String hrReceiptYyyymmdd, @RequestParam("hrReceiptBrRoyaltyAmt") Integer hrReceiptBrRoyaltyAmt,
                                                                            @RequestParam("hrReceiptFrRoyaltyAmt") Integer hrReceiptFrRoyaltyAmt) {
        return summaryService.headBranchMonthlySummarySave(hsYyyymm, brCode, hrReceiptYyyymmdd, hrReceiptBrRoyaltyAmt, hrReceiptFrRoyaltyAmt);
    }

//@@@@@@@@@@@@@@@@@@@@@ 행사 페이지 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 본사 지사와 소속된 가맹점 리스트를 호출한다. (계약기간 사이에서 한정)
    @GetMapping("headContractList")
    @ApiOperation(value = "본사 지사와 소속된 가맹점 리스트", notes = "본사가 지사의 가맹점 계약기간 사이이의 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headContractList() {
        return promotionService.headContractList();
    }

    // 본사 등록된 상품 리스트를 호출한다.
    @GetMapping("headItemList")
    @ApiOperation(value = "본사 등록된 상품 리스트", notes = "본사가 등록된 상품 리스트를 요청한다.")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headItemList() {
        return promotionService.headItemList();
    }

    // 본사 행사 등록 및 삭제
    @PostMapping("headPromotionSave")
    @ApiOperation(value = "본사 행사 등록 및 수정", notes = "본사가 행사 등록 및 수정,삭제를 한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headPromotionSave(@RequestBody PromotionSetDto promotionSetDto, HttpServletRequest request) {
        return promotionService.headPromotionSave(promotionSetDto, "HR", request);
    }

    // 본사 행사 조회
    @GetMapping("headPromotionList")
    @ApiOperation(value = "본사 행사 글 조회", notes = "본사가 글을 조회 한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headPromotionList(@RequestParam("branchId") Long branchId, @RequestParam("franchiseId") Long franchiseId,
                                                                 @RequestParam("filterDt") String filterDt,
                                                                 @RequestParam("hpName") String hpName, @RequestParam("hpStatus") String hpStatus) {
        return promotionService.headPromotionList(branchId, franchiseId, filterDt, hpName, hpStatus);
    }

    // 본사 행사의 세부정보 조회
    @GetMapping("headPromotionSub")
    @ApiOperation(value = "본사 행사 글의 세부정보 호출 ", notes = "본사가 행사글의 세부정보를 호출 한다. ")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Map<String, Object>> headPromotionSub(@RequestParam("hpId") Long hpId) {
        return promotionService.headPromotionSub(hpId);
    }

}
