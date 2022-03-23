package com.broadwave.toppos.User.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("select a from Customer a where a.bcHp = :bcHp and a.frCode = :frCode group by a.bcHp")
    Optional<Customer> findByBcHp(String bcHp, String frCode);

    @Query("select a from Customer a where a.bcId = :bcId")
    Optional<Customer> findByBcId(Long bcId);
}