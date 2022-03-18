package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
public interface TagGalleryCheckRepositoryCustom {
    // 택분실 갤러리 가맹점 응답데이터
    TagGalleryCheckListDto findByTagGalleryCheckList(Long btId);
}
