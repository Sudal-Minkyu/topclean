package com.broadwave.toppos.Head.Franohise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise,Long> {
    @Query("select a from Franchise a where a.frCode = :frCode")
    Optional<Franchise> findByFrCode(String frCode);
}