package com.broadwave.toppos.User;

import com.broadwave.toppos.Account.AccountRepositoryCustom;
import com.broadwave.toppos.Head.Item.Price.ItemPrice;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import com.broadwave.toppos.User.Customer.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    private final AccountRepositoryCustom accountRepositoryCustom;
    private final BranchCalendarRepositoryCustom branchCalendarRepositoryCustom;

    @Autowired
    public UserService(RequestRepository requestRepository, RequestDetailRepository requestDetailRepository,
                       CustomerRepository customerRepository, CustomerRepositoryCustom customerRepositoryCustom,
                       AccountRepositoryCustom accountRepositoryCustom, BranchCalendarRepositoryCustom branchCalendarRepositoryCustom){
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.customerRepository = customerRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.accountRepositoryCustom = accountRepositoryCustom;
        this.branchCalendarRepositoryCustom = branchCalendarRepositoryCustom;
    }

    // 고객등록
    public Customer customerSave(Customer customer){
        customerRepository.save(customer);
        return customer;
    }

    // 핸드폰 번호로 고객 조회
    public Optional<Customer> findByBcHp(String bcHp) {
        return customerRepository.findByBcHp(bcHp);
    }

    // 고유 ID값으로 고객 조회
    public Optional<Customer> findByBcId(Long bcId) {
        return customerRepository.findByBcId(bcId);
    }


    // 로그인한 가맹점의 대한 고객정보 조회
    public List<CustomerInfoDto> findByCustomerInfo(String frCode, String searchType, String searchString) {
        return customerRepositoryCustom.findByCustomerInfo(frCode, searchType, searchString);
    }

    // 로그인한 가맹점의 고객리스트 호출
    public List<CustomerListDto> findByCustomerList(String frCode, String searchType, String searchString) {
        return customerRepositoryCustom.findByCustomerList(frCode, searchType, searchString);
    }

    // 가맹점 메인페이지 전용 개인정보 호출
    public UserIndexDto findByUserInfo(String userid, String frCode) {
        return accountRepositoryCustom.findByUserInfo(userid, frCode);
    }

    // 태그번호, 출고예정일 데이터
    public List<EtcDataDto> findByEtc(Long frEstimateDuration, String frCode, String nowDate) {
        return branchCalendarRepositoryCustom.findByEtc(frEstimateDuration, frCode, nowDate);
    }



    // 문의 접수 API : 임시저장 또는 결제할시 저장한다. 마스터테이블, 세부테이블 저장
    @Transactional(rollbackFor = SQLException.class)
    public void requestAndDetailSave(Request request, List<RequestDetail> requestDetailList){
        try{
            Request requestSave = requestRepository.save(request);
            for(int i=0; i<requestDetailList.size(); i++){
                requestDetailList.get(i).setFrId(requestSave);
            }
            requestDetailRepository.saveAll(requestDetailList);
        }catch (Exception e){
            log.info("에러발생 트랜젝션실행 : "+e);
        }
    }

}
