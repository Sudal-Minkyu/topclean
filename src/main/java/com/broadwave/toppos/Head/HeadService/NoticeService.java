package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Notice.*;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeListDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewSubDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NoticeService {

    private final TokenProvider tokenProvider;
    private final NoticeRepository noticeRepository;
    private final NoticeRepositoryCustom noticeRepositoryCustom;

    @Autowired
    public NoticeService(TokenProvider tokenProvider, NoticeRepository noticeRepository, NoticeRepositoryCustom noticeRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.noticeRepository = noticeRepository;
        this.noticeRepositoryCustom = noticeRepositoryCustom;
    }

    // 공지사항 게시판 - 리스트 호출
    public ResponseEntity<Map<String, Object>> noticeList(String searchString, LocalDateTime filterFromDt, LocalDateTime filterToDt, Pageable pageable, HttpServletRequest request, String type) {
        log.info("noticeList 호출성공");

        AjaxResponse res = new AjaxResponse();

        // 검색조건
        log.info("searchString : "+searchString);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        Page<NoticeListDto> noticeListDtoPage = noticeRepositoryCustom.findByNoticeList(searchString, filterFromDt, filterToDt, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(noticeListDtoPage,type));
    }

    //  공지사항 게시판 - 글보기
    public ResponseEntity<Map<String, Object>> noticeView(Long hnId, HttpServletRequest request, String type) {
        log.info("noticeView 호출성공");

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
//        log.info("htId : "+htId);
        NoticeViewDto noticeViewDto = noticeRepositoryCustom.findByNoticeView(hnId);
        HashMap<String,Object> noticeViewInfo = new HashMap<>();

        if(noticeViewDto != null){
            noticeViewInfo.put("hnId", noticeViewDto.getHnId());
            noticeViewInfo.put("isWritter", type); // 1이면 지사, 2이면 가맹점
            noticeViewInfo.put("subject", noticeViewDto.getSubject());
            noticeViewInfo.put("content", noticeViewDto.getContent());
            noticeViewInfo.put("name", noticeViewDto.getName());
            noticeViewInfo.put("insertDateTime", noticeViewDto.getInsertDateTime());

            NoticeViewSubDto noticeViewPreDto = noticeRepositoryCustom.findByNoticePreView(noticeViewDto.getHnId());
            if(noticeViewPreDto != null){
                noticeViewInfo.put("prevId", noticeViewPreDto.getSubId());
                noticeViewInfo.put("prevSubject", noticeViewPreDto.getSubSubject());
                noticeViewInfo.put("prevInsertDateTime", noticeViewPreDto.getSubInsertDateTime());
            }else{
                noticeViewInfo.put("prevId", "");
                noticeViewInfo.put("prevSubject", "이전 글은 존재하지 않습니다.");
                noticeViewInfo.put("prevInsertDateTime", "");
            }
            NoticeViewSubDto noticeViewNextDto = noticeRepositoryCustom.findByNoticeNextView(noticeViewDto.getHnId());
            if(noticeViewNextDto != null){
                noticeViewInfo.put("nextId", noticeViewNextDto.getSubId());
                noticeViewInfo.put("nextSubject", noticeViewNextDto.getSubSubject());
                noticeViewInfo.put("nextvInsertDateTime", noticeViewNextDto.getSubInsertDateTime());
            }else{
                noticeViewInfo.put("nextId", "");
                noticeViewInfo.put("nextSubject", "다음 글은 존재하지 않습니다.");
                noticeViewInfo.put("nextvInsertDateTime", "");
            }
        }

        data.put("noticeViewDto",noticeViewInfo);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }







}
