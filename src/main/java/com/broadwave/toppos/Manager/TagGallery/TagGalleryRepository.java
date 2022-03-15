package com.broadwave.toppos.Manager.TagGallery;

import com.broadwave.toppos.Manager.TagGallery.TagGalleryFile.TagGalleryFileRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagGalleryRepository extends JpaRepository<TagGallery,Long>, TagGalleryFileRepositoryCustom {

}