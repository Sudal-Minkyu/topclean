package com.broadwave.toppos.User.ReuqestMoney.Requset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {
    @Query("select a from Request a where a.frNo = :frNo and a.frConfirmYn = :frConfirmYn and a.frCode = :frCode")
    Optional<Request> findByRequest(String frNo, String frConfirmYn, String frCode);

    @Query("select a from Request a where a.frNo = :frNo and a.frCode = :frCode")
    Optional<Request> findByRequestByFrNoFrCode(String frNo,  String frCode);
}