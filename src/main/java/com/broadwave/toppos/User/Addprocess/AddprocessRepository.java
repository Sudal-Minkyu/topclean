package com.broadwave.toppos.User.Addprocess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2021-01-03
 * Time :
 * Remark :
 */
@Repository
public interface AddprocessRepository extends JpaRepository<Addprocess,Long> {

    @Query("select a from Addprocess a where a.baType = :baType and a.baName = :baName")
    Optional<Addprocess> findByAddProcess(String baType, String baName);

}