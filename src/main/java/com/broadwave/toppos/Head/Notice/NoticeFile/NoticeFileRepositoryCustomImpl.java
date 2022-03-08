package com.broadwave.toppos.Head.Notice.NoticeFile;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-07
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class NoticeFileRepositoryCustomImpl extends QuerydslRepositorySupport implements NoticeFileRepositoryCustom {

    public NoticeFileRepositoryCustomImpl() {
        super(NoticeFile.class);
    }

    @Override
    public List<NoticeFileListDto> findByNoticeFileList(Long hnId) {

        QNoticeFile noticeFile  = QNoticeFile.noticeFile;

        JPQLQuery<NoticeFileListDto> query = from(noticeFile)
                .where(noticeFile.hnId.hnId.eq(hnId))
                .select(Projections.constructor(NoticeFileListDto.class,
                        noticeFile.hfId,
                        noticeFile.hfPath,
                        noticeFile.hfFilename,
                        noticeFile.hfOriginalFilename,
                        noticeFile.hfVolume
                ));

        return query.fetch();
    }

}
