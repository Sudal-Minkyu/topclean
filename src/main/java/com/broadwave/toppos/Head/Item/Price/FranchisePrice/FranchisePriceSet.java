package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-11-24
 * Time :
 * Remark : 상품그룹관리 페이지의 추가, 업데이트, 삭제 배열 가져오는 클래스
 */
@Setter
public class FranchisePriceSet {

    // 수정 행 리스트
    private ArrayList<FranchisePriceDto> update;

    // 추가 행 리스트
    private ArrayList<FranchisePriceDto> add;

//    // 삭제 행 리스트
//    private ArrayList<ItemDto> delete;

    public ArrayList<FranchisePriceDto> getUpdate() {
        return update;
    }

    public ArrayList<FranchisePriceDto> getAdd() {
        return add;
    }

//    public ArrayList<ItemDto> getDelete() {
//        return delete;
//    }
}
