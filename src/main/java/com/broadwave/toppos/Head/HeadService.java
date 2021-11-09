package com.broadwave.toppos.Head;

import com.broadwave.toppos.Head.Branoh.Branoh;
import com.broadwave.toppos.Head.Branoh.BranohListDto;
import com.broadwave.toppos.Head.Branoh.BranohRepository;
import com.broadwave.toppos.Head.Branoh.BranohRepositoryCustomImpl;
import com.broadwave.toppos.Head.Franohise.Franohise;
import com.broadwave.toppos.Head.Franohise.FranohiseListDto;
import com.broadwave.toppos.Head.Franohise.FranohiseRepository;
import com.broadwave.toppos.Head.Franohise.FranohiseRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeadService {

    private final FranohiseRepository franohiseRepository;
    private final BranohRepository branohRepository;

    private final FranohiseRepositoryCustom franohiseRepositoryCustom;
    private final BranohRepositoryCustomImpl branohRepositoryCustom;

    @Autowired
    public HeadService(BranohRepository branohRepository,  FranohiseRepository franohiseRepository, FranohiseRepositoryCustom franohiseRepositoryCustom, BranohRepositoryCustomImpl branohRepositoryCustom){
        this.branohRepository = branohRepository;
        this.franohiseRepository = franohiseRepository;
        this.franohiseRepositoryCustom = franohiseRepositoryCustom;
        this.branohRepositoryCustom = branohRepositoryCustom;
    }

    // 가맹점 저장
    public Franohise franohiseSave(Franohise franohise){
        franohiseRepository.save(franohise);
        return franohise;
    }

    // 지사 저장
    public Branoh branohSave(Branoh branoh){
        branohRepository.save(branoh);
        return branoh;
    }

    // 가맹점 리스트 API
    public List<FranohiseListDto> findByFranohiseList(String brAssignState, String frName) {
        return franohiseRepositoryCustom.findByFranohiseList(brAssignState, frName);
    }

    // 지사 리스트 API
    public List<BranohListDto> findByBranohList() {
        return branohRepositoryCustom.findByBranohList();
    }

    // 가맹점코드 중복확인 API
    public Optional<Franohise> findByFrCode(String frCode){
        return franohiseRepository.findByFrCode(frCode);
    }

    // 지점코드 중복확인 API
    public Optional<Branoh> findByBrCode(String brCode){
        return branohRepository.findByBrCode(brCode);
    }

}
