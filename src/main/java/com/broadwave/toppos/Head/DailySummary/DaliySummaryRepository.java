package com.broadwave.toppos.Head.DailySummary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaliySummaryRepository extends JpaRepository<DaliySummary, String>, DaliySummaryRepositoryCustom {

}