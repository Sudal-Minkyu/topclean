package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 RepositoryCustom
 */
public interface InspeotRepositoryCustom {
    List<InspeotListDto> findByInspeotList(Long fdId, String type);
    List<InspeotDto> findByInspeotDtoList(List<Long> fiId);

    List<InspeotYnDto> findByInspeotYnF(List<Long> fdIdList);
    List<InspeotYnDto> findByInspeotYnB(List<Long> fdIdList);
}
