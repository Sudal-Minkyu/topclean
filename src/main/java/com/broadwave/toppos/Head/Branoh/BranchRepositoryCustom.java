package com.broadwave.toppos.Head.Branoh;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-09
 * Time :
 * Remark :
 */
public interface BranchRepositoryCustom {
    List<BranchListDto> findByBranchList(String brName, String brCode, String brContractState);
}
