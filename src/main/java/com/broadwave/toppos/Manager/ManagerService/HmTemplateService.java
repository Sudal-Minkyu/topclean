package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.HmTemplate.HmTemplate;
import com.broadwave.toppos.Manager.HmTemplate.HmTemplateDto;
import com.broadwave.toppos.Manager.HmTemplate.HmTemplateRepository;
import com.broadwave.toppos.User.Customer.Customer;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerMessageListDto;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory.MessageHistory;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.MessageHistory.MessageHistoryRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-04-26
 * Time :
 * Remark : Toppos 지사 문자메세지 보내기 서비스
 */
@Slf4j
@Service
public class HmTemplateService {

    @Value("${toppos.templatecode.number}")
    private String templatecodeNumber;

    private final TokenProvider tokenProvider;

    private final CustomerRepository customerRepository;
    private final HmTemplateRepository hmTemplateRepository;
    private final RequestRepository requestRepository;
    private final MessageHistoryRepository messageHistoryRepository;

    @Autowired
    public HmTemplateService(TokenProvider tokenProvider, CustomerRepository customerRepository, HmTemplateRepository hmTemplateRepository,
                             RequestRepository requestRepository, MessageHistoryRepository messageHistoryRepository){
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
        this.hmTemplateRepository = hmTemplateRepository;
        this.requestRepository = requestRepository;
        this.messageHistoryRepository = messageHistoryRepository;
    }

    // 메세지 보낼 고객 리스트 호출
    public ResponseEntity<Map<String, Object>> messageCustomerList(String visitDayRange, Long franchiseId, Long branchId, HttpServletRequest request) {
        log.info("지사용 messageCustomerList 호출");

        log.info("visitDayRange : "+visitDayRange);
        log.info("franchiseId : "+franchiseId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String bcLastRequestDt = null;

        // visitDayRange : 0 (전체), 1 (1주일전 방문), 2 (1개월전 방문), 3 (3개월전 방문), 4 (6개월전 방문)
        Calendar calendar = Calendar.getInstance();
        if(!visitDayRange.equals("0")){
            switch (visitDayRange) {
                case "1":
                    log.info("일주일전");
                    calendar.add(Calendar.DATE, -7);
                    break;
                case "2":
                    log.info("1개월전");
                    calendar.add(Calendar.MONTH, -1);
                    break;
                case "3":
                    log.info("3개월전");
                    calendar.add(Calendar.MONTH, -3);
                    break;
                default:
                    log.info("6개월전");
                    calendar.add(Calendar.MONTH, -6);
                    break;
            }
            bcLastRequestDt = new java.text.SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        }
//        log.info("bcLastRequestDt : "+bcLastRequestDt);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String brCode;
        if(branchId != null){
            brCode = "hr";
        }else{
            brCode = (String) claims.get("brCode");
        }
//        log.info("현재 접속한 아이디 : "+login_id);
//        log.info("접속한 지사 코드 : "+brCode);

        List<CustomerMessageListDto> customerMessageListDtos = customerRepository.findByBrMessageCustomerList(visitDayRange, bcLastRequestDt, franchiseId, branchId, brCode);
//        log.info("customerMessageListDtos : "+customerMessageListDtos);
        data.put("gridListData",customerMessageListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 문자 메시지 보내기
    @Transactional
    public ResponseEntity<Map<String, Object>> messageSendCustomer(String type, List<Long> bcIdList, String hmMessage, String hmSendreqtimeDt, String msgType, HttpServletRequest request) {
        log.info("messageSendCustomer 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("bcIdList : "+bcIdList);
        log.info("hmMessage : "+hmMessage);
        log.info("hmSendreqtimeDt : "+hmSendreqtimeDt);
        log.info("msgType : "+msgType);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String brCode;
        log.info("현재 접속한 아이디 : "+login_id);
        if(type.equals("2")){
            brCode = (String) claims.get("brCode"); // 현재 지사 코드
            log.info("접속한 지사 코드 : "+brCode);
        }else{
            brCode = "hr";
        }

        LocalDateTime sendreqTime; // 예약발송시간;
        if(!hmSendreqtimeDt.equals("0")){
//            Timestamp sendreqStr = Timestamp.valueOf(String.valueOf(fmSendreqtimeDt));
//            long systemTimeMills = System.currentTimeMillis();
            sendreqTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(hmSendreqtimeDt)), TimeZone.getDefault().toZoneId());
        }else{
            sendreqTime = null;
        }
        log.info("sendreqTime : "+sendreqTime);

        List<MessageHistory> messageHistorieList = new ArrayList<>();
        MessageHistory messageHistory;

        List<String> bcHpList = new ArrayList<>();

        List<Customer> customerList = customerRepository.findByCustmerList(bcIdList);
        for(Customer customer : customerList){
            messageHistory = new MessageHistory();
            messageHistory.setBcId(customer);
            messageHistory.setFmType("05");
            messageHistory.setFrCode(null);
            messageHistory.setBrCode(brCode);
            messageHistory.setFmMessage(hmMessage);
            if(sendreqTime == null){
                messageHistory.setFmSendreqtimeDt(LocalDateTime.now());
            }else{
                messageHistory.setFmSendreqtimeDt(sendreqTime);
            }
            messageHistory.setInsertYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            messageHistory.setInsert_id(login_id);
            messageHistory.setInsertDateTime(LocalDateTime.now());

            bcHpList.add(customer.getBcHp());
            messageHistorieList.add(messageHistory);
        }

        List<MessageHistory> saveMessageHistorieList = messageHistoryRepository.saveAll(messageHistorieList);

        // 문자메세지 보내기
        boolean result = requestRepository.smsMessageBatchInsert(hmMessage, bcHpList, "fs_message_history", saveMessageHistorieList, templatecodeNumber, msgType, sendreqTime);
        log.info("result : "+result);

        return ResponseEntity.ok(res.success());
    }

    // 메세지 템플릿 6개 저장
    public ResponseEntity<Map<String, Object>> hmTemplateSave(String type, List<HmTemplateDto> hmTemplateDtos, HttpServletRequest request) {
        log.info("hmTemplateSave 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("hmTemplateDtos : "+hmTemplateDtos);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String brCode;
        if(type.equals("1")){
            brCode = "hr";
        }else{
            brCode = (String) claims.get("brCode"); // 현재 지사 코드
        }
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 지사 코드 : "+brCode);

        List<HmTemplate> hmTemplateList = new ArrayList<>();
        HmTemplate hmTmplate;
        for(HmTemplateDto hmTemplateDto : hmTemplateDtos){
            hmTmplate = new HmTemplate();
            if(hmTemplateDto.getHmId() != 0){
                Optional<HmTemplate> optionalHmTemplate = hmTemplateRepository.findById(hmTemplateDto.getHmId());
                if(optionalHmTemplate.isPresent()){
                    optionalHmTemplate.get().setHmMessage(hmTemplateDto.getHmMessage());
                    optionalHmTemplate.get().setHmSubject(hmTemplateDto.getHmSubject());
                    hmTmplate = optionalHmTemplate.get();
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "수정 할 "+ ResponseErrorCode.TP022.getDesc(), "다시 ", "다시 수정 할 템플릿을 확인해주세요"));
                }
            }else{
                hmTmplate.setHmNum(hmTemplateDto.getHmNum());
                hmTmplate.setHmSubject(hmTemplateDto.getHmSubject());
                hmTmplate.setHmMessage(hmTemplateDto.getHmMessage());
                hmTmplate.setBrCode(brCode);
                hmTmplate.setInsert_id(login_id);
                hmTmplate.setInsertDateTime(LocalDateTime.now());
            }

            hmTemplateList.add(hmTmplate);
        }

        hmTemplateRepository.saveAll(hmTemplateList);

        return ResponseEntity.ok(res.success());
    }

    // 메세지 템플릿 6개 호출
    public ResponseEntity<Map<String, Object>> hmTemplateList(String type, HttpServletRequest request) {
        log.info("hmTemplateList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String brCode;
        if(type.equals("1")){
            log.info("본사 템플릿 호출");
            brCode = "hr";
        }else{
            log.info("지사 템플릿 호출");
            // 클레임데이터 가져오기
            Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
            String login_id = claims.getSubject(); // 현재 아이디
            brCode = (String) claims.get("brCode"); // 현재 가맹점 코드
            log.info("현재 접속한 아이디 : "+login_id);
            log.info("접속한 지사 코드 : "+brCode);
        }

        List<HmTemplateDto> hmTemplateDtos = hmTemplateRepository.findByHmTemplateDtos(brCode);
//        log.info("hmTemplateDtos : "+hmTemplateDtos);
        data.put("gridListData",hmTemplateDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
