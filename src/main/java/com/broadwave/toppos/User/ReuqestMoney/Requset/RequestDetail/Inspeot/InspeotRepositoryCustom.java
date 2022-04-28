package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 RepositoryCustom
 */
public interface InspeotRepositoryCustom {

    List<InspeotListDto> findByInspeotList(Long fdId);

    List<InspeotMainListDto> findByInspeotB1(String brCode, Integer limit, String frCode);  // 메인페이지용 검품 리스트

    //  가맹검품/지사검품 조회 - 확인품 정보 요청
    InspeotInfoDto findByInspeotInfo(Long fiId, String code, String type);

}
