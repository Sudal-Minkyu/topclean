package com.broadwave.toppos.User.UserLoginLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog,Long> {
    @Query("select a from UserLoginLog a where a.frCode = :frCode and a.blLoginDt = :blLoginDt")
    Optional<UserLoginLog> findByUserLoginLog(String frCode, String blLoginDt);
}