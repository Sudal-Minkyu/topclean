package com.broadwave.toppos.Manager.TagGallery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagGalleryRepository extends JpaRepository<TagGallery,Long>, TagGalleryRepositoryCustom {
    // 게시물ID로 파일리스트 찾기
    @Query("select a from TagGallery a where a.btId = :btId")
    TagGallery findByTagGallery(Long btId);
}