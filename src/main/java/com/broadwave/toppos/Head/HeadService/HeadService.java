package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.AddCost.AddCost;
import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.AddCost.AddCostRepository;
import com.broadwave.toppos.Head.AddCost.AddCostRepositoryCustom;
import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchRepository;
import com.broadwave.toppos.Head.Branoh.BranchRepositoryCustomImpl;
import com.broadwave.toppos.Head.Franchise.*;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.*;
import com.broadwave.toppos.Head.Item.Group.A.*;
import com.broadwave.toppos.Head.Item.Group.B.*;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepository;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepositoryCustom;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceListDto;
import com.broadwave.toppos.Head.Item.ItemDtos.UserItemPriceSortDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePrice;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceListDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceRepository;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceRepositoryCustom;
import com.broadwave.toppos.Head.Item.Price.*;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//    private final TokenProvider tokenProvider;

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ItemGroupRepository ItemGroupRepository;
    private final ItemGroupSRepository ItemGroupSRepository;
    private final ItemRepository ItemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final FranchisePriceRepository franchisePriceRepository;
    private final AddCostRepository addCostRepository;

    private final BranchRepositoryCustomImpl branohRepositoryCustom;
    private final ItemGroupRepositoryCustom itemGroupRepositoryCustom;
    private final ItemGroupSRepositoryCustom itemGroupSRepositoryCustom;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final ItemPriceRepositoryCustom itemPriceRepositoryCustom;
    private final FranchisePriceRepositoryCustom franchisePriceRepositoryCustom;
    private final AddCostRepositoryCustom addCostRepositoryCustom;

    @Autowired
    public HeadService(ModelMapper modelMapper, AddCostRepository addCostRepository,
                       BranchRepository branchRepository, FranchiseRepository franchiseRepository, BranchRepositoryCustomImpl branohRepositoryCustom,
                       ItemGroupRepository ItemGroupRepository, ItemGroupRepositoryCustom itemGroupRepositoryCustom, ItemGroupSRepository ItemGroupSRepository, ItemGroupSRepositoryCustom itemGroupSRepositoryCustom,
                       ItemRepository ItemRepository, ItemRepositoryCustom itemRepositoryCustom, ItemPriceRepository itemPriceRepository, ItemPriceRepositoryCustom itemPriceRepositoryCustom,
                       FranchisePriceRepository franchisePriceRepository, FranchisePriceRepositoryCustom franchisePriceRepositoryCustom,
                       AddCostRepositoryCustom addCostRepositoryCustom){
        this.modelMapper = modelMapper;
        this.addCostRepository = addCostRepository;
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
        this.branohRepositoryCustom = branohRepositoryCustom;
        this.ItemGroupRepository = ItemGroupRepository;
        this.itemGroupRepositoryCustom = itemGroupRepositoryCustom;
        this.ItemGroupSRepository = ItemGroupSRepository;
        this.itemGroupSRepositoryCustom = itemGroupSRepositoryCustom;
        this.ItemRepository = ItemRepository;
        this.itemRepositoryCustom = itemRepositoryCustom;
        this.itemPriceRepository = itemPriceRepository;
        this.itemPriceRepositoryCustom = itemPriceRepositoryCustom;
        this.franchisePriceRepository = franchisePriceRepository;
        this.franchisePriceRepositoryCustom = franchisePriceRepositoryCustom;
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

        Optional<Franchise> optionalFranohise = findByFrCode(franchiseMapperDto.getFrCode());
        if( optionalFranohise.isPresent()){
            log.info("널이 아닙니다 : 업데이트");
            franchise.setId(optionalFranohise.get().getId());

            franchise.setBrId(optionalFranohise.get().getBrId());
            franchise.setBrCode(optionalFranohise.get().getBrCode());
            franchise.setBrAssignState(optionalFranohise.get().getBrAssignState());
            if(franchiseMapperDto.getFrTagNo() == null || franchiseMapperDto.getFrTagNo().equals("")) {
                franchise.setFrTagNo(franchiseMapperDto.getFrCode());
                franchise.setFrLastTagno(franchiseMapperDto.getFrCode()+"0000");
            }else{
                if(optionalFranohise.get().getFrLastTagno() != null){
                    franchise.setFrLastTagno(franchiseMapperDto.getFrTagNo() + optionalFranohise.get().getFrLastTagno().substring(3, 7));
                }else{
                    franchise.setFrLastTagno(franchiseMapperDto.getFrTagNo() + "0000");
                }
            }

            franchise.setModify_id(login_id);
            franchise.setModifyDateTime(LocalDateTime.now());
            franchise.setInsert_id(optionalFranohise.get().getInsert_id());
            franchise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());
        }else{
            log.info("널입니다. : 신규생성");
            if(franchiseMapperDto.getFrTagNo() == null || franchiseMapperDto.getFrTagNo().equals("")){
                franchise.setFrTagNo(franchiseMapperDto.getFrCode());
                franchise.setFrLastTagno(franchiseMapperDto.getFrCode()+"0000");
            }else{
                franchise.setFrLastTagno(franchiseMapperDto.getFrTagNo()+"0000");
            }
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

        Optional<Franchise> optionalFranohise = findByFrCode(frCode);
        Optional<Branch> optionalBranch = findByBrCode(brCode);
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
    public Branch branchSave(Branch branoh){
        branchRepository.save(branoh);
        return branoh;
    }

    // 가맹점 리스트 API
    public List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frRefCode, String frContractState) {
        return franchiseRepository.findByFranchiseList(brCode, brAssignState, frName, frCode, frRefCode, frContractState);
    }

    // 지사 리스트 API
    public List<BranchListDto> findByBranchList(String brName, String brCode, String brContractState) {
        return branohRepositoryCustom.findByBranchList(brName, brCode, brContractState);
    }

    // 가맹점코드 중복확인 API
    public Optional<Franchise> findByFrCode(String frCode){
        return franchiseRepository.findByFrCode(frCode);
    }

    // 지점코드 중복확인 API
    public Optional<Branch> findByBrCode(String brCode){
        return branchRepository.findByBrCode(brCode);
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
        ItemGroupRepository.save(itemGroup);
    }

    // 상품그룹 대분류 객체 가져오기
    public Optional<ItemGroup> findByBgItemGroupcode(String bgItemGroupcode) {
        return ItemGroupRepository.findByBgItemGroupcode(bgItemGroupcode);
    }

    // 상품그룹 대분류 리스트 호출
    public List<ItemGroupDto> findByItemGroupList() {
        return itemGroupRepositoryCustom.findByItemGroupList();
    }

    // 상품그룹 대분류 명칭 리스트 호출
    public List<ItemGroupNameListDto> findByItemGroupName() {
        return itemGroupRepositoryCustom.findByItemGroupName();
    }

    // 상품그룹 대분류 삭제
    public void findByItemGroupDelete(ItemGroup itemGroup){
        ItemGroupRepository.delete(itemGroup);
    }

    // 상품그룹 중분류 저장
    public void itemGroupSSave(ItemGroupS itemGroupS){
        ItemGroupSRepository.save(itemGroupS);
    }

    // 상품그룹 중분류 객체 가져오기
    public ItemGroupS findByItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        return itemGroupSRepositoryCustom.findByItemGroupcodeS(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 중분류 정보
    public ItemGroupSInfo findByBsItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        return itemGroupSRepositoryCustom.findByBsItemGroupcodeS(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 중분류 리스트 호출
    public List<ItemGroupSListDto> findByItemGroupSList(ItemGroup bgItemGroupcode) {
        return itemGroupSRepositoryCustom.findByItemGroupSList(bgItemGroupcode);
    }

    // 상품그룹 상품순서 리스트 가져오기 - 가맹점 검색
    public List<ItemGroupSUserListDto> findByItemGroupSUserList(String filterCode, String filterName) {
        return itemGroupSRepositoryCustom.findByItemGroupSUserList(filterCode, filterName);
    }

    // 상품그룹 중분류 삭제
    public void findByItemGroupSDelete(ItemGroupS itemGroupS) {
        ItemGroupSRepository.delete(itemGroupS);
    }

    // 상품그룹 상품소재 저장
    public void itemSave(Item item){
        ItemRepository.save(item);
    }

    // 상품그룹 상품소재 객체 가져오기
    public Optional<Item> findByBiItemcode(String biItemcode) {
        return ItemRepository.findByBiItemcode(biItemcode);
    }

    public Optional<Item> findByBiItem(String bgItemGroupcode, String bsItemGroupcodeS) {
        return ItemRepository.findByBiItem(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 상품소재 리스트 호출
    public List<ItemListDto> findByItemList(String bgItemGroupcode, String bsItemGroupcodeS, String biItemcode, String biName) {
        return itemRepositoryCustom.findByItemList(bgItemGroupcode, bsItemGroupcodeS, biItemcode, biName);
    }

    // 상품그룹 상품소재 삭제
    public void findByItemDelete(Item itemOptional) {
        ItemRepository.delete(itemOptional);
    }


    // @@@@@@@@@@@@@@@@@@@@ 상품 가격관리 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 상품코드를 통해 가격 검색
    public Optional<ItemPrice> findByItemPriceByBiItemcode(String biItemcode) {
        return itemPriceRepository.findByItemPriceByBiItemcode(biItemcode);
    }

    // 상품 가격 검색
    public ItemPriceDto findByItemPrice(String biItemcode, String closeDt) {
        return itemPriceRepositoryCustom.findByItemPrice(biItemcode, closeDt);
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

    // 상품 가격 리스트 호출
    public List<ItemPriceListDto> findByItemPriceList(String bgName, String biItemcode, String biName, String setDt) {
        return itemPriceRepositoryCustom.findByItemPriceList(bgName, biItemcode, biName, setDt);
    }

    // 상품 가격 업데이트 때 사용
    public Optional<ItemPrice> findByItemPriceOptional(String biItemcode, String setDt, String closeDt) {
        return itemPriceRepository.findByItemPriceOptional(biItemcode, setDt, closeDt);
    }

    // 상품 가격 삭제
    public void findByItemPriceDelete(List<ItemPrice> itemPrice) {
        itemPriceRepository.deleteAll(itemPrice);
    }

    // @@@@@@@@@@@@@@@@   가맹점 특정품목가격 페이지   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 가맹점 특정품목가격 저장
    public void franchisePriceSave(List<FranchisePrice> franchisePriceList) {
        franchisePriceRepository.saveAll(franchisePriceList);
    }

    // 가맹점 특정품목가격 리스트
    public List<FranchisePriceListDto> findByFranchisePriceList(String frCode) {
        return franchisePriceRepositoryCustom.findByFranchisePriceList(frCode);
    }

    // 가맹점 특정품목가격 중복검사
    public Optional<FranchisePrice> findByFranchisePrice(String biItemcode, String frCode) {
        return franchisePriceRepository.findByFranchisePrice(biItemcode, frCode);
    }

    // 가맹점 특정품목가격 삭제
    public void findByFranchisePriceDelete(List<FranchisePrice> franchisePriceList) {
        franchisePriceRepository.deleteAll(franchisePriceList);
    }

    // @@@@@@@@@@@@@@@@@@@@  가맹점 접수 페이지  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 가맹점 대분류 전용 순번적용
    public List<UserItemGroupSortDto> findByUserItemGroupSortDtoList(String frCode) {
        return itemGroupRepositoryCustom.findByUserItemGroupSortDtoList(frCode);
    }

    // 가맹점 가격 전용 순번적용
    public List<UserItemPriceSortDto> findByUserItemPriceSortList(String frCode, String nowDate) {
        return itemPriceRepositoryCustom.findByUserItemPriceSortList(frCode, nowDate);
    }

    // 가맹점 접수페이지 중분류 리스트 Dto
    public List<UserItemGroupSListDto> findByUserItemGroupSList(){
        return itemGroupSRepositoryCustom.findByUserItemGroupSList();
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

    // 지사코드로 조회하여 해당 지사에 배치된 가맹점이 존재하는지 확인하는 함수
    public List<FranchiseSearchDto> findByFranchiseBrcode(String brCode) {
        return franchiseRepository.findByFranchiseBrcode(brCode);
    }

    // 지사삭제
    public void findByBranchDelete(Branch branch) {
        branchRepository.delete(branch);
    }

    // 가맹점삭제
    public void findByFranchiseDelete(Franchise franchise) {
        franchiseRepository.delete(franchise);
    }

    // 모바일전용 가맹점명 호출 함수
    public FranchiseNameInfoDto findByFranchiseNameInfo(String frCode) {
        return franchiseRepository.findByFranchiseNameInfo(frCode);
    }

    // 멀티스크린 사용여부 가져오기
    public FranchiseMultiscreenDto findByFranchiseMultiscreen(String frCode) {
        return franchiseRepository.findByFranchiseMultiscreen(frCode);
    }

}
