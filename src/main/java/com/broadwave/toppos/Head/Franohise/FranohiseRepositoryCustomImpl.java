package com.broadwave.toppos.Head.Franohise;

import com.broadwave.toppos.Head.Branoh.QBranoh;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class FranohiseRepositoryCustomImpl extends QuerydslRepositorySupport implements FranohiseRepositoryCustom {

    public FranohiseRepositoryCustomImpl() {
        super(Franohise.class);
    }

    @Override
    public List<FranohiseListDto> findByFranohiseList(String brAssignState, String frName) {
        QFranohise franohise = QFranohise.franohise;
        QBranoh branoh = QBranoh.branoh;

        JPQLQuery<FranohiseListDto> query = from(franohise)
                .leftJoin(franohise.brId,branoh)
                .select(Projections.constructor(FranohiseListDto.class,
                        franohise.frCode,
                        franohise.frName,
                        franohise.frContractDt,
                        franohise.frContractFromDt,
                        franohise.frContractToDt,
                        franohise.frContractState,
                        franohise.frRemark,
                        branoh.brName
                ));

        query.orderBy(franohise.id.desc());

        if (!brAssignState.equals("")){
            query.where(franohise.brAssignState.eq(brAssignState));
        }

        if (!brAssignState.equals("")){
            query.where(franohise.frName.likeIgnoreCase(frName.concat("%")));
        }

        return query.fetch();
    }

}
