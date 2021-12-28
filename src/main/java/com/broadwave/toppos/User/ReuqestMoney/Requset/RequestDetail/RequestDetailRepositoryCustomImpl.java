package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
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
public class RequestDetailRepositoryCustomImpl extends QuerydslRepositorySupport implements RequestDetailRepositoryCustom {

    public RequestDetailRepositoryCustomImpl() {
        super(RequestDetailRepository.class);
    }

    public List<RequestDetailDto> findByRequestTempDetailList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;

        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;

        JPQLQuery<RequestDetailDto> query = from(requestDetail)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .select(Projections.constructor(RequestDetailDto.class,
                        requestDetail.id,

                        requestDetail.biItemcode,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        requestDetail.fdPattern,
                        requestDetail.fdPriceGrade,

                        requestDetail.fdOriginAmt,
                        requestDetail.fdNormalAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdRepairAmt,

                        requestDetail.fdAdd1Remark,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdAdd1Amt,

                        requestDetail.fdPressed,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdPollutionLevel,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,

                        requestDetail.fdDiscountGrade,
                        requestDetail.fdDiscountAmt,
                        requestDetail.fdQty,

                        requestDetail.fdRequestAmt,

                        requestDetail.fdRetryYn,

                        requestDetail.fdRemark,
                        requestDetail.fdEstimateDt,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName
                    ));

        query.where(requestDetail.frNo.eq(frNo));

        return query.fetch();
    }

    public List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;

        JPQLQuery<RequestDetailAmtDto> query = from(requestDetail)
                .select(Projections.constructor(RequestDetailAmtDto.class,
                        requestDetail.fdTotAmt
                ));

        query.where(requestDetail.frNo.eq(frNo));

        return query.fetch();
    }



}
