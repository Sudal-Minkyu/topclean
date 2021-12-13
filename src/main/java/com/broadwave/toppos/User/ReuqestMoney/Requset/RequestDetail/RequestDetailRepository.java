package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail,Long> {
    @Query("select a from RequestDetail a where a.frNo = :frNo and a.fdTag = :fdTag")
    Optional<RequestDetail> findByRequestDetail(String frNo, String fdTag);
}