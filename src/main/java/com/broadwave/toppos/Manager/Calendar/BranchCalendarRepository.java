package com.broadwave.toppos.Manager.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchCalendarRepository extends JpaRepository<BranchCalendar,Long> {

}