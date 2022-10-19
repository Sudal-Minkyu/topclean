package com.broadwave.toppos.Head.Calculate.ReceiptDaily;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDailyRepository extends JpaRepository<ReceiptDaily, Long> {

}