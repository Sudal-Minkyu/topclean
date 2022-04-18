package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerMessageListDto;
import com.broadwave.toppos.User.Customer.CustomerRepository;
import com.broadwave.toppos.User.Template.Template;
import com.broadwave.toppos.User.Template.TemplateDto;
import com.broadwave.toppos.User.Template.TemplateRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-04-15
 * Time :
 * Remark : Toppos 가맹점 문자메세지 보내기 서비스
 */
@Slf4j
@Service
public class TemplateService {

    private final TokenProvider tokenProvider;

    private final CustomerRepository customerRepository;
    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(TokenProvider tokenProvider, CustomerRepository customerRepository, TemplateRepository templateRepository){
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
        this.templateRepository = templateRepository;
    }

    // 메세지 보낼 고객 리스트 호출
    public ResponseEntity<Map<String, Object>> messageCustomerList(String visitDayRange, HttpServletRequest request) {
        log.info("messageCustomerList 호출");

        log.info("visitDayRange : "+visitDayRange);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String bcLastRequestDt = null;

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
        log.info("bcLastRequestDt : "+bcLastRequestDt);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 가맹점 코드 : "+frCode);

        List<CustomerMessageListDto> customerMessageListDtos = customerRepository.findByMessageCustomerList(visitDayRange, bcLastRequestDt, frCode);
        log.info("customerMessageListDtos : "+customerMessageListDtos);
        data.put("gridListData",customerMessageListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 문자 메시지 보내기
    public ResponseEntity<Map<String, Object>> messageSendCustomer(List<Long> bcIdList, String fmMessage, Timestamp fmSendreqtimeDt, HttpServletRequest request) {
        log.info("messageSendCustomer 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("bcIdList : "+bcIdList);
        log.info("fmMessage : "+fmMessage);
        log.info("fmSendreqtimeDt : "+fmSendreqtimeDt);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 가맹점 코드 : "+frCode);

        return ResponseEntity.ok(res.success());
    }

    // 메세지 템플릿 6개 저장
    public ResponseEntity<Map<String, Object>> templateSave(List<TemplateDto> templateDtos, HttpServletRequest request) {
        log.info("templateSave 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("templateDtos : "+templateDtos);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 가맹점 코드 : "+frCode);

        List<Template> templateList = new ArrayList<>();
        Template template;
        for(TemplateDto templateDto : templateDtos){
            template = new Template();
            if(templateDto.getFtId() != 0){
                Optional<Template> optionalTemplate = templateRepository.findById(templateDto.getFtId());
                if(optionalTemplate.isPresent()){
                    optionalTemplate.get().setFmMessage(templateDto.getFmMessage());
                    optionalTemplate.get().setFmSubject(templateDto.getFmSubject());
                    template = optionalTemplate.get();
                }else{
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "수정 할 "+ ResponseErrorCode.TP022.getDesc(), "다시 ", "다시 수정 할 템플릿을 확인해주세요"));
                }
            }else{
                template.setFmNum(templateDto.getFmNum());
                template.setFmSubject(templateDto.getFmSubject());
                template.setFmMessage(templateDto.getFmMessage());
                template.setFrCode(frCode);
                template.setInsert_id(login_id);
                template.setInsertDateTime(LocalDateTime.now());
            }

            templateList.add(template);
        }

        templateRepository.saveAll(templateList);

        return ResponseEntity.ok(res.success());
    }

    // 메세지 템플릿 6개 호출
    public ResponseEntity<Map<String, Object>> templateList(HttpServletRequest request) {
        log.info("templateList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 가맹점 코드 : "+frCode);

        List<TemplateDto> templateDtos = templateRepository.findByTemplateDtos(frCode);
        log.info("templateDtos : "+templateDtos);
        data.put("gridListData",templateDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }
}