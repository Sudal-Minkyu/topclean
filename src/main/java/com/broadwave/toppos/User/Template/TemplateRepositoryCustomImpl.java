package com.broadwave.toppos.User.Template;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-15
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class TemplateRepositoryCustomImpl extends QuerydslRepositorySupport implements TemplateRepositoryCustom {

    public TemplateRepositoryCustomImpl() {
        super(Template.class);
    }

    @Override
    public  List<TemplateDto> findByTemplateDtos(String frCode) {

        QTemplate template = QTemplate.template;

        JPQLQuery<TemplateDto> query = from(template)
                .select(Projections.constructor(TemplateDto.class,
                        template.ftId,
                        template.fmNum,
                        template.fmSubject,
                        template.fmMessage
                ));

        query.orderBy(template.fmNum.asc()).limit(6);
        query.where(template.frCode.eq(frCode));

        return query.fetch();
    }


}
