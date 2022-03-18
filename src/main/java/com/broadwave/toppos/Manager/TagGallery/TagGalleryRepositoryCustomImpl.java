package com.broadwave.toppos.Manager.TagGallery;

import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryListDto;
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
public class TagGalleryRepositoryCustomImpl extends QuerydslRepositorySupport implements TagGalleryRepositoryCustom {

    public TagGalleryRepositoryCustomImpl() {
        super(TagGallery.class);
    }

    public List<TagGalleryListDto>  findByTagGalleryList(String searchString, String filterFromDt, String filterToDt, String brCode){
        QTagGallery tagGallery = QTagGallery.tagGallery;
        JPQLQuery<TagGalleryListDto> query = from(tagGallery)
                .where(tagGallery.brCode.eq(brCode))
                .where(tagGallery.btInputDt.goe(filterFromDt).and(tagGallery.btInputDt.loe(filterToDt)))
                .select(Projections.constructor(TagGalleryListDto.class,
                        tagGallery.insertDateTime,
                        tagGallery.btId,
                        tagGallery.btBrandName,
                        tagGallery.btInputDate,
                        tagGallery.btMaterial,
                        tagGallery.btRemark
                ));

        if(searchString.equals("1")){
            query.where(tagGallery.brCloseYn.eq("N"));
        }

        query.orderBy(tagGallery.insertDateTime.desc());

        return query.fetch();
    }

}
