package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.*;
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

    public List<PhotoDto> findByPhotoDto(Long id){

        QPhoto photo = QPhoto.photo;

        JPQLQuery<PhotoDto> query = from(photo)
                .where(photo.fdId.id.eq(id))
                .select(Projections.constructor(PhotoDto.class,
                        photo.ffPath,
                        photo.ffFilename,
                        photo.ffRemark
                    ));
        return query.fetch();
    }

    public List<Photo> findByPhoto(Long id){

        QPhoto photo = QPhoto.photo;

        JPQLQuery<Photo> query = from(photo)
                .where(photo.fdId.id.eq(id))
                .select(Projections.constructor(Photo.class,
                        photo.Id,
                        photo.ffType,
                        photo.fdId,
                        photo.ffPath,
                        photo.ffFilename,
                        photo.ffRemark,
                        photo.insert_id,
                        photo.insertDateTime
                ));
        return query.fetch();
    }

}
