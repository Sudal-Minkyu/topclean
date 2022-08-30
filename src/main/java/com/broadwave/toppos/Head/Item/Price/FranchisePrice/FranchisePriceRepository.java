package com.broadwave.toppos.Head.Item.Price.FranchisePrice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranchisePriceRepository extends JpaRepository<FranchisePrice,Long>, FranchisePriceRepositoryCustom {
    @Query("select a from FranchisePrice a where a.biItemcode = :biItemcode and  a.frCode = :frCode")
    Optional<FranchisePrice> findByFranchisePrice(String biItemcode, String frCode);   // 가맹점 특정품목가격 중복검사
}