package com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagGalleryCheckRepository extends JpaRepository<TagGalleryCheck,Long>, TagGalleryCheckRepositoryCustom {

}