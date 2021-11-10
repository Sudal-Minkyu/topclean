package com.broadwave.toppos.Head;

import com.broadwave.toppos.Head.Branoh.Branch;
import com.broadwave.toppos.Head.Branoh.BranchListDto;
import com.broadwave.toppos.Head.Branoh.BranchRepository;
import com.broadwave.toppos.Head.Branoh.BranchRepositoryCustomImpl;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.Franohise.FranchiseListDto;
import com.broadwave.toppos.Head.Franohise.FranchiseRepository;
import com.broadwave.toppos.Head.Franohise.FranchiseRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeadService {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branohRepository;

    private final FranchiseRepositoryCustom franchiseRepositoryCustom;
    private final BranchRepositoryCustomImpl branohRepositoryCustom;

    @Autowired
    public HeadService(BranchRepository branohRepository, FranchiseRepository franchiseRepository, FranchiseRepositoryCustom franchiseRepositoryCustom, BranchRepositoryCustomImpl branohRepositoryCustom){
        this.branohRepository = branohRepository;
        this.franchiseRepository = franchiseRepository;
        this.franchiseRepositoryCustom = franchiseRepositoryCustom;
        this.branohRepositoryCustom = branohRepositoryCustom;
    }

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
    public List<FranchiseListDto> findByFranchiseList(String brAssignState, String frName, String frCode, String frContractState) {
        return franchiseRepositoryCustom.findByFranchiseList(brAssignState, frName, frCode, frContractState);
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

}
