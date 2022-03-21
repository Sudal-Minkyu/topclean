package com.broadwave.toppos.Manager.TagGallery.TagGalleryFile;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
public interface TagGalleryFileRepositoryCustom {
    // 택분실 갤러리 파일데이터 호출 : limit 3
    List<TagGalleryFileListDto> findByTagGalleryFileList(Long btId, Integer limit);
}
