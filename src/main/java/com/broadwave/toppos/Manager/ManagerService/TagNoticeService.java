package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeComment;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeCommentListDto;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeCommentRepository;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeCommentRepositoryCustom;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeListDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeMapperDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeViewDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeViewSubDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
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
    private final TagNoticeCommentRepository tagNoticeCommentRepository;
    private final TagNoticeCommentRepositoryCustom tagNoticeCommentRepositoryCustom;

    @Autowired
    public TagNoticeService(TokenProvider tokenProvider, TagNoticeRepositoryCustom tagNoticeRepositoryCustom,
                            TagNoticeCommentRepository tagNoticeCommentRepository, TagNoticeCommentRepositoryCustom tagNoticeCommentRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.tagNoticeRepositoryCustom = tagNoticeRepositoryCustom;
        this.tagNoticeCommentRepository = tagNoticeCommentRepository;
        this.tagNoticeCommentRepositoryCustom = tagNoticeCommentRepositoryCustom;
    }

    //  택분실게시판 - 등록
    public ResponseEntity<Map<String, Object>> lostNoticeSave(TagNoticeMapperDto tagNoticeMapperDto, HttpServletRequest request) {
        log.info("lostNoticeSave 호출");

        log.info("tagNoticeMapperDto : "+tagNoticeMapperDto);
        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        // AWS 파일저장
        if(tagNoticeMapperDto.getMultipartFileList() != null){
            log.info("multipartFileList.size() : "+tagNoticeMapperDto.getMultipartFileList().size());
            for(MultipartFile multipartFile : tagNoticeMapperDto.getMultipartFileList()){

                // 파일 오리지널 Name
                String files = multipartFile.getOriginalFilename();
                log.info("files : "+files);

                // 확장자
                String ext;
                if(files != null){
                    ext = '.'+files.substring(files.lastIndexOf(".") + 1);
                    log.info("ext : "+ext);
                }else{
                    ext = "";
                }

                // 파일 중복명 처리
                String storedFileName = UUID.randomUUID().toString().replace("-", "")+ext;
                log.info("storedFileName : "+storedFileName);

                // S3에 저장 할 파일주소
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                String filePath = "/toppos-branch-tagNotice/" + date.format(new Date());
                log.info("filePath : "+filePath);

//                String ffFilename = awss3Service.uploadObject(mFile, storedFileName, filePath);
//                data.put("ffPath",AWSBUCKETURL+filePath+"/");
//                data.put("ffFilename",ffFilename);
            }
        }else{
            log.info("첨부파일이 존재하지 않습니다");
        }


        return ResponseEntity.ok(res.success());
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
//        log.info("searchString : "+searchString);
//        log.info("filterFromDt : "+filterFromDt);
//        log.info("filterToDt : "+filterToDt);
        Page<TagNoticeListDto> tagNoticeListDtoPage = tagNoticeRepositoryCustom.findByTagNoticeList(searchString, filterFromDt, filterToDt, frbrCode, pageable);
        return ResponseEntity.ok(res.ResponseEntityPage(tagNoticeListDtoPage,type));
    }

    //  택분실게시판 - 글보기
    public ResponseEntity<Map<String, Object>> lostNoticeView(Long htId, HttpServletRequest request, String type) {

        log.info("tagNoticeView 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
//        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);

        // 검색조건
//        log.info("htId : "+htId);
        TagNoticeViewDto tagNoticeViewDto = tagNoticeRepositoryCustom.findByTagNoticeView(htId, frbrCode);

        HashMap<String,Object> tagNoticeViewInfo = new HashMap<>();
        if(tagNoticeViewDto != null){
            if(!tagNoticeViewDto.getBrCode().equals(frbrCode)){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP026.getCode(), "해당 글의 "+ ResponseErrorCode.TP026.getDesc(), null, null));
            }
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

    //  택분실게시판 - 댓글 리스트 호출
    public ResponseEntity<Map<String, Object>> lostNoticeCommentList(Long htId, HttpServletRequest request) {
        log.info("lostNoticeList 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디

        // 검색조건
        log.info(htId+" 의 댓글리스트 호출");
        List<TagNoticeCommentListDto> tagNoticeCommentList = tagNoticeCommentRepositoryCustom.findByTagNoticeCommentList(htId,login_id);
        data.put("commentListDto",tagNoticeCommentList);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  택분실게시판 - 댓글 작성 and 수정
    public ResponseEntity<Map<String, Object>> lostNoticeCommentSave(Long hcId, Long htId, String type, String comment, Long preId, HttpServletRequest request) {
        log.info("lostNoticeCommentSave 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        if(hcId != null){
            Optional<TagNoticeComment> optionalTagNoticeComment = tagNoticeCommentRepository.findById(hcId);
            if(optionalTagNoticeComment.isPresent()){
                log.info("수정 댓글입니다.");
                optionalTagNoticeComment.get().setHcComment(comment);
                optionalTagNoticeComment.get().setModify_id(login_id);
                optionalTagNoticeComment.get().setModifyDateTime(LocalDateTime.now());
                tagNoticeCommentRepository.save(optionalTagNoticeComment.get());
            }
        }else{
            log.info("신규 댓글입니다.");
            TagNoticeComment tagNoticeComment = new TagNoticeComment();
            tagNoticeComment.setHtId(htId);
            tagNoticeComment.setHcComment(comment);
            tagNoticeComment.setHcType(type);
            if(type.equals("2")){
                tagNoticeComment.setHcPreId(preId);
            }
            tagNoticeComment.setInsert_id(login_id);
            tagNoticeComment.setInsertDateTime(LocalDateTime.now());
            tagNoticeCommentRepository.save(tagNoticeComment);
        }

        return ResponseEntity.ok(res.success());
    }

}
