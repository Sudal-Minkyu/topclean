package com.broadwave.toppos.Head.Item.Group.A;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-11-17
 * Time :
 * Remark : 상품그룹관리 페이지의 추가, 업데이트, 삭제 배열 가져오는 클래스
 */
@Setter
public class ItemGroupSet {

    // 수정 행 리스트
    private ArrayList<ItemGroupDto> update;

    // 추가 행 리스트
    private ArrayList<ItemGroupDto> add;

    // 삭제 행 리스트
    private ArrayList<ItemGroupDto> delete;

    public ArrayList<ItemGroupDto> getUpdate() {
        return update;
    }

    public ArrayList<ItemGroupDto> getAdd() {
        return add;
    }

    public ArrayList<ItemGroupDto> getDelete() {
        return delete;
    }
}
