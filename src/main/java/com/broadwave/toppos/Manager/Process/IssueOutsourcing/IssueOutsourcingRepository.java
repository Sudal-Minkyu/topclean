package com.broadwave.toppos.Manager.Process.IssueOutsourcing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-05-20
 * Time :
 * Remark :
 */
@Repository
public interface IssueOutsourcingRepository extends JpaRepository<IssueOutsourcing,String>, IssueOutsourcingRepositoryCustom {

    // 외주출고 데이터 조회
    @Query("select a from IssueOutsourcing a where a.fdId = :fdId")
    IssueOutsourcing findByFdId(Long fdId);

}