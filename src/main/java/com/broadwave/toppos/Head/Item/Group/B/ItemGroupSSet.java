package com.broadwave.toppos.Head.Item.Group.B;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-11-17
 * Time :
 * Remark : 상품그룹관리 페이지의 추가, 업데이트, 삭제 배열 가져오는 클래스
 */
@Setter
public class ItemGroupSSet {

    // 수정 행 리스트
    private ArrayList<ItemGroupSDto> update;

    // 추가 행 리스트
    private ArrayList<ItemGroupSDto> add;

    // 삭제 행 리스트
    private ArrayList<ItemGroupSDto> delete;

    public ArrayList<ItemGroupSDto> getUpdate() {
        return update;
    }

    public ArrayList<ItemGroupSDto> getAdd() {
        return add;
    }

    public ArrayList<ItemGroupSDto> getDelete() {
        return delete;
    }
}
