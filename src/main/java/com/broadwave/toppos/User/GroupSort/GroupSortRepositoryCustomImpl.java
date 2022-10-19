package com.broadwave.toppos.User.GroupSort;

import com.broadwave.toppos.User.GroupSort.GroupSortDtos.GroupSortUpdateDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark :
 */
@Repository
public class GroupSortRepositoryCustomImpl extends QuerydslRepositorySupport implements GroupSortRepositoryCustom {

    public GroupSortRepositoryCustomImpl() {
        super(GroupSortRepository.class);
    }

    public List<GroupSortUpdateDto> findByGroupSortList(String frCode) {
        QGroupSort groupSort = QGroupSort.groupSort;

        JPQLQuery<GroupSortUpdateDto> query = from(groupSort)
                .select(Projections.constructor(GroupSortUpdateDto.class,
                        groupSort.bgItemGroupcode,
                        groupSort.insert_id,
                        groupSort.insertDateTime
                ));

        query.where(groupSort.frCode.eq(frCode));

        return query.fetch();
    }



}
