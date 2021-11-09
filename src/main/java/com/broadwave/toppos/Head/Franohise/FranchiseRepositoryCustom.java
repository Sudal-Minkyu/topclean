package com.broadwave.toppos.Head.Franohise;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark :
 */
public interface FranchiseRepositoryCustom {
    List<FranchiseListDto> findByFranchiseList(String brAssignState, String frName);
}
