package com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-03-04
 * Time :
 * Remark : Toppos 가맹점 QR 마감 Repository
 */
@Repository
public interface QrCloseRepository extends JpaRepository<QrClose,Long>, QrClostRepositoryCustom {

}