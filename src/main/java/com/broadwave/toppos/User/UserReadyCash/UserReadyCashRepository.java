package com.broadwave.toppos.User.UserReadyCash;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReadyCashRepository extends JpaRepository<UserReadyCash, Long>, UserReadyCashRepositoryCustom {
    @Query("select a from UserReadyCash a where a.frId = :franchiseId and a.bcYyyymmdd = :bcYyyymmdd")
    Optional<UserReadyCash> findByUserReadyCash(Long franchiseId, String bcYyyymmdd);
}