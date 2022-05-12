package com.broadwave.toppos.User.UserLogoutLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLogoutLogRepository extends JpaRepository<UserLogoutLog,Long>, UserLogoutLogRepositoryCustom {
    @Query("select a from UserLogoutLog a where a.frCode = :frCode and a.blLogoutDt = :blLogoutDt")
    Optional<UserLogoutLog> findByUserLogoutLog(String frCode, String blLogoutDt);
}