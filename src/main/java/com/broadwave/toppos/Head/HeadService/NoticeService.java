package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeListDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewSubDto;
import com.broadwave.toppos.Head.Notice.NoticeFile.NoticeFileListDto;
import com.broadwave.toppos.Head.Notice.NoticeFile.NoticeFileRepository;
import com.broadwave.toppos.Head.Notice.NoticeRepository;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository){
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
    }

    // 메인페이지에 필요한 공지사항리스트 가져오기 (limit 3)
    public List<NoticeListDto> branchMainNoticeList() {
        log.info("branchMainNoticeList 호출");
        return noticeRepository.findByMainNoticeList();
    }

    // 공지사항 게시판 - 리스트 호출
    public ResponseEntity<Map<String, Object>> noticeList(String searchString, String filterFromDt, String filterToDt, Pageable pageable, String type) {
        log.info("noticeList 호출성공");

        AjaxResponse res = new AjaxResponse();

        // 검색조건
        log.info("searchString : "+searchString);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        Page<NoticeListDto> noticeListDtoPage = noticeRepository.findByNoticeList(searchString, filterFromDt, filterToDt, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(noticeListDtoPage,type));
    }

    //  공지사항 게시판 - 글보기
    public ResponseEntity<Map<String, Object>> noticeView(Long hnId, String type) {
        log.info("noticeView 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 검색조건
//        log.info("htId : "+htId);
        NoticeViewDto noticeViewDto = noticeRepository.findByNoticeView(hnId);
        HashMap<String,Object> noticeViewInfo = new HashMap<>();

        List<NoticeFileListDto> noticeFileListDtos = noticeFileRepository.findByNoticeFileList(hnId);
        if(noticeViewDto != null){
            noticeViewInfo.put("hnId", noticeViewDto.getHnId());
            noticeViewInfo.put("isWritter", type); // 1이면 본사, 2이면 지사 or 가맹점
            noticeViewInfo.put("subject", noticeViewDto.getSubject());
            noticeViewInfo.put("content", noticeViewDto.getContent());
            noticeViewInfo.put("name", noticeViewDto.getName());
            noticeViewInfo.put("insertDateTime", noticeViewDto.getInsertDateTime());
            noticeViewInfo.put("fileList", noticeFileListDtos);

            NoticeViewSubDto noticeViewPreDto = noticeRepository.findByNoticePreView(noticeViewDto.getHnId());
            if(noticeViewPreDto != null){
                noticeViewInfo.put("prevId", noticeViewPreDto.getSubId());
                noticeViewInfo.put("prevSubject", noticeViewPreDto.getSubSubject());
                noticeViewInfo.put("prevInsertDateTime", noticeViewPreDto.getSubInsertDateTime());
            }else{
                noticeViewInfo.put("prevId", "");
                noticeViewInfo.put("prevSubject", "이전 글은 존재하지 않습니다.");
                noticeViewInfo.put("prevInsertDateTime", "");
            }
            NoticeViewSubDto noticeViewNextDto = noticeRepository.findByNoticeNextView(noticeViewDto.getHnId());
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
