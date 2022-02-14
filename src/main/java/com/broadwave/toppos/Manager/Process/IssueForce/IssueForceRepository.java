package com.broadwave.toppos.Manager.Process.IssueForce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-01-24
 * Time :
 * Remark :
 */
@Repository
public interface IssueForceRepository extends JpaRepository<IssueForce,Long> {

    // 강제출고 데이터 조회
    @Query("select a from IssueForce a where a.fdId = :fdId")
    IssueForce findByFdId(Long fdId);

}