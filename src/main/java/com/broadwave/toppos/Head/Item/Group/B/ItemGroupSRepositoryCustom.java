package com.broadwave.toppos.Head.Item.Group.B;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.*;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-22
 * Time :
 * Remark :
 */
public interface ItemGroupSRepositoryCustom {
    List<ItemGroupSListDto> findByItemGroupSList(ItemGroup bgItemGroupcode);

    ItemGroupSInfo findByBsItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS);

    ItemGroupS findByItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS);

    List<UserItemGroupSListDto> findByUserItemGroupSList();

    List<ItemGroupSUserListDto> findByItemGroupSUserList(String filterCode, String filterName);

    List<ItemGroupSEventListDto> findByItemGroupSEventList(String bgItemGroupcode);
}
