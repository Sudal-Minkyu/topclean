package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-04
 * Time :
 * Remark : Toppos 접수세부 검품정보 테이블 Repository
 */
@Repository
public interface InspeotRepository extends JpaRepository<Inspeot,Long> {

    @Transactional
    @Modifying
    @Query("delete from Inspeot a where a.id in :inspeotDeleteList")
    void findByInspectDelete(List<Long> inspeotDeleteList);

}