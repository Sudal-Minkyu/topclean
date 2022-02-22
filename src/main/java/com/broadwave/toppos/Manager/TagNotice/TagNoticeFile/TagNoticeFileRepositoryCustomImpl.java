package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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

}
