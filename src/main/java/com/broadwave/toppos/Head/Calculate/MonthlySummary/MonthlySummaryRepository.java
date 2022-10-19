package com.broadwave.toppos.Head.Calculate.MonthlySummary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, String>, MonthlySummaryRepositoryCustom {

}