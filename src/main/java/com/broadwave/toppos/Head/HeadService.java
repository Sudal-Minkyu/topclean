package com.broadwave.toppos.Head;

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
import com.broadwave.toppos.Head.Item.Price.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final FranchiseRepositoryCustom franchiseRepositoryCustom;
    private final BranchRepositoryCustomImpl branohRepositoryCustom;
    private final ItemGroupRepositoryCustom itemGroupRepositoryCustom;
    private final ItemGroupSRepositoryCustom itemGroupSRepositoryCustom;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final ItemPriceRepositoryCustom itemPriceRepositoryCustom;

    @Autowired
    public HeadService(BranchRepository branohRepository, FranchiseRepository franchiseRepository, FranchiseRepositoryCustom franchiseRepositoryCustom, BranchRepositoryCustomImpl branohRepositoryCustom,
                       ItemGroupRepository ItemGroupRepository, ItemGroupRepositoryCustom itemGroupRepositoryCustom, ItemGroupSRepository ItemGroupSRepository, ItemGroupSRepositoryCustom itemGroupSRepositoryCustom,
                       ItemRepository ItemRepository, ItemRepositoryCustom itemRepositoryCustom, ItemPriceRepository itemPriceRepository, ItemPriceRepositoryCustom itemPriceRepositoryCustom){
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
    }

    // // // // // // // // // // // // // // 가맹점, 지사 등록 매칭 페이지 // // // // // // // // // // // // //
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


    // // // // // // // // // // // // // // 상품 그룹관리 페이지 // // // // // // // // // // // // //
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

    // // // // // // // // // // // // // // 상품 가격관리 페이지 // // // // // // // // // // // // //
    // 상품 가격 검색
    public ItemPriceDto findByItemPrice(String biItemcode, String highClassYn, String setDtReplace) {
        return itemPriceRepositoryCustom.findByItemPrice(biItemcode, highClassYn, setDtReplace);
    }

    // 상품 가격 저장
    public void itemPriceSave(List<ItemPrice> itemPrice) throws Exception {
        itemPriceRepository.saveAll(itemPrice);
    }

    // 상품 가격 리스트 호출
    public List<ItemPriceListDto> findByItemPriceList() {
        return itemPriceRepositoryCustom.findByItemPriceList();
    }

}
