package com.broadwave.toppos.Manager.TagNotice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TagNoticeRepository extends JpaRepository<TagNotice,Long> {

}