package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestMessageRepository extends JpaRepository<RequestMessage,Long>, RequestMessageRepositoryCustom {

}