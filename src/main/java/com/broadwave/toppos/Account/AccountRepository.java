package com.broadwave.toppos.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>, AccountRepositoryCustom {
    Optional<Account> findByUserid(String userid);
    boolean existsByUserid(String userid);
}