package com.broadwave.toppos.Test.Girdtest;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-03
 * Time :
 * Remark : 그리드 테스트 테이블(리스트출력) RepositoryCustomImpl
 */
@Repository
public class GridTestRepositoryCustomImpl extends QuerydslRepositorySupport implements GridTestRepositoryCustom {

    public GridTestRepositoryCustomImpl() {
        super(GridTest.class);
    }

    @Override
    public List<GridTestDto> findByAllList() {
        QGridTest gridTest = QGridTest.gridTest;

        JPQLQuery<GridTestDto> query = from(gridTest)
                .select(Projections.constructor(GridTestDto.class,
                        gridTest.testName,
                        gridTest.testOld,
                        gridTest.testGender,
                        gridTest.testMoney
                ));

        query.orderBy(gridTest.id.desc());
        return query.fetch();
    }


}
