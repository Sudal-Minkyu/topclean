package com.broadwave.toppos.Manager;

import com.broadwave.toppos.Account.*;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendar;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarDto;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarListDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos Manager RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/manager") //  ( 권한 : 지사일반, 지사장 )
public class ManagerRestController {

    private final AccountService accountService;
    private final ManagerService managerService;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    @Autowired
    public ManagerRestController(AccountService accountService, TokenProvider tokenProvider, ModelMapper modelMapper, ManagerService managerService) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.managerService = managerService;
    }

    // 휴무일 지정
    @PostMapping("calendarSave")
    public ResponseEntity<Map<String,Object>> calendarSave(HttpServletRequest request,@RequestBody List<BranchCalendarDto> branchCalendarDtoList){
        log.info("calendarSave 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+brCode);

        Integer getYear = branchCalendarDtoList.get(0).getYear();
        if(getYear == null){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP010.getCode(), "저장 "+ResponseErrorCode.TP010.getDesc(), null, null));
        }else{
            log.info("조회 년도 : "+getYear);
        }

        String bcDate = getYear +"01"+ "01";
        String type;
//        log.info("bcDate : "+bcDate);
        Optional<BranchCalendar> optionalBranchCalendar = managerService.branchCalendarInfo(brCode, bcDate);
        if(optionalBranchCalendar.isPresent()){
            type = "Y"; // 수정
        }else{
            type = "N"; // 신규
        }

        int day = 1;
        String dateMonth;
        String dateDay;
        int count = 1;
        for(int month=1; month<13; month++){
            BranchCalendar branchCalendar = new BranchCalendar();
            Calendar cal = new GregorianCalendar(getYear, month-1, day);
            int daysOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//            log.info(getYear + "년 " + (month) + "월의 일수: " + daysOfMonth);
            for(int i=1; i<daysOfMonth+1; i++){
                if(month<10){
                    dateMonth = "0"+month;
                }else{
                    dateMonth = String.valueOf(month);
                }
                if(i<10){
                    dateDay = "0"+i;
                }else{
                    dateDay = String.valueOf(i);
                }

                String date = getYear +dateMonth+ dateDay;

//                log.info("date : "+date);
                if(count != branchCalendarDtoList.size()){
                    if(date.equals(branchCalendarDtoList.get(count).getBcDate())){
//                        log.info("Y로 저장합니다 : "+calendarSet.get(count).getBcDate());
                        branchCalendar.setBcDate(date);
                        branchCalendar.setBcDayoffYn("Y");
                        count++;
                    }else{
//                        log.info("N로 저장합니다");
                        branchCalendar.setBcDate(date);
                        branchCalendar.setBcDayoffYn("N");
                    }
                }else{
//                    log.info("N로 저장합니다");
                    branchCalendar.setBcDate(date);
                    branchCalendar.setBcDayoffYn("N");
                }

                branchCalendar.setBrCode(brCode);

                if(type.equals("Y")){
                    branchCalendar.setInsert_id(optionalBranchCalendar.get().getInsert_id());
                    branchCalendar.setInsertDateTime(optionalBranchCalendar.get().getInsertDateTime());
                    branchCalendar.setModify_id(login_id);
                    branchCalendar.setModifyDateTime(LocalDateTime.now());
                }else{
                    branchCalendar.setInsert_id(login_id);
                    branchCalendar.setInsertDateTime(LocalDateTime.now());
                }
//                log.info("branchCalendar : "+branchCalendar);

                managerService.branchCalendarSave(branchCalendar);

            }
        }

        return ResponseEntity.ok(res.success());
    }

    // 휴무일 데이터 받아오기
    @GetMapping("calendarInfo")
    public ResponseEntity<Map<String,Object>> calendarInfo(HttpServletRequest request, @RequestParam(value="targetYear", defaultValue="") String targetYear){
        log.info("calendarInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+brCode);

        if(targetYear == null || targetYear.equals("")){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP010.getCode(), "조회 "+ResponseErrorCode.TP010.getDesc(), null, null));
        }else{
            log.info("조회 할 년도 : "+targetYear);
        }

        List<HashMap<String,Object>> branchCalendarListData = new ArrayList<>();
        HashMap<String,Object> branchCalendarInfo;

        List<BranchCalendarListDto> branchCalendarListDtos = managerService.branchCalendarDtoList(brCode, targetYear);
//        log.info("branchCalendarListDtos : "+branchCalendarListDtos);

        for (BranchCalendarListDto branchCalendarListDto : branchCalendarListDtos) {

            branchCalendarInfo = new HashMap<>();

            branchCalendarInfo.put("bcDate", branchCalendarListDto.getBcDate());
            branchCalendarInfo.put("bcDayoffYn", branchCalendarListDto.getBcDayoffYn());

            branchCalendarListData.add(branchCalendarInfo);
        }

//        log.info(targetYear+"년도 휴가일정 리스트 : "+branchCalendarListData+", "+"지사코드 : "+brCode);
        data.put("gridListData",branchCalendarListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
