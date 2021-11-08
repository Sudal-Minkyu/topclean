package com.broadwave.toppos.Head.Franohise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranohiseRepository extends JpaRepository<Franohise,Long> {
    @Query("select a from Franohise a where a.frCode = :frCode")
    Optional<Franohise> findByFrCode(String frCode);
}