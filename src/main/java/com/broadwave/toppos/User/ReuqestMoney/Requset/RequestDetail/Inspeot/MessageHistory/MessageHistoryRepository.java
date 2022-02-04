package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-02-04
 * Time :
 * Remark : Toppos 가맹점 메세지 송신이력 Repository
 */
@Repository
public interface MessageHistoryRepository extends JpaRepository<MessageHistory,Long> {

}