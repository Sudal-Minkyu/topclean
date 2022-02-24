package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-02-22
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TagNoticeFileRepositoryCustomImpl extends QuerydslRepositorySupport implements TagNoticeFileRepositoryCustom {

    public TagNoticeFileRepositoryCustomImpl() {
        super(TagNoticeFile.class);
    }

    @Override
    public List<TagNoticeFileListDto> findByTagNoticeFileList(Long htId) {

        QTagNoticeFile tagNoticeFile  = QTagNoticeFile.tagNoticeFile;
        JPQLQuery<TagNoticeFileListDto> query = from(tagNoticeFile)
                .where(tagNoticeFile.htId.htId.eq(htId))
                .select(Projections.constructor(TagNoticeFileListDto.class,
                        tagNoticeFile.hfId,
                        tagNoticeFile.hfPath,
                        tagNoticeFile.hfFilename,
                        tagNoticeFile.hfOriginalFilename,
                        tagNoticeFile.hfVolume
                ));

        return query.fetch();
    }

}
