package com.broadwave.toppos.User.Customer;

import com.broadwave.toppos.Head.Branch.QBranch;
import com.broadwave.toppos.Head.Franchise.QFranchise;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerInfoDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerListDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerMessageListDto;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerUncollectListDto;
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
                        customer.bcId,
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
                        customer.bcWeddingAnniversary,
                        customer.bcQuitYn,
                        customer.bcQuitDate,
                        customer.insertDateTime
                ));

        query.orderBy(customer.bcId.desc());
        query.where(customer.frCode.eq(frCode));

        if(!searchString.equals("")){
            if(searchType.equals("0")){
                query.where(customer.bcName.likeIgnoreCase("%"+searchString+"%").or(customer.bcHp.likeIgnoreCase("%"+searchString+"%")));
            }else if(searchType.equals("1")){
                query.where(customer.bcName.likeIgnoreCase("%"+searchString+"%"));
            }else {
                query.where(customer.bcHp.likeIgnoreCase("%"+searchString+"%"));
            }
        }

        return query.fetch();
    }

    @Override
    public List<CustomerInfoDto> findByCustomerInfo(String frCode, String searchType, String searchString) {

        QCustomer customer = QCustomer.customer;

        JPQLQuery<CustomerInfoDto> query = from(customer)
                .select(Projections.constructor(CustomerInfoDto.class,
                        customer.bcId,
                        customer.bcName,
                        customer.bcHp,
                        customer.bcAddress,
                        customer.bcGrade,
                        customer.bcValuation,
                        customer.bcRemark,
                        customer.bcLastRequestDt,
                        customer.bcWeddingAnniversary
                ));

        query.where(customer.frCode.eq(frCode));

        if(searchString != null){
            switch (searchType) {
                case "0":
                    query.where(customer.bcName.likeIgnoreCase("%"+searchString+"%").or(customer.bcHp.likeIgnoreCase("%"+searchString+"%").or(customer.bcAddress.likeIgnoreCase("%"+searchString+"%"))));
                    break;
                case "1":
                    query.where(customer.bcName.likeIgnoreCase("%"+searchString+"%"));
                    break;
                case "2":
                    query.where(customer.bcHp.likeIgnoreCase("%"+searchString+"%"));
                    break;
                default:
                    query.where(customer.bcAddress.likeIgnoreCase("%"+searchString+"%"));
                    break;
            }
        }

        return query.fetch();
    }

    @Override
    public List<CustomerUncollectListDto> findByCustomerUncollectList(String frCode, String searchType, String searchString) {
        QCustomer customer = QCustomer.customer;

        JPQLQuery<CustomerUncollectListDto> query = from(customer)
                .select(Projections.constructor(CustomerUncollectListDto.class,
                        customer.bcId,
                        customer.bcName,
                        customer.bcHp,
                        customer.bcAddress
                ));

        query.orderBy(customer.bcId.desc());
        query.where(customer.frCode.eq(frCode));

        if(searchString != null){
            switch (searchType) {
                case "0":
                    query.where(customer.bcName.likeIgnoreCase("%"+searchString+"%").or(customer.bcHp.likeIgnoreCase("%"+searchString+"%").or(customer.bcAddress.likeIgnoreCase("%"+searchString+"%"))));
                    break;
                case "1":
                    query.where(customer.bcName.likeIgnoreCase("%"+searchString+"%"));
                    break;
                case "2":
                    query.where(customer.bcHp.likeIgnoreCase("%"+searchString+"%"));
                    break;
                default:
                    query.where(customer.bcAddress.likeIgnoreCase("%"+searchString+"%"));
                    break;
            }
        }

        return query.fetch();
    }

    // 가맹점 문자메세지 보낼 고객 리스트
    @Override
    public List<CustomerMessageListDto> findByMessageCustomerList(String visitDayRange, String bcLastRequestDt, String frCode) {

        QCustomer customer = QCustomer.customer;

        JPQLQuery<CustomerMessageListDto> query = from(customer)
                .select(Projections.constructor(CustomerMessageListDto.class,
                        customer.bcId,
                        customer.bcName,
                        customer.bcHp
                ));

        query.orderBy(customer.bcName.asc());
        query.where(customer.frCode.eq(frCode));

        if(!visitDayRange.equals("0")){
            customer.bcLastRequestDt.goe(bcLastRequestDt);
        }

        return query.fetch();
    }

    // 지사가 문자메세지 보낼 고객 리스트
    @Override
    public List<CustomerMessageListDto> findByBrMessageCustomerList(String visitDayRange, String bcLastRequestDt, Long franchiseId, String brCode) {

        QCustomer customer = QCustomer.customer;
        QFranchise franchise = QFranchise.franchise;
        QBranch branch = QBranch.branch;

        JPQLQuery<CustomerMessageListDto> query = from(customer)
                .innerJoin(franchise).on(franchise.frCode.eq(customer.frCode))
                .innerJoin(branch).on(branch.brCode.eq(franchise.brCode))
                .select(Projections.constructor(CustomerMessageListDto.class,
                        customer.bcId,
                        customer.bcName,
                        customer.bcHp
                ));

        query.orderBy(customer.bcName.asc());

        if(franchiseId != 0){
            query.where(franchise.id.eq(franchiseId));
        }

        query.where(branch.brCode.eq(brCode));

        if(!visitDayRange.equals("0")){
            customer.bcLastRequestDt.goe(bcLastRequestDt);
        }

        return query.fetch();
    }

}
