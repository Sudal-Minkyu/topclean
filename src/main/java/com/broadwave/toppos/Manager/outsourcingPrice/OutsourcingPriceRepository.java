package com.broadwave.toppos.Manager.outsourcingPrice;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 JPA Data
 */
public interface OutsourcingPriceRepository extends JpaRepository<OutsourcingPrice, OutsourcingPricePK>, OutsourcingPriceRepositoryCustom {
}
