package com.broadwave.toppos.Manager.TagGallery;

import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryListDto;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
public interface TagGalleryRepositoryCustom {
    // 택분실 갤러리 리스트 호출
    List<TagGalleryListDto> findByTagGalleryList(String searchString, String filterFromDt, String filterToDt, String brCode);
}
