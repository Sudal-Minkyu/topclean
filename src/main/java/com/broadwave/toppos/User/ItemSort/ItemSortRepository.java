package com.broadwave.toppos.User.ItemSort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark :
 */
@Repository
public interface ItemSortRepository extends JpaRepository<ItemSort,Long> {

}