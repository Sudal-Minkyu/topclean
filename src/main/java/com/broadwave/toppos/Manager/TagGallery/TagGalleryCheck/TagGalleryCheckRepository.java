package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagGalleryCheckRepository extends JpaRepository<TagGalleryCheck,Long>, TagGalleryCheckRepositoryCustom {

    // 게시물ID로 체크리스트 찾기
    @Query("select a from TagGalleryCheck a where a.btId.btId = :btId")
    List<TagGalleryCheck> findByTagGalleryCheck(Long btId);

    // 게시물ID와 가맹점코드로 파일리스트 찾기
    @Query("select a from TagGalleryCheck a where a.btId.btId = :btId and a.frCode = :frCode")
    TagGalleryCheck findByTagGalleryFrCode(Long btId, String frCode);

}