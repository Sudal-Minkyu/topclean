package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-02-22
 * Time :
 * Remark :
 */
public interface TagNoticeFileRepositoryCustom {

    List<TagNoticeFileListDto> findByTagNoticeFileList(Long htId); // 파일리스트 호출

}
