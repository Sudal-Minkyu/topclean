package com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose;

import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos.QrCloseCountListDto;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos.QrCloseCountSubListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-07-12
 * Time :
 * Remark :
 */
public interface QrClostRepositoryCustom {

    List<QrCloseCountListDto> findByQrClostCntList(String brCode, String filterFromDt, String filterToDt); // QrClost 건수 왼쪽 리스트 Dto

    List<QrCloseCountSubListDto> findByQrClostCntSubList(String brCode, String insertYyyymmdd); // QrClost 오른쪽 리스트 Dto

}
