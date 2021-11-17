package com.broadwave.toppos.Test.Girdtest;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-03
 * Time :
 * Remark : 그리드 테스트 테이블(리스트출력) RepositoryCustom
 */
public interface GridTestRepositoryCustom {
    List<GridTestDto> findByAllList();
}
