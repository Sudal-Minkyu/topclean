package com.broadwave.toppos.User.ReuqestMoney.UserService;

import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.EtcDataDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.*;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import com.broadwave.toppos.keygenerate.KeyGenerateService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-12-14
 * Time :
 * Remark : Toppos 가맹점 접수 전용 서비스
 */
@Slf4j
@Service
public class ReceiptService {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final KeyGenerateService keyGenerateService;
    private final HeadService headService;
    private final ModelMapper modelMapper;

    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;

    private final RequestRepositoryCustom requestRepositoryCustom;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    private final CustomerRepository customerRepository;

    private final BranchCalendarRepositoryCustom branchCalendarRepositoryCustom;

    @Autowired
    public ReceiptService(UserService userService, RequestRepository requestRepository, RequestDetailRepository requestDetailRepository, TokenProvider tokenProvider, HeadService headService,
                          ModelMapper modelMapper,
                          CustomerRepository customerRepository, BranchCalendarRepositoryCustom branchCalendarRepositoryCustom,
                          KeyGenerateService keyGenerateService, RequestRepositoryCustom requestRepositoryCustom, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.userService = userService;
        this.headService = headService;
        this.requestRepository = requestRepository;
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;

        this.requestDetailRepository = requestDetailRepository;
        this.customerRepository = customerRepository;
        this.branchCalendarRepositoryCustom = branchCalendarRepositoryCustom;
        this.keyGenerateService = keyGenerateService;
        this.requestRepositoryCustom = requestRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
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


    // 접수 마스터테이블 임시저장 리스트 호출
    public List<RequestListDto> findByRequestTempList(String frCode){
        return requestRepositoryCustom.findByRequestTempList(frCode);
    }

    // 접수 세부테이블 임시저장 리스트 호출
    public List<RequestDetailDto> findByRequestTempDetailList(String frNo) {
        return requestDetailRepositoryCustom.findByRequestTempDetailList(frNo);
    }

    // 접수페이지 가맹점 세탁접수 API
    public ResponseEntity<Map<String,Object>> requestSave(RequestDetailSet requestDetailSet, HttpServletRequest request) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 현재 날짜 받아오기
        LocalDateTime localDateTime = LocalDateTime.now();
        String nowDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("현재 날짜 yyyymmdd : "+nowDate);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드

        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        RequestMapperDto etcData = requestDetailSet.getEtc(); // etc 데이터 얻기

        ArrayList<RequestDetailDto> addList = requestDetailSet.getAdd(); // 추가 리스트 얻기
        ArrayList<RequestDetailDto> updateList = requestDetailSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<RequestDetailDto> deleteList = requestDetailSet.getDelete(); // 제거 리스트 얻기

        log.info("ECT 데이터 : "+etcData);
        log.info("추가 리스트 : "+addList);
        log.info("수정 리스트 : "+updateList);
        log.info("삭제 리스트 : "+deleteList);
        log.info("추가 사이즈 : "+addList.size());
        log.info("수정 사이즈 : "+updateList.size());
        log.info("삭제 사이즈 : "+deleteList.size());

        // 현재 고객을 받아오기
        Optional<Customer> optionalCustomer = userService.findByBcHp(etcData.getBcHp());
        if(!optionalCustomer.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP018.getCode(), ResponseErrorCode.TP018.getDesc(), "문자", "고객번호 : "+etcData.getBcHp()));
        }else{

            Request requestSave;
            if(etcData.getFrNo() != null){
                log.info("접수마스터 테이블 수정합니다. 접수코드 : "+etcData.getFrNo());
                Optional<Request> optionalRequest = findByRequest(etcData.getFrNo());
                if(!optionalRequest.isPresent()){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "접수 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "접수코드 : "+etcData.getFrNo()));
                }else{
                    optionalRequest.get().setFrTotalAmount(etcData.getFrTotalAmount());
                    optionalRequest.get().setFrDiscountAmount(etcData.getFrDiscountAmount());
                    optionalRequest.get().setFrNormalAmount(etcData.getFrNormalAmount());
                    optionalRequest.get().setFrQty(addList.size()+updateList.size());
                    optionalRequest.get().setFrYyyymmdd(nowDate);
                    optionalRequest.get().setModity_id(login_id);
                    optionalRequest.get().setModity_date(LocalDateTime.now());
                    requestSave = optionalRequest.get();
                }
            }else{
                log.info("접수마스터 테이블 신규 저장합니다.");

                requestSave = modelMapper.map(etcData, Request.class);

                requestSave.setBcId(optionalCustomer.get());
                requestSave.setBcCode(frbrCode);
                requestSave.setFrCode(frCode);
                requestSave.setFrYyyymmdd(nowDate);

                requestSave.setFrQty(addList.size()+updateList.size());
                requestSave.setFrRefBoxCode(null); // 무인보관함 연계시 무인보관함 접수번호 : 일단 무조건 NULL
                requestSave.setFr_insert_id(login_id);
                requestSave.setFr_insert_date(LocalDateTime.now());

                log.info("접수마스터 테이블 저장 or 수정 : "+requestSave);
            }

            log.info("etcData.getCheckNum() : "+etcData.getCheckNum());
            if(etcData.getCheckNum().equals("1")){
                requestSave.setFrUncollectYn("Y");
                requestSave.setFrConfirmYn("N");
            }else{
                requestSave.setFrUncollectYn("N");
                requestSave.setFrConfirmYn("N");
            }

            String lastTagNo = null; // 마지막 태그번호
            List<RequestDetail> requestDetailList = new ArrayList<>(); // 세부테이블 객체 리스트
            // 접수 세부 테이블 저장
            if(addList.size()!=0){
                for (RequestDetailDto requestDetailDto : addList) {
                    log.info("RequestDetailDto : "+requestDetailDto);
                    RequestDetail requestDetail = modelMapper.map(requestDetailDto, RequestDetail.class);

                    requestDetail.setBiItemcode(requestDetailDto.getBiItemcode());
                    requestDetail.setFdState("S1");
                    requestDetail.setFdStateDt(LocalDateTime.now());
                    requestDetail.setFdCancel("N");
                    requestDetail.setFdTotAmt(requestDetailDto.getFdRequestAmt());
                    requestDetail.setFdEstimateDt(requestDetailDto.getFrEstimateDate());
                    requestDetail.setInsert_id(login_id);
                    requestDetail.setInsert_date(LocalDateTime.now());
                    lastTagNo = requestDetailDto.getFdTag();
                    requestDetailList.add(requestDetail);
                }
            }
            // 접수 세부 테이블 업데이트
            if(updateList.size()!=0){
                for (RequestDetailDto requestDetailDto : updateList) {
                    log.info("수정로직 FrNo : "+etcData.getFrNo());
                    log.info("수정로직 FdTag : "+requestDetailDto.getFdTag());
                    Optional<RequestDetail> optionalRequestDetail = findByRequestDetail(etcData.getFrNo(), requestDetailDto.getFdTag());
                    if(!optionalRequestDetail.isPresent()){
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 "+ResponseErrorCode.TP009.getDesc(), "문자", "택번호 : "+requestDetailDto.getFdTag()));
                    }else{
                        optionalRequestDetail.get().setBiItemcode(requestDetailDto.getBiItemcode());
                        optionalRequestDetail.get().setFdColor(requestDetailDto.getFdColor());
                        optionalRequestDetail.get().setFdPattern(requestDetailDto.getFdPattern());
                        optionalRequestDetail.get().setFdPriceGrade(requestDetailDto.getFdPriceGrade());

                        optionalRequestDetail.get().setFdOriginAmt(requestDetailDto.getFdOriginAmt());
                        optionalRequestDetail.get().setFdNormalAmt(requestDetailDto.getFdNormalAmt());
                        optionalRequestDetail.get().setFdRepairRemark(requestDetailDto.getFdRepairRemark());
                        optionalRequestDetail.get().setFdRepairAmt(requestDetailDto.getFdRepairAmt());

                        optionalRequestDetail.get().setFdAdd1Remark(requestDetailDto.getFdAdd1Remark());
                        optionalRequestDetail.get().setFdAdd1SpecialYn(requestDetailDto.getFdAdd1SpecialYn());
                        optionalRequestDetail.get().setFdAdd1Amt(requestDetailDto.getFdAdd1Amt());

                        optionalRequestDetail.get().setFdPressed(requestDetailDto.getFdPressed());
                        optionalRequestDetail.get().setFdWhitening(requestDetailDto.getFdWhitening());
                        optionalRequestDetail.get().setFdPollution(requestDetailDto.getFdPollution());
                        optionalRequestDetail.get().setFdPollutionLevel(requestDetailDto.getFdPollutionLevel());
                        optionalRequestDetail.get().setFdStarch(requestDetailDto.getFdStarch());
                        optionalRequestDetail.get().setFdWaterRepellent(requestDetailDto.getFdWaterRepellent());

                        optionalRequestDetail.get().setFdDiscountGrade(requestDetailDto.getFdDiscountGrade());
                        optionalRequestDetail.get().setFdDiscountAmt(requestDetailDto.getFdDiscountAmt());
                        optionalRequestDetail.get().setFdQty(requestDetailDto.getFdQty());

                        optionalRequestDetail.get().setFdRequestAmt(requestDetailDto.getFdRequestAmt());
                        optionalRequestDetail.get().setFdRetryYn(requestDetailDto.getFdRetryYn());

                        optionalRequestDetail.get().setFdRemark(requestDetailDto.getFdRemark());
                        optionalRequestDetail.get().setFdEstimateDt(requestDetailDto.getFrEstimateDate());

                        optionalRequestDetail.get().setModity_id(login_id);
                        optionalRequestDetail.get().setModity_date(LocalDateTime.now());
//                        RequestDetail requestDetail = optionalRequestDetail.get();
                        requestDetailList.add(optionalRequestDetail.get());
                    }
                }
            }
            log.info("requestDetailList : "+requestDetailList);

            // 현재 접수한 고객의 대한 마지막방문일자 업데이트
            optionalCustomer.get().setBcLastRequestDt(nowDate);
            Customer customer = optionalCustomer.get();

            Request requestSaveO = requestAndDetailSave(requestSave, requestDetailList, customer);


            // 모두 저장되면 최종 택번호 업데이트
            Optional<Franchise> optionalFranchise = headService.findByFrCode(frCode); // 가맹점
            log.info("마지막 택번호 : "+lastTagNo);
            if(optionalFranchise.isPresent()){
                if(addList.size()==0){
                    lastTagNo = optionalFranchise.get().getFrLastTagno();
                }
                optionalFranchise.get().setFrLastTagno(lastTagNo);

                headService.franchiseSave(optionalFranchise.get());
                log.info(optionalFranchise.get().getFrName()+" 가맹점 택번호 업데이트 완료 : "+lastTagNo);
            }

            data.put("frNo",requestSaveO.getFrNo());

            return ResponseEntity.ok(res.dataSendSuccess(data));

        }
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

            for (RequestDetail requestDetail : requestDetailList) {
                if (requestDetail.getFrNo() == null) {
                    requestDetail.setFrNo(frNo);
                    requestDetail.setFrId(requestSave);
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

}
