package com.broadwave.toppos.Manager.outsourcingPrice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 JPA Data
 */
public interface OutsourcingPriceRepository extends JpaRepository<OutsourcingPrice, OutsourcingPricePK>, OutsourcingPriceRepositoryCustom {



    @Query("select o from Promotion o")
    List<OutsourcingPrice> findAllItemcodeAndBrCode();
}
