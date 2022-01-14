package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long> {

    @Transactional
    @Modifying
    @Query("delete from Photo a where a.fiId.id in :photoDeleteList")
    void findByInspectPhotoDelete(List<Long> photoDeleteList);

}