package com.broadwave.toppos.Manager.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchCalendarRepository extends JpaRepository<BranchCalendar,Long>, BranchCalendarRepositoryCustom {
    @Query("select a from BranchCalendar a where a.brCode = :brCode and a.bcDate = :bcDate")
    Optional<BranchCalendar> branchCalendarInfo(String brCode, String bcDate);
}