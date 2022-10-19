package com.broadwave.toppos.Manager.TagGallery;

import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryDetailDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryListDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryMainListDto;

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

    // 택분실 갤러리 상세보기 호출
    TagGalleryDetailDto findByTagGalleryDetail(Long btId, String brCode);

    // 메인페이지용 택분실 갤러리 리스트 호출
    List<TagGalleryMainListDto> findByTagGalleryMainList(String brCode);
}
