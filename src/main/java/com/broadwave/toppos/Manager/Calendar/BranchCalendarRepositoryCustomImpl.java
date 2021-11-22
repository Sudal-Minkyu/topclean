package com.broadwave.toppos.Manager.Calendar;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepositoryCustom;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
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


}
