package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.QPhoto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 RepositoryCustomImpl
 */
@Slf4j
@Repository
public class InspeotRepositoryCustomImpl extends QuerydslRepositorySupport implements InspeotRepositoryCustom {

    public InspeotRepositoryCustomImpl() {
        super(Inspeot.class);
    }

    @Override
    public List<InspeotListDto> findByInspeotList(Long fdId, String type){
        QInspeot inspeot = QInspeot.inspeot;
        QPhoto photo = QPhoto.photo;
        JPQLQuery<InspeotListDto> query = from(inspeot)
                .leftJoin(photo).on(photo.fiId.eq(inspeot))
                .where(inspeot.fdId.id.eq(fdId))
                .select(Projections.constructor(InspeotListDto.class,
                        inspeot.id,
                        inspeot.fdId.id,
                        inspeot.fiType,
                        inspeot.fiComment,
                        inspeot.fiAddAmt,
                        inspeot.fiPhotoYn,
                        inspeot.fiSendMsgYn,
                        inspeot.fiCustomerConfirm,
                        inspeot.insertDateTime,
                        photo.ffPath,
                        photo.ffFilename
                ));

        if(type.equals("1")){
            query.where(inspeot.fiType.eq("F"));
        }

        query.orderBy(inspeot.id.desc());

        return query.fetch();
    }

    @Override
    public List<InspeotYnDto> findByInspeotYnF(List<Long> fdIdList){
        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<InspeotYnDto> query = from(inspeot)
                .where(inspeot.fdId.id.in(fdIdList)
                        .and(inspeot.fiCustomerConfirm.eq("1")
                                .and(inspeot.fiType.eq("F"))))
                .groupBy(inspeot.fdId.id)

                .select(Projections.constructor(InspeotYnDto.class,
                        inspeot.fdId.id
                ));

        query.orderBy(inspeot.id.desc());

        return query.fetch();
    }

    @Override
    public List<InspeotYnDto> findByInspeotYnB(List<Long> fdIdList){
        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<InspeotYnDto> query = from(inspeot)
                .where(inspeot.fdId.id.in(fdIdList)
                        .and(inspeot.fiCustomerConfirm.eq("1")
                                .and(inspeot.fiType.eq("B"))))
                .groupBy(inspeot.fdId.id)

                .select(Projections.constructor(InspeotYnDto.class,
                        inspeot.fdId.id
                ));

        query.orderBy(inspeot.id.desc());

        return query.fetch();
    }

}
