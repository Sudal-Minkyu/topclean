package com.broadwave.toppos.Head.Item.Price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPriceRepository extends JpaRepository<ItemPrice,Long>, ItemPriceRepositoryCustom {

    @Query("select a from ItemPrice a where a.biItemcode = :biItemcode and a.setDt = :setDt and a.closeDt = :closeDt")
    Optional<ItemPrice> findByItemPriceOptional(String biItemcode, String setDt, String closeDt);   // 상품 가격 업데이트 때 사용

    @Query("select a from ItemPrice a where a.biItemcode = :biItemcode")
    Optional<ItemPrice> findByItemPriceByBiItemcode(String biItemcode);

}