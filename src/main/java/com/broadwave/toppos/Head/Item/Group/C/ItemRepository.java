package com.broadwave.toppos.Head.Item.Group.C;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    @Query("select a from Item a where a.biItemCode = :biItemCode")
    Optional<Item> findByBiItemCode(String biItemCode);
}