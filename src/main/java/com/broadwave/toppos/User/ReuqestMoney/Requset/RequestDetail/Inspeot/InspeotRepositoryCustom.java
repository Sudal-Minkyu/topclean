package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotMainListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotYnDto;

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

    List<InspeotYnDto> findByInspeotStateList(List<Long> fdIdList, String type);
    List<InspeotYnDto> findByInspeotYnFAndType1(List<Long> fdIdList);
    List<InspeotYnDto> findByInspeotYnBAndType1(List<Long> fdIdList);
    List<InspeotYnDto> findByInspeotYnBAndType3(List<Long> fdIdList);

    List<InspeotMainListDto> findByInspeotB1(String brCode, Integer limit);  // 메인페이지용 검품 리스트

}
