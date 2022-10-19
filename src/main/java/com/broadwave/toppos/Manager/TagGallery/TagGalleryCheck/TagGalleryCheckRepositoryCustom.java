package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
public interface TagGalleryCheckRepositoryCustom {
    // 택분실 갤러리 가맹점 응답데이터
    List<TagGalleryCheckListDto> findByTagGalleryCheckList(Long btId);
}
