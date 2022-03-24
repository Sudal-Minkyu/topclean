package com.broadwave.toppos.Head.Item.Group.C;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-24
 * Time :
 * Remark :
 */
public interface ItemRepositoryCustom {
    List<ItemListDto> findByItemList(String bgItemGroupcode, String bsItemGroupcodeS, String biItemcode, String biName);
}
