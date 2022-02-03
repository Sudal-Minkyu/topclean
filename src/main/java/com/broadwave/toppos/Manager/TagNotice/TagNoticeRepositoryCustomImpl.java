package com.broadwave.toppos.Manager.TagNotice;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2021-11-18
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TagNoticeRepositoryCustomImpl extends QuerydslRepositorySupport implements TagNoticeRepositoryCustom {

    public TagNoticeRepositoryCustomImpl() {
        super(TagNotice.class);
    }

    @Override
    public Page<TagNoticeListDto> findByTagNoticeList(String searchType, String searchString, Pageable pageable) {
        QTagNotice tagNotice  = QTagNotice.tagNotice;

        JPQLQuery<TagNoticeListDto> query = from(tagNotice)
                .select(Projections.constructor(TagNoticeListDto.class,
                        tagNotice.htSubject,
                        tagNotice.insert_id,
                        tagNotice.insertDateTime
                ));

//        if (!piFacilityType.equals("")){
//            query.where(performance.piFacilityType.eq(piFacilityType));
//        }
//        if (!piKind.equals("")){
//            query.where(performance.piKind.eq(piKind));
//        }
//        if (!piFacilityName.equals("")){
//            query.where(performance.piFacilityName.containsIgnoreCase(piFacilityName));
//        }

        query.orderBy(tagNotice.htId.desc());

        final List<TagNoticeListDto> performanceListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(performanceListDtos, pageable, query.fetchCount());
    }




}
