package com.broadwave.toppos.Head.Branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long>, BranchRepositoryCustom {
    @Query("select a from Branch a where a.brCode = :brCode")
    Optional<Branch> findByBrCode(String brCode);
}