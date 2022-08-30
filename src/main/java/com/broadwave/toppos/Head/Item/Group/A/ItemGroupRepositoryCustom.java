package com.broadwave.toppos.Head.Item.Group.A;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupEventListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.UserItemGroupSortDto;

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
    List<UserItemGroupSortDto> findByUserItemGroupSortDtoList(String frCode);

    List<ItemGroupEventListDto> findByItemGroupEventList();
}
