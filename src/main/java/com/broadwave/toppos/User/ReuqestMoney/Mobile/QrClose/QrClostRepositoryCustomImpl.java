package com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose;

import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos.QrCloseCountListDto;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos.QrCloseCountSubListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-07-12
 * Time :
 * Remark :
 */
@Repository
public class QrClostRepositoryCustomImpl extends QuerydslRepositorySupport implements QrClostRepositoryCustom {

    public QrClostRepositoryCustomImpl() {
        super(QrClose.class);
    }

    @Override
    public List<QrCloseCountListDto> findByQrClostCntList(String brCode, String filterFromDt, String filterToDt) {

        QQrClose qrClose = QQrClose.qrClose;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<QrCloseCountListDto> query = from(qrClose)
                .innerJoin(franchise).on(franchise.frCode.eq(qrClose.frCode).and(franchise.brCode.eq(brCode)))
                .where(qrClose.insertYyyymmdd.loe(filterToDt).and(qrClose.insertYyyymmdd.goe(filterFromDt)))
                .select(Projections.constructor(QrCloseCountListDto.class,
                        qrClose.insertYyyymmdd,
                        qrClose.count()
                ));

        query.groupBy(qrClose.insertYyyymmdd);

        return query.fetch();
    }

    @Override
    public List<QrCloseCountSubListDto> findByQrClostCntSubList(String brCode, String insertYyyymmdd) {

        QQrClose qrClose = QQrClose.qrClose;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<QrCloseCountSubListDto> query = from(qrClose)
                .innerJoin(franchise).on(franchise.frCode.eq(qrClose.frCode).and(franchise.brCode.eq(brCode)))
                .where(qrClose.insertYyyymmdd.eq(insertYyyymmdd))
                .select(Projections.constructor(QrCloseCountSubListDto.class,
                    qrClose.insertDt,
                    franchise.frName
                ))
                .orderBy(qrClose.insertDt.asc());


        return query.fetch();
    }

}
