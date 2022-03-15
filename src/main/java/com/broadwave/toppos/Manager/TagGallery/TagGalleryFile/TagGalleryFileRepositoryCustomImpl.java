package com.broadwave.toppos.Manager.TagGallery.TagGalleryFile;

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
public class TagGalleryFileRepositoryCustomImpl extends QuerydslRepositorySupport implements TagGalleryFileRepositoryCustom {

    public TagGalleryFileRepositoryCustomImpl() {
        super(TagGalleryFile.class);
    }


}
