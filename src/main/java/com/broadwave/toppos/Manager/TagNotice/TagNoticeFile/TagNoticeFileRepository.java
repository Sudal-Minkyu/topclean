package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TagNoticeFileRepository extends JpaRepository<TagNoticeFile,Long>, TagNoticeFileRepositoryCustom {

    // 게시물ID로 파일리스트 찾기
    @Query("select a from TagNoticeFile a where a.htId.htId = :htId")
    List<TagNoticeFile> findByTagNoticeFileDelete(Long htId);

    // 게시물ID로 삭제할 파일만 골라 찾기
    @Query("select a from TagNoticeFile a where a.hfId in :deleteFileList")
    List<TagNoticeFile> findByTagNoticeFileDeleteList(List<Long> deleteFileList);

    // 게시물 파일 일괄삭제
    @Transactional
    @Modifying
    @Query("delete from TagNoticeFile a where a.hfId in :deleteFileList")
    void tagNoticeFileListDelete(List<Long> deleteFileList);

}