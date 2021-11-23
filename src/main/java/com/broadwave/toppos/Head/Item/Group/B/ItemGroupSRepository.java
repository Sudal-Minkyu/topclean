package com.broadwave.toppos.Head.Item.Group.B;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemGroupSRepository extends JpaRepository<ItemGroupS,Long> {
//    @Query("select a from ItemGroupS a where a.bsItemGroupcodeS = :bsItemGroupcodeS and a.bgItemGroupcode.bgItemGroupcode = :bgItemGroupcode")
//    Optional<ItemGroupS> findByItemGroupcodeS(String bgItemGroupcode, String bsItemGroupcodeS);
}