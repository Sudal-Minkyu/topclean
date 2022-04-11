package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestMessage.RequestMessage;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestMessage.RequestMessageRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouceForce;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouseRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-01-19
 * Time :
 * Remark : Toppos 가맹점 접수건 현재상태 변화 서비스
 */
@Slf4j
@Service
public class ReceiptStateService {

    private final TokenProvider tokenProvider;

    private final InhouseRepository inhouseRepository;
    private final CustomerRepository customerRepository;
    private final RequestMessageRepository requestMessageRepository;
    private final RequestDetailRepository requestDetailRepository;

    @Autowired
    public ReceiptStateService(TokenProvider tokenProvider, RequestMessageRepository requestMessageRepository, CustomerRepository customerRepository,
                               InhouseRepository inhouseRepository, RequestDetailRepository requestDetailRepository){
        this.tokenProvider = tokenProvider;
        this.inhouseRepository = inhouseRepository;
        this.customerRepository = customerRepository;
        this.requestMessageRepository = requestMessageRepository;
        this.requestDetailRepository = requestDetailRepository;
    }

    //  접수테이블의 상태 변화 API - 수기마감페이지, 가맹점입고 페이지, 지사반송건전송 페이지, 세탁인도 페이지 공용함수
    @Transactional
    public ResponseEntity<Map<String, Object>> franchiseStateChange(List<Long> fdIdList, String stateType, HttpServletRequest request) {
        log.info("franchiseStateChange 호출");

//        log.info("fdIdList : "+fdIdList);
//        log.info("stateType : "+stateType);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        // stateType 상태값
        // "S1"이면 수기마감페이지 버튼 "S1" -> "S2"
        // "S4"이면 가맹점입고 페이지 버튼 "S4" -> "S5"
        // "S3"이면 지사반송페이지 버튼 "S3" -> "S2"
        // "S7"이면 가맹점강제입고 페이지 버튼 "S7" -> "S8"
        switch (stateType) {
            // 수정완료 - 2022/03/03
            case "S1": { // 수기마감
                log.info("수기마감 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS1List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S2");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS2Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS2Time(LocalDateTime.now());
                    requestDetail.setFdS2Type("01");

                    requestDetail.setFdFrState("S2");
                    requestDetail.setFdFrStateTime(LocalDateTime.now());

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);
                break;
            }
            // 수정완료 - 2022/03/03
            case "S4": {
                log.info("가맹점입고 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS4List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);

                List<String> frNoList = new ArrayList<>();
                for (RequestDetail requestDetail : requestDetailList) {
                    if(!frNoList.contains(requestDetail.getFrNo())){
                        frNoList.add(requestDetail.getFrNo());
                    }

//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S5");
                    requestDetail.setFdS5Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS5Time(LocalDateTime.now());
                    requestDetail.setFdFrState("S5");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdFrStateTime(LocalDateTime.now());

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);

                log.info("frNoList : "+frNoList);
                InputCheckMessage(frNoList);
                break;
            }
//            case "S3": {
//                log.info("지사반송 처리");
//                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS3List(fdIdList);
////            log.info("requestDetailList : "+requestDetailList);
//                for (RequestDetail requestDetail : requestDetailList) {
////                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
//                    requestDetail.setFdPreState(stateType); // 이전상태 값
//                    requestDetail.setFdPreStateDt(LocalDateTime.now());
//                    requestDetail.setFdState("S2");
//                    requestDetail.setFdStateDt(LocalDateTime.now());
//
//                    requestDetail.setFdS2Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
//                    requestDetail.setFdS2Time(LocalDateTime.now());
//                    requestDetail.setFdS2Type("03");
//
//                    requestDetail.setModify_id(login_id);
//                    requestDetail.setModify_date(LocalDateTime.now());
//                }
//                requestDetailRepository.saveAll(requestDetailList);
//                break;
//            }
            // 수정완료 - 2022/03/03
            case "S7": {
                log.info("가맹점강제입고 처리");
                Optional<InhouceForce> inhouceForce;
                List<InhouceForce> inhouceForceList = new ArrayList<>();
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS7List(fdIdList);

                List<String> frNoList = new ArrayList<>();

//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
                    if(!frNoList.contains(requestDetail.getFrNo())){
                        frNoList.add(requestDetail.getFrNo());
                    }

//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(requestDetail.getFdState()); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S8");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS8Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS8Time(LocalDateTime.now());

                    requestDetail.setFdS4Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS4Time(LocalDateTime.now());
                    requestDetail.setFdS4Id(login_id);
                    requestDetail.setFdS4Type("03");

                    requestDetail.setFdFrState("S8");
                    requestDetail.setFdFrStateTime(LocalDateTime.now());

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());

                    inhouceForce = inhouseRepository.findById(requestDetail.getId());
                    // 가맹 입고처리 insert
                    if(inhouceForce.isPresent()){
                        inhouceForce.get().setFdId(requestDetail.getId());
                        inhouceForce.get().setFrCode(frCode);
                        inhouceForce.get().setBrCode(frbrCode);
                        inhouceForce.get().setFiDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                        inhouceForce.get().setFiTime(LocalDateTime.now());
                        inhouceForce.get().setInsert_id(login_id);
                        inhouceForceList.add(inhouceForce.get());
                    }else{
                        InhouceForce newInhouceForce = new InhouceForce();
                        newInhouceForce.setFdId(requestDetail.getId());
                        newInhouceForce.setFrCode(frCode);
                        newInhouceForce.setBrCode(frbrCode);
                        newInhouceForce.setFiDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                        newInhouceForce.setFiTime(LocalDateTime.now());
                        newInhouceForce.setInsert_id(login_id);
                        inhouceForceList.add(newInhouceForce);
                    }
                }
                requestDetailRepository.saveAll(requestDetailList);
                inhouseRepository.saveAll(inhouceForceList);
                log.info("frNoList : "+frNoList);
                InputCheckMessage(frNoList);
                break;
            }
            // 수정완료 - 2022/03/03
            case "S3":
            case "S5":
            case "S8": {
                log.info("세탁인도 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS5OrS8OrS3List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());인
                    if(requestDetail.getFdS4Type().equals("04") || requestDetail.getFdS4Type().equals("06") || requestDetail.getFdS4Type().equals("08")){
                        // 조건 - 04 : 반품출고 S7 - 02, 06 : 확인품(미확인) - 반품, 08 : 확인품(거부) - 반품
                        requestDetail.setFdS6Type("02");
                    }else{
                        // 조건 - 01 : 일반출고, 02 : 강제출고 S7 - 01, 03: 가맹점강제입고출고 - 가맹점 강제입고처리시, 05 : 확인품(미확인) - 출고, 07 : 확인품(수락) - 출고
                        requestDetail.setFdS6Type("01");
                    }
                    requestDetail.setFdPreState(requestDetail.getFdState()); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S6");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS6Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS6Time(LocalDateTime.now());

                    requestDetail.setFdFrState("S6");
                    requestDetail.setFdFrStateTime(LocalDateTime.now());

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);
                break;
            }
            default:
                return ResponseEntity.ok(res.fail("문자", stateType + " 타입은 존재하지 않습니다.", "문자", "관리자에게 문의해주세요."));
        }
        
        return ResponseEntity.ok(res.success());
    }

    // 가맹점 입고시 메세지테이블 insert 함수
    public void InputCheckMessage(List<String> frNoList){
        log.info("frNoList : "+frNoList);
        List<RequestDetailInputMessageDto> requestDetailInputMessageDtos = requestDetailRepository.findByRequestDetailInputMessage(frNoList);
        log.info("requestDetailInputMessageDtos : "+requestDetailInputMessageDtos);
        List<RequestMessage> requestMessageList = new ArrayList<>();
        String fmMessage;
        String bcName;
        for(RequestDetailInputMessageDto requestDetailInputMessageDto : requestDetailInputMessageDtos){
            // 모든 상품 가맹점입고시 메세지 테이블 insert
            Optional<Customer> optionalCustomer = customerRepository.findByBcId(requestDetailInputMessageDto.getBcId());
            RequestMessage requestMessage = new RequestMessage(); // \n\n
            requestMessage.setFrCode(requestDetailInputMessageDto.getFrCode());
            requestMessage.setFrNo(requestDetailInputMessageDto.getFrNo());
            if(optionalCustomer.isPresent()){
                bcName = optionalCustomer.get().getBcName()+"님";
                requestMessage.setBcId(optionalCustomer.get());
            }else{
                requestMessage.setBcId(null);
                bcName = "";
            }

            fmMessage = "안녕하세요 "+bcName+"\n"+"맡겨주신 "+requestDetailInputMessageDto.getBgName()+" "+requestDetailInputMessageDto.getBiName();
            if(requestDetailInputMessageDto.getFrQty() > 1){
                int qty = requestDetailInputMessageDto.getFrQty()-1;
                fmMessage = fmMessage +"외 "+qty+"건의 물품이 세탁완료 되었습니다.";
            }else{
                fmMessage = fmMessage +"물품이 세탁완료 되었습니다.";
            }

            requestMessage.setFmMessage(fmMessage);
            requestMessage.setFmMessageYn("N");
            requestMessage.setInsertYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            requestMessage.setInsertDateTime(LocalDateTime.now());
            requestMessageList.add(requestMessage);
        }
        requestMessageRepository.saveAll(requestMessageList);
    }

    //  수기마감 - 세부테이블 접수상태 리스트 + 검품이 존재할시 상태가 고객수락일 경우에만 호출
    public ResponseEntity<Map<String,Object>> franchiseReceiptCloseList(HttpServletRequest request){
        log.info("franchiseReceiptCloseList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 수기마감 페이지에 보여줄 리스트 호출
        List<RequestDetailCloseListDto> requestDetailCloseListDtos = requestDetailRepository.findByRequestDetailCloseList(frCode);
//        List<Long> fdIdList = new ArrayList<>();
//        for(RequestDetailCloseListDto requestDetailCloseListDto : requestDetailCloseListDtos){
//            fdIdList.add(requestDetailCloseListDto.getFdId());
//        }
//        List<InspeotYnDto> inspeotYnDtos = inspeotRepositoryCustom.findByInspeotStateList(fdIdList,"1");
        log.info("requestDetailCloseListDtos : "+requestDetailCloseListDtos);
        data.put("gridListData",requestDetailCloseListDtos);
//        data.put("removeFrId",inspeotYnDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 모바일 수기마감 리스트
    public int findByRequestDetailMobileCloseList(String frCode) {
        return requestDetailRepository.findByRequestDetailMobileCloseList(frCode);
    }

    //  가맹점입고 - 세부테이블 지사출고 상태 리스트
    public ResponseEntity<Map<String, Object>> franchiseReceiptFranchiseInList(HttpServletRequest request) {
        log.info("franchiseReceiptFranchiseInList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 가맹점입고 페이지에 보여줄 리스트 호출
        List<RequestDetailFranchiseInListDto> requestDetailFranchiseInListDtos = requestDetailRepository.findByRequestDetailFranchiseInList(frCode);
        data.put("gridListData",requestDetailFranchiseInListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  가맹점입고취소 상태 변화 API
    public ResponseEntity<Map<String, Object>> franchiseInCancelChange(List<Long> fdIdList, HttpServletRequest request) {
        log.info("franchiseInCancelChange 호출");

//        log.info("fdIdList : "+fdIdList);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS5List(fdIdList);
//          log.info("requestDetailList : "+requestDetailList);
        for (RequestDetail requestDetail : requestDetailList) {
//         log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
            requestDetail.setFdPreState(requestDetail.getFdState()); // 이전상태 값
            requestDetail.setFdPreStateDt(LocalDateTime.now());
            requestDetail.setFdState("S4");
            requestDetail.setFdStateDt(LocalDateTime.now());

            requestDetail.setFdFrState("S4");
            requestDetail.setFdFrStateTime(LocalDateTime.now());

            requestDetail.setModify_id(login_id);
            requestDetail.setModify_date(LocalDateTime.now());
        }
        requestDetailRepository.saveAll(requestDetailList);

        return ResponseEntity.ok(res.success());
    }

    //  가맹점입고취소 - 세부테이블 가맹점입고상태 리스트
    public ResponseEntity<Map<String, Object>> franchiseReceiptFranchiseInCancelList(String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("franchiseReceiptFranchiseInCancelList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 지사반송 페이지에 보여줄 리스트 호출
        List<RequestDetailFranchiseInCancelListDto> requestDetailFranchiseInCancelListDtos = requestDetailRepository.findByRequestDetailFranchiseInCancelList(frCode, filterFromDt, filterToDt);
        data.put("gridListData",requestDetailFranchiseInCancelListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

//    //  지사반송 - 세부테이블 지사반송 상태 리스트
//    public ResponseEntity<Map<String, Object>> franchiseReceiptReturnList(HttpServletRequest request) {
//        log.info("franchiseReceiptReturnList 호출");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//
//        // 지사반송 페이지에 보여줄 리스트 호출
//        List<RequestDetailReturnListDto> requestDetailFranchiseInListDtos = requestDetailRepository.findByRequestDetailReturnList(frCode);
//        data.put("gridListData",requestDetailFranchiseInListDtos);
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }

    //  가맹점강제입고 - 세부테이블 강제출고 상태 리스트
    public ResponseEntity<Map<String, Object>> franchiseReceiptForceList(Long bcId, String fdTag, HttpServletRequest request) {
        log.info("franchiseReceiptForceList 호출");

        log.info("bcId: "+bcId);
        log.info("fdTag: "+fdTag);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frTagNo = (String) claims.get("frTagNo"); // 현재 가맹점의 택번호(2자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("현재 접속한 가맹점 택번호 : "+frTagNo);

        // 가맹점강제입고 페이지에 보여줄 리스트 호출
        List<RequestDetailForceListDto> requestDetailForceListDtos = requestDetailRepository.findByRequestDetailForceList(bcId, frTagNo+fdTag, frCode);
        data.put("gridListData",requestDetailForceListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  세탁인도 - 세부테이블 지사입고, 강제입고 상태 리스트
    public ResponseEntity<Map<String, Object>> franchiseReceiptDeliveryList(Long bcId, HttpServletRequest request) {
        log.info("franchiseReceiptDeliveryList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 세탁인도 페이지에 보여줄 리스트 호출
        List<RequestDetailDeliveryDto> requestDetailDeliveryDtos = requestDetailRepository.findByRequestDetailDeliveryList(frCode, bcId);
        data.put("gridListData",requestDetailDeliveryDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
