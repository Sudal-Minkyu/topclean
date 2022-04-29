package com.broadwave.toppos.Manager.TagNotice;

import com.broadwave.toppos.Account.QAccount;
import com.broadwave.toppos.Manager.TagNotice.Comment.QTagNoticeComment;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeListDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeTestDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeViewDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeViewSubDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2022-02-04
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TagNoticeRepositoryCustomImpl extends QuerydslRepositorySupport implements TagNoticeRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public TagNoticeRepositoryCustomImpl() {
        super(TagNotice.class);
    }

    // 게시물 리스트 데이터호출
    @Override
    public Page<TagNoticeListDto> findByTagNoticeList(String searchString, LocalDateTime filterFromDt, LocalDateTime filterToDt, String frbrCode, Pageable pageable) {
        QTagNotice tagNotice  = QTagNotice.tagNotice;
        QTagNoticeComment tagNoticeComment  = QTagNoticeComment.tagNoticeComment;

        JPQLQuery<TagNoticeListDto> query = from(tagNotice)
                .leftJoin(tagNoticeComment).on(tagNoticeComment.htId.eq(tagNotice.htId))
                .select(Projections.constructor(TagNoticeListDto.class,
                        tagNoticeComment.count(),
                        tagNotice.htId,
                        tagNotice.htSubject,
                        tagNotice.insert_id,
                        tagNotice.insertDateTime
                ));

        query.orderBy(tagNotice.htId.desc()).groupBy(tagNotice.htId);
        query.where(tagNotice.brCode.eq(frbrCode));

        if(searchString != null){
            query.where(tagNotice.htSubject.containsIgnoreCase(searchString));
        }

        if(filterFromDt != null){
            query.where(tagNotice.insertDateTime.goe(filterFromDt));
        }

        if(filterToDt != null){
            query.where(tagNotice.insertDateTime.loe(filterToDt));
        }

        final List<TagNoticeListDto> tagNoticeListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(tagNoticeListDtos, pageable, query.fetchCount());
    }

    // 게시물 글 데이터호출
    @Override
    public TagNoticeViewDto findByTagNoticeView(Long htId, String frbrCode) {

        QTagNotice tagNotice = QTagNotice.tagNotice;
        QAccount account = QAccount.account;

        JPQLQuery<TagNoticeViewDto> query =
                from(tagNotice)
                .innerJoin(account).on(account.userid.eq(tagNotice.insert_id))
                .select(Projections.constructor(TagNoticeViewDto.class,
                        tagNotice.htId,
                        tagNotice.brCode,
                        tagNotice.htSubject,
                        tagNotice.htContent,
                        account.username,
                        tagNotice.insertDateTime
                ));

        query.where(tagNotice.htId.eq(htId).and(tagNotice.brCode.eq(frbrCode)));

        return query.fetchOne();
    }

    // 이전 글 데이터호출
    @Override
    public TagNoticeViewSubDto findByTagNoticePreView(Long htId, String frbrCode) {

        QTagNotice tagNotice = QTagNotice.tagNotice;

        JPQLQuery<TagNoticeViewSubDto> query =
                from(tagNotice)
                        .where(tagNotice.htId.lt(htId)).orderBy(tagNotice.htId.desc()).limit(1)
                        .select(Projections.constructor(TagNoticeViewSubDto.class,
                            tagNotice.htId,
                            tagNotice.htSubject,
                            tagNotice.insertDateTime
                        ));

        query.where(tagNotice.brCode.eq(frbrCode));

        return query.fetchOne();
    }

    // 다음 글 데이터호출
    @Override
    public TagNoticeViewSubDto findByTagNoticeNextView(Long htId, String frbrCode) {

        QTagNotice tagNotice = QTagNotice.tagNotice;

        JPQLQuery<TagNoticeViewSubDto> query =
                from(tagNotice)
                        .where(tagNotice.htId.gt(htId)).limit(1)
                        .select(Projections.constructor(TagNoticeViewSubDto.class,
                                tagNotice.htId,
                                tagNotice.htSubject,
                                tagNotice.insertDateTime
                        ));

        query.where(tagNotice.brCode.eq(frbrCode));

        return query.fetchOne();
    }

//    @Override
//    public List<TagNoticeTestDto> findByGroupByTest() {
//
//
//        QTagNotice tagNotice  = QTagNotice.tagNotice;
//
//
//        JPQLQuery<TagNoticeTestDto> query = from(tagNotice)
//                .select(Projections.constructor(TagNoticeTestDto.class,
//                        tagNotice.htSubject,
//                        tagNotice.insert_id,
//                        tagNotice.count()
//                ));
//
//        query.groupBy(tagNotice.htSubject,tagNotice.insert_id);
//
//        return query.fetch();
//
//    }
//
//    @Override
//    public List<TagNoticeTestDto> findByGroupByNativeTest() {
//
//        EntityManager em = getEntityManager();
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("SELECT ht_subject as subject ,insert_id, \n");
//        sb.append("  sum(case WHEN LENGTH(ht_subject) > 5 THEN 2 ELSE 0 END )  numOfCount  \n");
//        sb.append("   FROM hb_tag_notice where insert_id = ?1  GROUP BY insert_id,ht_subject  \n");
//
//
//        Query query = em.createNativeQuery(sb.toString());
//
//
//        query.setParameter(1, "admin1");
//
//
//        List<TagNoticeTestDto> result = jpaResultMapper.list(query, TagNoticeTestDto.class);
//
//
//        return result;
//
//    }


}
