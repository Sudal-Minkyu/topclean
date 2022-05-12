package com.broadwave.toppos.Head.Branch;

import com.broadwave.toppos.Head.Branch.BranchDtos.head.BranchSearchInfoDto;
import com.broadwave.toppos.Head.Branoh.BranchDtos.BranchListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark :
 */
public interface BranchRepositoryCustom {
    List<BranchListDto> findByBranchList(String brName, String brCode, String brContractState);

    List<BranchSearchInfoDto> findByBranchSearchInfo();

}
