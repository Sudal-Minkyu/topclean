package com.broadwave.toppos.Loginlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginlogRepository extends JpaRepository<Loginlog,Long> {

}