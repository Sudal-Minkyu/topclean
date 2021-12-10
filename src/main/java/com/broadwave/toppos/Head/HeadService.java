package com.broadwave.toppos.Head;

import com.broadwave.toppos.Head.AddCost.AddCostDto;
import com.broadwave.toppos.Head.AddCost.AddCostRepositoryCustom;
import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchRepository;
import com.broadwave.toppos.Head.Branoh.BranchRepositoryCustomImpl;
import com.broadwave.toppos.Head.Franohise.*;
import com.broadwave.toppos.Head.Item.Group.A.*;
import com.broadwave.toppos.Head.Item.Group.B.*;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepository;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepositoryCustom;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePrice;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceListDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceRepository;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceRepositoryCustom;
import com.broadwave.toppos.Head.Item.Price.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HeadService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branohRepository;
    private final ItemGroupRepository ItemGroupRepository;
    private final ItemGroupSRepository ItemGroupSRepository;
    private final ItemRepository ItemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final FranchisePriceRepository franchisePriceRepository;

    private final FranchiseRepositoryCustom franchiseRepositoryCustom;
    private final BranchRepositoryCustomImpl branohRepositoryCustom;
    private final ItemGroupRepositoryCustom itemGroupRepositoryCustom;
    private final ItemGroupSRepositoryCustom itemGroupSRepositoryCustom;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final ItemPriceRepositoryCustom itemPriceRepositoryCustom;
    private final FranchisePriceRepositoryCustom franchisePriceRepositoryCustom;
    private final AddCostRepositoryCustom costRepositoryCustom;

    @Autowired
    public HeadService(BranchRepository branohRepository, FranchiseRepository franchiseRepository, FranchiseRepositoryCustom franchiseRepositoryCustom, BranchRepositoryCustomImpl branohRepositoryCustom,
                       ItemGroupRepository ItemGroupRepository, ItemGroupRepositoryCustom itemGroupRepositoryCustom, ItemGroupSRepository ItemGroupSRepository, ItemGroupSRepositoryCustom itemGroupSRepositoryCustom,
                       ItemRepository ItemRepository, ItemRepositoryCustom itemRepositoryCustom, ItemPriceRepository itemPriceRepository, ItemPriceRepositoryCustom itemPriceRepositoryCustom,
                       FranchisePriceRepository franchisePriceRepository, FranchisePriceRepositoryCustom franchisePriceRepositoryCustom, AddCostRepositoryCustom costRepositoryCustom){
        this.branohRepository = branohRepository;
        this.franchiseRepository = franchiseRepository;
        this.franchiseRepositoryCustom = franchiseRepositoryCustom;
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
        this.costRepositoryCustom = costRepositoryCustom;
    }


    // @@@@@@@@@@@@@@@@@@@@@@ 가맹점, 지사 등록 매칭 페이지 @@@@@@@@@@@@@@@@@@@@
    // 가맹점 저장
    public Franchise franchiseSave(Franchise franchise){
        franchiseRepository.save(franchise);
        return franchise;
    }

    // 지사 저장
    public Branch branchSave(Branch branoh){
        branohRepository.save(branoh);
        return branoh;
    }

    // 가맹점 리스트 API
    public List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frContractState) {
        return franchiseRepositoryCustom.findByFranchiseList(brCode, brAssignState, frName, frCode, frContractState);
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
        return branohRepository.findByBrCode(brCode);
    }

    // 가맹점 정보 호출하기
    public FranchisInfoDto findByFranchiseInfo(String frCode) {
        return franchiseRepositoryCustom.findByFranchiseInfo(frCode);
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
    public List<ItemListDto> findByItemList(String bgItemGroupcode, String bsItemGroupcodeS) {
        return itemRepositoryCustom.findByItemList(bgItemGroupcode, bsItemGroupcodeS);
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
    public ItemPriceDto findByItemPrice(String biItemcode, String setDtReplace) {
        return itemPriceRepositoryCustom.findByItemPrice(biItemcode, setDtReplace);
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
    public List<ItemPriceListDto> findByItemPriceList() {
        return itemPriceRepositoryCustom.findByItemPriceList();
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
        return costRepositoryCustom.findByAddCost();
    }

}
