package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-03-30
 * Time :
 * Remark :
 */
@Repository
public interface FindRepository extends JpaRepository<Find,Long>, FindRepositoryCustom {

    @Query("select a from Find a where a.id in :ffIdList")
    List<Find> findByFindCheckList(List<Long> ffIdList); //  // 지사 물건찾기 확인 업데이트

}