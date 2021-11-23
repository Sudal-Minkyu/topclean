package com.broadwave.toppos.Head.Item.Group.B;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark :
 */
public interface ItemGroupSRepositoryCustom {
    List<ItemGroupSDto> findByItemGroupSList(String bgItemGroupcode);
}
