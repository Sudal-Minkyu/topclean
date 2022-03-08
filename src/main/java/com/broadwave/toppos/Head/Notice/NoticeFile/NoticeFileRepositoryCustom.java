package com.broadwave.toppos.Head.Notice.NoticeFile;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-07
 * Time :
 * Remark :
 */
public interface NoticeFileRepositoryCustom {

    List<NoticeFileListDto> findByNoticeFileList(Long hnId); // 파일리스트 호출

}
