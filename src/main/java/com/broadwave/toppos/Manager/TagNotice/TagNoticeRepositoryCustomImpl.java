package com.broadwave.toppos.Manager.TagNotice;

import com.broadwave.toppos.Account.QAccount;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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

    // 게시물 리스트 데이터호출
    @Override
    public Page<TagNoticeListDto> findByTagNoticeList(String searchType, String searchString, String frbrCode, Pageable pageable) {
        QTagNotice tagNotice  = QTagNotice.tagNotice;

        JPQLQuery<TagNoticeListDto> query = from(tagNotice)
                .select(Projections.constructor(TagNoticeListDto.class,
                        tagNotice.htId,
                        tagNotice.htSubject,
                        tagNotice.insert_id,
                        tagNotice.insertDateTime
                ));

        query.orderBy(tagNotice.htId.desc());
        tagNotice.brCode.eq(frbrCode);

//        if(searchString != null){
//            switch (searchType) {
//                case "0":
//                    query.where(tagNotice.htSubject.containsIgnoreCase(searchString).or(tagNotice.insert_id.containsIgnoreCase(searchString).or(tagNotice.bcAddress.containsIgnoreCase(searchString))));
//                    break;
//                case "1":
//                    query.where(customer.bcName.containsIgnoreCase(searchString));
//                    break;
//                case "2":
//                    query.where(customer.bcHp.containsIgnoreCase(searchString));
//                    break;
//                default:
//                    query.where(customer.bcAddress.containsIgnoreCase(searchString));
//                    break;
//            }
//        }

        final List<TagNoticeListDto> performanceListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(performanceListDtos, pageable, query.fetchCount());
    }

    // 게시물 글 데이터호출
    @Override
    public TagNoticeViewDto findByTagNoticeView(Long htId,String login_id, String frbrCode) {

        QTagNotice tagNotice = QTagNotice.tagNotice;
        QAccount account = QAccount.account;

        JPQLQuery<TagNoticeViewDto> query =
                from(tagNotice)
                .innerJoin(account).on(account.userid.eq(tagNotice.insert_id))
                .select(Projections.constructor(TagNoticeViewDto.class,
                        tagNotice.htId,
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



}
