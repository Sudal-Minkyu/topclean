package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.Head.Branoh.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Manager.Process.Issue.QIssue;
import com.broadwave.toppos.Manager.Process.IssueForce.QIssueForce;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.QInspeot;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.*;
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
import java.util.List;
/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class RequestDetailRepositoryCustomImpl extends QuerydslRepositorySupport implements RequestDetailRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public RequestDetailRepositoryCustomImpl() {
        super(RequestDetailRepository.class);
    }

    public List<RequestDetailDto> findByRequestTempDetailList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        // 접수 세부 리스트
        JPQLQuery<RequestDetailDto> query = from(requestDetail)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .select(Projections.constructor(RequestDetailDto.class,
                        requestDetail.id,
                        requestDetail.biItemcode,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        requestDetail.fdPattern,
                        requestDetail.fdPriceGrade,
                        requestDetail.fdOriginAmt,
                        requestDetail.fdNormalAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdPressed,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdPollutionLevel,
                        requestDetail.fdPollutionLocFcn,
                        requestDetail.fdPollutionLocFcs,
                        requestDetail.fdPollutionLocFcb,
                        requestDetail.fdPollutionLocFlh,
                        requestDetail.fdPollutionLocFrh,
                        requestDetail.fdPollutionLocFlf,
                        requestDetail.fdPollutionLocFrf,
                        requestDetail.fdPollutionLocBcn,
                        requestDetail.fdPollutionLocBcs,
                        requestDetail.fdPollutionLocBcb,
                        requestDetail.fdPollutionLocBrh,
                        requestDetail.fdPollutionLocBlh,
                        requestDetail.fdPollutionLocBrf,
                        requestDetail.fdPollutionLocBlf,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdDiscountGrade,
                        requestDetail.fdDiscountAmt,
                        requestDetail.fdQty,
                        requestDetail.fdRequestAmt,
                        requestDetail.fdRetryYn,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdRemark,
                        requestDetail.fdEstimateDt,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdAgreeType,
                        requestDetail.fdSignImage,

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
        query.where(requestDetail.frNo.eq(frNo));
        return query.fetch();
    }
    public List<RequestDetailAmtDto> findByRequestDetailAmtList(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        JPQLQuery<RequestDetailAmtDto> query = from(requestDetail)
                .select(Projections.constructor(RequestDetailAmtDto.class,
                        requestDetail.fdNormalAmt,
                        requestDetail.fdTotAmt
                ));
        query.where(requestDetail.frNo.eq(frNo));
        return query.fetch();
    }


    // 통합조회 querydsl
    public List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt){
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT \n");
        sb.append("c.bc_name, b.fr_yyyymmdd, b.fr_insert_date, a.fd_id, b.fr_id, \n");
        sb.append("b.fr_no, a.fd_tag, a.bi_itemcode, a.fd_state, a.fd_pre_state, \n");
        sb.append("a.fd_s2_dt, a.fd_s3_dt, a.fd_s4_dt, \n");
        sb.append("CASE WHEN a.fd_s8_dt IS NULL THEN \n");
        sb.append("(CASE WHEN a.fd_s7_dt IS NULL THEN a.fd_s5_dt \n");
        sb.append("ELSE a.fd_s7_dt end) \n");
        sb.append("ELSE a.fd_s8_dt \n");
        sb.append("END as fdS5Dt, \n");
        sb.append("a.fd_s6_dt, a.fd_s6_time, a.fd_s6_cancel_yn, a.fd_s6_cancel_time, a.fd_cancel, a.fd_cacel_dt, a.fd_color, a.fd_pattern, a.fd_price_grade, \n");
        sb.append("a.fd_origin_amt, a.fd_normal_amt, a.fd_add2_amt, a.fd_add2_remark, \n");
        sb.append("a.fd_pollution, a.fd_discount_grade, a.fd_discount_amt, a.fd_qty, a.fd_request_amt, \n");
        sb.append("a.fd_special_yn, a.fd_tot_amt, a.fd_remark, a.fd_estimate_dt, a.fd_retry_yn, a.fd_urgent_yn, a.fd_pressed, \n");
        sb.append("a.fd_add1_amt, a.fd_add1_remark, a.fd_repair_amt, a.fd_repair_remark, a.fd_whitening, a.fd_pollution_level, \n");
        sb.append("a.fd_water_repellent, a.fd_starch, a.fd_pollution_loc_fcn, a.fd_pollution_loc_fcs, a.fd_pollution_loc_fcb, \n");
        sb.append("a.fd_pollution_loc_flh, a.fd_pollution_loc_frh, a.fd_pollution_loc_flf, a.fd_pollution_loc_frf, \n");
        sb.append("a.fd_pollution_loc_bcn, a.fd_pollution_loc_bcs, a.fd_pollution_loc_bcb, a.fd_pollution_loc_brh, a.fd_pollution_loc_blh, \n");
        sb.append("a.fd_pollution_loc_brf, a.fd_pollution_loc_blf, b.fr_ref_type, a.fd_agree_type, a.fd_sign_image, \n");
        sb.append("IFNULL(d.fi_id,null), IFNULL(d.fi_customer_confirm,null), IFNULL(e.fi_id,null), IFNULL(e.fi_customer_confirm,null), IFNULL(f.fp_cancel_yn,'Y'), \n");

        sb.append("CASE \n"); // 반품
        sb.append("WHEN a.fd_pollution_loc_fcn = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_fcs = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_fcb = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_flh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_frh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_flf = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_frf = 'Y' THEN 1 \n");
        sb.append("ELSE 0 END fdPollutionType, \n");
        sb.append("CASE \n"); // 반품
        sb.append("WHEN a.fd_pollution_loc_bcn = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_bcs = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_bcb = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_blh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_brh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_blf = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_brf = 'Y' THEN 1 \n");
        sb.append("ELSE 0 END fdPollutionBack \n");

        sb.append("FROM fs_request_dtl a \n");
        sb.append("INNER JOIN fs_request b ON b.fr_id = a.fr_id \n");
        sb.append("INNER JOIN bs_customer c ON c.bc_id = b.bc_id \n");
        sb.append("LEFT OUTER JOIN fs_request_inspect d ON d.fd_id = a.fd_id AND d.fi_type='F' \n");
        sb.append("LEFT OUTER JOIN fs_request_inspect e ON e.fd_id = a.fd_id AND e.fi_type='B' \n");
        sb.append("LEFT OUTER JOIN fs_request_payment f ON f.fr_id = b.fr_id AND f.fp_cancel_yn='Y' AND f.fp_id = ( SELECT MAX(fp_id) from fs_request_payment WHERE b.fr_id= fr_id AND fp_cancel_yn='Y' ) \n");

        sb.append("WHERE \n");
        sb.append("b.fr_code= ?1 \n");
        sb.append("AND a.fd_cancel='N' \n");
        sb.append("AND b.fr_confirm_yn='Y' \n");
        sb.append("AND b.fr_yyyymmdd  <= ?2 \n");
        sb.append("AND b.fr_yyyymmdd  >= ?3 \n");

        if(!filterCondition.equals("")) {
            if (filterCondition.equals("F")) {
                sb.append("AND d.fi_type= ?4 \n");
            } else if(filterCondition.equals("B")) {
                sb.append("AND e.fi_type= ?4 \n");
            }else {
                sb.append("AND a.fd_state= ?4 \n");
            }
            if(!searchTag.equals("")) {
                sb.append("AND a.fd_tag LIKE ?5 \n");
                if(bcId != null){
                    sb.append("AND c.bc_id= ?6 \n");
                }
            }else{
                if(bcId != null){
                    sb.append("AND c.bc_id= ?5 \n");
                }
            }
        }else{
            if(!searchTag.equals("")){
                sb.append("AND a.fd_tag LIKE ?4 \n");
                if(bcId != null){
                    sb.append("AND c.bc_id= ?5 \n");
                }
            }else{
                if(bcId != null){
                    sb.append("AND c.bc_id= ?4 \n");
                }
            }
        }

        sb.append("ORDER BY a.fd_id DESC; \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, frCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);

        if(!filterCondition.equals("")) {
            query.setParameter(4, filterCondition);
            if(!searchTag.equals("")){
                query.setParameter(5, "%"+searchTag+"%");
                if(bcId != null){
                    query.setParameter(6, bcId);
                }
            }else{
                if(bcId != null){
                    query.setParameter(5, bcId);
                }
            }
        }else{
            if(!searchTag.equals("")){
                query.setParameter(4, "%"+searchTag+"%");
                if(bcId != null){
                    query.setParameter(5, bcId);
                }
            }else{
                if(bcId != null){
                    query.setParameter(4, bcId);
                }
            }
        }

        return jpaResultMapper.list(query, RequestDetailSearchDto.class);

    }


    // 미수관리 페이지의 접수 상세보기 querydsl
    public List<RequestDetailUncollectDto> findByRequestDetailUncollectList(String frCode, Long frId){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailUncollectDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.frId.id.eq(frId)))
                .select(Projections.constructor(RequestDetailUncollectDto.class,
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
                        requestDetail.fdS6Dt,

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
        query.orderBy(requestDetail.id.desc());
        return query.fetch();
    }

    // 수기마감 querydsl
    public List<RequestDetailCloseListDto> findByRequestDetailCloseList(String frCode){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT DISTINCT \n");

        sb.append("a.fd_id, b.fr_yyyymmdd, c.bc_name, a.fd_tag, f.bg_name, g.bs_name, e.bi_name, \n");
        sb.append("a.fd_price_grade, a.fd_retry_yn, a.fd_pressed, a.fd_add1_amt, a.fd_add1_remark, a.fd_repair_amt, \n");
        sb.append("a.fd_repair_remark, a.fd_whitening, a.fd_pollution, a.fd_starch, a.fd_water_repellent, a.fd_urgent_yn, \n");
        sb.append("a.fd_tot_amt, a.fd_remark, a.fd_color, \n");

        sb.append("CASE \n"); // 반품
        sb.append("WHEN a.fd_pollution_loc_fcn = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_fcs = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_fcb = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_flh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_frh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_flf = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_frf = 'Y' THEN 1 \n");
        sb.append("ELSE 0 END fdPollutionType, \n");

        sb.append("CASE \n"); // 반품
        sb.append("WHEN a.fd_pollution_loc_bcn = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_bcs = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_bcb = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_blh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_brh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_blf = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_brf = 'Y' THEN 1 \n");
        sb.append("ELSE 0 END fdPollutionBack \n");

        sb.append("FROM fs_request_dtl a \n");

        sb.append("INNER JOIN fs_request b on a.fr_id=b.fr_id \n");
        sb.append("INNER JOIN bs_customer c on b.bc_id=c.bc_id \n");
        sb.append("INNER JOIN bs_item e on a.bi_itemcode=e.bi_itemcode \n");
        sb.append("INNER JOIN bs_item_group f ON e.bg_item_groupcode=f.bg_item_groupcode \n");
        sb.append("INNER JOIN bs_item_group_s g on e.bs_item_groupcode_s=g.bs_item_groupcode_s and e.bg_item_groupcode=g.bg_item_groupcode \n");
        sb.append("LEFT OUTER JOIN fs_request_inspect h ON a.fd_id =h.fd_id AND h.fi_type ='F' \n");

        sb.append("WHERE b.fr_confirm_yn='Y' \n");
        sb.append("AND b.fr_code= ?1 \n");
        sb.append("AND a.fd_cancel='N' \n");
        sb.append("AND f.bg_item_groupcode != 'D18' \n");
        sb.append("AND (a.fd_state='S1' AND IFNULL(h.fi_customer_confirm,'X') IN ('X','2'))\n"); // 접수상태이면서 확인품이없거나 확인품이 고객수락인경우
        sb.append("ORDER BY a.fd_id ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, frCode);

        return jpaResultMapper.list(query, RequestDetailCloseListDto.class);
    }

    // 모바일 전용 수기마감 querydsl
    public int findByRequestDetailMobileCloseList(String frCode){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE fs_request_dtl a\n");
        sb.append("     INNER JOIN fs_request b ON a.fr_id = b.fr_id \n");
        sb.append("     LEFT OUTER JOIN fs_request_inspect c ON a.fd_id =c.fd_id AND c.fi_type ='F' \n");
        sb.append("     INNER JOIN bs_item d on a.bi_itemcode=d.bi_itemcode \n");
        sb.append("     INNER JOIN bs_item_group e ON d.bg_item_groupcode=e.bg_item_groupcode \n");
        sb.append("         SET a.fd_state = 'S2', a.fd_state_dt= NOW(), \n");
        sb.append("         a.fd_pre_state = 'S1', a.fd_pre_state_dt= NOW(), \n");
        sb.append("         a.fd_s2_dt = DATE_FORMAT(NOW(),'%Y%m%d'), a.fd_s2_time= NOW(), a.fd_s2_type= '02', \n");
        sb.append("         a.fd_fr_state = 'S2', a.fd_fr_state_time= NOW(), \n");
        sb.append("         a.modify_id = 'QRClose', a.modify_date= NOW() \n");
        sb.append("             WHERE b.fr_code = ?1 AND b.fr_confirm_yn ='Y' AND a.fd_cancel ='N' AND e.bg_item_groupcode != 'D18' \n");
        sb.append("             AND (a.fd_state='S1' AND IFNULL(c.fi_customer_confirm,'X') IN ('X','2')); \n"); // # 접수상태이면서 확인품이없거나 확인품이 고객수락인경우

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, frCode);

        return query.executeUpdate();
    }

    // 가맹접입고 querydsl
    public List<RequestDetailFranchiseInListDto> findByRequestDetailFranchiseInList(String frCode){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailFranchiseInListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N").and(requestDetail.fdState.eq("S4"))))
                .select(Projections.constructor(RequestDetailFranchiseInListDto.class,
                        requestDetail.id,
                        requestDetail.fdS4Type,
                        requestDetail.fdS4Dt,
                        customer.bcName,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.biItemcode,
                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,
                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,
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
        query.orderBy(requestDetail.id.asc());
        return query.fetch();
    }

    // 가맹접입고취소 querydsl
    public List<RequestDetailFranchiseInCancelListDto> findByRequestDetailFranchiseInCancelList(String frCode, String fromDt, String toDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailFranchiseInCancelListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .where(requestDetail.fdState.eq("S3").or(requestDetail.fdState.eq("S5")))
                .select(Projections.constructor(RequestDetailFranchiseInCancelListDto.class,
                        requestDetail.id,
                        requestDetail.fdS5Dt,
                        customer.bcName,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.biItemcode,
                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,
                        request.frYyyymmdd,
                        requestDetail.fdS4Dt,
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
        query.orderBy(requestDetail.id.asc());

        if(fromDt != null){
            query.where(requestDetail.fdS5Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS5Dt.loe(toDt));
        }

        return query.fetch();
    }

//    // 지사반송 querydsl
//    public List<RequestDetailReturnListDto> findByRequestDetailReturnList(String frCode){
//        QRequestDetail requestDetail = QRequestDetail.requestDetail;
//        QRequest request = QRequest.request;
//        QItemGroup itemGroup = QItemGroup.itemGroup;
//        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
//        QItem item = QItem.item;
//        QCustomer customer = QCustomer.customer;
//        JPQLQuery<RequestDetailReturnListDto> query = from(requestDetail)
//                .innerJoin(requestDetail.frId, request)
//                .innerJoin(request.bcId, customer)
//                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
//                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
//                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
//                .where(request.frConfirmYn.eq("Y"))
//                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")).and(requestDetail.fdState.eq("S3")))
//                .select(Projections.constructor(RequestDetailReturnListDto.class,
//                        requestDetail.id,
//                        request.frYyyymmdd,
//                        customer.bcName,
//                        requestDetail.fdTag,
//                        itemGroup.bgName,
//                        itemGroupS.bsName,
//                        item.biName,
//                        requestDetail.fdPriceGrade,
//                        requestDetail.fdRetryYn,
//                        requestDetail.fdPressed,
//                        requestDetail.fdAdd1Amt,
//                        requestDetail.fdAdd1Remark,
//                        requestDetail.fdRepairAmt,
//                        requestDetail.fdRepairRemark,
//                        requestDetail.fdWhitening,
//                        requestDetail.fdPollution,
//                        requestDetail.fdWaterRepellent,
//                        requestDetail.fdStarch,
//                        requestDetail.fdUrgentYn,
//                        requestDetail.fdTotAmt,
//                        requestDetail.fdRemark,
//                        requestDetail.fdColor
//                ));
//        query.orderBy(requestDetail.id.asc());
//        return query.fetch();
//    }

    // 가맹접강제입고 querydsl
    public List<RequestDetailForceListDto> findByRequestDetailForceList(Long bcId, String fdTag, String frCode){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailForceListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .where(requestDetail.fdState.eq("S1").or(requestDetail.fdState.eq("S2")))
                .select(Projections.constructor(RequestDetailForceListDto.class,
                        requestDetail.id,
                        requestDetail.fdState,
                        customer.bcName,
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
                        requestDetail.fdRemark,
                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,

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
//        query.groupBy(requestDetail.fdTag).orderBy(requestDetail.id.asc());

        if(bcId != null){
            query.where(customer.bcId.eq(bcId));
        }

        if(!fdTag.equals("")){
            query.where(requestDetail.fdTag.likeIgnoreCase("%"+fdTag+"%"));
        }

        return query.fetch();
    }

    // 세탁인도 querydsl
    public List<RequestDetailDeliveryDto> findByRequestDetailDeliveryList(String frCode, Long bcId){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        QFranchise franchise;
        JPQLQuery<RequestDetailDeliveryDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailDeliveryDto.class,
                        request.frRefType,
                        customer.bcName,
                        requestDetail.id,
                        request.frYyyymmdd,
                        request.fr_insert_date,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdState,
                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,
                        new CaseBuilder()
                                .when(requestDetail.fdS8Dt.isNull().or(requestDetail.fdS8Dt.isEmpty())).then(
                                new CaseBuilder()
                                        .when(requestDetail.fdS7Dt.isNull().or(requestDetail.fdS7Dt.isEmpty())).then(
                                        requestDetail.fdS5Dt
                                )
                                        .otherwise(requestDetail.fdS7Dt)
                        )
                                .otherwise(requestDetail.fdS8Dt),
                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdRemark,
                        requestDetail.fdEstimateDt,
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
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S5").or(requestDetail.fdState.eq("S8").or(requestDetail.fdState.eq("S3"))));
        if(bcId != null){
            query.where(request.bcId.bcId.eq(bcId));
        }
        return query.fetch();
    }

    // 검품이력 조회 및 메세지 querydsl
    public List<RequestDetailInspectDto> findByRequestDetailInspectList(String frCode, Long bcId, String searchTag, String filterFromDt, String filterToDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<RequestDetailInspectDto> query = from(requestDetail)

                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)

                .innerJoin(inspeot).on(inspeot.fdId.eq(requestDetail))

                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .where(request.frYyyymmdd.loe(filterToDt).and(request.frYyyymmdd.goe(filterFromDt)).and(request.frConfirmYn.eq("Y")))
                .where(requestDetail.frId.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailInspectDto.class,
                        request.frRefType,
                        customer.bcName,
                        customer.bcId,
                        requestDetail.id,
                        request.frYyyymmdd,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdState,
                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,
                        new CaseBuilder()
                                .when(requestDetail.fdS8Dt.isNull().or(requestDetail.fdS8Dt.isEmpty())).then(
                                new CaseBuilder()
                                        .when(requestDetail.fdS7Dt.isNull().or(requestDetail.fdS7Dt.isEmpty())).then(
                                        requestDetail.fdS5Dt
                                )
                                        .otherwise(requestDetail.fdS7Dt)
                        )
                                .otherwise(requestDetail.fdS8Dt),
                        requestDetail.fdS6Dt,
                        requestDetail.fdPriceGrade,
                        requestDetail.fdRetryYn,
                        requestDetail.fdPressed,
                        requestDetail.fdAdd1Amt,
                        requestDetail.fdAdd1Remark,
                        requestDetail.fdRepairAmt,
                        requestDetail.fdRepairRemark,
                        requestDetail.fdWhitening,
                        requestDetail.fdPollution,
                        requestDetail.fdStarch,
                        requestDetail.fdWaterRepellent,
                        requestDetail.fdUrgentYn,
                        requestDetail.fdTotAmt,
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
        query.orderBy(requestDetail.id.asc()).groupBy(requestDetail, inspeot.fdId);
        if(!searchTag.equals("")){
            query.where(requestDetail.fdTag.likeIgnoreCase("%"+searchTag+"%"));
        }
        if(bcId != null){
            query.where(request.bcId.bcId.eq(bcId));
        }

        return query.fetch();
    }

    // 영업일보 통계 재세탁, 부착물 sum querydsl - 5
    @Override
    public List<RequestDetailBusinessdayListDto> findByRequestDetailBusinessdayList(String frCode, String filterFromDt, String filterToDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        JPQLQuery<RequestDetailBusinessdayListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)))
                .select(Projections.constructor(RequestDetailBusinessdayListDto.class,
                        request.frYyyymmdd,
                        new CaseBuilder()
                                .when(requestDetail.fdRetryYn.eq("Y")).then(1)
                                .otherwise(0).sum(),
                        new CaseBuilder()
                                .when(requestDetail.biItemcode.substring(0,3).eq("D17")).then(1)
                                .otherwise(0).sum()
                ));
        query.groupBy(request.frYyyymmdd).orderBy(request.frYyyymmdd.asc());
        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));
        return query.fetch();
    }
    // 영업일보 통계 총 출고 sum querydsl - 6
    @Override
    public List<RequestDetailBusinessdayDeliveryDto> findByRequestDetailBusinessdayDeliveryList(String frCode, String filterFromDt, String filterToDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBusinessdayDeliveryDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .where(requestDetail.fdS6Dt.goe(filterFromDt).and(requestDetail.fdS6Dt.loe(filterToDt)).and(requestDetail.fdS6Dt.isNotNull()))
                .select(Projections.constructor(RequestDetailBusinessdayDeliveryDto.class,
                        requestDetail.fdS6Dt,
                        customer.countDistinct()
                ));
        query.groupBy(requestDetail.fdS6Dt).orderBy(requestDetail.fdS6Dt.asc());
        // 기본조건문
        query.where(request.frCode.eq(frCode).and(request.frConfirmYn.eq("Y")));
        return query.fetch();
    }

    // 영수증출력
    public List<RequestDetailPaymentPaper> findByRequestDetailPaymentPaper(String frNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        JPQLQuery<RequestDetailPaymentPaper> query = from(requestDetail)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .select(Projections.constructor(RequestDetailPaymentPaper.class,
                        requestDetail.fdTag,
                        requestDetail.fdColor,
                        itemGroup.bgName,
                        itemGroupS.bsName,
                        item.biName,
                        requestDetail.fdSpecialYn,
                        requestDetail.fdTotAmt,
                        requestDetail.fdEstimateDt
                ));
        query.where(requestDetail.frNo.eq(frNo));
        return query.fetch();
    }

    // 지사출고 querydsl
    public List<RequestDetailReleaseListDto> findByRequestDetailReleaseList(String brCode, Long frId, String fromDt, String toDt, String isUrgent){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT  DISTINCT \n");

        sb.append("a.fd_id, d.fr_name, b.fr_code, a.fd_s2_dt, a.fd_tag, a.fd_color, f.bg_name, g.bs_name, e.bi_name, \n");
        sb.append("a.fd_price_grade, a.fd_retry_yn, a.fd_pressed, a.fd_add1_amt, a.fd_add1_remark, a.fd_repair_amt, \n");
        sb.append("a.fd_repair_remark, a.fd_whitening, a.fd_pollution, a.fd_water_repellent, a.fd_starch, a.fd_urgent_yn, \n");
        sb.append("c.bc_name, a.fd_estimate_dt, a.fd_tot_amt, a.fd_state, \n");

        sb.append("CASE \n");
        sb.append("WHEN h.fi_customer_confirm IS NULL AND fd_s7_type = '01' THEN '02' \n");
        sb.append("WHEN h.fi_customer_confirm IS NULL AND fd_s7_type = '02' THEN '04' \n");
        sb.append("WHEN h.fi_customer_confirm = 1 THEN '99' \n");
        sb.append("WHEN h.fi_customer_confirm = 2 THEN '07' \n");
        sb.append("WHEN h.fi_customer_confirm = 3 THEN '08' \n");
        sb.append("ELSE '01' \n");
        sb.append("END as fdS4Type, \n");

        sb.append("CASE \n"); // 반품
        sb.append("WHEN a.fd_pollution_loc_fcn = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_fcs = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_fcb = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_flh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_frh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_flf = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_frf = 'Y' THEN 1 \n");
        sb.append("ELSE 0 END fdPollutionType, \n");

        sb.append("CASE \n"); // 반품
        sb.append("WHEN a.fd_pollution_loc_bcn = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_bcs = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_bcb = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_blh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_brh = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_blf = 'Y' THEN 1 \n");
        sb.append("WHEN a.fd_pollution_loc_brf = 'Y' THEN 1 \n");
        sb.append("ELSE 0 END fdPollutionBack \n");

        sb.append("FROM fs_request_dtl a \n");
        sb.append("INNER JOIN fs_request b on a.fr_id=b.fr_id \n");
        sb.append("INNER JOIN bs_customer c on b.bc_id=c.bc_id \n");
        sb.append("INNER JOIN bs_franchise d on b.fr_code=d.fr_code \n");
        sb.append("INNER JOIN bs_item e on a.bi_itemcode=e.bi_itemcode \n");
        sb.append("INNER JOIN bs_item_group f ON e.bg_item_groupcode=f.bg_item_groupcode \n");
        sb.append("INNER JOIN bs_item_group_s g on e.bs_item_groupcode_s=g.bs_item_groupcode_s and e.bg_item_groupcode=g.bg_item_groupcode \n");

        sb.append("LEFT OUTER JOIN fs_request_inspect h ON a.fd_id =h.fd_id AND h.fi_type ='B' \n");

        sb.append("WHERE b.fr_confirm_yn='Y' \n");
        sb.append("AND b.br_code= ?1 \n");
        sb.append("AND a.fd_cancel='N' \n");
        sb.append("AND a.fd_s2_dt>= ?2 \n");
        sb.append("AND a.fd_s2_dt<= ?3 \n");
        if(frId != 0){
            sb.append("AND d.fr_id = ?4 \n");
        }
        if(isUrgent.equals("Y")){
            if(frId != 0) {
                sb.append("AND a.fd_urgent_yn = ?5 \n");
            }else{
                sb.append("AND a.fd_urgent_yn = ?4 \n");
            }
        }
        sb.append("AND (a.fd_state='S2' OR a.fd_state='S7') \n");
//        sb.append("(a.fd_state='S2' AND IFNULL(h.fi_customer_confirm,'X') IN ('X','2')) \n");
//        sb.append("OR a.fd_state='S7') \n");
        sb.append("ORDER BY a.fd_id ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, fromDt);
        query.setParameter(3, toDt);

        if(frId != 0){
            query.setParameter(4, frId);
        }
        if(isUrgent.equals("Y")){
            if(frId != 0) {
                query.setParameter(5, isUrgent);
            }else{
                query.setParameter(4, isUrgent);
            }
        }

        return jpaResultMapper.list(query, RequestDetailReleaseListDto.class);

//        QRequestDetail requestDetail = QRequestDetail.requestDetail;
//        QRequest request = QRequest.request;
//        QFranchise franchise = QFranchise.franchise;
//        QItemGroup itemGroup = QItemGroup.itemGroup;
//        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
//        QItem item = QItem.item;
//        QCustomer customer = QCustomer.customer;
//
//        JPQLQuery<RequestDetailReleaseListDto> query = from(requestDetail)
//                .innerJoin(requestDetail.frId, request)
//                .innerJoin(request.bcId, customer)
//                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
//                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
//                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
//                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
//                .where(request.frConfirmYn.eq("Y"))
//                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
//                .select(Projections.constructor(RequestDetailReleaseListDto.class,
//                        requestDetail.id,
//                        franchise.frName,
//                        franchise.frCode,
//                        requestDetail.fdS2Dt,
//                        requestDetail.fdTag,
//                        requestDetail.fdColor,
//                        itemGroup.bgName,
//                        itemGroupS.bsName,
//                        item.biName,
//                        requestDetail.fdPriceGrade,
//                        requestDetail.fdRetryYn,
//                        requestDetail.fdPressed,
//                        requestDetail.fdAdd1Amt,
//                        requestDetail.fdAdd1Remark,
//                        requestDetail.fdRepairAmt,
//                        requestDetail.fdRepairRemark,
//                        requestDetail.fdWhitening,
//                        requestDetail.fdPollution,
//                        requestDetail.fdWaterRepellent,
//                        requestDetail.fdStarch,
//                        requestDetail.fdUrgentYn,
//                        customer.bcName,
//                        requestDetail.fdEstimateDt,
//                        requestDetail.fdTotAmt,
//                        requestDetail.fdState
//                ));
//        query.orderBy(requestDetail.id.asc());
//        query.where(requestDetail.fdState.eq("S2").or(requestDetail.fdState.eq("S7")));
//        if(frId != 0){
//            query.where(franchise.id.eq(frId));
//        }
//        if(isUrgent.equals("Y")){
//            query.where(requestDetail.fdUrgentYn.eq(isUrgent));
//        }
//        if(fromDt != null){
//            query.where(requestDetail.fdS2Dt.goe(fromDt));
//        }
//        if(toDt != null){
//            query.where(requestDetail.fdS2Dt.loe(toDt));
//        }
//        return query.fetch();
    }

    // 지사강제출고 querydsl
    public List<RequestDetailReleaseListDto> findByRequestDetailReleaseForceList(String brCode, Long frId, String fromDt, String toDt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailReleaseListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailReleaseListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        franchise.frCode,
                        requestDetail.fdS2Dt,
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
                        customer.bcName,
                        requestDetail.fdEstimateDt,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState
                ));
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2").or(requestDetail.fdState.eq("S7")));
        if(frId != 0){
            query.where(franchise.id.eq(frId));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Dt.loe(toDt));
        }
        return query.fetch();
    }

    // 지사출고취소 querydsl
    public  List<RequestDetailReleaseCancelListDto> findByRequestDetailReleaseCancelList(String brCode, Long frId, String fromDt, String toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailReleaseCancelListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailReleaseCancelListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.fdS2Time,
                        requestDetail.fdS4Time,
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
                        requestDetail.fdRemark,
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState,
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
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S4"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase("%"+tagNo+"%"));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS4Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS4Dt.loe(toDt));
        }
        return query.fetch();
    }

//    // 지사반송 querydsl
//    public  List<RequestDetailBranchReturnListDto> findByRequestDetailBranchReturnList(String brCode, Long frId, LocalDateTime fromDt, LocalDateTime toDt, String tagNo){
//        QRequestDetail requestDetail = QRequestDetail.requestDetail;
//        QRequest request = QRequest.request;
//        QFranchise franchise = QFranchise.franchise;
//        QItemGroup itemGroup = QItemGroup.itemGroup;
//        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
//        QItem item = QItem.item;
//        QCustomer customer = QCustomer.customer;
//
//        JPQLQuery<RequestDetailBranchReturnListDto> query = from(requestDetail)
//                .innerJoin(requestDetail.frId, request)
//                .innerJoin(request.bcId, customer)
//                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
//                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
//                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
//                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
//                .where(request.frConfirmYn.eq("Y"))
//                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
//                .select(Projections.constructor(RequestDetailBranchReturnListDto.class,
//                        requestDetail.id,
//                        franchise.frName,
//                        requestDetail.insert_date,
//                        requestDetail.fdS2Time,
//                        requestDetail.fdTag,
//                        requestDetail.fdColor,
//                        itemGroup.bgName,
//                        itemGroupS.bsName,
//                        item.biName,
//                        requestDetail.fdPriceGrade,
//                        requestDetail.fdRetryYn,
//                        requestDetail.fdPressed,
//                        requestDetail.fdAdd1Amt,
//                        requestDetail.fdAdd1Remark,
//                        requestDetail.fdRepairAmt,
//                        requestDetail.fdRepairRemark,
//                        requestDetail.fdWhitening,
//                        requestDetail.fdPollution,
//                        requestDetail.fdWaterRepellent,
//                        requestDetail.fdStarch,
//                        requestDetail.fdUrgentYn,
//                        customer.bcName,
//                        requestDetail.fdTotAmt,
//                        requestDetail.fdState,
//                        requestDetail.fdPreState
//                ));
//        query.orderBy(requestDetail.id.asc());
//        query.where(requestDetail.fdState.eq("S2"));
//        query.where(franchise.id.eq(frId));
//        if(!tagNo.equals("")){
//            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase(tagNo));
//        }
//        if(fromDt != null){
//            query.where(requestDetail.fdS2Time.goe(fromDt));
//        }
//        if(toDt != null){
//            query.where(requestDetail.fdS2Time.loe(toDt));
//        }
//        return query.fetch();
//    }

    // 가맹점강제출고 querydsl
    public  List<RequestDetailBranchForceListDto> findByRequestDetailBranchForceList(String brCode, Long frId, String fromDt, String toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailBranchForceListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchForceListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState,

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
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase("%"+tagNo+"%"));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Dt.loe(toDt));
        }
        return query.fetch();
    }

    // 가맹점반품출고 querydsl
    public  List<RequestDetailBranchReturnListDto> findByRequestDetailBranchReturnList(String brCode, Long frId, String fromDt, String toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailBranchReturnListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReturnListDto.class,
                        requestDetail.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState,

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
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase("%"+tagNo+"%"));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Dt.loe(toDt));
        }
        return query.fetch();
    }

    // 확인품등록 querydsl
    public  List<RequestDetailBranchInspectListDto> findByRequestDetailBranchInspectList(String brCode, Long frId, String fromDt, String toDt, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        QInspeot inspeot = QInspeot.inspeot;

        JPQLQuery<RequestDetailBranchInspectListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .leftJoin(inspeot).on(inspeot.fdId.eq(requestDetail).and(inspeot.fiType.eq("B")))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchInspectListDto.class,
                        requestDetail.id,
                        inspeot.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdPreState,
                        inspeot.fiCustomerConfirm,

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
        query.orderBy(requestDetail.id.asc());
        query.where(requestDetail.fdState.eq("S2"));
        query.where(franchise.id.eq(frId));
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase("%"+tagNo+"%"));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Dt.loe(toDt));
        }
        return query.fetch();
    }

    // 확인품현황 querydsl
    public  List<RequestDetailBranchInspectionCurrentListDto> findByRequestDetailBranchInspectionCurrentList(String brCode, Long frId, String fromDt, String toDt, String tagNo){

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        QInspeot inspeot = QInspeot.inspeot;
        JPQLQuery<RequestDetailBranchInspectionCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(inspeot).on(inspeot.fdId.eq(requestDetail))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchInspectionCurrentListDto.class,
                        requestDetail.id,
                        inspeot.id,
                        franchise.frName,
                        requestDetail.insert_date,
                        requestDetail.fdS2Time,
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
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        inspeot.fiAddAmt.sum(),
                        inspeot.fiCustomerConfirm,

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
        query.orderBy(requestDetail.id.asc()).groupBy(requestDetail.id);
        query.where(inspeot.fiType.eq("B"));

        if(frId != 0 || !tagNo.equals("")){
            query.where(franchise.id.eq(frId));
            query.where(requestDetail.fdTag.substring(3,7).likeIgnoreCase("%"+tagNo+"%"));
        }
        if(fromDt != null){
            query.where(requestDetail.fdS2Dt.goe(fromDt));
        }
        if(toDt != null){
            query.where(requestDetail.fdS2Dt.loe(toDt));
        }
        return query.fetch();
    }

    // 택번호조회 querydsl
    public List<RequestDetailTagSearchListDto> findByRequestDetailTagSearchList(String brCode, Long frId, String tagNo){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        JPQLQuery<RequestDetailTagSearchListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailTagSearchListDto.class,
                        franchise.frName,
                        request.frRefType,
                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,
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
                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark,
                        requestDetail.fdS3Dt,
                        requestDetail.fdS7Dt,
                        requestDetail.fdS8Dt,

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
        query.orderBy(requestDetail.id.asc());
        if(frId != 0) {
            query.where(franchise.id.eq(frId));
        }
        if(!tagNo.equals("")){
            query.where(requestDetail.fdTag.likeIgnoreCase("%"+tagNo+"%"));
        }
        return query.fetch();
    }

    // 입고현황, 체류세탁물현황 - 왼쪽 리스트 호출API
    @Override
    public List<RequestDetailBranchStoreCurrentListDto> findByRequestDetailBranchStoreCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.fr_code, c.fr_name, b.fd_s2_dt, \n");
        sb.append("  COUNT(*),  \n");
        sb.append("  sum(case when IFNULL(b.fd_s4_dt,'') = '' THEN 0 ELSE 1 END),  \n");
        sb.append("  (COUNT(*) -sum(case when IFNULL(b.fd_s4_dt,'') = '' THEN 0 ELSE 1 END)) as remain_cnt,  \n");
        sb.append("  sum(b.fd_tot_amt)");
        sb.append("         FROM fs_request a \n");
        sb.append("             INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id  \n");
        sb.append("             INNER JOIN bs_franchise c ON a.fr_code = c.fr_code  \n");
        sb.append("             WHERE a.br_code = ?1 AND a.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
        sb.append("             AND b.fd_s2_dt >= ?2 \n");
        sb.append("             AND b.fd_s2_dt <= ?3  \n");
        if(franchiseId != null){
            sb.append("             AND c.fr_id = ?4  \n");
        }
        sb.append("             GROUP BY a.fr_code, c.fr_name, b.fd_s2_dt  \n");
        if(type.equals("2")){
            sb.append("          HAVING remain_cnt > 0  \n");
        }
        sb.append("             ORDER BY c.fr_name ASC, b.fd_s2_dt ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != null) {
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestDetailBranchStoreCurrentListDto.class);
    }

    // 지사입고현황 - querydsl
    public  List<RequestDetailBranchInputCurrentListDto> findByRequestDetailBranchInputCurrentList(String brCode, String frCode, String fdS2Dt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchInputCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchInputCurrentListDto.class,
                        request.frRefType,
                        requestDetail.fdS2Type,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,

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

                        customer.bcName,
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

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));
        query.where(requestDetail.fdS2Dt.eq(fdS2Dt));

        return query.fetch();
    }

    // 체류세탁물현황 - querydsl
    public  List<RequestDetailBranchRemainCurrentListDto> findByRequestDetailBranchRemainCurrentList(String brCode, String frCode, String fdS2Dt){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchRemainCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchRemainCurrentListDto.class,

                        request.frRefType,
                        requestDetail.fdS2Type,

                        requestDetail.fdS2Dt,
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

                        customer.bcName,
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

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));
        query.where(requestDetail.fdS2Dt.eq(fdS2Dt).and(requestDetail.fdS4Dt.isNull()));

        return query.fetch();
    }

    // 지사출고현황, 지사강제출고현황 - 왼쪽 리스트 호출API
    @Override
    public List<RequestDetailBranchReleaseCurrentListDto> findByRequestDetailBranchReleaseCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        if(type.equals("1")){
            sb.append("SELECT a.fr_code, d.fr_name, a.mi_dt, \n");
            sb.append("  sum(case when IFNULL(b.fd_s4_dt,'') = '' THEN 0 ELSE 1 END) as output_cnt,  \n");
            sb.append("  sum(b.fd_tot_amt)");
            sb.append("         FROM mr_issue a \n");
            sb.append("             INNER JOIN fs_request_dtl b ON a.mi_id = b.mi_id  \n");
            sb.append("             INNER JOIN fs_request c ON c.fr_id = b.fr_id  \n");
            sb.append("             INNER JOIN bs_franchise d ON c.fr_code = d.fr_code  \n");
            sb.append("             WHERE a.br_code = ?1 AND c.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
            sb.append("             AND a.mi_dt >= ?2 \n");
            sb.append("             AND a.mi_dt <= ?3  \n");
            if(franchiseId != null){
                sb.append("             AND d.fr_id = ?4  \n");
            }
            sb.append("             GROUP BY a.fr_code, d.fr_name, a.mi_dt  \n");
            sb.append("             HAVING output_cnt > 0  \n");
            sb.append("             ORDER BY d.fr_name ASC, a.mi_dt ASC \n");
        }else{
            sb.append("SELECT a.fr_code, d.fr_name, a.mr_dt, \n");
            sb.append("  sum(case when IFNULL(b.fd_s7_dt,'') = '' THEN 0 ELSE 1 END) as output_cnt,  \n");
            sb.append("  sum(b.fd_tot_amt)");
            sb.append("         FROM mr_issue_force a \n");
            sb.append("             INNER JOIN fs_request_dtl b ON a.fd_id = b.fd_id  \n");
            sb.append("             INNER JOIN fs_request c ON b.fr_id = c.fr_id  \n");
            sb.append("             INNER JOIN bs_franchise d ON c.fr_code = d.fr_code  \n");
            sb.append("             WHERE a.br_code = ?1 AND c.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
            sb.append("             AND a.mr_dt >= ?2 \n");
            sb.append("             AND a.mr_dt <= ?3  \n");
            if(franchiseId != null){
                sb.append("             AND d.fr_id = ?4  \n");
            }
            sb.append("             GROUP BY a.fr_code, d.fr_name, a.mr_dt  \n");
            sb.append("             HAVING output_cnt > 0  \n");
            sb.append("             ORDER BY d.fr_name ASC, a.mr_dt ASC \n");
        }

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != null) {
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestDetailBranchReleaseCurrentListDto.class);
    }

    //  지사출고현황 - querydsl
    public List<RequestDetailBranchReleaseCurrentRightListDto> findByRequestDetailBranchReleaseCurrentRightList(String brCode, String frCode, String fdS4Dt){
        QIssue issue = QIssue.issue;
        QRequest request = QRequest.request;
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReleaseCurrentRightListDto> query = from(issue)
                .innerJoin(requestDetail).on(issue.eq(requestDetail.miId))
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(issue.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReleaseCurrentRightListDto.class,

                        request.frRefType,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS4Dt,

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

                        customer.bcName,
                        requestDetail.fdTotAmt,
                        requestDetail.fdState,
                        requestDetail.fdRemark,

                        issue.miNo,

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

        query.orderBy(requestDetail.id.asc());
        query.where(issue.frCode.eq(frCode).and(issue.miDt.eq(fdS4Dt)));

        return query.fetch();
    }

    // 지사강제출고현황 - querydsl
    public  List<RequestDetailBranchReleaseForceCurrentRightListDto> findByRequestDetailBranchReleaseForceCurrentRightList(String brCode, String frCode, String fdS7Dt){

        QIssueForce issueForce = QIssueForce.issueForce;

        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReleaseForceCurrentRightListDto> query = from(issueForce)
                .innerJoin(requestDetail).on(issueForce.fdId.eq(requestDetail.id))
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReleaseForceCurrentRightListDto.class,

                        request.frRefType,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS7Dt,

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

                        customer.bcName,
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

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode).and(issueForce.mrDt.eq(fdS7Dt)));

        return query.fetch();
    }

    // 미출고현황 - querydsl
    public List<RequestDetailBranchUnReleaseCurrentListDto> findByRequestDetailBranchUnReleaseList(String brCode, Long franchiseId, String filterFromDt, String filterToDt, String type){
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<RequestDetailBranchUnReleaseCurrentListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(franchise).on(request.frCode.eq(franchise.frCode))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchUnReleaseCurrentListDto.class,
                        request.frCode,
                        franchise.frName,
                        requestDetail.count(),
                        requestDetail.fdTotAmt.sum()
                ));

        query.orderBy(franchise.frName.asc()).groupBy(request.frCode,franchise.frName).having(requestDetail.count().gt(0));

        if(franchiseId != null) {
            query.where(franchise.id.eq(franchiseId));
        }

        if(type.equals("1")) {
            query.where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)));
        }else{
            query.where(requestDetail.fdEstimateDt.goe(filterFromDt).and(requestDetail.fdEstimateDt.loe(filterToDt)));
        }

        query.where(requestDetail.fdState.eq("S2"));

        return query.fetch();
    }

    // 미출고현황 오른쪽리스트 - querydsl
    public List<RequestDetailBranchUnReleaseCurrentRightListDto> findByRequestDetailBranchUnReleaseCurrentRightList(String brCode, String frCode, String filterFromDt, String filterToDt, String type) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchUnReleaseCurrentRightListDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchUnReleaseCurrentRightListDto.class,

                        request.frRefType,

                        request.frYyyymmdd,
                        requestDetail.fdS2Dt,
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

                        customer.bcName,
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

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));

        if(type.equals("1")) {
            query.where(request.frYyyymmdd.goe(filterFromDt).and(request.frYyyymmdd.loe(filterToDt)));
        }else{
            query.where(requestDetail.fdEstimateDt.goe(filterFromDt).and(requestDetail.fdEstimateDt.loe(filterToDt)));
        }

        query.where(requestDetail.fdState.eq("S2"));

        return query.fetch();
    }

    // 가맹반송현황 - querydsl
    public List<RequestDetailBranchReturnCurrentListDto> findByRequestDetailBranchReturnCurrentList(String brCode, Long franchiseId, String filterFromDt, String filterToDt) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.fr_code, c.fr_name, b.fd_s3_dt, \n");
        sb.append("  sum(case when IFNULL(b.fd_s3_dt,'') = '' THEN 0 ELSE 1 END) as tot_amt,  \n");
        sb.append("  sum(b.fd_tot_amt)");
        sb.append("         FROM fs_request a \n");
        sb.append("             INNER JOIN fs_request_dtl b ON a.fr_id = b.fr_id  \n");
        sb.append("             INNER JOIN bs_franchise c ON a.fr_code = c.fr_code  \n");
        sb.append("             WHERE a.br_code = ?1 AND a.fr_confirm_yn ='Y' AND b.fd_cancel ='N'  \n");
        sb.append("             AND b.fd_s3_dt >= ?2 \n");
        sb.append("             AND b.fd_s3_dt <= ?3  \n");
        if(franchiseId != null){
            sb.append("             AND c.fr_id = ?4  \n");
        }
        sb.append("             GROUP BY a.fr_code, c.fr_name, b.fd_s3_dt  \n");
        sb.append("             HAVING tot_amt > 0  \n");
        sb.append("             ORDER BY c.fr_name ASC, b.fd_s3_dt ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != null) {
            query.setParameter(4, franchiseId);
        }

        return jpaResultMapper.list(query, RequestDetailBranchReturnCurrentListDto.class);
    }

    // 가맹반송현황 오른쪽리스트 - querydsl
    public List<RequestDetailBranchReturnCurrentRightListDto> findByRequestDetailBranchReturnRightCurrentList(String brCode, String frCode, String fdS3Dt) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<RequestDetailBranchReturnCurrentRightListDto> query = from(requestDetail)
          .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))
                .where(request.frConfirmYn.eq("Y"))
                .where(request.brCode.eq(brCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailBranchReturnCurrentRightListDto.class,

                        request.frRefType,

                        requestDetail.fdS2Dt,
                        requestDetail.fdS3Dt,

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

                        customer.bcName,
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

        query.orderBy(requestDetail.id.asc());
        query.where(request.frCode.eq(frCode));

        query.where(requestDetail.fdS3Dt.eq(fdS3Dt));

        return query.fetch();
    }

    @Override
    public List<RequestDetailInputMessageDto> findByRequestDetailInputMessage(List<String> frNoList){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE checkdata AS \n");
        sb.append("( \n");
        sb.append("SELECT b.fr_code, a.fr_no, a.fd_state, b.fr_qty, d.bc_id, e.bi_name, f.bg_name \n");
        sb.append("FROM fs_request_dtl a \n");
        sb.append("INNER JOIN fs_request b ON b.fr_id = a.fr_id \n");
        sb.append("INNER JOIN bs_customer d ON d.bc_id = b.bc_id \n");
        sb.append("INNER JOIN bs_item e ON e.bi_itemcode = a.bi_itemcode \n");
        sb.append("INNER JOIN bs_item_group f ON f.bg_item_groupcode = e.bg_item_groupcode \n");
        sb.append("WHERE a.fr_no IN ?1 AND a.fd_cancel = 'N'  AND b.fr_confirm_yn = 'Y' \n");
        sb.append(") \n");
        // 해당 접수건의 상품이 모두 가맹점입고 상태인것만 리스트 호출한다.
        sb.append("SELECT a.fr_code, a.fr_no, a.bc_id, a.fr_qty, a.bg_name, a.bi_name \n");
        sb.append("FROM checkdata a \n");
        sb.append("WHERE (SELECT COUNT(*) FROM checkdata x1 WHERE x1.fr_no = a.fr_no) = \n");
        sb.append("(SELECT COUNT(*) FROM checkdata x1 WHERE x1.fr_no = a.fr_no AND (x1.fd_state = 'S5' OR x1.fd_state = 'S8' OR x1.fd_state = 'S3')) \n");
        sb.append("GROUP BY a.fr_no, a.bc_id \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, frNoList);

        return jpaResultMapper.list(query, RequestDetailInputMessageDto.class);
    }

    public RequestDetailMessageDto findByRequestDetailReceiptMessage(String frNo, String frCode) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;
        JPQLQuery<RequestDetailMessageDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))

                .where(request.frConfirmYn.eq("Y"))
                .where(request.frCode.eq(frCode).and(requestDetail.fdCancel.eq("N")))
                .select(Projections.constructor(RequestDetailMessageDto.class,
                        request.id,
                        customer,

                        franchise.frName,
                        franchise.frTelNo,

                        request.frQty,

                        request.frTotalAmount,
                        request.frPayAmount,

                        itemGroup.bgName,
                        item.biName
                ));

        query.groupBy(requestDetail.frNo);

        query.where(requestDetail.frNo.eq(frNo));

        return query.fetchOne();
    }

    // 확인품등록전 받아올 데이터 호출API
    public RequestDetailBranchInspeotDto findByBranchInspeotDto(Long fdId) {
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItem item = QItem.item;
        QBranch branch = QBranch.branch;
        QFranchise franchise = QFranchise.franchise;

        JPQLQuery<RequestDetailBranchInspeotDto> query = from(requestDetail)
                .innerJoin(requestDetail.frId, request)
                .innerJoin(branch).on(branch.brCode.eq(request.brCode))
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))

                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.fdCancel.eq("N"))
                .select(Projections.constructor(RequestDetailBranchInspeotDto.class,
                        requestDetail,
                        branch.brName,
                        franchise.frTelNo,
                        itemGroup.bgName,
                        item.biName
                ));
        query.where(requestDetail.id.eq(fdId));

        return query.fetchOne();
    }

}
