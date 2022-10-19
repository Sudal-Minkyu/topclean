package com.broadwave.toppos.User.Addprocess;

import com.broadwave.toppos.User.Addprocess.AddprocessDtos.AddprocessDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-10
 * Time :
 * Remark :
 */
public interface AddprocessRepositoryCustom {
    List<AddprocessDto> findByAddProcessDtoList(String frCode, String baType);

    List<Addprocess> findByAddProcessList(String frCode, String baType);
}
