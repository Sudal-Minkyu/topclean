package com.broadwave.toppos.User.ReuqestMoney.Requset.Find;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-03-30
 * Time :
 * Remark :
 */
@Repository
public interface FindRepository extends JpaRepository<Find,Long>, FindRepositoryCustom {

}