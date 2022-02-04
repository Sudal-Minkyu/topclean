package com.broadwave.toppos.Manager.TagNotice.Comment;

import com.broadwave.toppos.Account.QAccount;
import com.broadwave.toppos.Manager.TagNotice.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestUnCollectDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
public class TagNoticeCommentRepositoryCustomImpl extends QuerydslRepositorySupport implements TagNoticeCommentRepositoryCustom {

    public TagNoticeCommentRepositoryCustomImpl() {
        super(TagNoticeComment.class);
    }

    @Override
    public List<TagNoticeCommentListDto> findByTagNoticeCommentList(Long htId, String login_id){
        QTagNoticeComment tagNoticeComment = QTagNoticeComment.tagNoticeComment;
        QAccount account = QAccount.account;
        JPQLQuery<TagNoticeCommentListDto> query = from(tagNoticeComment)
                .innerJoin(account).on(account.userid.eq(tagNoticeComment.insert_id))
                .select(Projections.constructor(TagNoticeCommentListDto.class,
                        tagNoticeComment.hcId,
                        account.username,
                        tagNoticeComment.hcComment,
                        new CaseBuilder()
                                .when(tagNoticeComment.modifyDateTime.isNotNull()).then(tagNoticeComment.modifyDateTime)
                                .otherwise(tagNoticeComment.insertDateTime),
                        tagNoticeComment.hcType,
                        tagNoticeComment.hcPreId,
                        new CaseBuilder()
                                .when(tagNoticeComment.insert_id.eq(login_id)).then("1")
                                .otherwise("2")
                ));

        query.where(tagNoticeComment.htId.eq(htId));

        return query.fetch();
    }

}
