package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyBusinessdayListDto;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyDto;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyDtos.SaveMoneyListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-16
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class SaveMoneyRepositoryCustomImpl extends QuerydslRepositorySupport implements SaveMoneyRepositoryCustom {

    public SaveMoneyRepositoryCustomImpl() {
        super(SaveMoney.class);
    }

    @Override
    public List<SaveMoneyDto> findBySaveMoney(Customer customer) {
        QSaveMoney saveMoney = QSaveMoney.saveMoney;

        JPQLQuery<SaveMoneyDto> query = from(saveMoney)
                .select(Projections.constructor(SaveMoneyDto.class,
                        saveMoney.fsType,
                        saveMoney.fsAmt
                ));

        query.where(saveMoney.fsType.eq("1").or(saveMoney.fsType.eq("2"))
                .and(saveMoney.fsClose.eq("N"))
                .and(saveMoney.bcId.eq(customer))
        );

        return query.fetch();
    }

    @Override
    public List<SaveMoneyListDto> findBySaveMoneyList(List<Long> customerIdList, String fsType) {
        QSaveMoney saveMoney = QSaveMoney.saveMoney;

        JPQLQuery<SaveMoneyListDto> query = from(saveMoney)
                .groupBy(saveMoney.bcId)
                .orderBy(saveMoney.bcId.bcId.desc())
                .select(Projections.constructor(SaveMoneyListDto.class,
                        saveMoney.bcId.bcId,
                        saveMoney.fsType,
                        saveMoney.fsAmt.sum()
                ));

        query.where(saveMoney.fsType.eq(fsType)
                .and(saveMoney.fsClose.eq("N"))
                .and(saveMoney.bcId.bcId.in(customerIdList))
        );

        return query.fetch();
    }

    // 영업일보 통계 적립금사용금액 sum querydsl - 4
    @Override
    public List<SaveMoneyBusinessdayListDto> findBySaveMoneyBusinessdayListDto(String frCode, String filterFromDt, String filterToDt){
        QSaveMoney saveMoney = QSaveMoney.saveMoney;

        QCustomer customer = QCustomer.customer;

        JPQLQuery<SaveMoneyBusinessdayListDto> query = from(saveMoney)
                .innerJoin(customer).on(saveMoney.bcId.eq(customer))
                .where(customer.frCode.eq(frCode))
                .where(saveMoney.fsYyyymmdd.goe(filterFromDt).and(saveMoney.fsYyyymmdd.loe(filterToDt)))
                .select(Projections.constructor(SaveMoneyBusinessdayListDto.class,
                        saveMoney.fsYyyymmdd,
                        new CaseBuilder()
                                .when(saveMoney.fsType.eq("2")).then(saveMoney.fsAmt)
                                .otherwise(0).sum()
                ));

        query.groupBy(saveMoney.fsYyyymmdd).orderBy(saveMoney.fsYyyymmdd.asc());

        return query.fetch();
    }

}
