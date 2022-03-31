package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class PhotoRepositoryCustomImpl extends QuerydslRepositorySupport implements PhotoRepositoryCustom {

    public PhotoRepositoryCustomImpl() {
        super(PhotoRepository.class);
    }

    public List<PhotoDto> findByPhotoDtoRequestDtlList(Long fdId){

        QPhoto photo = QPhoto.photo;

        JPQLQuery<PhotoDto> query = from(photo)
                .where(photo.fdId.id.eq(fdId))
                .select(Projections.constructor(PhotoDto.class,
                        photo.id,
                        photo.ffPath,
                        photo.ffFilename,
                        photo.ffRemark
                    ));
        return query.fetch();
    }

    public List<PhotoDto> findByPhotoDtoInspeotList(Long fiId){

        QPhoto photo = QPhoto.photo;

        JPQLQuery<PhotoDto> query = from(photo)
                .where(photo.fiId.id.eq(fiId)).limit(6)
                .select(Projections.constructor(PhotoDto.class,
                        photo.id,
                        photo.ffPath,
                        photo.ffFilename,
                        photo.ffRemark
                ));
        return query.fetch();
    }

}
