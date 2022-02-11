package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotYnDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouceForce;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouseRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    @Autowired
    public ReceiptStateService(TokenProvider tokenProvider,
                               InhouseRepository inhouseRepository, InspeotRepositoryCustom inspeotRepositoryCustom,
                               RequestDetailRepository requestDetailRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.inhouseRepository = inhouseRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    //  접수테이블의 상태 변화 API - 수기마감페이지, 가맹점입고 페이지, 지사반송건전송 페이지, 세탁인도 페이지 공용함수
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

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);
                break;
            }
            case "S4": {
                log.info("가맹점입고 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS4List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S5");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS5Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS5Time(LocalDateTime.now());
                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);
                break;
            }
            case "S2": {
                log.info("지사출고 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS2List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S4");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS2Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS2Time(LocalDateTime.now());
                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);
                break;
            }
            case "S3": {
                log.info("지사반송 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS3List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S2");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS2Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS2Time(LocalDateTime.now());
                    requestDetail.setFdS2Type("03");

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());
                }
                requestDetailRepository.saveAll(requestDetailList);
                break;
            }
            case "S7": {
                log.info("가맹점강제입고 처리");
                InhouceForce inhouceForce;
                List<InhouceForce> inhouceForceList = new ArrayList<>();
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS7List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S8");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS8Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS8Time(LocalDateTime.now());

                    requestDetail.setModify_id(login_id);
                    requestDetail.setModify_date(LocalDateTime.now());

                    // 가맹 입고처리 insert
                    inhouceForce = new InhouceForce();
                    inhouceForce.setFdId(requestDetail.getId());
                    inhouceForce.setFrCode(frCode);
                    inhouceForce.setBrCode(frbrCode);
                    inhouceForce.setFiDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    inhouceForce.setFiTime(LocalDateTime.now());
                    inhouceForce.setInsert_id(login_id);
                    inhouceForceList.add(inhouceForce);
                }
                requestDetailRepository.saveAll(requestDetailList);
                inhouseRepository.saveAll(inhouceForceList);
                break;
            }
            case "S5":
            case "S8": {
                log.info("세탁인도 처리");
                List<RequestDetail> requestDetailList = requestDetailRepository.findByRequestDetailS5OrS8List(fdIdList);
//            log.info("requestDetailList : "+requestDetailList);
                for (RequestDetail requestDetail : requestDetailList) {
//                log.info("가져온 frID 값 : "+requestDetailList.get(i).getFrId());
                    requestDetail.setFdPreState(stateType); // 이전상태 값
                    requestDetail.setFdPreStateDt(LocalDateTime.now());
                    requestDetail.setFdState("S6");
                    requestDetail.setFdStateDt(LocalDateTime.now());

                    requestDetail.setFdS6Dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    requestDetail.setFdS6Time(LocalDateTime.now());

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
        List<RequestDetailCloseListDto> requestDetailCloseListDtos = requestDetailRepositoryCustom.findByRequestDetailCloseList(frCode);
        List<Long> fdIdList = new ArrayList<>();
        for(RequestDetailCloseListDto requestDetailCloseListDto : requestDetailCloseListDtos){
            fdIdList.add(requestDetailCloseListDto.getFdId());
        }
        List<InspeotYnDto> inspeotYnDtos = inspeotRepositoryCustom.findByInspeotStateList(fdIdList,"1");

        data.put("gridListData",requestDetailCloseListDtos);
        data.put("removeFrId",inspeotYnDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
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
        List<RequestDetailFranchiseInListDto> requestDetailFranchiseInListDtos = requestDetailRepositoryCustom.findByRequestDetailFranchiseInList(frCode);
        data.put("gridListData",requestDetailFranchiseInListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  지사반송 - 세부테이블 지사반송 상태 리스트
    public ResponseEntity<Map<String, Object>> franchiseReceiptReturnList(HttpServletRequest request) {
        log.info("franchiseReceiptReturnList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 지사반송 페이지에 보여줄 리스트 호출
        List<RequestDetailReturnListDto> requestDetailFranchiseInListDtos = requestDetailRepositoryCustom.findByRequestDetailReturnList(frCode);
        data.put("gridListData",requestDetailFranchiseInListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  가맹점강제입고 - 세부테이블 강제출고 상태 리스트
    public ResponseEntity<Map<String, Object>> franchiseReceiptForceList(HttpServletRequest request) {
        log.info("franchiseReceiptForceList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        // 가맹점강제입고 페이지에 보여줄 리스트 호출
        List<RequestDetailForceListDto> requestDetailForceListDtos = requestDetailRepositoryCustom.findByRequestDetailForceList(frCode);
        List<Long> fdIdList = new ArrayList<>();
        for(RequestDetailForceListDto requestDetailForceListDto : requestDetailForceListDtos){
            fdIdList.add(requestDetailForceListDto.getFdId());
        }
//        log.info("fdIdList : "+fdIdList);
        List<InspeotYnDto> inspeotYnDtos = inspeotRepositoryCustom.findByInspeotStateList(fdIdList,"2");
//        log.info("inspeotYnDtos : "+inspeotYnDtos);
        fdIdList.clear();
        for(InspeotYnDto inspeotYnDto : inspeotYnDtos){
            fdIdList.add(inspeotYnDto.getFdId());
        }
        data.put("gridListData",requestDetailForceListDtos);
        data.put("checkFdId",fdIdList);

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
        List<RequestDetailDeliveryDto> requestDetailDeliveryDtos = requestDetailRepositoryCustom.findByRequestDetailDeliveryList(frCode, bcId);
        data.put("gridListData",requestDetailDeliveryDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
