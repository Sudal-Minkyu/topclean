package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Payment.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Request;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.Photo;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Photo.PhotoRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoney;
import com.broadwave.toppos.User.ReuqestMoney.SaveMoney.SaveMoneyRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    private final AWSS3Service awss3Service;
    private final PaymentRepository paymentRepository;
    private final PaymentRepositoryCustom paymentRepositoryCustom;
    private final PhotoRepository photoRepository;
    private final InspeotRepository inspeotRepository;
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final RequestRepository requestRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;
    private final SaveMoneyRepository saveMoneyRepository;

    @Autowired
    public ReceiptStateService(ModelMapper modelMapper, TokenProvider tokenProvider, UserService userService, AWSS3Service awss3Service, PhotoRepository photoRepository,
                               PaymentRepository paymentRepository, PaymentRepositoryCustom paymentRepositoryCustom, InspeotRepository inspeotRepository,
                               RequestRepository requestRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom, InspeotRepositoryCustom inspeotRepositoryCustom,
                               RequestDetailRepository requestDetailRepository, SaveMoneyRepository saveMoneyRepository){
        this.modelMapper = modelMapper;
        this.inspeotRepository = inspeotRepository;
        this.awss3Service = awss3Service;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.photoRepository = photoRepository;
        this.requestRepository = requestRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.paymentRepository = paymentRepository;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
        this.paymentRepositoryCustom = paymentRepositoryCustom;
        this.saveMoneyRepository = saveMoneyRepository;
    }

    //  접수테이블의 상태 변화 API - 수기마감페이지, 가맹점입고 페이지, 지사반송건전송 페이지, 세탁인도 페이지 공용함수
    public ResponseEntity<Map<String, Object>> franchiseStateChange(List<Long> fdIdList, String stateType, HttpServletRequest request) {
        log.info("franchiseStateChange 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // stateType 상태값
        // "S1"이면 수기마감페이지 버튼 "S1" -> "S2"
        // "S4"이면 가맹점입고 페이지 버튼 "S4" -> "S5"
        // "S3"이면 지사반송페이지 버튼 "S3" -> "S2"
        // "S7"이면 가맹점강제입고 페이지 버튼 "S7" -> "S8"
        if(stateType.equals("S1")){ // 수기마감

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
//        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
//        log.info("소속된 지사 코드 : "+frbrCode);

        // 수기마감 페이지에 보여줄 리스트 호출
        List<RequestDetailCloseListDto> requestDetailCloseListDtos = requestDetailRepositoryCustom.findByRequestDetailCloseList(frCode);
        List<Long> fdIdList = new ArrayList<>();
        for(RequestDetailCloseListDto requestDetailCloseListDto : requestDetailCloseListDtos){
            fdIdList.add(requestDetailCloseListDto.getFdId());
        }
        List<InspeotYnDto> inspeotYnDtos = inspeotRepositoryCustom.findByInspeotClosingList(fdIdList);

        data.put("gridListData",requestDetailCloseListDtos);
        data.put("removeFrId",inspeotYnDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

//    //  통합조회용 - 접수세부 테이블
//    public ResponseEntity<Map<String, Object>> franchiseRequestDetailSearch(Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt, HttpServletRequest request) {
//        log.info("franchiseRequestDetailSearch 호출");
////        log.info("고객ID bcId : "+bcId);
////        log.info("택번호 searchTag : "+searchTag);
////        log.info("조회타입 filterCondition : "+filterCondition);
////        log.info("시작 접수일자 filterFromDt : "+filterFromDt);
////        log.info("종료 접수일자 filterToDt : "+filterToDt);
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//
//        List<RequestDetailSearchDto> requestDetailSearchDtoList = requestDetailSearch(frCode, bcId, searchTag, filterCondition, filterFromDt, filterToDt); //  통합조회용 - 접수세부 호출
//
//        List<Long> frIdList = new ArrayList<>();
//        List<Long> fdIdList = new ArrayList<>();
//        for(int i=0; i<requestDetailSearchDtoList.size(); i++){
//            frIdList.add(requestDetailSearchDtoList.get(i).getFrId());
//            fdIdList.add(requestDetailSearchDtoList.get(i).getFdId());
//        }
//
//        // 결제 취소여부 리스트 호출
//        List<PaymentCencelYnDto> paymentCencelYnDtoList = paymentRepositoryCustom.findByPaymentCancelYn(frIdList);
//        List<Long> cencelList = new ArrayList<>();
//        for(int i=0; i<paymentCencelYnDtoList.size(); i++){
//            cencelList.add(paymentCencelYnDtoList.get(i).getFrId());
//        }
//
//        // 검품 등록여부 리스트 호출
//        List<InspeotYnDto> inspeotYnDtoFList = inspeotRepositoryCustom.findByInspeotYnF(fdIdList); // 가맹검품 여부
//        List<InspeotYnDto> inspeotYnDtoBList = inspeotRepositoryCustom.findByInspeotYnB(fdIdList); // 지사검품(확인품) 여부
//        List<Long> inspeotListF = new ArrayList<>();
//        List<Long> inspeotListB = new ArrayList<>();
//        for(int i=0; i<inspeotYnDtoFList.size(); i++){
//            inspeotListF.add(inspeotYnDtoFList.get(i).getFdId());
//        }
//        for(int i=0; i<inspeotYnDtoBList.size(); i++){
//            inspeotListB.add(inspeotYnDtoBList.get(i).getFdId());
//        }
//
//        // 조회 리스트
//        List<RequestDetailSearchDtoSub> requestDetailSearchDtoSubList = new ArrayList<>();
//        for(RequestDetailSearchDto requestDetailSearchDto : requestDetailSearchDtoList){
//            RequestDetailSearchDtoSub requestDetailSearchDtoSub = modelMapper.map(requestDetailSearchDto,RequestDetailSearchDtoSub.class);
//            requestDetailSearchDtoSub.setFpCancelYn("Y");
//            requestDetailSearchDtoSubList.add(requestDetailSearchDtoSub);
//        }
//
//        data.put("gridListData",requestDetailSearchDtoSubList);
//        data.put("cencelList",cencelList);
//        data.put("inspeotListF",inspeotListF);
//        data.put("inspeotListB",inspeotListB);
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }
//
//    //  통합조회용 - 접수세부 호출용 함수
//    private List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt) {
//        return requestDetailRepositoryCustom.requestDetailSearch(frCode, bcId, searchTag, filterCondition, filterFromDt, filterToDt);
//    }
//
//    //  통합조회용 - 접수 세부테이블 수정
//    @Transactional
//    public ResponseEntity<Map<String, Object>> franchiseRequestDetailUpdate(RequestDetailUpdateDto requestDetailUpdateDto, HttpServletRequest request) {
//        log.info("franchiseRequestDetailUpdate 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//
//        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(requestDetailUpdateDto.getFdId());
//        if(!optionalRequestDetail.isPresent()){
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "수정 할 "+ ResponseErrorCode.TP022.getDesc(), null,null));
//        }else{
//            Integer nowFdTotAmt = optionalRequestDetail.get().getFdTotAmt();
//            Integer updateFdTotAmt = requestDetailUpdateDto.getFdTotAmt();
//
//            // RequestDetail 수정 시작
//            optionalRequestDetail.get().setFdTotAmt(updateFdTotAmt);
//
//            optionalRequestDetail.get().setFdColor(requestDetailUpdateDto.getFdColor());
//            optionalRequestDetail.get().setFdPattern(requestDetailUpdateDto.getFdPattern());
//            optionalRequestDetail.get().setFdPriceGrade(requestDetailUpdateDto.getFdPriceGrade());
//            optionalRequestDetail.get().setFdOriginAmt(requestDetailUpdateDto.getFdOriginAmt());
//            optionalRequestDetail.get().setFdNormalAmt(requestDetailUpdateDto.getFdNormalAmt());
//
//            optionalRequestDetail.get().setFdAdd2Amt(requestDetailUpdateDto.getFdAdd2Amt());
//            optionalRequestDetail.get().setFdAdd2Remark(requestDetailUpdateDto.getFdAdd2Remark());
//            optionalRequestDetail.get().setFdRepairRemark(requestDetailUpdateDto.getFdRepairRemark());
//            optionalRequestDetail.get().setFdRepairAmt(requestDetailUpdateDto.getFdRepairAmt());
//            optionalRequestDetail.get().setFdSpecialYn(requestDetailUpdateDto.getFdSpecialYn());
//
//            optionalRequestDetail.get().setFdAdd1Amt(requestDetailUpdateDto.getFdAdd1Amt());
//            optionalRequestDetail.get().setFdAdd1Remark(requestDetailUpdateDto.getFdAdd1Remark());
//            optionalRequestDetail.get().setFdPressed(requestDetailUpdateDto.getFdPressed());
//            optionalRequestDetail.get().setFdWhitening(requestDetailUpdateDto.getFdWhitening());
//            optionalRequestDetail.get().setFdPollution(requestDetailUpdateDto.getFdPollution());
//
//            optionalRequestDetail.get().setFdPollutionLevel(requestDetailUpdateDto.getFdPollutionLevel());
//            optionalRequestDetail.get().setFdStarch(requestDetailUpdateDto.getFdStarch());
//            optionalRequestDetail.get().setFdWaterRepellent(requestDetailUpdateDto.getFdWaterRepellent());
//            optionalRequestDetail.get().setFdDiscountGrade(requestDetailUpdateDto.getFdDiscountGrade());
//            optionalRequestDetail.get().setFdDiscountAmt(requestDetailUpdateDto.getFdDiscountAmt());
//
//            optionalRequestDetail.get().setFdQty(requestDetailUpdateDto.getFdQty());
//            optionalRequestDetail.get().setFdRequestAmt(requestDetailUpdateDto.getFdRequestAmt());
//            optionalRequestDetail.get().setFdRetryYn(requestDetailUpdateDto.getFdRetryYn());
//            optionalRequestDetail.get().setFdUrgentYn(requestDetailUpdateDto.getFdUrgentYn());
//            optionalRequestDetail.get().setFdRemark(requestDetailUpdateDto.getFdRemark());
//
//            optionalRequestDetail.get().setModify_id(login_id);
//            optionalRequestDetail.get().setModify_date(LocalDateTime.now());
//            RequestDetail requestDetailSave = optionalRequestDetail.get();
////            log.info("requestDetailSave : "+requestDetailSave);
//
//            if(nowFdTotAmt>updateFdTotAmt){
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP023.getCode(), ResponseErrorCode.TP023.getDesc(), "문자","수정 금액(전/후) : "+nowFdTotAmt+" / "+updateFdTotAmt));
//            }else if(nowFdTotAmt.equals(updateFdTotAmt)){
//                // 업데이트만 실행
//                requestDetailRepository.save(requestDetailSave);
//            }else{
//                // 업데이트 실행 후 마스터테이블 수정 함수호출
//                requestDetailRepository.save(requestDetailSave);
//
//                // 만약 금액이 변동되었을 시 수정후, 마스터테이블도 업데이트하기 (가격이 높아졌을때만 시행, 작아지면 리턴처리)
//                userService.requestDetailUpdateFromMasterUpdate(requestDetailUpdateDto.getFrNo(), frCode);
//            }
//
//        }
//
//        return ResponseEntity.ok(res.success());
//
//    }
//
//    //  통합조회용 - 결제취소 전 결제내역리스트 요청
//    public ResponseEntity<Map<String, Object>> franchiseDetailCencelDataList(Long frId, HttpServletRequest request) {
//        log.info("franchiseDetailCencelDataList 호출");
//
//        log.info("접수마스터 ID frId : "+frId);
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//
//        List<PaymentCencelDto> paymentCencelDtoList = paymentRepositoryCustom.findByRequestDetailCencelDataList(frCode, frId);
////        log.info("paymentCencelDtoList : "+paymentCencelDtoList);
//        data.put("gridListData",paymentCencelDtoList);
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }
//
//    //  통합조회용 - 결제취소 요청
//    public ResponseEntity<Map<String, Object>> franchiseRequestDetailCencel(Long fpId, String type, HttpServletRequest request) {
//        log.info("franchiseRequestDetailCencel 호출");
//
//        log.info("결제 ID fpId : "+fpId);
//        log.info("적립급/취소 타입 : "+type);
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//
//        Optional<Payment> optionalPayment = paymentRepository.findById(fpId);
//        if(!optionalPayment.isPresent()) {
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "적립금 전환 " + ResponseErrorCode.TP022.getDesc(), null, null));
//        }else{
//            if(type.equals("1")){
//                Optional<Request> optionalRequest = requestRepository.findById(optionalPayment.get().getFrId().getId());
//                if(!optionalRequest.isPresent()){
//                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "결제 취소 할 " + ResponseErrorCode.TP022.getDesc(), null, null));
//                }else{
//                    log.info("결제 취소합니다.");
//                    optionalPayment.get().setFpCancelYn("Y");
//                    paymentRepository.save(optionalPayment.get());
//
//                    // 마스터테이블의 계산가격을 업데이트한다.
//                    optionalRequest.get().setFrPayAmount(optionalRequest.get().getFrPayAmount()-optionalPayment.get().getFpAmt());
//                    optionalRequest.get().setFrUncollectYn("Y");
//                    optionalRequest.get().setModify_id(login_id);
//                    optionalRequest.get().setModify_date(LocalDateTime.now());
//                    requestRepository.save(optionalRequest.get());
//                }
//            }else{
//                log.info("적립금으로 전환합니다.");
//                optionalPayment.get().setFpCancelYn("Y");
//                optionalPayment.get().setFpSavedMoneyYn("Y");
//                paymentRepository.save(optionalPayment.get());
//
//                SaveMoney saveMoney = new SaveMoney();
//                saveMoney.setBcId(optionalPayment.get().getBcId());
//                saveMoney.setFpId(optionalPayment.get());
//                saveMoney.setFsType("1");
//                saveMoney.setFsClose("N");
//                saveMoney.setFsAmt(optionalPayment.get().getFpAmt());
//                saveMoney.setInsert_id(login_id);
//                saveMoney.setInsert_date(LocalDateTime.now());
//                saveMoneyRepository.save(saveMoney);
//            }
//        }
//
//        return ResponseEntity.ok(res.success());
//    }
//
//    // 검품등록 API(가맹점, 지사)
//    public ResponseEntity<Map<String, Object>> franchiseInspectionSave(InspeotMapperDto inspeotMapperDto, MultipartHttpServletRequest multi, String AWSBUCKETURL) throws NoSuchElementException {
//        log.info("franchiseInspectionSave 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(multi.getHeader("Authorization"));
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//
//        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(inspeotMapperDto.getFdId());
//        if(!optionalRequestDetail.isPresent()){
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"검풍등록 할 "+ResponseErrorCode.TP024.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
//        }else{
//
//            Inspeot inspeot = new Inspeot();
//            inspeot.setFdId(optionalRequestDetail.get());
//            inspeot.setFrCode(optionalRequestDetail.get().getFrId().getFrCode());
//            inspeot.setBrCode(optionalRequestDetail.get().getFrId().getBcCode());
//            inspeot.setFiType(inspeotMapperDto.getFiType());
//            inspeot.setFiComment(inspeotMapperDto.getFiComment());
//            inspeot.setFiAddAmt(inspeotMapperDto.getFiAddAmt());
//            inspeot.setFiPhotoYn("N");
//            inspeot.setFiSendMsgYn("N");
//            inspeot.setFiCustomerConfirm("1");
//            // 밑에 주석 값은 널로 등록한다.
////            inspeot.setFiProgressStateDt(); -> null;
////            inspeot.setFiMessage(); -> null;
////            inspeot.setFiMessageSendDt(); -> null;
//            inspeot.setInsert_id(login_id);
//            inspeot.setInsertDateTime(LocalDateTime.now());
//
//            //파일저장
//            try{
//                if(multi.getFiles("source").get(0).getSize() != 0){
//                    log.info("사진 포함 등록");
//
//                    inspeot.setFiPhotoYn("Y");
//                    Inspeot saveInspeot = inspeotRepository.save(inspeot);
//
//                    Iterator<String> files = multi.getFileNames();
//                    String uploadFile = files.next();
//                    MultipartFile mFile = multi.getFile(uploadFile);
//
//                    assert mFile != null;
//                    if(!mFile.isEmpty()) {
//                        Photo photo = new Photo();
//
//                        // 파일 중복명 처리
//                        String genId = UUID.randomUUID().toString().replace("-", "");
//
//                        // S3에 저장 할 파일주소
//                        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
//                        String filePath;
//                        if(inspeotMapperDto.getFiType().equals(("F"))){
//                            photo.setFfType("02");
//                            filePath = "/toppos-franchise-inspection/" + date.format(new Date());
//                        }else{
//                            photo.setFfType("03");
//                            filePath = "/toppos-manager-inspection/" + date.format(new Date());
//                        }
//
//                        String storedFileName = genId + ".png";
//                        String ffFilename = awss3Service.uploadObject(mFile, storedFileName, filePath);
//
//                        photo.setFiId(saveInspeot);
//                        photo.setFfPath(AWSBUCKETURL+filePath+"/");
//                        photo.setFfFilename(ffFilename);
//                        photo.setInsert_id(login_id);
//                        photo.setInsertDateTime(LocalDateTime.now());
//                        photoRepository.save(photo);
//                    }else{
//                        log.info("사진파일을 못불러왔습니다.");
//                    }
//
//                }
//            }catch (Exception e){
//                inspeotRepository.save(inspeot);
//                log.info(e+" -> 사진 미포함 등록");
//            }
//        }
//
//        return ResponseEntity.ok(res.success());
//
//    }
//
//    //  통합조회용 - 등록 검품 삭제
//    @Transactional
//    public ResponseEntity<Map<String, Object>> franchiseInspectionDelete(InspeotSet inspeotSet) {
//        log.info("franchiseInspectionDelete 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        ArrayList<InspeotDto> list = inspeotSet.getList();
//
//        if(list.size() != 0){
//            List<Long> inspeotDeleteList = new ArrayList<>();
//            List<Long> photoDeleteList = new ArrayList<>();
//            for(InspeotDto inspeotDto : list){
//                inspeotDeleteList.add(inspeotDto.getFiId());
//            }
//
//            List<InspeotDto> inspeotDtos = inspeotRepositoryCustom.findByInspeotDtoList(inspeotDeleteList);
//            for(InspeotDto inspeotDto : inspeotDtos){
//                if(inspeotDto.getFiPhotoYn().equals("Y")){
//                    photoDeleteList.add(inspeotDto.getFiId());
//                }
//            }
//
//            try {
//                inspeotRepository.findByInspectDelete(inspeotDeleteList);
//                photoRepository.findByInspectPhotoDelete(photoDeleteList);
//            }catch (Exception e){
//                log.info("예외 발생 : "+e);
//            }
//        }
//
//        return ResponseEntity.ok(res.success());
//    }
//
//    //  통합조회용 - 검품 리스트 요청
//    public ResponseEntity<Map<String, Object>> franchiseInspectionList(Long fdId, String type) {
//        log.info("franchiseInspectionList 호출");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        List<InspeotListDto> inspeotList = inspeotRepositoryCustom.findByInspeotList(fdId, type);
//        data.put("gridListData",inspeotList);
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }
//
//    //  통합조회용 - 접수 취소
//    public ResponseEntity<Map<String, Object>> franchiseReceiptCancel(Long fdId, HttpServletRequest request) {
//        log.info("franchiseReceiptCancel 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//
//        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(fdId);
//        if(!optionalRequestDetail.isPresent()){
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"접수취소 할 "+ResponseErrorCode.TP024.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
//        }else{
//            optionalRequestDetail.get().setFdCancel("Y");
//            optionalRequestDetail.get().setFdCacelDt(LocalDateTime.now());
//            optionalRequestDetail.get().setModify_id(login_id);
//            optionalRequestDetail.get().setModify_date(LocalDateTime.now());
//            requestDetailRepository.save(optionalRequestDetail.get());
//        }
//
//        return ResponseEntity.ok(res.success());
//    }
//
//    //  통합조회용 - 인도 취소
//    public ResponseEntity<Map<String, Object>> franchiseLeadCancel(Long fdId, HttpServletRequest request) {
//        log.info("franchiseLeadCancel 호출");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//
//        String fdState;
//        String fdPreState;
//        Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(fdId);
//        if(!optionalRequestDetail.isPresent()){
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"인도취소 할 "+ResponseErrorCode.TP024.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
//        }else{
//            fdState =  optionalRequestDetail.get().getFdState(); // 바뀌기 전 현재상태
//            if(fdState.equals("S6")){
//                fdPreState =  optionalRequestDetail.get().getFdPreState(); // 바뀌기 전 이전상태
//                optionalRequestDetail.get().setFdState(fdPreState);
//                optionalRequestDetail.get().setFdStateDt(LocalDateTime.now());
//                optionalRequestDetail.get().setFdPreState(fdState);
//                optionalRequestDetail.get().setFdPreStateDt(LocalDateTime.now());
//                optionalRequestDetail.get().setModify_id(login_id);
//                optionalRequestDetail.get().setModify_date(LocalDateTime.now());
//                requestDetailRepository.save(optionalRequestDetail.get());
//            }else{
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP024.getCode(),"해당상품은 인도취소한 상품입니다.", "문자", "재조회 후 다시 시도해주세요."));
//            }
//        }
//
//        data.put("fdPreState",fdPreState);
//
//        return ResponseEntity.ok(res.success());
//    }
//
//    //  통합조회용 - 검품 고객 수락/거부
//    @Transactional
//    public ResponseEntity<Map<String, Object>> franchiseInspectionYn(Long fiId, String type, Integer fiAddAmt, HttpServletRequest request) {
//        log.info("franchiseInspectionYn 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String login_id = claims.getSubject(); // 현재 아이디
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        log.info("현재 접속한 아이디 : "+login_id);
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//
//        Optional<Inspeot> optionalInspeot = inspeotRepository.findById(fiId);
//        if(!optionalInspeot.isPresent()){
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"검품 수락및거부 할"+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
//        }else{
//            optionalInspeot.get().setFiProgressStateDt(LocalDateTime.now());
//            optionalInspeot.get().setModify_id(login_id);
//            optionalInspeot.get().setModify_date(LocalDateTime.now());
//
//            if(type.equals("2")){
//                log.info("검품 수락 ID : "+fiId);
//                optionalInspeot.get().setFiCustomerConfirm("2");
//
//                Optional<RequestDetail> optionalRequestDetail = requestDetailRepository.findById(optionalInspeot.get().getFdId().getId());
//                if(!optionalRequestDetail.isPresent()){
//                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"업데이트 할 "+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
//                }else{
//                    optionalRequestDetail.get().setFdAdd2Amt(optionalRequestDetail.get().getFdAdd2Amt()+fiAddAmt);
//                    optionalRequestDetail.get().setFdTotAmt(optionalRequestDetail.get().getFdTotAmt()+fiAddAmt);
//                    optionalRequestDetail.get().setModify_id(login_id);
//                    optionalRequestDetail.get().setModify_date(LocalDateTime.now());
//
//                    Optional<Request> optionalRequest = requestRepository.request(optionalRequestDetail.get().getFrNo(), frCode);
//                    if(!optionalRequest.isPresent()){
//                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(),"업데이트 할 "+ResponseErrorCode.TP022.getDesc(), "문자", "재조회 후 다시 시도해주세요."));
//                    }else {
//                        Integer frTotalAmount = optionalRequest.get().getFrTotalAmount()+fiAddAmt;
//                        optionalRequest.get().setFrTotalAmount(frTotalAmount);
//                        if(frTotalAmount <= optionalRequest.get().getFrPayAmount()){
//                            optionalRequest.get().setFrUncollectYn("N");
//                        }else{
//                            optionalRequest.get().setFrUncollectYn("Y");
//                        }
//                        optionalRequest.get().setModify_id(login_id);
//                        optionalRequest.get().setModify_date(LocalDateTime.now());
//
//                        inspeotRepository.save(optionalInspeot.get());
//                        requestDetailRepository.save(optionalRequestDetail.get());
//                        requestRepository.save(optionalRequest.get());
//                    }
//                }
//            }else{
//                log.info("검품 거부 ID : "+fiId);
//                optionalInspeot.get().setFiCustomerConfirm("3");
//                inspeotRepository.save(optionalInspeot.get());
//            }
//        }
//
//        return ResponseEntity.ok(res.success());
//    }


}
