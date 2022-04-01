package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.QRequestDetail;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-01
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class FindRepositoryCustomImpl extends QuerydslRepositorySupport implements FindRepositoryCustom {

    public FindRepositoryCustomImpl() {
        super(Find.class);
    }

    // 물건찾기 리스트 Dto
    @Override
    public List<FindListDto> findByFindList(Long franchiseId, String brCode, String filterFromDt, String filterToDt, String ffState){

        QFind find = QFind.find;
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QCustomer customer = QCustomer.customer;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<FindListDto> query = from(find)
                .innerJoin(requestDetail).on(requestDetail.eq(find.fdId))
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(customer).on(customer.eq(request.bcId))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .orderBy(find.id.desc())
                .select(Projections.constructor(FindListDto.class,
                        franchise.frName,
                        customer.bcName,
                        find.ffYyyymmdd,
                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,
                        requestDetail.fdTag,
                        requestDetail.fdColor,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdTotAmt,
                        requestDetail.fdState,

                        find.ffState,

                        requestDetail.fdRemark
                ));

        if(ffState != null){
            query.where(find.ffState.eq(ffState));
        }

        if(franchiseId != 0){
            query.where(franchise.id.eq(franchiseId));
        }
        query.where(request.brCode.eq(brCode));
        query.where(find.ffYyyymmdd.goe(filterFromDt).and(find.ffYyyymmdd.loe(filterToDt)));

        return query.fetch();
    }


    // 모바일 전용 수기마감 querydsl
    public int findByFindCheckUpdate(List<Long> ffIdList, String login_id){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE fs_request_find a\n");
        sb.append("SET \n");
        sb.append("a.ff_state = '02', \n");
        sb.append("a.modify_id = ?2, a.modify_date= NOW() \n");
        sb.append("WHERE a.ff_id in ?1  \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, ffIdList);
        query.setParameter(2, login_id);

        return query.executeUpdate();
    }

}
