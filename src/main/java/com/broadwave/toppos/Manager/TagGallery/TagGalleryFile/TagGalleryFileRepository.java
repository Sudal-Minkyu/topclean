package com.broadwave.toppos.Manager.TagGallery.TagGalleryFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TagGalleryFileRepository extends JpaRepository<TagGalleryFile,Long>, TagGalleryFileRepositoryCustom {

    // 게시물ID로 파일리스트 찾기
    @Query("select a from TagGalleryFile a where a.btId.btId = :btId")
    List<TagGalleryFile> findByTagGalleryFile(Long btId);

    // 게시물ID로 삭제할 파일만 골라 찾기
    @Query("select a from TagGalleryFile a where a.bfId in :deleteFileList")
    List<TagGalleryFile> findByTagGalleryFileDeleteList(List<Long> deleteFileList);

    // 게시물 파일 일괄삭제
    @Transactional
    @Modifying
    @Query("delete from TagGalleryFile a where a.bfId in :deleteFileList")
    void tagGalleryFileListDelete(List<Long> deleteFileList);


}