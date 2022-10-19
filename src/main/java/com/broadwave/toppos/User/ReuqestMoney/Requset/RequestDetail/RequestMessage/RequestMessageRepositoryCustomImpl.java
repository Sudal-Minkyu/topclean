package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class RequestMessageRepositoryCustomImpl extends QuerydslRepositorySupport implements RequestMessageRepositoryCustom {

    public RequestMessageRepositoryCustomImpl() {
        super(RequestMessageRepository.class);
    }

}
