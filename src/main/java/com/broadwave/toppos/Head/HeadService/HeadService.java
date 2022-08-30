package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.AddCost.AddCost;
import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.AddCost.AddCostRepository;
import com.broadwave.toppos.Head.AddCost.AddCostRepositoryCustom;
import com.broadwave.toppos.Head.Branch.Branch;
import com.broadwave.toppos.Head.Branch.BranchRepository;
import com.broadwave.toppos.Head.Branoh.BranchDtos.BranchListDto;
import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.*;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.UserItemGroupSortDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepository;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupS;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSInfo;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSListDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSUserListDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.UserItemGroupSListDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSRepository;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepository;
import com.broadwave.toppos.Head.Item.ItemDtos.UserItemPriceSortDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePrice;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceRepository;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.ItemPriceSetDtDto;
import com.broadwave.toppos.Head.Item.Price.ItemPrice;
import com.broadwave.toppos.Head.Item.Price.ItemPriceRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user.RequestSearchDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.Head.Branoh.BranchDtos.BranchMapperDto;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class HeadService {

    private final ModelMapper modelMapper;

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ItemGroupRepository itemGroupRepository;
    private final ItemGroupSRepository itemGroupSRepository;
    private final ItemRepository itemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final FranchisePriceRepository franchisePriceRepository;
    private final AddCostRepository addCostRepository;
    private final RequestRepository requestRepository;
    private final AddCostRepositoryCustom addCostRepositoryCustom;

    @Autowired
    public HeadService(ModelMapper modelMapper, AddCostRepository addCostRepository,
                       BranchRepository branchRepository, FranchiseRepository franchiseRepository,
                       ItemGroupRepository itemGroupRepository, ItemGroupSRepository itemGroupSRepository,
                       ItemRepository itemRepository, ItemPriceRepository itemPriceRepository,
                       FranchisePriceRepository franchisePriceRepository, RequestRepository requestRepository, AddCostRepositoryCustom addCostRepositoryCustom){
        this.modelMapper = modelMapper;
        this.addCostRepository = addCostRepository;
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.itemGroupSRepository = itemGroupSRepository;
        this.itemRepository = itemRepository;
        this.itemPriceRepository = itemPriceRepository;
        this.franchisePriceRepository = franchisePriceRepository;
        this.requestRepository = requestRepository;
        this.addCostRepositoryCustom = addCostRepositoryCustom;
    }


    // @@@@@@@@@@@@@@@@@@@@@@ 가맹점, 지사 등록 매칭 페이지 @@@@@@@@@@@@@@@@@@@@
    // 가맹점 저장
    public Franchise franchise(Franchise franchise){
        franchiseRepository.save(franchise);
        return franchise;
    }

    // 가맹점 신규저장 및 업데이트
    public ResponseEntity<Map<String,Object>> franchiseSave(FranchiseMapperDto franchiseMapperDto, HttpServletRequest request){
        log.info("franchiseSave 호출");

        AjaxResponse res = new AjaxResponse();

        Franchise franchise = modelMapper.map(franchiseMapperDto, Franchise.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Franchise> optionalFranohise = franchiseRepository.findByFrCode(franchiseMapperDto.getFrCode());

        // 가맹점 택코드, 마지막번호 set
        if(franchiseMapperDto.getFrTagNo() == null || franchiseMapperDto.getFrTagNo().equals("")) {
            if(franchise.getFrTagType().equals("2")) {
                franchise.setFrTagNo("99");
                franchise.setFrLastTagno("9900001");
            }else{
                franchise.setFrTagNo("999");
                franchise.setFrLastTagno("9990001");
            }
        }else{
            if(franchise.getFrTagType().equals("2")){
                franchise.setFrLastTagno(franchise.getFrTagNo() + "00001");
            }else{
                franchise.setFrLastTagno(franchise.getFrTagNo() + "0001");
            }
        }

        if( optionalFranohise.isPresent()){
            log.info("널이 아닙니다 : 업데이트");
            franchise.setId(optionalFranohise.get().getId());

            franchise.setBrId(optionalFranohise.get().getBrId());
            franchise.setBrCode(optionalFranohise.get().getBrCode());
            franchise.setBrAssignState(optionalFranohise.get().getBrAssignState());

            franchise.setModify_id(login_id);
            franchise.setModifyDateTime(LocalDateTime.now());
            franchise.setInsert_id(optionalFranohise.get().getInsert_id());
            franchise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());
        }else{
            log.info("널입니다. : 신규생성");
            if(franchiseMapperDto.getFrEstimateDuration() == null ){
                franchise.setFrEstimateDuration(2);
            }
            franchise.setBrId(null);
            franchise.setBrCode(null);
            franchise.setBrAssignState("01");
            franchise.setInsert_id(login_id);
            franchise.setInsertDateTime(LocalDateTime.now());
        }

        Franchise franchiseSave =  franchiseRepository.save(franchise);
        log.info("가맹점 저장 성공 : id '" + franchiseSave.getFrCode() + "'");
        return ResponseEntity.ok(res.success());
    }

    // 가맹점의 대한 지사배치 등록 API
    public ResponseEntity<Map<String,Object>> franchiseAssignment(String frCode, String brCode, String bot_brAssignState,
                                                                  Double bot_frCarculateRateBr, Double bot_frCarculateRateFr,
                                                                  Double bot_frRoyaltyRateBr, Double bot_frRoyaltyRateFr,
                                                                  HttpServletRequest request){
        log.info("franchiseAssignment 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);

        log.info("bot_brAssignState : "+bot_brAssignState);

        Optional<Franchise> optionalFranohise = franchiseRepository.findByFrCode(frCode);
        Optional<Branch> optionalBranch = branchRepository.findByBrCode(brCode);
        if(optionalFranohise.isPresent() && optionalBranch.isPresent()){

            optionalFranohise.get().setFrCarculateRateBr(bot_frCarculateRateBr);
            optionalFranohise.get().setFrCarculateRateFr(bot_frCarculateRateFr);
            optionalFranohise.get().setFrRoyaltyRateBr(bot_frRoyaltyRateBr);
            optionalFranohise.get().setFrRoyaltyRateFr(bot_frRoyaltyRateFr);

            optionalFranohise.get().setBrId(optionalBranch.get());
            optionalFranohise.get().setBrCode(optionalBranch.get().getBrCode());
            optionalFranohise.get().setBrAssignState(bot_brAssignState);

            optionalFranohise.get().setModify_id(login_id);
            optionalFranohise.get().setModifyDateTime(LocalDateTime.now());

            franchiseRepository.save(optionalFranohise.get());
        }else{
            log.info("정보가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), ResponseErrorCode.TP005.getDesc(),ResponseErrorCode.TP006.getCode(), ResponseErrorCode.TP006.getDesc()));
        }

        return ResponseEntity.ok(res.success());
    }

    // 지사 저장
    public ResponseEntity<Map<String, Object>> branchSave(BranchMapperDto branohMapperDto, HttpServletRequest request) {
        log.info("branchSave 호출");

        AjaxResponse res = new AjaxResponse();

        Branch Branoh = modelMapper.map(branohMapperDto, Branch.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Branch> optionalBranoh = branchRepository.findByBrCode(branohMapperDto.getBrCode());
        if (optionalBranoh.isPresent()) {
//            log.info("널이 아닙니다 : 업데이트");
            Branoh.setId(optionalBranoh.get().getId());

            Branoh.setModify_id(login_id);
            Branoh.setModifyDateTime(LocalDateTime.now());
            Branoh.setInsert_id(optionalBranoh.get().getInsert_id());
            Branoh.setInsertDateTime(optionalBranoh.get().getInsertDateTime());
        } else {
//            log.info("널입니다. : 신규생성");
            Branoh.setInsert_id(login_id);
            Branoh.setInsertDateTime(LocalDateTime.now());
        }

        Branch BranohSave = branchRepository.save(Branoh);
        log.info("지사 저장 성공 : id '" + BranohSave.getBrCode() + "'");
        return ResponseEntity.ok(res.success());
    }

    // 가맹점 리스트 API
    public List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frRefCode, String frContractState) {
        return franchiseRepository.findByFranchiseList(brCode, brAssignState, frName, frCode, frRefCode, frContractState);
    }

    // 지사 리스트 API
    public List<BranchListDto> findByBranchList(String brName, String brCode, String brContractState) {
        return branchRepository.findByBranchList(brName, brCode, brContractState);
    }

    // 가맹점코드 중복확인 API
    public ResponseEntity<Map<String, Object>> franchiseOverlap(String frCode){
        log.info("franchiseOverlap 호출");

        log.info("가맹점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        Optional<Franchise> franohiseOptional = franchiseRepository.findByFrCode(frCode);
        if (franohiseOptional.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), null, null));
        } else {
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());

    }

    // 지점코드 중복확인 API
    public ResponseEntity<Map<String, Object>> branchOverlap(String brCode){
        log.info("branchOverlap 호출");

        log.info("지점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        if (brCode.equals("hr")) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), "문자", "해당 코드는 본사전용 코드입니다."));
        }

        Optional<Branch> BranohOptional = branchRepository.findByBrCode(brCode);
        if (BranohOptional.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(), null, null));
        } else {
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }

    // 가맹점 정보 호출하기
    public FranchiseInfoDto findByFranchiseInfo(String frCode) {
        return franchiseRepository.findByFranchiseInfo(frCode);
    }

    // 가맹점 전용 나의정보 호출하기
    public FranchiseUserDto findByFranchiseUserInfo(String frCode) {
        return franchiseRepository.findByFranchiseUserInfo(frCode);
    }

    // @@@@@@@@@@@@@@@@@@@@    상품 그룹관리 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 상품그룹 대분류  저장
    public void itemGroupSave(ItemGroup itemGroup){
        itemGroupRepository.save(itemGroup);
    }

    // 상품그룹 대분류 객체 가져오기
    public Optional<ItemGroup> findByBgItemGroupcode(String bgItemGroupcode) {
        return itemGroupRepository.findByBgItemGroupcode(bgItemGroupcode);
    }

    // 상품그룹 대분류 리스트 호출
    public List<ItemGroupDto> findByItemGroupList() {
        return itemGroupRepository.findByItemGroupList();
    }

    // 상품그룹 대분류 명칭 리스트 호출
    public List<ItemGroupNameListDto> findByItemGroupName() {
        return itemGroupRepository.findByItemGroupName();
    }

    // 상품그룹 대분류 삭제
    public void findByItemGroupDelete(ItemGroup itemGroup){
        itemGroupRepository.delete(itemGroup);
    }

    // 상품그룹 중분류 저장
    public void itemGroupSSave(ItemGroupS itemGroupS){
        itemGroupSRepository.save(itemGroupS);
    }

    // 상품그룹 중분류 객체 가져오기
    public ItemGroupS findByItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        return itemGroupSRepository.findByItemGroupcodeS(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 중분류 정보
    public ItemGroupSInfo findByBsItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        return itemGroupSRepository.findByBsItemGroupcodeS(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 중분류 리스트 호출
    public List<ItemGroupSListDto> findByItemGroupSList(ItemGroup bgItemGroupcode) {
        return itemGroupSRepository.findByItemGroupSList(bgItemGroupcode);
    }

    // 상품그룹 상품순서 리스트 가져오기 - 가맹점 검색
    public List<ItemGroupSUserListDto> findByItemGroupSUserList(String filterCode, String filterName) {
        return itemGroupSRepository.findByItemGroupSUserList(filterCode, filterName);
    }

    // 상품그룹 중분류 삭제
    public void findByItemGroupSDelete(ItemGroupS itemGroupS) {
        itemGroupSRepository.delete(itemGroupS);
    }

    // 상품그룹 상품소재 저장
    public void itemSave(Item item){
        itemRepository.save(item);
    }

    // 상품그룹 상품소재 객체 가져오기
    public Optional<Item> findByBiItemcode(String biItemcode) {
        return itemRepository.findByBiItemcode(biItemcode);
    }

    public Optional<Item> findByBiItem(String bgItemGroupcode, String bsItemGroupcodeS) {
        return itemRepository.findByBiItem(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 상품소재 리스트 호출
    public List<ItemListDto> findByItemList(String bgItemGroupcode, String bsItemGroupcodeS, String biItemcode, String biName) {
        return itemRepository.findByItemList(bgItemGroupcode, bsItemGroupcodeS, biItemcode, biName);
    }

    // 상품그룹 상품소재 삭제
    public void findByItemDelete(Item itemOptional) {
        itemRepository.delete(itemOptional);
    }


    // @@@@@@@@@@@@@@@@@@@@ 상품 가격관리 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 상품코드를 통해 가격 검색
    public Optional<ItemPrice> findByItemPriceByBiItemcode(String biItemcode) {
        return itemPriceRepository.findByItemPriceByBiItemcode(biItemcode);
    }

    // 상품 가격 멀티 저장
    @Transactional(rollbackFor = SQLException.class)
    public void itemPriceSaveAll(List<ItemPrice> itemSavePrice, List<ItemPrice> itemUpdatePrice) {
        try{
            itemPriceRepository.saveAll(itemUpdatePrice);
            itemPriceRepository.saveAll(itemSavePrice);
        }catch (Exception e){
            log.info("e : "+e);
        }
    }

    // 상품 가격 저장
    public void itemPriceSave(List<ItemPrice> itemPrice){
            itemPriceRepository.saveAll(itemPrice);
    }


    // @@@@@@@@@@@@@@@@   가맹점 특정품목가격 페이지   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 가맹점 특정품목가격 저장
    public void franchisePriceSave(List<FranchisePrice> franchisePriceList) {
        franchisePriceRepository.saveAll(franchisePriceList);
    }

    // @@@@@@@@@@@@@@@@@@@@  가맹점 접수 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 가맹점 대분류 전용 순번적용
    public List<UserItemGroupSortDto> findByUserItemGroupSortDtoList(String frCode) {
        return itemGroupRepository.findByUserItemGroupSortDtoList(frCode);
    }

    // 가맹점 가격 전용 순번적용
    public List<UserItemPriceSortDto> findByUserItemPriceSortList(String frCode, String nowDate) {
        return itemPriceRepository.findByUserItemPriceSortList(frCode, nowDate);
    }

    // 가맹점 접수페이지 중분류 리스트 Dto
    public List<UserItemGroupSListDto> findByUserItemGroupSList(){
        return itemGroupSRepository.findByUserItemGroupSList();
    }

    // 가맹점 가격셋팅 테이블 호출
    public AddCostDto findByAddCost() {
        return addCostRepositoryCustom.findByAddCost();
    }

    // 가격셋팅 할인율 설정 API
    public ResponseEntity<Map<String, Object>> findByAddCostUpdate(AddCostDto addCostDto, HttpServletRequest request) {

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);

        Optional<AddCost> optionalAddCost = findByAddCost("000");
        if(!optionalAddCost.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 "+ResponseErrorCode.TP005.getDesc(), null,null));
        }else{
            AddCost addCost = modelMapper.map(addCostDto,AddCost.class);
            addCost.setBcId(optionalAddCost.get().getBcId());
            addCost.setInsert_id(optionalAddCost.get().getInsert_id());
            addCost.setInsertDateTime(optionalAddCost.get().getInsertDateTime());
            addCost.setModify_id(login_id);
            addCost.setModifyDateTime(LocalDateTime.now());
            addCostRepository.save(addCost);
        }

        return ResponseEntity.ok(res.success());
    }

    // 가격 셋팅테이블 아이디 : "000" 조회
    public Optional<AddCost> findByAddCost(String bcId){
        return addCostRepository.findByAddCost(bcId);
    }

    // 가격셋팅 테이블 정보조회 API
    public ResponseEntity<Map<String, Object>> addCostInfo() {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AddCostDto addCostDto = findByAddCost();
        data.put("addCostDto",addCostDto);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 모바일전용 가맹점명 호출 함수
    public FranchiseNameInfoDto findByFranchiseNameInfo(String frCode) {
        return franchiseRepository.findByFranchiseNameInfo(frCode);
    }

    // 멀티스크린 사용여부 가져오기
    public FranchiseMultiscreenDto findByFranchiseMultiscreen(String frCode) {
        return franchiseRepository.findByFranchiseMultiscreen(frCode);
    }

    public List<ItemPriceSetDtDto> findByItemPriceSetDtList() {
        return itemPriceRepository.findByItemPriceSetDtList();
    }

    // 가맹점 삭제 API
    public ResponseEntity<Map<String, Object>> franchiseDelete(String frCode) {
        log.info("franchiseDelete 호출");
        log.info("요청된 가맹점코드 : " + frCode);

        AjaxResponse res = new AjaxResponse();

        Optional<Franchise> optionalFranchise = franchiseRepository.findByFrCode(frCode);
        if (!optionalFranchise.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 " + ResponseErrorCode.TP005.getDesc(), "문자", "가맹점코드(3자리)  : " + frCode));
        } else {
            log.info("가맹점코드 : " + optionalFranchise.get().getFrCode());
            List<RequestSearchDto> request = requestRepository.findByRequestFrCode(optionalFranchise.get().getFrCode());
            if (request.size() == 0) {
                log.info(optionalFranchise.get().getFrCode() + " 가맹점삭제 진행");
                franchiseRepository.delete(optionalFranchise.get());
            } else {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP011.getCode(), "해당 가맹점의 " + ResponseErrorCode.TP011.getDesc(), "문자", "가맹점코드(3자리)  : " + frCode));
            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 지사 삭제 API
    public ResponseEntity<Map<String, Object>> branchDelete(String brCode) {
        log.info("branchDelete 호출");
        log.info("요청된 지사코드 : " + brCode);

        AjaxResponse res = new AjaxResponse();

        Optional<Branch> optionalBranch = branchRepository.findByBrCode(brCode);
        if (!optionalBranch.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 " + ResponseErrorCode.TP005.getDesc(), "문자", "지사코드(2자리) : " + brCode));
        } else {
            log.info("지사코드 : " + optionalBranch.get().getBrCode());
            List<FranchiseSearchDto> franchise = franchiseRepository.findByFranchiseBrcode(optionalBranch.get().getBrCode());
            if (franchise.size() == 0) {
                log.info(optionalBranch.get().getBrCode() + " 지사삭제 진행");
                branchRepository.delete(optionalBranch.get());
            } else {
                return ResponseEntity.ok(res.fail("문자", "해당 지사에 배정된 가맹점이 존재합니다.", "문자", "지사코드(2자리) : " + optionalBranch.get().getBrCode()));
            }
        }

        return ResponseEntity.ok(res.success());
    }
}
