package com.broadwave.toppos.Head.Item.Group.A;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-18
 * Time :
 * Remark :
 */
public interface ItemGroupRepositoryCustom {
    List<ItemGroupDto> findByItemGroupList();
    List<ItemGroupNameListDto> findByItemGroupName();
}
