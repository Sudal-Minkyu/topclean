package com.broadwave.toppos.Head.Franchise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise,Long>, FranchiseRepositoryCustom {
    @Query("select a from Franchise a where a.frCode = :frCode")
    Optional<Franchise> findByFrCode(String frCode);
}