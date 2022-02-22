package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeComment;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeCommentListDto;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeCommentRepository;
import com.broadwave.toppos.Manager.TagNotice.TagNotice;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeListDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeMapperDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeViewDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeDtos.TagNoticeViewSubDto;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeFile.TagNoticeFile;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeFile.TagNoticeFileRepository;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @Value("${toppos.aws.s3.bucket.url}")
    private String AWSBUCKETURL;

    private final TokenProvider tokenProvider;

    private final AWSS3Service awss3Service;

    private final TagNoticeRepository tagNoticeRepository;
    private final TagNoticeFileRepository tagNoticeFileRepository;
    private final TagNoticeCommentRepository tagNoticeCommentRepository;

    @Autowired
    public TagNoticeService(TokenProvider tokenProvider, AWSS3Service awss3Service,
                            TagNoticeRepository tagNoticeRepository,
                            TagNoticeFileRepository tagNoticeFileRepository,
                            TagNoticeCommentRepository tagNoticeCommentRepository){
        this.tokenProvider = tokenProvider;
        this.awss3Service = awss3Service;
        this.tagNoticeRepository = tagNoticeRepository;
        this.tagNoticeFileRepository = tagNoticeFileRepository;
        this.tagNoticeCommentRepository = tagNoticeCommentRepository;
    }

    //  택분실게시판 - 등록&수정
    public ResponseEntity<Map<String, Object>> lostNoticeSave(TagNoticeMapperDto tagNoticeMapperDto, HttpServletRequest request) throws IOException {
        log.info("lostNoticeSave 호출");

        log.info("tagNoticeMapperDto : "+tagNoticeMapperDto);
        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        TagNotice tagNotice = new TagNotice();
        TagNotice saveTagNotice;
        Optional<TagNotice> optionalTagNotice = tagNoticeRepository.findById(tagNoticeMapperDto.getHtId());
        if(optionalTagNotice.isPresent()){
            log.info("수정입니다.");
            optionalTagNotice.get().setHtSubject(tagNoticeMapperDto.getHtSubject());
            optionalTagNotice.get().setHtContent(tagNoticeMapperDto.getHtContent());
            optionalTagNotice.get().setModify_id(login_id);
            optionalTagNotice.get().setModifyDateTime(LocalDateTime.now());
            saveTagNotice = tagNoticeRepository.save(optionalTagNotice.get());
        }else{
            log.info("신규입니다.");
            tagNotice.setBrCode(brCode);
            tagNotice.setHtSubject(tagNoticeMapperDto.getHtSubject());
            tagNotice.setHtContent(tagNoticeMapperDto.getHtContent());
            tagNotice.setInsert_id(login_id);
            tagNotice.setInsertDateTime(LocalDateTime.now());
            saveTagNotice = tagNoticeRepository.save(tagNotice);
        }

        List<TagNoticeFile> tagNoticeFileList = new ArrayList<>();
        // AWS 파일저장
        if(tagNoticeMapperDto.getMultipartFileList() != null){
            TagNoticeFile tagNoticeFile;

            log.info("multipartFileList.size() : "+tagNoticeMapperDto.getMultipartFileList().size());
            for(MultipartFile multipartFile : tagNoticeMapperDto.getMultipartFileList()){

                tagNoticeFile = new TagNoticeFile();
                tagNoticeFile.setHtId(saveTagNotice);

                // 파일 오리지널 Name
                String originalFilename = multipartFile.getOriginalFilename();
                log.info("originalFilename : "+originalFilename);
                tagNoticeFile.setHfOriginalFilename(originalFilename);

                // 파일 오리지널 Name
                long fileSize = multipartFile.getSize();
                log.info("fileSize : "+fileSize);
                tagNoticeFile.setHtVolume(fileSize);

                // 확장자
                String ext;
                if(originalFilename != null){
                    ext = '.'+originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                    log.info("ext : "+ext);
                }else{
                    ext = "";
                }

                // 파일 중복명 처리
                String fileName = UUID.randomUUID().toString().replace("-", "")+ext;
                log.info("fileName : "+fileName);
                tagNoticeFile.setHfFilename(fileName);

                // S3에 저장 할 파일주소
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                String filePath = "/toppos-manager-tagNotice/" + date.format(new Date());
                log.info("filePath : "+AWSBUCKETURL+filePath+"/");
                tagNoticeFile.setHfPath(AWSBUCKETURL+filePath+"/");
                awss3Service.nomalFileUploadObject(multipartFile, fileName, filePath);

                tagNoticeFileList.add(tagNoticeFile);
            }

            log.info("tagNoticeFileList : "+tagNoticeFileList);
            tagNoticeFileRepository.saveAll(tagNoticeFileList);
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
        Page<TagNoticeListDto> tagNoticeListDtoPage = tagNoticeRepository.findByTagNoticeList(searchString, filterFromDt, filterToDt, frbrCode, pageable);
        return ResponseEntity.ok(res.ResponseEntityPage(tagNoticeListDtoPage,type));
    }

    //  택분실게시판 - 글보기
    public ResponseEntity<Map<String, Object>> lostNoticeView(Long htId, HttpServletRequest request, String type) {

        log.info("tagNoticeView 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));

        String code;
        if(type.equals("1")){
            code = (String) claims.get("brCode"); // 현재 지사 코드
        }else{
            code = (String) claims.get("frbrCode"); // 소속된 지사 코드
        }
        log.info("소속된 지사 코드 : "+code);

        // 검색조건
//        log.info("htId : "+htId);
        TagNoticeViewDto tagNoticeViewDto = tagNoticeRepository.findByTagNoticeView(htId, code);

        HashMap<String,Object> tagNoticeViewInfo = new HashMap<>();
        if(tagNoticeViewDto != null){
            if(!tagNoticeViewDto.getBrCode().equals(code)){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP026.getCode(), "해당 글의 "+ ResponseErrorCode.TP026.getDesc(), null, null));
            }
            tagNoticeViewInfo.put("htId", tagNoticeViewDto.getHtId());
            tagNoticeViewInfo.put("isWritter", type); // 1이면 지사, 2이면 가맹점
            tagNoticeViewInfo.put("subject", tagNoticeViewDto.getSubject());
            tagNoticeViewInfo.put("content", tagNoticeViewDto.getContent());
            tagNoticeViewInfo.put("name", tagNoticeViewDto.getName());
            tagNoticeViewInfo.put("insertDateTime", tagNoticeViewDto.getInsertDateTime());

            TagNoticeViewSubDto tagNoticeViewPreDto = tagNoticeRepository.findByTagNoticePreView(tagNoticeViewDto.getHtId(), code);
            if(tagNoticeViewPreDto != null){
                tagNoticeViewInfo.put("prevId", tagNoticeViewPreDto.getSubId());
                tagNoticeViewInfo.put("prevSubject", tagNoticeViewPreDto.getSubSubject());
                tagNoticeViewInfo.put("prevInsertDateTime", tagNoticeViewPreDto.getSubInsertDateTime());
            }else{
                tagNoticeViewInfo.put("prevId", "");
                tagNoticeViewInfo.put("prevSubject", "이전 글은 존재하지 않습니다.");
                tagNoticeViewInfo.put("prevInsertDateTime", "");
            }
            TagNoticeViewSubDto tagNoticeViewNextDto = tagNoticeRepository.findByTagNoticeNextView(tagNoticeViewDto.getHtId(), code);
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
        List<TagNoticeCommentListDto> tagNoticeCommentList = tagNoticeCommentRepository.findByTagNoticeCommentList(htId,login_id);
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
