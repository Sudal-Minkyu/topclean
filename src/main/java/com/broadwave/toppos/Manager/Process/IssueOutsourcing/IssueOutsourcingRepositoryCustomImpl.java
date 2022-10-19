package com.broadwave.toppos.Manager.Process.IssueOutsourcing;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.Head.Item.Group.A.QItemGroup;
import com.broadwave.toppos.Head.Item.Group.B.QItemGroupS;
import com.broadwave.toppos.Head.Item.Group.C.QItem;
import com.broadwave.toppos.Manager.Process.IssueOutsourcing.IssueOutsourcingDtos.IssueOutsourcingListDto;
import com.broadwave.toppos.Manager.Process.IssueOutsourcing.IssueOutsourcingDtos.IssueOutsourcingSubListDto;
import com.broadwave.toppos.User.Customer.QCustomer;
import com.broadwave.toppos.User.ReuqestMoney.Requset.QRequest;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.QRequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailOutsourcingReceiptListDto;
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
 * Date : 2022-05-23
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class IssueOutsourcingRepositoryCustomImpl extends QuerydslRepositorySupport implements IssueOutsourcingRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public IssueOutsourcingRepositoryCustomImpl() {
        super(IssueOutsourcing.class);
    }

    // 지사 외주입고 NativeQuery
    public List<RequestDetailOutsourcingReceiptListDto> findByRequestDetailOutsourcingReceiptList(String brCode, Long frId, String filterFromDt, String filterToDt){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT  DISTINCT \n");

        sb.append("a.fd_id, d.fr_name, b.fr_code, j.mo_dt, a.fd_tag, a.fd_color, f.bg_name, g.bs_name, e.bi_name, \n");
        sb.append("a.fd_price_grade, a.fd_retry_yn, a.fd_pressed, a.fd_add1_amt, a.fd_add1_remark, a.fd_repair_amt, \n");
        sb.append("a.fd_repair_remark, a.fd_whitening, a.fd_pollution, a.fd_water_repellent, a.fd_starch, a.fd_urgent_yn, \n");
        sb.append("c.bc_name, a.fd_tot_amt, a.fd_state, a.fd_outsourcing_amt, \n");

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
        sb.append("ELSE 0 END fdPollutionBack, \n");

        sb.append("a.fd_promotion_type, a.fd_promotion_discount_rate \n");

        sb.append("FROM mr_issue_outsourcing j \n");

        sb.append("INNER JOIN fs_request_dtl a on j.fd_id=a.fd_id \n");
        sb.append("INNER JOIN fs_request b on a.fr_id=b.fr_id \n");
        sb.append("INNER JOIN bs_customer c on b.bc_id=c.bc_id \n");
        sb.append("INNER JOIN bs_franchise d on b.fr_code=d.fr_code \n");
        sb.append("INNER JOIN bs_item e on a.bi_itemcode=e.bi_itemcode \n");
        sb.append("INNER JOIN bs_item_group f ON e.bg_item_groupcode=f.bg_item_groupcode \n");
        sb.append("INNER JOIN bs_item_group_s g on e.bs_item_groupcode_s=g.bs_item_groupcode_s and e.bg_item_groupcode=g.bg_item_groupcode \n");

        sb.append("WHERE b.fr_confirm_yn='Y' \n");
        sb.append("AND b.br_code= ?1 \n");
        sb.append("AND a.fd_cancel='N' \n");
        sb.append("AND j.mo_dt>= ?2 \n");
        sb.append("AND j.mo_dt<= ?3 \n");
        if(frId != 0){
            sb.append("AND d.fr_id = ?4 \n");
        }

        sb.append("AND a.fd_state='O1' \n");
        sb.append("ORDER BY a.fd_id ASC \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);

        if(frId != 0){
            query.setParameter(4, frId);
        }

        return jpaResultMapper.list(query, RequestDetailOutsourcingReceiptListDto.class);
    }

    // 지사 외주/출고 현황 왼쪽 NativeQuery
    @Override
    public List<IssueOutsourcingListDto> findByIssueOutsourcingList(String brCode, Long franchiseId, String filterFromDt, String filterToDt) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT x.fr_name,x.inoutdt as fdO1Dt ,SUM(x.deliveryCount) as deliveryCount,SUM(x.receiptCount) as receiptCount, \n");
        sb.append("SUM(x.fdTotAmt) as fdTotAmt, SUM(x.fdOutsourcingAmt) as fdOutsourcingAmt \n");
            sb.append("FROM ( \n");
                sb.append("SELECT d.fr_name, a.mo_dt inoutdt, COUNT(*) deliveryCount,0 receiptCount, \n");
                sb.append("SUM(b.fd_tot_amt) as fdTotAmt, SUM(b.fd_outsourcing_amt) as fdOutsourcingAmt \n");
                sb.append("FROM mr_issue_outsourcing a \n");
                sb.append("INNER JOIN fs_request_dtl b ON a.fd_id = b.fd_id \n");
                sb.append("INNER JOIN fs_request c ON c.fr_id = b.fr_id \n");
                sb.append("INNER JOIN bs_franchise d ON d.fr_code = c.fr_code \n");
                sb.append("WHERE a.br_code = ?1 AND a.mo_dt >= ?2 AND a.mo_dt<= ?3 \n");
                if(franchiseId != 0){
                    sb.append("AND d.fr_id = ?4 \n");
                }
                sb.append("GROUP BY d.fr_name,a.mo_dt \n");

                sb.append("UNION all \n");

                sb.append("SELECT d.fr_name, b.fd_o2_dt, 0 deliveryCount, COUNT(*)  receiptCount, \n");
                sb.append("SUM(b.fd_tot_amt) as fdTotAmt, SUM(b.fd_outsourcing_amt) as fdOutsourcingAmt \n");
                sb.append("FROM mr_issue_outsourcing a \n");
                sb.append("INNER JOIN fs_request_dtl b ON a.fd_id = b.fd_id \n");
                sb.append("INNER JOIN fs_request c ON c.fr_id = b.fr_id\n");
                sb.append("INNER JOIN bs_franchise d ON d.fr_code = c.fr_code \n");
                sb.append("WHERE a.br_code = ?1 AND b.fd_o2_dt >= ?2 AND b.fd_o2_dt<= ?3 \n");
                if(franchiseId != 0){
                    sb.append("AND d.fr_id = ?4 \n");
                }
                sb.append("GROUP BY d.fr_name,b.fd_o2_dt \n");
            sb.append(") x\n");
        sb.append("GROUP BY x.fr_name,x.inoutdt \n");
        sb.append("ORDER BY x.fr_name,x.inoutdt \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != 0){
            query.setParameter(4, franchiseId);
        }
        return jpaResultMapper.list(query, IssueOutsourcingListDto.class);
    }

    // 지사 외주/출고 현황 오른쪽 Querydsl
    public List<IssueOutsourcingSubListDto> findByIssueOutsourcingSubList(String brCode, String fdO1Dt) {

        QIssueOutsourcing issueOutsourcing = QIssueOutsourcing.issueOutsourcing;
        QRequestDetail requestDetail = QRequestDetail.requestDetail;
        QRequest request = QRequest.request;
        QItemGroup itemGroup = QItemGroup.itemGroup;
        QItemGroupS itemGroupS = QItemGroupS.itemGroupS;
        QItem item = QItem.item;
        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<IssueOutsourcingSubListDto> query = from(issueOutsourcing)
                .innerJoin(requestDetail).on(requestDetail.id.eq(issueOutsourcing.fdId))
                .innerJoin(requestDetail.frId, request)
                .innerJoin(request.bcId, customer)
                .innerJoin(franchise).on(franchise.frCode.eq(request.frCode))
                .innerJoin(branch).on(branch.brCode.eq(franchise.brCode))
                .innerJoin(item).on(requestDetail.biItemcode.eq(item.biItemcode))
                .innerJoin(itemGroup).on(item.bgItemGroupcode.eq(itemGroup.bgItemGroupcode))
                .innerJoin(itemGroupS).on(item.bsItemGroupcodeS.eq(itemGroupS.bsItemGroupcodeS).and(item.bgItemGroupcode.eq(itemGroupS.bgItemGroupcode.bgItemGroupcode)))

                .where(request.frConfirmYn.eq("Y"))
                .where(requestDetail.fdCancel.eq("N"))

                .select(Projections.constructor(IssueOutsourcingSubListDto.class,

                        branch.brName,
                        franchise.frName,

                        requestDetail.fdO1Dt,
                        requestDetail.fdO2Dt,

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
                        requestDetail.fdOutsourcingAmt,

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

                        requestDetail.fdPromotionType,
                        requestDetail.fdPromotionDiscountRate

                ));

        query.groupBy(issueOutsourcing.fdId).distinct().orderBy(requestDetail.id.asc());
        query.where(request.brCode.eq(brCode));
        query.where(requestDetail.fdO1Dt.eq(fdO1Dt).and(issueOutsourcing.moDt.eq(fdO1Dt)));

        return query.fetch();
    }

}
