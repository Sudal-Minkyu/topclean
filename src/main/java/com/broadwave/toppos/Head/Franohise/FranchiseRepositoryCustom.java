package com.broadwave.toppos.Head.Franohise;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark :
 */
public interface FranchiseRepositoryCustom {
    List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frContractState);

    FranchisInfoDto findByFranchiseInfo(String frCode);

    List<FranchiseSearchDto> findByFranchiseBrcode(String brCode);

    FranchisUserDto findByFranchiseUserInfo(String frCode);
}
