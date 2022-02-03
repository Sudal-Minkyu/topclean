package com.broadwave.toppos.Manager.Calendar;

import com.broadwave.toppos.Head.Franohise.QFranchise;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarListDto;
import com.broadwave.toppos.User.UserDtos.EtcDataDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-18
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class BranchCalendarRepositoryCustomImpl extends QuerydslRepositorySupport implements BranchCalendarRepositoryCustom {

    public BranchCalendarRepositoryCustomImpl() {
        super(BranchCalendar.class);
    }

    @Override
    public List<BranchCalendarListDto> branchCalendarDtoList(String brCode, String targetYear) {
         QBranchCalendar branchCalendar = QBranchCalendar.branchCalendar;

        JPQLQuery<BranchCalendarListDto> query = from(branchCalendar)
            .select(Projections.constructor(BranchCalendarListDto.class,
                branchCalendar.bcDate,
                branchCalendar.bcDayoffYn
            ));

        query.where(branchCalendar.brCode.eq(brCode));
        query.where(branchCalendar.bcDate.substring(0,4).eq(targetYear));

        return query.fetch();
    }

    // 가맹점 출고예정일, 택번호 호출
    @Override
    public List<EtcDataDto> findByEtc(Long frEstimateDuration, String frCode, String nowDate) {

        QBranchCalendar branchCalendar = QBranchCalendar.branchCalendar;
        QFranchise franchise = QFranchise.franchise;

        log.info(frEstimateDuration+"일 후 출고예정");

        JPQLQuery<EtcDataDto> query = from(branchCalendar)
            .innerJoin(franchise).on(franchise.frCode.eq(frCode))
            .where(branchCalendar.bcDate.gt(nowDate).and(branchCalendar.bcDayoffYn.eq("N")))
            .orderBy(branchCalendar.bcDate.asc()).limit(frEstimateDuration)
            .select(Projections.constructor(EtcDataDto.class,
                    franchise.frCode,
                    franchise.frName,
                    franchise.frLastTagno,
                    branchCalendar.bcDate,
                    franchise.frBusinessNo,
                    franchise.frRpreName,
                    franchise.frTelNo
            ));
        return query.fetch();
    }


}
