package com.broadwave.toppos.Head.Branoh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranohRepository extends JpaRepository<Branoh,Long> {
    @Query("select a from Branoh a where a.brCode = :brCode")
    Optional<Branoh> findByBrCode(String brCode);
}