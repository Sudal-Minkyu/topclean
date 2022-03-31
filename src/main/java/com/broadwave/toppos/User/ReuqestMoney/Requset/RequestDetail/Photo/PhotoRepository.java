package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.Inspeot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long>, PhotoRepositoryCustom {

    @Transactional
    @Modifying
    @Query("delete from Photo a where a.fdId.id in :fdIdList")
    void findByRequestDetailPhotoListDelete(List<Long> fdIdList);

    @Transactional
    @Modifying
    @Query("delete from Photo a where a.fdId.id = :fdId")
    void findByRequestDetailPhotoDelete(Long fdId);

    @Transactional
    @Modifying
    @Query("delete from Photo a where a.fiId.id in :photoDeleteList")
    void findByInspectPhotoDelete(List<Long> photoDeleteList);

    // 게시물ID로 삭제할 파일만 골라 찾기
    @Query("select a from Photo a where a.id in :deleteFileList")
    List<Photo> findByPhotoDeleteList(List<Long> deleteFileList);

    @Query("select a from Photo a where a.fiId.id = :fiId")
    Photo findByPhoto(Long fiId);

}