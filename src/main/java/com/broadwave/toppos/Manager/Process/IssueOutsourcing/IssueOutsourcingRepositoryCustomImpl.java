package com.broadwave.toppos.Manager.Process.IssueOutsourcing;

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

    // 지사 외주/출고 현황 왼쪽 NativeQuery
    @Override
    public List<IssueOutsourcingListDto> findByIssueOutsourcingList(String brCode, Long franchiseId, String filterFromDt, String filterToDt) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE checkdata AS \n");
            sb.append("( \n");
                sb.append("SELECT d.fr_name, a.mo_dt, \n");
                sb.append("IF(b.fd_o2_dt IS NULL, 'O1', 'O2') type, \n");
                sb.append("SUM(b.fd_tot_amt) as fdTotAmt, SUM(b.fd_outsourcing_amt) as fdOutsourcingAmt \n");
                sb.append(" FROM mr_issue_outsourcing a \n");
                sb.append("INNER JOIN fs_request_dtl b ON a.fd_id = b.fd_id \n");
                sb.append("INNER JOIN fs_request c ON c.fr_id = b.fr_id\n");
                sb.append("INNER JOIN bs_franchise d ON d.fr_code = c.fr_code \n");
                sb.append("WHERE a.br_code = ?1 AND c.fr_confirm_yn='Y' AND b.fd_cancel='N'  AND a.mo_dt >= ?2 AND a.mo_dt<= ?3 \n");
                if(franchiseId != 0){
                    sb.append("AND d.fr_id = ?4 \n");
                }
                sb.append("GROUP BY a.mo_dt \n");
            sb.append(") \n");

        sb.append("SELECT a.fr_name ,a.mo_dt, \n");
        sb.append("(SELECT COUNT(*) from checkdata x1 WHERE a.mo_dt = x1.mo_dt AND x1.type ='O1') AS deliveryCount, \n");
        sb.append("(SELECT COUNT(*) from checkdata x2 WHERE a.mo_dt = x2.mo_dt AND x2.type ='O2') AS receiptCount, \n");
        sb.append("a.fdTotAmt, a.fdOutsourcingAmt \n");
        sb.append("FROM checkdata a \n");
        sb.append("GROUP BY a.fr_name, a.mo_dt ORDER BY a.fr_name, a.mo_dt; \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, brCode);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, filterToDt);
        if(franchiseId != 0){
            query.setParameter(4, franchiseId);
        }
        return jpaResultMapper.list(query, IssueOutsourcingListDto.class);
    }

}
