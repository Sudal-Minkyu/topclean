package com.broadwave.toppos.Head.Item.Price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPriceRepository extends JpaRepository<ItemPrice,Long> {

    @Query("select a from ItemPrice a where a.biItemcode = :biItemcode and a.setDt = :setDt and a.closeDt = :closeDt")
    Optional<ItemPrice> findByItemPriceOptional(String biItemcode, String setDt, String closeDt);

    @Query("select a from ItemPrice a where a.biItemcode = :biItemcode")
    Optional<ItemPrice> findByItemPriceByBiItemcode(String biItemcode);

}