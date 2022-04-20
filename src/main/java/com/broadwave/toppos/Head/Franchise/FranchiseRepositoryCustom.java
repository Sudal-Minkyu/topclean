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
    List<FranchiseListDto> findByFranchiseList(String brCode, String brAssignState, String frName, String frCode,String frRefCode, String frContractState);

    FranchiseInfoDto findByFranchiseInfo(String frCode);

    List<FranchiseSearchDto> findByFranchiseBrcode(String brCode);

    FranchiseUserDto findByFranchiseUserInfo(String frCode);

    List<FranchiseManagerListDto> findByManagerInFranchise(String brCode);

    FranchiseNameInfoDto findByFranchiseNameInfo(String frCode);

    // 멀티스크린 사용여부 가져오기
    FranchiseMultiscreenDto findByFranchiseMultiscreen(String frCode);

    // 택번호, 택번호타입 가져오기
    FranchiseTagDataDto findByFranchiseTag(String frCode);

}
