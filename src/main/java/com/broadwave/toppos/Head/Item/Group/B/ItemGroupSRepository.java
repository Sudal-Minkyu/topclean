package com.broadwave.toppos.Head.Item.Group.B;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemGroupSRepository extends JpaRepository<ItemGroupS,Long>, ItemGroupSRepositoryCustom {

}