package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory;

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
 * Date : 2022-04-18
 * Time :
 * Remark : Toppos 가맹점 메세지 송신이력 RepositoryCustomImpl
 */
@Slf4j
@Repository
public class MessageHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements MessageHistoryRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public MessageHistoryRepositoryCustomImpl() {
        super(MessageHistory.class);
    }


    @Override
    public List<MessageHistoryListDto> findByMessageHistoryList(String frCode, String filterFromDt, String filterToDt){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("WITH RECURSIVE messageHistory AS \n");
        sb.append("( \n");
        sb.append("SELECT a.insert_yyyymmdd, a.fm_type, COUNT(*) cnt FROM fs_message_history a \n");
        sb.append("WHERE a.insert_yyyymmdd <= ?1 AND a.insert_yyyymmdd >= ?2 AND a.fr_code = ?3 \n");
        sb.append("AND a.fm_type IN( '01','02','04') \n");
        sb.append("GROUP BY a.insert_yyyymmdd,a.fm_type \n");

        sb.append("UNION ALL \n");

        sb.append("SELECT b.insert_yyyymmdd, 'xx' as fm_type, COUNT(*) cnt FROM fs_request_input_message b \n");
        sb.append("WHERE b.insert_yyyymmdd <= ?1 AND b.insert_yyyymmdd >= ?2 AND b.fr_code = ?3 \n");
        sb.append("GROUP BY b.insert_yyyymmdd) \n");

        sb.append("SELECT DISTINCT a.insert_yyyymmdd, \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x1 WHERE a.insert_yyyymmdd = x1.insert_yyyymmdd AND x1.fm_type = '01'),0) + \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x2 WHERE a.insert_yyyymmdd = x2.insert_yyyymmdd AND x2.fm_type = '02'),0) + \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x3 WHERE a.insert_yyyymmdd = x3.insert_yyyymmdd AND x3.fm_type = '04'),0) + \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x4 WHERE a.insert_yyyymmdd = x4.insert_yyyymmdd AND x4.fm_type = 'xx'),0) \n");
        sb.append("AS total, \n");

        sb.append("IFNULL((SELECT cnt from messageHistory x1 WHERE a.insert_yyyymmdd = x1.insert_yyyymmdd AND x1.fm_type = '01'),0) AS fmType01_cnt, \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x2 WHERE a.insert_yyyymmdd = x2.insert_yyyymmdd AND x2.fm_type = '02'),0) AS fmType02_cnt, \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x3 WHERE a.insert_yyyymmdd = x3.insert_yyyymmdd AND x3.fm_type = '04'),0) AS fmType04_cnt, \n");
        sb.append("IFNULL((SELECT cnt from messageHistory x4 WHERE a.insert_yyyymmdd = x4.insert_yyyymmdd AND x4.fm_type = 'xx'),0) AS fmTypeXX_cnt \n");
        sb.append("FROM messageHistory a; \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, filterToDt);
        query.setParameter(2, filterFromDt);
        query.setParameter(3, frCode);

        return jpaResultMapper.list(query, MessageHistoryListDto.class);

    }

    @Override
    public List<MessageHistorySubListDto> findByMessageHistorySubList(String frCode, String insertYyyymmdd){

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT a.fm_type, a.insert_date, b.bc_name, b.bc_hp, a.fm_message \n");
        sb.append("FROM fs_message_history a \n");
        sb.append("INNER JOIN bs_customer b ON b.bc_id = a.bc_id \n");
        sb.append("WHERE a.insert_yyyymmdd = ?1 AND a.fr_code = ?2 \n");
        sb.append("AND a.fm_type IN('01','02','04') \n");

        sb.append("UNION ALL \n");

        sb.append("SELECT 'xx' as fm_type, c.insert_date ddd, d.bc_name, d.bc_hp, c.fm_message \n");
        sb.append("FROM fs_request_input_message c \n");
        sb.append("INNER JOIN bs_customer d ON d.bc_id = c.bc_id \n");
        sb.append("WHERE c.insert_yyyymmdd = ?1 AND c.fr_code = ?2 \n");

        sb.append("ORDER BY fm_type, insert_date asc \n");

        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, insertYyyymmdd);
        query.setParameter(2, frCode);

        return jpaResultMapper.list(query, MessageHistorySubListDto.class);
    }

}
