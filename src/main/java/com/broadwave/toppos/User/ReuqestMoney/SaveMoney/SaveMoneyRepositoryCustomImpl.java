package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import com.broadwave.toppos.User.Customer.Customer;
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

}
