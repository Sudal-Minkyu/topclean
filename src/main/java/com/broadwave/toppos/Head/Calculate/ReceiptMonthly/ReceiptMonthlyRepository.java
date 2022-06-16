package com.broadwave.toppos.Head.Calculate.ReceiptMonthly;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptMonthlyRepository extends JpaRepository<ReceiptMonthly, Long>, ReceiptMonthlyRepositoryCustom {

}