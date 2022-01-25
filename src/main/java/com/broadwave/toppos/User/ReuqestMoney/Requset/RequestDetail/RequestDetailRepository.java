package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail,Long> {

    @Query("select a from RequestDetail a where a.frNo = :frNo and a.fdTag = :fdTag")
    Optional<RequestDetail> findByRequestDetail(String frNo, String fdTag);

    @Query("select a from RequestDetail a where a.frNo = :frNo")
    List<RequestDetail> findByRequestTempDetail(String frNo);

    // 수기마감 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S1' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS1List(List<Long> fdIdList);

    // 가맹점입고 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S4' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS4List(List<Long> fdIdList);

    // 지사반송 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S3' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS3List(List<Long> fdIdList);

    // 가맹점강제입고 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S7' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS7List(List<Long> fdIdList);

    // 세탁인도 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and (a.fdState = 'S5' or a.fdState = 'S8') and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS5List(List<Long> fdIdList);

}