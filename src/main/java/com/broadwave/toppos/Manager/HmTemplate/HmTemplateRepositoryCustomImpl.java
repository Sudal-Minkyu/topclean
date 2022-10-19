package com.broadwave.toppos.Manager.HmTemplate;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-26
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class HmTemplateRepositoryCustomImpl extends QuerydslRepositorySupport implements HmTemplateRepositoryCustom {

    public HmTemplateRepositoryCustomImpl() {
        super(HmTemplate.class);
    }

    @Override
    public  List<HmTemplateDto> findByHmTemplateDtos(String brCode) {

        QHmTemplate hmTemplate = QHmTemplate.hmTemplate;

        JPQLQuery<HmTemplateDto> query = from(hmTemplate)
                .select(Projections.constructor(HmTemplateDto.class,
                    hmTemplate.hmId,
                    hmTemplate.hmNum,
                    hmTemplate.hmSubject,
                    hmTemplate.hmMessage
                ));

        query.orderBy(hmTemplate.hmNum.asc()).limit(6);
        query.where(hmTemplate.brCode.eq(brCode));

        return query.fetch();
    }


}
