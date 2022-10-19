package com.broadwave.toppos.Head.AddCost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddCostRepository extends JpaRepository<AddCost,Long> {
    @Query("select a from AddCost a where a.bcId = :bcId")
    Optional<AddCost> findByAddCost(String bcId);
}