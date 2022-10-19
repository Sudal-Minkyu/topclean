package com.broadwave.toppos.User.ReuqestMoney.SaveMoney;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveMoneyRepository extends JpaRepository<SaveMoney,Long> {

}