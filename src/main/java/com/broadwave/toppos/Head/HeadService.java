package com.broadwave.toppos.Head;

import com.broadwave.toppos.Head.Branoh.Branoh;
import com.broadwave.toppos.Head.Branoh.BranohRepository;
import com.broadwave.toppos.Head.Franohise.Franohise;
import com.broadwave.toppos.Head.Franohise.FranohiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HeadService {

    FranohiseRepository franohiseRepository;
    BranohRepository branohRepository;

    @Autowired
    public HeadService(BranohRepository branohRepository,  FranohiseRepository franohiseRepository){
        this.branohRepository = branohRepository;
        this.franohiseRepository = franohiseRepository;
    }

    public Optional<Franohise> findByFrCode(String frCode){
        return franohiseRepository.findByFrCode(frCode);
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


}
