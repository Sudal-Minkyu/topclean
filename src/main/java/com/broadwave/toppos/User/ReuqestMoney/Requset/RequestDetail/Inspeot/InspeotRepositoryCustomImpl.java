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
    public List<InspeotDto> findByInspeotDtoList(List<Long> fiId){
        QInspeot inspeot = QInspeot.inspeot;
        JPQLQuery<InspeotDto> query = from(inspeot)
                .where(inspeot.id.in(fiId).and(inspeot.fiSendMsgYn.eq("N").and(inspeot.fiCustomerConfirm.eq("1"))))
                .select(Projections.constructor(InspeotDto.class,
                        inspeot.id,
                        inspeot.fiPhotoYn,
                        inspeot.fiSendMsgYn,
                        inspeot.fiCustomerConfirm
                ));

        return query.fetch();
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

    // 수기마감 - 검품 미확인("1")이거나, 고객거부상태("3")를 포함한 리스트 호출
    @Override
    public List<InspeotYnDto> findByInspeotClosingList(List<Long> fdIdList){
        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<InspeotYnDto> query = from(inspeot)
                .where(inspeot.fdId.id.in(fdIdList).and(inspeot.fiCustomerConfirm.eq("1")).and(inspeot.fiCustomerConfirm.eq("3")))
                .groupBy(inspeot.fdId.id)

                .select(Projections.constructor(InspeotYnDto.class,
                        inspeot.fdId.id
                ));

        query.orderBy(inspeot.id.desc());

        return query.fetch();
    }


    // 검품 타입이 F(가맹검품)이고 미확인인 상태의 리스트 호출
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

    // 검품 타입이 B(지사검품)이고 미확인인 상태의 리스트 호출
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
