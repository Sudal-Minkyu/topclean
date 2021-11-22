package com.broadwave.toppos.Manager;

import com.broadwave.toppos.Manager.Calendar.BranchCalendar;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarListDto;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepository;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final BranchCalendarRepository branchCalendarRepository;
    private final BranchCalendarRepositoryCustom branchCalendarRepositoryCustom;

    @Autowired
    public ManagerService(BranchCalendarRepository branchCalendarRepository, BranchCalendarRepositoryCustom branchCalendarRepositoryCustom){
        this.branchCalendarRepository = branchCalendarRepository;
        this.branchCalendarRepositoryCustom = branchCalendarRepositoryCustom;
    }

    // 업무휴무일지정 저장
    public void branchCalendarSave(BranchCalendar branchCalendar){
        branchCalendarRepository.save(branchCalendar);
    }

    // 업무휴무일 데이터가져오기
    public List<BranchCalendarListDto> branchCalendarDtoList(String brCode, String targetYear){
        return branchCalendarRepositoryCustom.branchCalendarDtoList(brCode, targetYear);
    }


}
