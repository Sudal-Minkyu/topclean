package com.broadwave.toppos.User.Template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template,Long>, TemplateRepositoryCustom {

}