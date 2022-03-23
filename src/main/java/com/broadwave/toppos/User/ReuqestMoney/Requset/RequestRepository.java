package com.broadwave.toppos.User.ReuqestMoney.Requset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long>, RequestRepositoryCustom {

    @Query("select a from Request a where a.frNo = :frNo and a.frCode = :frCode")
    Optional<Request> request(String frNo, String frCode);

    @Query("select a from Request a where a.frNo = :frNo and a.frConfirmYn = :frConfirmYn and a.frCode = :frCode")
    Optional<Request> findByRequest(String frNo, String frConfirmYn, String frCode);

    @Query("select a from Request a where a.id in :frIdList order by a.id desc")
    List<Request> findByRequestList(List<Long> frIdList);

    @Query("select a from Request a where a.fpId.id in :fpId")
    List<Request> findByRequestPaymentList(Long fpId);

}