package com.broadwave.toppos.Manager.TagNotice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-01-28
 * Time :
 * Remark :
 */
public interface TagNoticeRepositoryCustom {
    Page<TagNoticeListDto> findByTagNoticeList(String searchString, LocalDateTime filterFromDt, LocalDateTime filterToDt, String frbrCode, Pageable pageable);

    TagNoticeViewDto findByTagNoticeView(Long htId, String login_id, String frbrCode);
    TagNoticeViewSubDto findByTagNoticePreView(Long htId, String frbrCode);
    TagNoticeViewSubDto findByTagNoticeNextView(Long htId, String frbrCode);
}
