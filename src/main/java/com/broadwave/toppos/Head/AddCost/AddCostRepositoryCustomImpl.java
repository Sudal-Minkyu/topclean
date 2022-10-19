package com.broadwave.toppos.Head.AddCost;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class AddCostRepositoryCustomImpl extends QuerydslRepositorySupport implements AddCostRepositoryCustom {

    public AddCostRepositoryCustomImpl() {
        super(AddCost.class);
    }

    @Override
    public AddCostDto findByAddCost() {
         QAddCost addCost = QAddCost.addCost;

        JPQLQuery<AddCostDto> query = from(addCost)
                .select(Projections.constructor(AddCostDto.class,
                        addCost.bcVipDcRt,
                        addCost.bcVvipDcRt,
                        addCost.bcHighRt,
                        addCost.bcPremiumRt,
                        addCost.bcChildRt,

                        addCost.bcPressed,
                        addCost.bcWhitening,

                        addCost.bcPollution1,
                        addCost.bcPollution2,
                        addCost.bcPollution3,
                        addCost.bcPollution4,
                        addCost.bcPollution5,

                        addCost.bcStarch,
                        addCost.bcWaterRepellent,

                        addCost.bcUrgentRate1,
                        addCost.bcUrgentRate2,
                        addCost.bcUrgentAmt1,
                        addCost.bcSmsUnitPrice,
                        addCost.bcProgramFeeBr,
                        addCost.bcProgramFeeFr

                ));

        query.where(addCost.bcId.eq("000"));

        return query.fetchOne();
    }

}
