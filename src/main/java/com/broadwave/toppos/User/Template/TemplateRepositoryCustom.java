package com.broadwave.toppos.User.Template;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-15
 * Time :
 * Remark :
 */
public interface TemplateRepositoryCustom {

    List<TemplateDto> findByTemplateDtos(String frCode);

}
