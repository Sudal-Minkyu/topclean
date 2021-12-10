package com.broadwave.toppos.Head.Addprocess;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark :
 */
public interface AddProcessRepositoryCustom {
    List<AddprocessDto> findByAddProcess(String frCode, String baType);
}
