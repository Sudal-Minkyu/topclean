package com.broadwave.toppos.Manager.TagNotice.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagNoticeCommentRepository extends JpaRepository<TagNoticeComment,Long> {

}