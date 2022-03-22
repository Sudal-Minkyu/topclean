package com.broadwave.toppos.Head.Franchise;

import com.broadwave.toppos.Head.Franchise.FranchiseDtos.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark :
 */
public interface FranchiseRepositoryCustom {
    List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode, String frContractState);

    FranchiseInfoDto findByFranchiseInfo(String frCode);

    List<FranchiseSearchDto> findByFranchiseBrcode(String brCode);

    FranchiseUserDto findByFranchiseUserInfo(String frCode);

    List<FranchiseManagerListDto> findByManagerInFranchise(String brCode);

    FranchiseNameInfoDto findByFranchiseNameInfo(String frCode);

    // 멀티스크린 사용여부 가져오기
    FranchiseMultiscreenDto findByFranchiseMultiscreen(String frCode);
}
