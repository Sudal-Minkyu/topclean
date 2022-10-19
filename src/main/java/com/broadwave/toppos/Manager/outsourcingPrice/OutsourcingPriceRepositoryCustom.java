package com.broadwave.toppos.Manager.outsourcingPrice;

import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListDto;

import java.util.List;

/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 QueryDSL 추상체
 */
public interface OutsourcingPriceRepositoryCustom {

    List<OutsourcingPriceListDto> findByOutsourcingPriceList(String biItemcode, String biName, String bpOutsourcingYn, String brCode);

    OutsourcingPriceDto findByOutsourcingPrice(String biItemcode, String brCode);

    OutsourcingPrice findByOutsourcingPriceAll(String biItemcodes, String brCode);

}
