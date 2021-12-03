package com.broadwave.toppos.User.Customer;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Minkyu
 * Date : 2021-11-16
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class CustomerRepositoryCustomImpl extends QuerydslRepositorySupport implements CustomerRepositoryCustom {

    public CustomerRepositoryCustomImpl() {
        super(Customer.class);
    }

    @Override
    public List<CustomerListDto> findByCustomerList(String frCode, String searchType, String searchString) {
        QCustomer customer = QCustomer.customer;

        JPQLQuery<CustomerListDto> query = from(customer)
                .select(Projections.constructor(CustomerListDto.class,
                        customer.bcName,
                        customer.bcHp,
                        customer.bcSex,
                        customer.bcAddress,
                        customer.bcBirthday,
                        customer.bcAge,
                        customer.bcGrade,
                        customer.bcValuation,
                        customer.bcMessageAgree,
                        customer.bcAgreeType,
                        customer.bcSignImage,
                        customer.bcRemark,
                        customer.bcQuitYn,
                        customer.bcQuitDate
                ));

        query.orderBy(customer.id.desc());
        query.where(customer.frCode.eq(frCode));

        if(searchType.equals("0")){
            query.where(customer.bcName.containsIgnoreCase(searchString).or(customer.bcHp.containsIgnoreCase(searchString)));
        }else if(searchType.equals("1")){
            query.where(customer.bcName.containsIgnoreCase(searchString));
        }else {
            query.where(customer.bcHp.containsIgnoreCase(searchString));
        }

        return query.fetch();
    }

    @Override
    public CustomerInfoDto findByCustomerInfo(String frCode, String searchType, String searchString) {

        QCustomer customer = QCustomer.customer;

        JPQLQuery<CustomerInfoDto> query = from(customer)
                .select(Projections.constructor(CustomerInfoDto.class,
                        customer.id,
                        customer.bcName,
                        customer.bcHp,
                        customer.bcAddress,
                        customer.bcGrade,
                        customer.bcValuation
                ));

        query.where(customer.frCode.eq(frCode));

        switch (searchType) {
            case "0":
                query.where(customer.bcName.containsIgnoreCase(searchString).or(customer.bcHp.containsIgnoreCase(searchString).or(customer.bcAddress.containsIgnoreCase(searchString))));
                break;
            case "1":
                query.where(customer.bcName.containsIgnoreCase(searchString));
                break;
            case "2":
                query.where(customer.bcHp.containsIgnoreCase(searchString));
                break;
            default:
                query.where(customer.bcAddress.containsIgnoreCase(searchString));
                break;
        }

        return query.fetchOne();
    }

}
