package com.broadwave.toppos.Manager.Process.Issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long>, IssueRepositoryCustom {

}