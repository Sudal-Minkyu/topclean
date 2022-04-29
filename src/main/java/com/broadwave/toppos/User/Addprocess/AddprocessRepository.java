package com.broadwave.toppos.User.Addprocess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2022-01-03
 * Time :
 * Remark :
 */
@Repository
public interface AddprocessRepository extends JpaRepository<Addprocess,Long>, AddprocessRepositoryCustom {

}