package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-12-24
 * Time :
 * Remark :
 */
public interface PhotoRepositoryCustom {
    List<PhotoDto> findByPhotoDtoList(Long id);

    List<Photo> findByPhotoList(Long id);
}
