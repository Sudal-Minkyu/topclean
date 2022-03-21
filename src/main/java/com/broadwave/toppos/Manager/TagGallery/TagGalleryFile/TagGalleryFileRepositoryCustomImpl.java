package com.broadwave.toppos.Manager.TagGallery.TagGalleryFile;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<TagGalleryFileListDto> findByTagGalleryFileList(Long btId, Integer limit){
        QTagGalleryFile tagGalleryFile = QTagGalleryFile.tagGalleryFile;
        JPQLQuery<TagGalleryFileListDto> query = from(tagGalleryFile)
            .where(tagGalleryFile.btId.btId.eq(btId))

            .select(Projections.constructor(TagGalleryFileListDto.class,
                    tagGalleryFile.bfId,
                    tagGalleryFile.bfPath,
                    tagGalleryFile.bfFilename
            ));

        query.orderBy(tagGalleryFile.bfId.asc()).limit(limit);

        return query.fetch();
    }

}
