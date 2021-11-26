package com.broadwave.toppos.Head.Item.Group.C;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    @Query("select a from Item a where a.biItemcode = :biItemcode")
    Optional<Item> findByBiItemcode(String biItemcode);

    @Query("select a from Item a where a.bgItemGroupcode = :bgItemGroupcode and a.bsItemGroupcodeS = :bsItemGroupcodeS")
    Optional<Item> findByBiItem(String bgItemGroupcode, String bsItemGroupcodeS);

}