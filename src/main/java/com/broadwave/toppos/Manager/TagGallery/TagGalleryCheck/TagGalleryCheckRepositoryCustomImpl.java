package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TagGalleryCheckRepositoryCustomImpl extends QuerydslRepositorySupport implements TagGalleryCheckRepositoryCustom {

    public TagGalleryCheckRepositoryCustomImpl() {
        super(TagGalleryCheck.class);
    }


}
