package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-01
 * Time :
 * Remark : 상품 가격관리 페이지 업데이트, 삭제 배열 가져오는 클래스
 */
@Setter
public class ItemPriceSet {

    // 수정 행 리스트
    private ArrayList<ItemPriceDto> update;

    // 삭제 행 리스트
    private ArrayList<ItemPriceDto> delete;

    public ArrayList<ItemPriceDto> getUpdate() {
        return update;
    }

    public ArrayList<ItemPriceDto> getDelete() {
        return delete;
    }
}
