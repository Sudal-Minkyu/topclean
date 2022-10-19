package com.broadwave.toppos.Manager.HmTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HmTemplateRepository extends JpaRepository<HmTemplate,Long>, HmTemplateRepositoryCustom {

}