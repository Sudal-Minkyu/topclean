package com.broadwave.toppos.Head.Franchise;

import com.broadwave.toppos.Head.Franchise.FranchiseDtos.*;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseEventListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseSearchInfoDto;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseTagNoSearchInfoDto;

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

    // 지사코드로 조회하여 해당 지사에 배치된 가맹점이 존재하는지 확인하는 함수
    List<FranchiseSearchDto> findByFranchiseBrcode(String brCode);

    FranchiseUserDto findByFranchiseUserInfo(String frCode);

    List<FranchiseManagerListDto> findByManagerInFranchise(String brCode);

    FranchiseNameInfoDto findByFranchiseNameInfo(String frCode);

    // 멀티스크린 사용여부 가져오기
    FranchiseMultiscreenDto findByFranchiseMultiscreen(String frCode);

    // 택번호, 택번호타입 가져오기
    FranchiseTagDataDto findByFranchiseTag(String frCode);

    List<FranchiseSearchInfoDto> findByFranchiseSearchInfo();
    List<FranchiseTagNoSearchInfoDto> findByFranchiseTagNoSearchInfo();

    void findByFranchiseDue(String yyyymmdd, String frCode); // proc_hc_daily_fr 프로시저 호출

    List<FranchiseEventListDto> findByEventList(Long brId, String nowDate);

}
