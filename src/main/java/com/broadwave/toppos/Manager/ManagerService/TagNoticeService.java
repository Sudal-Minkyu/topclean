package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.Calendar.BranchCalendar;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepository;
import com.broadwave.toppos.Manager.Calendar.BranchCalendarRepositoryCustom;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarDto;
import com.broadwave.toppos.Manager.Calendar.CalendarDtos.BranchCalendarListDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeListDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeRepositoryCustom;
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

    private final TagNoticeRepositoryCustom tagNoticeRepositoryCustom;

    @Autowired
    public TagNoticeService(TokenProvider tokenProvider, TagNoticeRepositoryCustom tagNoticeRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.tagNoticeRepositoryCustom = tagNoticeRepositoryCustom;
    }

    //  택분실게시판 - 리스트호출 테이블
    public ResponseEntity<Map<String, Object>> lostNoticeList(String searchType, String searchString, Pageable pageable) {
        log.info("lostNoticeList 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
//        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
//        log.info("소속된 지사 코드 : "+frbrCode);

        // 검색조건
        log.info("searchType : "+searchType);
        log.info("searchString : "+searchString);
        Page<TagNoticeListDto> tagNoticeListDtoPage = tagNoticeRepositoryCustom.findByTagNoticeList(searchType, searchString, pageable);

        data.put("tagNoticeListDtoPage",tagNoticeListDtoPage);
        return ResponseEntity.ok(res.ResponseEntityPage(tagNoticeListDtoPage));

//        return ResponseEntity.ok(res.ResponseEntityPage(tagNoticeListDtoPage));
    }














}
