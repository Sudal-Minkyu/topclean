package com.broadwave.toppos.User.ReuqestMoney.Requset;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Find.QFind;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.QInspeot;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.QRequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestFindListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head.RequestReceiptListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.head.RequestReceiptListSubDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestRealTimeListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestRealTimeListSubDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.manager.RequestWeekAmountDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.user.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class RequestRepositoryCustomImpl extends QuerydslRepositorySupport implements RequestRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public RequestRepositoryCustomImpl() {
        super(Request.class);
    }

    @Override
    public List<RequestListDto> findByRequestTempList(String frCode){
        QRequest request = QRequest.request;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestListDto> query = from(request)
                .innerJoin(request.bcId, customer)
                .select(Projections.constructor(RequestListDto.class,
                        request.frNo,
                        request.fr_insert_date,
                        customer.bcName,
                        customer.bcHp
                ));

        query.orderBy(request.fr_insert_date.desc());
        query.where(request.frConfirmYn.eq("N").and(request.frCode.eq(frCode)));
        return query.fetch();
    }

    @Override
    public List<RequestCollectDto> findByRequestCollectList(Customer customer, String nowDate){
        QRequest request = QRequest.request;

        JPQLQuery<RequestCollectDto> query = from(request)
                .select(Projections.constructor(RequestCollectDto.class,
                        request.frNo,
                        request.frYyyymmdd,
                        request.frUncollectYn,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y"))
                .and(request.bcId.eq(customer)));

//        if(nowDate != null) {
//            query.where(request.frYyyymmdd.lt(nowDate)); //전일미수금만 출력 -> 2021/12/17일 변경 해당조건의 대한 모든 날짜의 데이터를 받아온다.
//        }

        return query.fetch();
    }

    // 고객리스트의 오늘까지 포함미수금 리스트 호출
    @Override
    public List<RequestUnCollectDto> findByUnCollectList(List<Long> customerIdList, String nowDate){
        QRequest request = QRequest.request;

        JPQLQuery<RequestUnCollectDto> query = from(request)
                .groupBy(request.bcId)
                .orderBy(request.bcId.bcId.desc())
                .select(Projections.constructor(RequestUnCollectDto.class,
                        request.bcId.bcId,
                        new CaseBuilder()
                                .when(request.frTotalAmount.isNotNull()).then(request.frTotalAmount.sum())
                                .otherwise(0),
                        new CaseBuilder()
                                .when(request.frPayAmount.isNotNull()).then(request.frPayAmount.sum())
                                .otherwise(0)
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y")
                .and(request.frYyyymmdd.loe(nowDate)
                .and(request.bcId.bcId.in(customerIdList))
        )));

        return query.fetch();
    }

    // 고객리스트의 전일미수금 리스트 호출
    @Override
    public List<RequestUnCollectDto> findByBeforeUnCollectList(List<Long> customerIdList, String nowDate){
        QRequest request = QRequest.request;

        JPQLQuery<RequestUnCollectDto> query = from(request)
                .groupBy(request.bcId)
                .orderBy(request.bcId.bcId.desc())
                .select(Projections.constructor(RequestUnCollectDto.class,
                        request.bcId.bcId,
                        new CaseBuilder()
                                .when(request.frTotalAmount.isNotNull()).then(request.frTotalAmount.sum())
                                .otherwise(0),
                        new CaseBuilder()
                                .when(request.frPayAmount.isNotNull()).then(request.frPayAmount.sum())
                                .otherwise(0)
                ));

        query.where(request.frUncollectYn.eq("Y")
                        .and(request.frConfirmYn.eq("Y")
                        .and(request.frYyyymmdd.lt(nowDate)
                        .and(request.bcId.bcId.in(customerIdList))
                )));

        return query.fetch();
    }

    // 미수금 완납처리시 사용하는 쿼리
    @Override
    public List<RequestInfoDto> findByRequestList(String frCode, String nowDate, Customer customer){
        QRequest request = QRequest.request;
        QCustomer qcustomer = QCustomer.customer;

        JPQLQuery<RequestInfoDto> query = from(request)
                .innerJoin(request.bcId, qcustomer)
                .select(Projections.constructor(RequestInfoDto.class,
                        request.id,
                        request.frNo,
                        qcustomer,
                        request.frCode,
                        request.brCode,
                        request.frYyyymmdd,
                        request.frQty,
                        request.frNormalAmount,
                        request.frDiscountAmount,
                        request.frTotalAmount,
                        request.frPayAmount,
                        request.frUncollectYn,
                        request.frConfirmYn,
                        request.frRefType,
                        request.frRefBoxCode,
                        request.fr_insert_id,
                        request.fr_insert_date,
                        request.modify_id,
                        request.modify_date
                ));

        query.where(request.frUncollectYn.eq("Y")
                        .and(request.frConfirmYn.eq("Y")
                        .and(request.frYyyymmdd.lt(nowDate)
                        .and(request.frCode.eq(frCode)
                        .and(request.bcId.eq(customer)
                        )))));

        return query.fetch();
    }

    @Override
    public List<RequestSearchDto> findByRequestFrCode(String frCode){
        QRequest request = QRequest.request;

        JPQLQuery<RequestSearchDto> query = from(request)
                .select(Projections.constructor(RequestSearchDto.class,
                        request.frNo
                ));

        query.where(request.frCode.eq(frCode));
        query.limit(1);

        return query.fetch();
    }

    // 미수관리 페이지의 고객선택 후 리스트 Dto
    @Override
    public  List<RequestCustomerUnCollectDto> findByRequestCustomerUnCollectList(Long bcId, String frCode){
        QRequest request = QRequest.request;

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<RequestCustomerUnCollectDto> query = from(request)
                .innerJoin(requestDetail).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .groupBy(request).orderBy(request.id.desc())
                .select(Projections.constructor(RequestCustomerUnCollectDto.class,
                        request.id,
                        request.frYyyymmdd,
                        request.count(),
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y")
                        .and(request.bcId.bcId.eq(bcId)
                                .and(request.frCode.eq(frCode))
                        )));

        return query.fetch();
    }

    // 미수관리 페이지의 미수금 결제할 접수테이블 선택후 리스트 Dto
    @Override
    public List<RequestCustomerUnCollectDto> findByRequestUnCollectPayList(List<Long> frIdList, String frCode){
        QRequest request = QRequest.request;

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;

        JPQLQuery<RequestCustomerUnCollectDto> query = from(request)
                .innerJoin(requestDetail).on(requestDetail.frId.eq(request))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .groupBy(request).orderBy(request.id.desc())
                .select(Projections.constructor(RequestCustomerUnCollectDto.class,
                        request.id,
                        request.frYyyymmdd,
                        request.count(),
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        request.frTotalAmount,
                        request.frPayAmount
                ));

        query.where(request.frUncollectYn.eq("Y")
                .and(request.frConfirmYn.eq("Y")
                        .and(request.id.in(frIdList)
                                .and(request.frCode.eq(frCode))
                        )));

        return query.fetch();
    }

    // 영업일보 통계 리스트 querydsl - 1
    @Override
    public List<RequestBusinessdayListDto> findByBusinessDayList(String frCode, String filterFromDt, String filterToDt){
        QRequest request = QRequest.request;

        JPQLQuery<RequestBusinessdayListDto> query = from(request)
                .where(request.frYyyymmdd.loe(filterToDt).and(request.frYyyymmdd.goe(filterFromDt)))
                .select(Projections.constructor(RequestBusinessdayListDto.class,
                        request.frYyyymmdd,
                        request.frQty.sum(),
                        request.frTotalAmount.sum()

                ));

        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());

        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));

        return query.fetch();
    }

    // 영업일보 통계 리스트 querydsl - 2
    @Override
    public List<RequestBusinessdayCustomerListDto> findByBusinessDayCustomerList(String frCode, String filterFromDt, String filterToDt){
        QRequest request = QRequest.request;

        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestBusinessdayCustomerListDto> query = from(request)
                .innerJoin(customer).on(request.bcId.eq(customer))
                .where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)))
                .select(Projections.constructor(RequestBusinessdayCustomerListDto.class,
                        request.frYyyymmdd,
                        customer.countDistinct()
                ));

        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());

        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));

        return query.fetch();
    }

    // 영수증 출력 가맹점정보 데이터 호출
    @Override
    public RequestPaymentPaperDto findByRequestPaymentPaper(String frNo, Long frId, String frCode) {

        QRequest request = QRequest.request;
        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<RequestPaymentPaperDto> query =
                from(request)
                        .innerJoin(customer).on(customer.eq(request.bcId))
                        .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                        .select(Projections.constructor(RequestPaymentPaperDto.class,
                                request.frNo,

                                franchise.frCode,
                                franchise.frName,
                                franchise.frBusinessNo,
                                franchise.frRpreName,
                                franchise.frTelNo,
                                franchise.frTagType,

                                customer,

                                request.frYyyymmdd,
                                request.frNormalAmount,
                                request.frDiscountAmount,
                                request.frTotalAmount,
                                request.frPayAmount
                        ));

        if(frCode != null){
            query.where(request.frCode.eq(frCode));
        }

        if(!frNo.equals("")){
            query.where(request.frNo.eq(frNo));
        }else{
            query.where(request.id.eq(frId));
        }

        return query.fetchOne();
    }

    // 가맹점 메인페이지 History 리스트 호출 함수
    @Override
    public List<RequestHistoryListDto> findByRequestHistory(String frCode, String nowDate) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.typename, a.ctime, a.bc_name, a.bc_hp \n");
        sb.append("     FROM ( \n");

        sb.append("SELECT DISTINCT '접수' AS typename, a.fr_insert_date ctime, b.bc_name, b.bc_hp \n");
        sb.append("FROM fs_request a \n");
        sb.append("INNER JOIN bs_customer b ON a.bc_id = b.bc_id \n");
        sb.append("WHERE a.fr_code=?1 AND  a.fr_confirm_yn='Y' AND a.fr_yyyymmdd=?2 \n");

        sb.append("UNION ALL \n");

        sb.append("SELECT DISTINCT '인도' AS typename, c.fd_s6_time ctime, b.bc_name, b.bc_hp \n");
        sb.append("FROM fs_request a \n");
        sb.append("INNER JOIN bs_customer b ON a.bc_id = b.bc_id \n");
        sb.append("INNER JOIN fs_request_dtl c ON a.fr_id = c.fr_id \n");
        sb.append("WHERE a.fr_code=?1 AND  a.fr_confirm_yn='Y' AND  c.fd_cancel='N' AND c.fd_s6_dt=?2 \n");

        sb.append(") a  \n");
        sb.append("ORDER BY a.ctime DESC limit 6 \n");


        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, frCode);
        query.setParameter(2, nowDate);

        return jpaResultMapper.list(query, RequestHistoryListDto.class);

    }

    // 임시저장한 내역이 존재하는지/
    @Override
    public RequestTempDto findByRequestTemp(String frCode) {

        QRequest request = QRequest.request;

        JPQLQuery<RequestTempDto> query =
                from(request)
                        .where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("N")))
                        .groupBy(request.frNo).limit(1)
                        .select(Projections.constructor(RequestTempDto.class,
                                request.frNo
                        ));

        return query.fetchOne();
    }

    // 1주일간의 가맹점 접수금액 그래프용
    @Override
    public List<RequestWeekAmountDto> findByRequestWeekAmount(String brCode){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE chart1 AS ( \n");
        sb.append("SELECT c.fr_name,sum(b.fd_tot_amt) amount \n");
        sb.append("FROM fs_request a \n");
        sb.append("INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id \n");
        sb.append("INNER JOIN bs_franchise c ON a.fr_code = c.fr_code \n");
        sb.append("WHERE a.br_code = ?1 \n");
        sb.append("AND a.fr_confirm_yn ='Y' AND b.fd_cancel ='N' \n");
        sb.append("AND a.fr_yyyymmdd > DATE_FORMAT(DATE_SUB(now(),INTERVAL 7 DAY  ),'%Y%m%d') \n"); // 7일전 조건문
        sb.append("GROUP BY c.fr_name \n");
        sb.append("UNION ALL \n");
        sb.append("SELECT fr_name, 0 amount \n");
        sb.append("FROM bs_franchise \n");
        sb.append("WHERE br_code = ?1 ) \n");

        sb.append("SELECT fr_name,SUM(amount) amount \n");
        sb.append("FROM chart1 \n");
        sb.append("GROUP BY fr_name \n");
        sb.append("UNION ALL \n");
        sb.append("SELECT '합계',SUM(amount) amount  \n");
        sb.append("FROM chart1 \n");
        sb.append("ORDER BY FIELD(fr_name,'합계') DESC, fr_name ASC; \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);

        return jpaResultMapper.list(query, RequestWeekAmountDto.class);
    }

    // 1주일간의 일자별 가맹점 접수금액
    @Override
    public List<RequestWeekAmountDto> findByRequestWeekDaysAmount(String brCode){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE chart2 AS ( \n");
        sb.append("SELECT DATE_FORMAT(a.fr_yyyymmdd,'%m.%d') yyyymmdd ,sum(b.fd_tot_amt) amount \n");
        sb.append("FROM fs_request a \n");

        sb.append("INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id \n");
        sb.append("INNER JOIN bs_franchise c ON a.fr_code = c.fr_code \n");

        sb.append("WHERE a.br_code = ?1 \n");
        sb.append("AND a.fr_confirm_yn ='Y' AND b.fd_cancel ='N' \n");
        sb.append("AND a.fr_yyyymmdd > DATE_FORMAT(DATE_SUB(now(),INTERVAL 7 DAY  ),'%Y%m%d') \n"); // 7일전 조건문
        sb.append("GROUP BY DATE_FORMAT(a.fr_yyyymmdd,'%m.%d') \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 6 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 5 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 4 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 3 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 2 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 1 DAY  ),'%m.%d'), 0 amount \n");
        sb.append("UNION ALL SELECT DATE_FORMAT(DATE_SUB(now(),INTERVAL 0 DAY  ),'%m.%d'), 0 amount \n");
        sb.append(") \n");
        sb.append("SELECT yyyymmdd,SUM(amount) amount \n");
        sb.append("FROM chart2 \n");
        sb.append("GROUP BY yyyymmdd \n");
        sb.append("UNION ALL \n");
        sb.append("SELECT '합계',SUM(amount) amount \n");
        sb.append("FROM chart2 \n");
        sb.append("ORDER BY FIELD(yyyymmdd,'합계') DESC, yyyymmdd ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);

        return jpaResultMapper.list(query, RequestWeekAmountDto.class);
    }

    // 카카오 메세지 테이블 Native쿼리 -> messageType : "K" 카카오, "L" LMS(유료문자), "S"(90문자), "B" -> nextmessage+LMS
    @Override
    public boolean kakaoMessage(String message, String nextmessage, String buttonJson, String templatecode, String tableName, Long tableId, String TelNumber, String templatecodeNumber, String messageType) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        try{
            sb.append("insert into SMSQ_SEND \n");
            sb.append("(dest_no, call_back, msg_contents, sendreq_time, msg_instm, msg_type, \n");
            sb.append("k_next_type, k_next_contents, k_resyes, k_template_code, k_button_json, app_etc1, app_etc2) \n");

            sb.append("VALUES( ?1, ?2, ?3, NOW(), NOW(), ?4, \n");
            sb.append("'B', ?5, 'Y', ?6, ?7, \n");
            sb.append("?8, CONCAT(?9) ); \n");

            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, TelNumber);
            query.setParameter(2, templatecodeNumber);
            query.setParameter(3, message);
            query.setParameter(4, messageType);
            query.setParameter(5, nextmessage);
            query.setParameter(6, templatecode);
            query.setParameter(7, buttonJson);
            query.setParameter(8, tableName);
            query.setParameter(9, tableId);

            query.executeUpdate();

            return true;

        }catch (Exception e){
            return false;
        }
    }

    // 문자 메세지 테이블 Native쿼리 -> messageType : "L" LMS(유료문자), "S"(90문자이하)
    @Override
    public boolean smsMessage(String message, String TelNumber, String tableName, Long tableId, String templatecodeNumber, String messageType, LocalDateTime sendreqTime) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();
//        System.out.println("");
//        System.out.println("TelNumber : "+TelNumber);
//        System.out.println("templatecodeNumber : "+templatecodeNumber);
//        System.out.println("message : "+message);
//        System.out.println("messageType : "+messageType);
//        System.out.println("tableName : "+tableName);
//        System.out.println("tableId : "+tableId);
//        System.out.println("sendreqTime : "+sendreqTime);
//        System.out.println("");
        try{
            sb.append("insert into SMSQ_SEND \n");
            sb.append("(dest_no, call_back, msg_contents, msg_type, app_etc1, app_etc2, msg_instm, sendreq_time) \n");
            sb.append("VALUES( ?1, ?2, ?3, ?4, ?5, CONCAT(?6), NOW(), \n");

            if(sendreqTime != null){
                sb.append("?7); \n");
            }else{
                sb.append("NOW()); \n");
            }

            Query query = em.createNativeQuery(sb.toString());
            query.setParameter(1, TelNumber);
            query.setParameter(2, templatecodeNumber);
            query.setParameter(3, message);
            query.setParameter(4, messageType);
            query.setParameter(5, tableName);
            query.setParameter(6, tableId);
            if(sendreqTime != null){
                query.setParameter(7, sendreqTime);
            }

            query.executeUpdate();

            return true;

        }catch (Exception e){
            System.out.println("e : "+e);
            return false;
        }
    }


    // 마스터테이블의 fpId가 존재할시 해당 결제 마스터테이블의 임이의 택번호 하나를 반환하는 쿼리
    public RequestFdTagDto findByRequestDetailFdTag(String frCode, Long frId) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT IFNULL(d.fd_tag,'') FROM fs_request a \n");
        sb.append("LEFT OUTER JOIN fs_request_payment b on a.fp_id = b.fp_id \n");
        sb.append("LEFT OUTER JOIN fs_request c on c.fr_id = b.fr_id \n");
        sb.append("LEFT OUTER JOIN fs_request_dtl d on d.fr_id = c.fr_id \n");
        sb.append("WHERE a.fr_id = ?1 AND a.fr_code = ?2 LIMIT 1; \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, frId);
        query.setParameter(2, frCode);

        return jpaResultMapper.uniqueResult(query, RequestFdTagDto.class);
    }

    // 실시간접수현황 왼쪽 NativeQuery
    @Override
    public List<RequestRealTimeListDto> findByRequestRealTimeList(Long franchiseId, String brCode, String filterFromDt, String filterToDt) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT \n");
        sb.append("c.fr_name, a.fr_yyyymmdd, COUNT(DISTINCT a.bc_id), COUNT(*), SUM(b.fd_tot_amt) \n");
        sb.append("FROM  fs_request a \n");
        sb.append("INNER JOIN fs_request_dtl b on b.fr_id=a.fr_id \n");
        sb.append("INNER JOIN bs_franchise c on c.fr_code=a.fr_code \n");
        sb.append("WHERE \n");
        sb.append("a.br_code= ?1 \n");
        sb.append("AND a.fr_confirm_yn='Y' \n");
        sb.append("AND b.fd_cancel='N' \n");
        sb.append("AND a.fr_yyyymmdd>= ?2 \n");
        sb.append("AND a.fr_yyyymmdd<= ?3 \n");
        if(franchiseId != 0){
            sb.append("AND c.fr_id = ?4 \n");
        }
        sb.append("GROUP BY c.fr_name, a.fr_yyyymmdd ORDER BY c.fr_name,a.fr_yyyymmdd ASC; \n");


        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != 0){
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestRealTimeListDto.class);

    }

    //  실시간접수현황 오른쪽 - querydsl
    public List<RequestRealTimeListSubDto> findByRequestRealTimeSubList(String frYyyymmdd, String brCode) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<RequestRealTimeListSubDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestRealTimeListSubDto.class,

                        franchise.frName,
                        customer.bcName,
                        request.frYyyymmdd,

                        requestDetail.fdTag,
                        requestDetail.fdColor,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark,

                        new CaseBuilder()
                                .when(requestDetail.fdPollutionLocFcn.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFcs.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFcb.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFlh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFrh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFlf.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFrf.eq("Y")).then(1)
                                .otherwise(0),
                        new CaseBuilder()
                                .when(requestDetail.fdPollutionLocBcn.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBcs.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBcb.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBlh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBrh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBlf.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBrf.eq("Y")).then(1)
                                .otherwise(0)

                ));

        query.orderBy(requestDetail.id.asc()).distinct();
        query.where(request.frYyyymmdd.eq(frYyyymmdd));

        return query.fetch();
    }

    // 물건찾기 등록 리스트 Dto
    @Override
    public List<RequestFindListDto> findByRequestFindList(Long bcId, String frCode, String filterFromDt, String filterToDt, String searchTag, String ffStateType){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QCustomer customer = QCustomer.customer;
        QItem item = QItem.item;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QFind find = QFind.find;

        JPQLQuery<RequestFindListDto> query = from(requestDetail)
                .innerJoin(request).on(requestDetail.frId.eq(request))
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(customer).on(customer.eq(request.bcId))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .leftJoin(find).on(find.fdId.eq(requestDetail))
                .orderBy(requestDetail.id.desc())
                .select(Projections.constructor(RequestFindListDto.class,

                        requestDetail.id,
                        customer.bcName,

                        request.frYyyymmdd,
                        requestDetail.fdEstimateDt,

                        requestDetail.fdTag,
                        requestDetail.fdColor,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdTotAmt,
                        requestDetail.fdState,

                        requestDetail.fdRemark,

                        requestDetail.fdS2Dt,

                        new CaseBuilder()
                                .when(find.ffState.isNull()).then("미요청")
                                .otherwise(find.ffState),

                        new CaseBuilder()
                                .when(requestDetail.fdPollutionLocFcn.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFcs.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFcb.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFlh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFrh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFlf.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFrf.eq("Y")).then(1)
                                .otherwise(0),
                        new CaseBuilder()
                                .when(requestDetail.fdPollutionLocBcn.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBcs.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBcb.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBlh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBrh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBlf.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBrf.eq("Y")).then(1)
                                .otherwise(0)
                ));

        if(ffStateType.equals("02")){
            query.where(find.ffState.isNotNull());
        }

        if(searchTag != null && !searchTag.isEmpty() ){
            query.where(requestDetail.fdTag.likeIgnoreCase("%"+searchTag+"%"));
        }
        if(bcId != 0){
            query.where(customer.bcId.eq(bcId));
        }
        query.where(request.frCode.eq(frCode).and(requestDetail.fdState.notEqualsIgnoreCase("S6")));
        
        //출고예정일이 오늘일자보다 작은 조건 추가
        query.where(requestDetail.fdEstimateDt.lt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));

        query.where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)));

        return query.fetch();
    }

    // 본사 접수현황 왼쪽 NativeQuery
    @Override
    public List<RequestReceiptListDto> findByHeadReceiptList(Long branchId, Long franchiseId, String filterFromDt, String filterToDt) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT \n");
        sb.append("d.br_id, d.br_name, c.fr_id, c.fr_name, a.fr_yyyymmdd, COUNT(*), SUM(b.fd_tot_amt) \n");
        sb.append("FROM  fs_request a \n");
        sb.append("INNER JOIN fs_request_dtl b on b.fr_id=a.fr_id \n");
        sb.append("INNER JOIN bs_franchise c on c.fr_code=a.fr_code \n");
        sb.append("INNER JOIN bs_branch d on d.br_code = c.br_code \n");
        sb.append("WHERE \n");
        sb.append("a.fr_confirm_yn='Y' \n");
        sb.append("AND b.fd_cancel='N' \n");
        sb.append("AND a.fr_yyyymmdd>= ?1 \n");
        sb.append("AND a.fr_yyyymmdd<= ?2 \n");
        if(branchId != 0){
            sb.append("AND d.br_id = ?3 \n");
            if(franchiseId != 0){
                sb.append("AND c.fr_id = ?4 \n");
                sb.append("GROUP BY d.br_name, c.fr_name, a.fr_yyyymmdd ORDER BY d.br_name, c.fr_name, a.fr_yyyymmdd ASC; \n");
            }else{
                sb.append("GROUP BY d.br_name, a.fr_yyyymmdd ORDER BY d.br_name, a.fr_yyyymmdd ASC; \n");
            }
        }else{
            sb.append("GROUP BY a.fr_yyyymmdd ORDER BY a.fr_yyyymmdd ASC; \n");
        }

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterFromDt);
        query.setParameter(2, filterToDt);
        if(branchId != 0){
            query.setParameter(3, branchId);
            if(franchiseId != 0){
                query.setParameter(4, franchiseId);
            }
        }

        return jpaResultMapper.list(query, RequestReceiptListDto.class);
    }

    //  본사 접수현황 오른쪽 - querydsl
    public List<RequestReceiptListSubDto> findByHeadReceiptSubList(Long branchId, Long franchiseId, String frYyyymmdd) {

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        QInspeot inspeot1 = new QInspeot("inspeot1");
        QInspeot inspeot2 = new QInspeot("inspeot2");

        JPQLQuery<RequestReceiptListSubDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(branch).on(branch.brCode.eq(franchise.brCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                // 검품여부 leftJoin
                .leftJoin(inspeot1).on(inspeot1.fdId.eq(requestDetail).and(inspeot1.fiType.eq("F").and(inspeot1.fiCustomerConfirm.eq("3")))) // 가맹점
                .leftJoin(inspeot2).on(inspeot2.fdId.eq(requestDetail).and(inspeot2.fiType.eq("B").and(inspeot2.fiCustomerConfirm.eq("3")))) // 지사

                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.fdCancel.eq("N"))

                .select(Projections.constructor(RequestReceiptListSubDto.class,

                        branch.brName,
                        franchise.frName,

                        customer.bcName,
                        customer.bcHp,
                        customer.bcGrade,

                        requestDetail.fdTag,
                        request.fr_insert_date,
                        requestDetail.fdEstimateDt,

                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,

                        requestDetail.fdQty,
                        requestDetail.fdColor,
                        requestDetail.fdPattern,

                        requestDetail.fdUrgentType,
                        requestDetail.fdUrgentYn,

                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdStarch,

                        requestDetail.fdAdd2Amt,
                        requestDetail.fdUrgentAmt,
                        requestDetail.fdNormalAmt,

                        requestDetail.fdTotAmt,
                        requestDetail.fdDiscountAmt,
                        requestDetail.fdState,

                        new CaseBuilder()
                                .when(requestDetail.fdPollutionLocFcn.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFcs.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFcb.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFlh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFrh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFlf.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocFrf.eq("Y")).then(1)
                                .otherwise(0),
                        new CaseBuilder()
                                .when(requestDetail.fdPollutionLocBcn.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBcs.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBcb.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBlh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBrh.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBlf.eq("Y")).then(1)
                                .when(requestDetail.fdPollutionLocBrf.eq("Y")).then(1)
                                .otherwise(0),

                        requestDetail.fdS2Dt,
                        requestDetail.fdS5Dt,
                        requestDetail.fdS4Dt,
                        requestDetail.fdS3Dt,
                        requestDetail.fdS6Dt,
                        requestDetail.fdS6Time,

                        new CaseBuilder()
                                .when(inspeot1.isNotNull().and(inspeot2.isNotNull())).then(inspeot1.fiProgressStateDt)
                                .otherwise(
                                        new CaseBuilder().when(inspeot1.isNotNull()).then(inspeot1.fiProgressStateDt)
                                                .otherwise(inspeot2.fiProgressStateDt))

                ));

        query.orderBy(requestDetail.id.asc()).distinct();

        if(branchId != 0){
            query.where(branch.id.eq(branchId));
        }
        if(franchiseId != 0){
            query.where(franchise.id.eq(franchiseId));
        }

        query.where(request.frYyyymmdd.eq(frYyyymmdd));

        return query.fetch();
    }


}
