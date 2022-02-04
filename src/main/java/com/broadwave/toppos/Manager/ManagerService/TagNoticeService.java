package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendar;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepository;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarDto;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarListDto;
import com.broadwave.toppos.Manager.TagNotice.*;
import com.broadwave.toppos.User.Customer.CustomerDtos.CustomerUncollectListDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-01-28
 * Time :
 * Remark : Toppos 지사 - 택분실게시판 서비스
 */
@Slf4j
@Service
public class TagNoticeService {

    private final TokenProvider tokenProvider;

    private final TagNoticeRepository tagNoticeRepository;
    private final TagNoticeRepositoryCustom tagNoticeRepositoryCustom;

    @Autowired
    public TagNoticeService(TokenProvider tokenProvider, TagNoticeRepository tagNoticeRepository, TagNoticeRepositoryCustom tagNoticeRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.tagNoticeRepository = tagNoticeRepository;
        this.tagNoticeRepositoryCustom = tagNoticeRepositoryCustom;
    }

    //  택분실게시판 - 리스트호출 테이블
    public ResponseEntity<Map<String, Object>> lostNoticeList(String searchString, LocalDateTime filterFromDt, LocalDateTime filterToDt, Pageable pageable, HttpServletRequest request, String type) {
        log.info("lostNoticeList 호출성공");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frbrCode;
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        if(type.equals("1")){
            frbrCode = (String) claims.get("brCode"); // 현재 지사 코드
            log.info("현재 접속한 지사 코드 : "+frbrCode);
        }else{
            frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
            log.info("현재 접속한 소속된 지사 코드 : "+frbrCode);
        }

        // 검색조건
        log.info("searchString : "+searchString);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        Page<TagNoticeListDto> tagNoticeListDtoPage = tagNoticeRepositoryCustom.findByTagNoticeList(searchString, filterFromDt, filterToDt, frbrCode, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(tagNoticeListDtoPage,type));
    }

    //  택분실게시판 - 글보기
    public ResponseEntity<Map<String, Object>> findByTagNoticeView(Long htId, HttpServletRequest request, String type) {

        log.info("findByTagNoticeView 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        // 검색조건
        log.info("htId : "+htId);

        TagNoticeViewDto tagNoticeViewDto = tagNoticeRepositoryCustom.findByTagNoticeView(htId, login_id, frbrCode);
        HashMap<String,Object> tagNoticeViewInfo = new HashMap<>();

        if(tagNoticeViewDto != null){
            tagNoticeViewInfo.put("htId", tagNoticeViewDto.getHtId());
            tagNoticeViewInfo.put("isWritter", type); // 1이면 지사, 2이면 가맹점
            tagNoticeViewInfo.put("subject", tagNoticeViewDto.getSubject());
            tagNoticeViewInfo.put("content", tagNoticeViewDto.getContent());
            tagNoticeViewInfo.put("name", tagNoticeViewDto.getName());
            tagNoticeViewInfo.put("insertDateTime", tagNoticeViewDto.getInsertDateTime());

            TagNoticeViewSubDto tagNoticeViewPreDto = tagNoticeRepositoryCustom.findByTagNoticePreView(tagNoticeViewDto.getHtId(), frbrCode);
            if(tagNoticeViewPreDto != null){
                tagNoticeViewInfo.put("prevId", tagNoticeViewPreDto.getSubId());
                tagNoticeViewInfo.put("prevSubject", tagNoticeViewPreDto.getSubSubject());
                tagNoticeViewInfo.put("prevInsertDateTime", tagNoticeViewPreDto.getSubInsertDateTime());
            }else{
                tagNoticeViewInfo.put("prevId", "");
                tagNoticeViewInfo.put("prevSubject", "이전 글은 존재하지 않습니다.");
                tagNoticeViewInfo.put("prevInsertDateTime", "");
            }
            TagNoticeViewSubDto tagNoticeViewNextDto = tagNoticeRepositoryCustom.findByTagNoticeNextView(tagNoticeViewDto.getHtId(), frbrCode);
            if(tagNoticeViewNextDto != null){
                tagNoticeViewInfo.put("nextId", tagNoticeViewNextDto.getSubId());
                tagNoticeViewInfo.put("nextSubject", tagNoticeViewNextDto.getSubSubject());
                tagNoticeViewInfo.put("nextvInsertDateTime", tagNoticeViewNextDto.getSubInsertDateTime());
            }else{
                tagNoticeViewInfo.put("nextId", "");
                tagNoticeViewInfo.put("nextSubject", "다음 글은 존재하지 않습니다.");
                tagNoticeViewInfo.put("nextvInsertDateTime", "");
            }
        }

        data.put("tagNoticeViewDto",tagNoticeViewInfo);

        return ResponseEntity.ok(res.dataSendSuccess(data));




    }




}
