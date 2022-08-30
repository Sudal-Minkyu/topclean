package com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos;

import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-01
 * Time :
 * Remark : 상품 가격관리 페이지 업데이트, 삭제 배열 가져오는 클래스
 */
@Data
public class ItemPriceSet {

    // 저장 행 리스트
    private ArrayList<ItemPriceDto> add;

    // 수정 행 리스트
    private ArrayList<ItemPriceDto> update;

    // 삭제 행 리스트
    private ArrayList<ItemPriceDto> delete;

}
