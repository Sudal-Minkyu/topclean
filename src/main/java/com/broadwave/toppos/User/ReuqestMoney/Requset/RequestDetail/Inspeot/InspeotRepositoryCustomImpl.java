package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.QPhoto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    @Autowired
    JpaResultMapper jpaResultMapper;

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
        }else if(type.equals("2")) {
            query.where(inspeot.fiType.eq("B"));
        }

        query.orderBy(inspeot.id.desc());

        return query.fetch();
    }


    // 검품 타입이 F(가맹검품)이고 미확인인 상태의 리스트 호출
    @Override
    public List<InspeotYnDto> findByInspeotYnFAndType1(List<Long> fdIdList){
        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<InspeotYnDto> query = from(inspeot)
                .where(inspeot.fdId.id.in(fdIdList)
                                .and(inspeot.fiType.eq("F")))
                .groupBy(inspeot.fdId.id)

                .select(Projections.constructor(InspeotYnDto.class,
                        inspeot.fdId.id
                ));

        query.orderBy(inspeot.id.desc());

        return query.fetch();
    }

    // 검품 타입이 B(지사검품)이고 미확인인 상태의 리스트 호출
    @Override
    public List<InspeotYnDto> findByInspeotYnBAndType1(List<Long> fdIdList){
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

    // 메인페이지용 검품 리스트 호출쿼리
    @Override
    public List<InspeotMainListDto> findByInspeotB1(String brCode, Integer limit, String frCode){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT g.fr_name, d.bc_name, f.bg_name, b.fd_tag, c.fi_customer_confirm, a.fr_yyyymmdd, IFNULL(b.fd_s2_dt,'x') \n");
        sb.append("FROM fs_request a \n");
        sb.append("INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id \n");
        sb.append("LEFT OUTER JOIN fs_request_inspect c ON c.fd_id = b.fd_id \n");
        sb.append("INNER JOIN bs_customer d ON d.bc_id = a.bc_id \n");
        sb.append("INNER JOIN bs_item e ON e.bi_itemcode = b.bi_itemcode \n");
        sb.append("INNER JOIN bs_item_group f ON f.bg_item_groupcode = e.bg_item_groupcode \n");
        sb.append("INNER JOIN bs_franchise g ON g.fr_code = a.fr_code \n");
        sb.append("WHERE a.br_code = ?1 AND b.fd_cancel = 'N' AND a.fr_confirm_yn = 'Y' \n");
        if(limit==3){
            sb.append("AND IFNULL(b.fd_s2_dt,'x') <>'x' \n");
        }
        if(frCode != null){
            sb.append("AND c.fr_code = ?3 \n");
        }
        sb.append("AND c.fi_type = 'B' \n");
        sb.append("ORDER BY c.insert_date DESC LIMIT ?2 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, brCode);
        query.setParameter(2, limit);
        if(frCode != null){
            query.setParameter(3, frCode);
        }
        return jpaResultMapper.list(query, InspeotMainListDto.class);

    }

    //  지사검품 조회 - 확인품 정보 요청
    @Override
    public InspeotInfoDto findByInspeotInfo(Long fiId, String code, String type){

        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<InspeotInfoDto> query = from(inspeot)
//                .where(inspeot.id.in(fiId).and(inspeot.fiSendMsgYn.eq("N").and(inspeot.fiCustomerConfirm.eq("1"))))
                .where(inspeot.id.in(fiId))
                .select(Projections.constructor(InspeotInfoDto.class,
                        inspeot.id,
                        inspeot.fiComment,
                        inspeot.fiAddAmt,
                        inspeot.fiPhotoYn,
                        inspeot.fiSendMsgYn,
                        inspeot.fiCustomerConfirm
                ));

        if(type.equals("1")){
            inspeot.brCode.eq(code);
        }else{
            inspeot.frCode.eq(code);
        }

        return query.fetchOne();
    }

}
