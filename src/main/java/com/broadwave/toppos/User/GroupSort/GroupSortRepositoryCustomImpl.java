package com.broadwave.toppos.User.GroupSort;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-08
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class GroupSortRepositoryCustomImpl extends QuerydslRepositorySupport implements GroupSortRepositoryCustom {

    public GroupSortRepositoryCustomImpl() {
        super(GroupSort.class);
    }

    @Override
    public List<GroupSortDto> findByGroupSortList(String frCode) {
        QGroupSort groupSort = QGroupSort.groupSort;

        JPQLQuery<GroupSortDto> query = from(groupSort)
                .select(Projections.constructor(GroupSortDto.class,
                        groupSort.frCode,
                        groupSort.bgItemGroupcode,
                        groupSort.bgSort
                ));

        query.where(groupSort.frCode.eq(frCode));
        query.orderBy(groupSort.bgSort.asc());

        return query.fetch();
    }

}
