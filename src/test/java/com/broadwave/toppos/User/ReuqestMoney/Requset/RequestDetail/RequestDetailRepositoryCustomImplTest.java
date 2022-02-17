package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.manager.RequestDetailBranchStoreCurrentListDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author InSeok
 * Date : 2022-02-17
 * Remark :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestDetailRepositoryCustomImplTest {

    @Autowired RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    @Test
    @Ignore
    public void findByRequestDetailBranchStoreCurrentList_Test(){
        List<RequestDetailBranchStoreCurrentListDto> results = requestDetailRepositoryCustom.findByRequestDetailBranchStoreCurrentList("br",2L,"20220201","20220216", "1");

    }

}