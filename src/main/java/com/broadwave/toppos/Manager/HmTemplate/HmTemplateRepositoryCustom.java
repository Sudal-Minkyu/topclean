package com.broadwave.toppos.Manager.HmTemplate;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-04-26
 * Time :
 * Remark :
 */
public interface HmTemplateRepositoryCustom {

    List<HmTemplateDto> findByHmTemplateDtos(String brCode);

}
