package com.broadwave.toppos.Head.Item.Group.A;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemGroupRepository extends JpaRepository<ItemGroup,Long> {
    @Query("select a from ItemGroup a where a.bgItemGroupcode = :bgItemGroupcode")
    Optional<ItemGroup> findByBgItemGroupcode(String bgItemGroupcode);
}