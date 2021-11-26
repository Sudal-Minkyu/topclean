package com.broadwave.toppos.Head.Item.Price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPriceRepository extends JpaRepository<ItemPrice,Long> {

}