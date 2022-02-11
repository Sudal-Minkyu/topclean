package com.broadwave.toppos.Head.Notice;

import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeListDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewSubDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2022-02-07
 * Time :
 * Remark :
 */
public interface NoticeRepositoryCustom {
    Page<NoticeListDto> findByNoticeList(String searchString, LocalDateTime filterFromDt, LocalDateTime filterToDt, Pageable pageable);

    NoticeViewDto findByNoticeView(Long hnId);
    NoticeViewSubDto findByNoticePreView(Long hnId);
    NoticeViewSubDto findByNoticeNextView(Long hnId);

}
