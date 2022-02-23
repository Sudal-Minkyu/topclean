package com.broadwave.toppos.Manager.TagNotice.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagNoticeCommentRepository extends JpaRepository<TagNoticeComment,Long>, TagNoticeCommentRepositoryCustom {
    // 게시물ID로 댓글 찾기
    @Query("select a from TagNoticeComment a where a.htId = :htId")
    List<TagNoticeComment> findByTagNoticeCommentDelete(Long htId);
}