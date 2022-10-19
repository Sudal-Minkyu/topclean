package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail,Long>, RequestDetailRepositoryCustom {

    @Query("select a from RequestDetail a where a.frNo = :frNo and a.id = :fdId")
    Optional<RequestDetail> findByRequestDetail(String frNo, Long fdId);

    @Query("select a from RequestDetail a where a.frNo = :frNo")
    List<RequestDetail> findByRequestTempDetail(String frNo);

    // 접수세부 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList")
    List<RequestDetail> findByRequestDetailList(List<Long> fdIdList);

    // 수기마감 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S1' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS1List(List<Long> fdIdList);

    // 가맹점입고 할 접수테이블 리스트 호출 or 지사출고 취소 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S4' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS4List(List<Long> fdIdList);

    // 가맹점입고취소 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and (a.fdState = 'S5' or a.fdState = 'S3') and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS5List(List<Long> fdIdList);

    // 지사반송 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'S2' and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS3List(List<Long> fdIdList);

    // 가맹점강제입고 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and (a.fdState = 'S1' or a.fdState = 'S2') and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS7List(List<Long> fdIdList);

    // 세탁인도 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and (a.fdState = 'S5' or a.fdState = 'S8' or a.fdState = 'S3') and a.fdCancel = 'N' order by a.id desc")
    List<RequestDetail> findByRequestDetailS5OrS8OrS3List(List<Long> fdIdList);

    // 지사출고 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and (a.fdState = 'S2' or a.fdState = 'S7' or a.fdState = 'O2') and a.fdCancel = 'N' order by a.id asc")
    List<RequestDetail> findByRequestDetailS2OrS7OrO2List(List<Long> fdIdList);

    // 지사 외주출고 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and (a.fdState = 'S2' or a.fdState = 'O2') and a.fdCancel = 'N' order by a.id asc")
    List<RequestDetail> findByRequestDetailS2OrO2List(List<Long> fdIdList);

    // 지사 외주입고 할 접수테이블 리스트 호출
    @Query("select a from RequestDetail a where a.id in :fdIdList and a.fdState = 'O1' and a.fdCancel = 'N' order by a.id asc")
    List<RequestDetail> findByRequestDetailO1List(List<Long> fdIdList);

}