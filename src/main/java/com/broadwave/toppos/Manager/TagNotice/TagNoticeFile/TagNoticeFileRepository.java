package com.broadwave.toppos.Manager.TagNotice.TagNoticeFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagNoticeFileRepository extends JpaRepository<TagNoticeFile,Long>, TagNoticeFileRepositoryCustom {

}