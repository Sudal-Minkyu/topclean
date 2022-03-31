package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-24
 * Time :
 * Remark :
 */
public interface PhotoRepositoryCustom {
    List<PhotoDto> findByPhotoDtoRequestDtlList(Long fdId);
    List<PhotoDto> findByPhotoDtoInspeotList(Long fiId);
}
