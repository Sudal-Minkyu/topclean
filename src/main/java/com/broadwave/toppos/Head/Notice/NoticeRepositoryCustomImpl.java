package com.broadwave.toppos.Head.Notice;

import com.broadwave.toppos.Account.QAccount;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeListDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewSubDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark :
 */
@Repository
public class NoticeRepositoryCustomImpl extends QuerydslRepositorySupport implements NoticeRepositoryCustom {

    public NoticeRepositoryCustomImpl() {
        super(Notice.class);
    }

    // 공지사항 메인페이지용 리스트 데이터 호출
    @Override
    public List<NoticeListDto> findByMainNoticeList(String brCode) {
        QNotice notice  = QNotice.notice;

        JPQLQuery<NoticeListDto> query = from(notice)
                .select(Projections.constructor(NoticeListDto.class,
                        notice.hnId,
                        notice.hnType,
                        notice.hnSubject,
                        notice.insert_id,
                        notice.insertDateTime
                ));

        if(brCode != null){
            notice.brCode.eq(brCode);
        }

        query.orderBy(notice.hnId.desc()).limit(5);

        return query.fetch();
    }

    // 공지사항 리스트 데이터 호출
    @Override
    public Page<NoticeListDto> findByNoticeList(String hnType, String searchString, String filterFromDt, String filterToDt, Pageable pageable) {
        QNotice notice  = QNotice.notice;

        JPQLQuery<NoticeListDto> query = from(notice)
                .select(Projections.constructor(NoticeListDto.class,
                        notice.hnId,
                        notice.hnType,
                        notice.hnSubject,
                        notice.insert_id,
                        notice.insertDateTime
                ));

        query.orderBy(notice.hnId.desc());

        if(hnType.equals("01")){
            query.where(notice.hnType.eq(hnType));
        }else if(hnType.equals("02")){
            query.where(notice.hnType.eq(hnType));
        }

        if(searchString != null){
            query.where(notice.hnSubject.containsIgnoreCase(searchString));
        }

        if(filterFromDt != null){
            query.where(notice.hnYyyymmdd.goe(filterFromDt));
        }

        if(filterToDt != null){
            query.where(notice.hnYyyymmdd.loe(filterToDt));
        }

        final List<NoticeListDto> noticeListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(noticeListDtos, pageable, query.fetchCount());
    }

    // 게시물 글 데이터호출
    @Override
    public NoticeViewDto findByNoticeView(Long hnId) {

        QNotice notice = QNotice.notice;
        QAccount account = QAccount.account;

        JPQLQuery<NoticeViewDto> query =
                from(notice)
                .innerJoin(account).on(account.userid.eq(notice.insert_id))
                .select(Projections.constructor(NoticeViewDto.class,
                        notice.hnId,
                        notice.hnSubject,
                        notice.hnContent,
                        account.username,
                        notice.hnType,
                        notice.insertDateTime
                ));

        query.where(notice.hnId.eq(hnId));

        return query.fetchOne();
    }

    // 이전 글 데이터호출
    @Override
    public NoticeViewSubDto findByNoticePreView(Long hnId) {

        QNotice notice = QNotice.notice;

        JPQLQuery<NoticeViewSubDto> query =
                from(notice)
                        .where(notice.hnId.lt(hnId)).orderBy(notice.hnId.desc()).limit(1)
                        .select(Projections.constructor(NoticeViewSubDto.class,
                                notice.hnId,
                                notice.hnSubject,
                                notice.hnType,
                                notice.insertDateTime
                        ));

        return query.fetchOne();
    }

    // 다음 글 데이터호출
    @Override
    public NoticeViewSubDto findByNoticeNextView(Long hnId) {

        QNotice notice = QNotice.notice;

        JPQLQuery<NoticeViewSubDto> query =
                from(notice)
                        .where(notice.hnId.gt(hnId)).limit(1)
                        .select(Projections.constructor(NoticeViewSubDto.class,
                                notice.hnId,
                                notice.hnSubject,
                                notice.hnType,
                                notice.insertDateTime
                        ));

        return query.fetchOne();
    }



}
