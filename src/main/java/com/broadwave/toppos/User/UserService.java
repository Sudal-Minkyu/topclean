package com.broadwave.toppos.User;

import com.broadwave.toppos.Account.AccountRepositoryCustom;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import com.broadwave.toppos.User.Customer.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepositoryCustom;
import com.broadwave.toppos.keygenerate.KeyGenerateService;
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

    private final KeyGenerateService keyGenerateService;
    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;

    private final RequestRepositoryCustom requestRepositoryCustom;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    private final CustomerRepository customerRepository;
    private final CustomerRepositoryCustom customerRepositoryCustom;

    private final AccountRepositoryCustom accountRepositoryCustom;
    private final BranchCalendarRepositoryCustom branchCalendarRepositoryCustom;

    @Autowired
    public UserService(RequestRepository requestRepository, RequestDetailRepository requestDetailRepository,
                       CustomerRepository customerRepository, CustomerRepositoryCustom customerRepositoryCustom,
                       AccountRepositoryCustom accountRepositoryCustom, BranchCalendarRepositoryCustom branchCalendarRepositoryCustom,
                       KeyGenerateService keyGenerateService,  RequestRepositoryCustom requestRepositoryCustom, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.customerRepository = customerRepository;
        this.customerRepositoryCustom = customerRepositoryCustom;
        this.accountRepositoryCustom = accountRepositoryCustom;
        this.branchCalendarRepositoryCustom = branchCalendarRepositoryCustom;
        this.keyGenerateService = keyGenerateService;
        this.requestRepositoryCustom = requestRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
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

    // 접수코드를 통한 접수마스터 테이블 조회
    public Optional<Request> findByRequest(String frNo){
        return requestRepository.findByRequest(frNo);
    }

    // 접수코드와 태그번호를 통한 접수세부 테이블 조회
    public Optional<RequestDetail> findByRequestDetail(String frNo, String fdTag){
        return requestDetailRepository.findByRequestDetail(frNo, fdTag);
    }

    // 문의 접수 API : 임시저장 또는 결제할시 저장한다. 마스터테이블, 세부테이블 저장
    @Transactional(rollbackFor = SQLException.class)
    public Request requestAndDetailSave(Request request, List<RequestDetail> requestDetailList, Customer customer){
        try{
            String frNo;
            if (request.getFrNo() == null || request.getFrNo().isEmpty()){
                frNo = keyGenerateService.keyGenerate("fs_request", request.getFrCode()+request.getFrYyyymmdd(), request.getFr_insert_id());
                request.setFrNo(frNo);
            }else{
                frNo = request.getFrNo();
            }
            log.info("frNo : "+frNo);
            Request requestSave = requestRepository.save(request);

            for(int i=0; i<requestDetailList.size(); i++){
                if(requestDetailList.get(i).getFrNo()==null){
                    requestDetailList.get(i).setFrNo(frNo);
                    requestDetailList.get(i).setFrId(requestSave);
                }
            }
            customerRepository.save(customer);
            requestDetailRepository.saveAll(requestDetailList);

            return requestSave;
        }catch (Exception e){
            log.info("에러발생 트랜젝션실행 : "+e);
            return null;
        }
    }

    // 접수 마스터테이블 임시저장 리스트 호출
    public List<RequestListDto> findByRequestTempList(String frCode){
        return requestRepositoryCustom.findByRequestTempList(frCode);
    }

    public List<RequestDetailDto> findByRequestTempDetailList(String frNo) {
        return requestDetailRepositoryCustom.findByRequestTempDetailList(frNo);
    }
}
