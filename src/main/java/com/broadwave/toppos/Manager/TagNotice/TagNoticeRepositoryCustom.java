package com.broadwave.toppos.Manager.TagNotice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Minkyu
 * Date : 2022-01-28
 * Time :
 * Remark :
 */
public interface TagNoticeRepositoryCustom {
    Page<TagNoticeListDto> findByTagNoticeList(String searchType, String searchString, Pageable pageable);
}