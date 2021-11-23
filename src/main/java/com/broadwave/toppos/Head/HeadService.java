package com.broadwave.toppos.Head;

import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchRepository;
import com.broadwave.toppos.Head.Branoh.BranchRepositoryCustomImpl;
import com.broadwave.toppos.Head.Franohise.*;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepository;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepositoryCustom;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupS;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSRepository;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeadService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branohRepository;
    private final ItemGroupRepository ItemGroupRepository;
    private final ItemGroupSRepository ItemGroupSRepository;

    private final FranchiseRepositoryCustom franchiseRepositoryCustom;
    private final BranchRepositoryCustomImpl branohRepositoryCustom;
    private final ItemGroupRepositoryCustom itemGroupRepositoryCustom;
    private final ItemGroupSRepositoryCustom itemGroupSRepositoryCustom;

    @Autowired
    public HeadService(BranchRepository branohRepository, FranchiseRepository franchiseRepository, FranchiseRepositoryCustom franchiseRepositoryCustom, BranchRepositoryCustomImpl branohRepositoryCustom,
                       ItemGroupRepository ItemGroupRepository, ItemGroupRepositoryCustom itemGroupRepositoryCustom, ItemGroupSRepository ItemGroupSRepository, ItemGroupSRepositoryCustom itemGroupSRepositoryCustom){
        this.branohRepository = branohRepository;
        this.franchiseRepository = franchiseRepository;
        this.franchiseRepositoryCustom = franchiseRepositoryCustom;
        this.branohRepositoryCustom = branohRepositoryCustom;
        this.ItemGroupRepository = ItemGroupRepository;
        this.itemGroupRepositoryCustom = itemGroupRepositoryCustom;
        this.ItemGroupSRepository = ItemGroupSRepository;
        this.itemGroupSRepositoryCustom = itemGroupSRepositoryCustom;
    }

    // // // // // // // // // // // // // // 가맹점, 지사 등록 매칭 페이지 // // // // // // // // // // // // 
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


    // // // // // // // // // // // // // // 상품그룹관리 페이지 // // // // // // // // // // // // //
    // 상품그룹 대분류  저장
    public void itemGroupSave(ItemGroup itemGroup){
        ItemGroupRepository.save(itemGroup);
    }

    // 상품그룹 대분류 중복확인
    public Optional<ItemGroup> findByBgItemGroupcode(String bgItemGroupcode) {
        return ItemGroupRepository.findByBgItemGroupcode(bgItemGroupcode);
    }

    // 상품그룹 대분류 리스트 호출
    public List<ItemGroupDto> findByItemGroupList() {
        return itemGroupRepositoryCustom.findByItemGroupList();
    }

    // 상품그룹 대분류 삭제
    public void findByItemGroupDelete(ItemGroup itemGroup){
        ItemGroupRepository.delete(itemGroup);
    }

    // 상품그룹 중분류 저장
    public void itemGroupSSave(ItemGroupS itemGroupS){
        ItemGroupSRepository.save(itemGroupS);
    }

    // 상품그룹 중분류 중복확인
    public Optional<ItemGroupS> findByBsItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS) {
        return ItemGroupSRepository.findByBsItemGroupcodeS(bgItemGroupcode, bsItemGroupcodeS);
    }

    // 상품그룹 중분류 리스트 호출
    public List<ItemGroupSDto> findByItemGroupSList(String bgItemGroupcode) {
        return itemGroupSRepositoryCustom.findByItemGroupSList(bgItemGroupcode);
    }

    // 상품그룹 중분류 삭제
    public void findByItemGroupSDelete(ItemGroupS itemGroupS) {
        ItemGroupSRepository.delete(itemGroupS);
    }






}
