package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2022-01-24
 * Time :
 * Remark :
 */
@Repository
public interface InhouseRepository extends JpaRepository<InhouceForce,Long> {

////     강제입고 데이터 조회
//    @Query("select a from IssueForce a where a.fdId = :fdId")
//    InhouceForce findByFdId(Long fdId);

}