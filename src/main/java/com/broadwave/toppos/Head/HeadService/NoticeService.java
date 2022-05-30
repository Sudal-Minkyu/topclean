package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Head.Notice.Notice;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeListDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeMapperDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewDto;
import com.broadwave.toppos.Head.Notice.NoticeDtos.NoticeViewSubDto;
import com.broadwave.toppos.Head.Notice.NoticeFile.NoticeFile;
import com.broadwave.toppos.Head.Notice.NoticeFile.NoticeFileListDto;
import com.broadwave.toppos.Head.Notice.NoticeFile.NoticeFileRepository;
import com.broadwave.toppos.Head.Notice.NoticeRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.TagNotice.Comment.TagNoticeComment;
import com.broadwave.toppos.Manager.TagNotice.TagNotice;
import com.broadwave.toppos.Manager.TagNotice.TagNoticeFile.TagNoticeFile;
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
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class NoticeService {

    @Value("${toppos.aws.s3.bucket.url}")
    private String AWSBUCKETURL;

    private final TokenProvider tokenProvider;
    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final AWSS3Service awss3Service;

    @Autowired
    public NoticeService(TokenProvider tokenProvider, NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository, AWSS3Service awss3Service){
        this.tokenProvider = tokenProvider;
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.awss3Service = awss3Service;
    }

    // 메인페이지에 필요한 공지사항리스트 가져오기
    public List<NoticeListDto> branchMainNoticeList(String brCode) {
        log.info("branchMainNoticeList 호출");
        return noticeRepository.findByMainNoticeList(brCode);
    }

    // 공지사항 게시판 - 리스트 호출
    public ResponseEntity<Map<String, Object>> noticeList(String hnType, String searchString, String filterFromDt, String filterToDt, Pageable pageable, String type) {
        log.info("noticeList 호출성공");

        AjaxResponse res = new AjaxResponse();

        // 검색조건
        log.info("hnType : "+hnType);
        log.info("searchString : "+searchString);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        Page<NoticeListDto> noticeListDtoPage = noticeRepository.findByNoticeList(hnType, searchString, filterFromDt, filterToDt, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(noticeListDtoPage,type));
    }

    //  공지사항 게시판 - 글보기
    public ResponseEntity<Map<String, Object>> noticeView(Long hnId, String type) {
        log.info("noticeView 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 검색조건
//        log.info("hnId : "+hnId);
//        log.info("type : "+type);

        NoticeViewDto noticeViewDto = noticeRepository.findByNoticeView(hnId);
        HashMap<String,Object> noticeViewInfo = new HashMap<>();

        List<NoticeFileListDto> noticeFileListDtos = noticeFileRepository.findByNoticeFileList(hnId);
        log.info("noticeViewDto : "+noticeViewDto);
        if(noticeViewDto != null){
            noticeViewInfo.put("hnId", noticeViewDto.getHnId());
            // 1이면 본사 or 지사 , 2이면 가맹점
            if(type.equals("1")){
                noticeViewInfo.put("isWritter", "1");
            }else if(type.equals("2")){
                if(noticeViewDto.getHnType().equals("02")){
                    noticeViewInfo.put("isWritter", "1");
                }else{
                    noticeViewInfo.put("isWritter", "2");
                }
            }else{
                noticeViewInfo.put("isWritter", "2");
            }
            noticeViewInfo.put("subject", noticeViewDto.getSubject());
            noticeViewInfo.put("content", noticeViewDto.getContent());
            noticeViewInfo.put("hnType", noticeViewDto.getHnType());
            noticeViewInfo.put("name", noticeViewDto.getName());
            noticeViewInfo.put("insertDateTime", noticeViewDto.getInsertDateTime());
            noticeViewInfo.put("fileList", noticeFileListDtos);

            NoticeViewSubDto noticeViewPreDto = noticeRepository.findByNoticePreView(noticeViewDto.getHnId());
            if(noticeViewPreDto != null){
                noticeViewInfo.put("preId", noticeViewPreDto.getSubId());
                noticeViewInfo.put("preSubject", noticeViewPreDto.getSubSubject());
                noticeViewInfo.put("preHnType", noticeViewPreDto.getHnType());
                noticeViewInfo.put("preInsertDateTime", noticeViewPreDto.getSubInsertDateTime());
            }else{
                noticeViewInfo.put("preId", "");
                noticeViewInfo.put("preSubject", "이전 글은 존재하지 않습니다.");
                noticeViewInfo.put("preHnType", "");
                noticeViewInfo.put("preInsertDateTime", "");
            }
            NoticeViewSubDto noticeViewNextDto = noticeRepository.findByNoticeNextView(noticeViewDto.getHnId());
            if(noticeViewNextDto != null){
                noticeViewInfo.put("nextId", noticeViewNextDto.getSubId());
                noticeViewInfo.put("nextSubject", noticeViewNextDto.getSubSubject());
                noticeViewInfo.put("nextHnType", noticeViewNextDto.getHnType());
                noticeViewInfo.put("nextInsertDateTime", noticeViewNextDto.getSubInsertDateTime());
            }else{
                noticeViewInfo.put("nextId", "");
                noticeViewInfo.put("nextSubject", "다음 글은 존재하지 않습니다.");
                noticeViewInfo.put("nextHnType", "");
                noticeViewInfo.put("nextInsertDateTime", "");
            }
        }

        data.put("noticeViewDto",noticeViewInfo);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


    public ResponseEntity<Map<String, Object>> noticeSave(NoticeMapperDto noticeMapperDto, HttpServletRequest request, String type) throws IOException {
        log.info("noticeSave 호출");

        log.info("noticeMapperDto : "+noticeMapperDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode;
        if(type.equals("2")){
            brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
            log.info("현재 접속한 지사 코드 : "+brCode);
        }else{
            brCode = null;
        }
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        Notice notice = new Notice();
        Notice saveNotice;
        if(noticeMapperDto.getId() != null){
            Optional<Notice> optionalNotice = noticeRepository.findById(noticeMapperDto.getId());

            if(optionalNotice.isPresent()){
                if(type.equals("2") && optionalNotice.get().getHnType().equals("01")){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP007.getCode(), "해당 글을 수정할 "+ResponseErrorCode.TP007.getDesc(), null, null));
                }

                log.info("공지사항 게시판 글을 수정합니다.");
                optionalNotice.get().setHnSubject(noticeMapperDto.getSubject());
                optionalNotice.get().setHnContent(noticeMapperDto.getContent());
                optionalNotice.get().setModify_id(login_id);
                optionalNotice.get().setModifyDateTime(LocalDateTime.now());

                if(noticeMapperDto.getDeleteFileList() != null) {
                    // AWS 파일 삭제
                    List<NoticeFile> noticeFileList = noticeFileRepository.findByNoticeFileDeleteList(noticeMapperDto.getDeleteFileList());
                    for(NoticeFile noticeFile : noticeFileList){
                        String insertDate =noticeFile.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        String path = "/toppos-notice-file/"+insertDate;
                        log.info("path : "+path);
                        String filename = noticeFile.getHfFilename();
                        log.info("filename : "+filename);
                        awss3Service.deleteObject(path,filename);
                    }
                    noticeFileRepository.noticeFileListDelete(noticeMapperDto.getDeleteFileList());
                }

                saveNotice = noticeRepository.save(optionalNotice.get());
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "해당 글은 "+ResponseErrorCode.TP030.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
            }
        }else{
            log.info("공지사항 게시판 글이 신규입니다.");
            notice.setBrCode(brCode);
            if(type.equals("2")){
                notice.setHnType("02");
            }else{
                notice.setHnType("01");
            }
            notice.setHnSubject(noticeMapperDto.getSubject());
            notice.setHnContent(noticeMapperDto.getContent());
            notice.setHnYyyymmdd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            notice.setInsert_id(login_id);
            notice.setInsertDateTime(LocalDateTime.now());
            saveNotice = noticeRepository.save(notice);
        }

        List<NoticeFile> noticeFileList = new ArrayList<>();
        // AWS 파일저장
        if(noticeMapperDto.getMultipartFileList() != null){
            NoticeFile noticeFile;
//            log.info("multipartFileList.size() : "+noticeMapperDto.getMultipartFileList().size());
            for(MultipartFile multipartFile : noticeMapperDto.getMultipartFileList()){
                noticeFile = new NoticeFile();
                noticeFile.setHnId(saveNotice);
                // 파일 오리지널 Name
                String originalFilename = Normalizer.normalize(Objects.requireNonNull(multipartFile.getOriginalFilename()), Normalizer.Form.NFC);
//                log.info("originalFilename : "+originalFilename);
                noticeFile.setHfOriginalFilename(originalFilename);
                // 파일 Size
                long fileSize = multipartFile.getSize();
//                log.info("fileSize : "+fileSize);
                noticeFile.setHfVolume(fileSize);
                // 확장자
                String ext;
                ext = '.'+originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//                log.info("ext : "+ext);
                // 파일 중복명 처리
                String fileName = UUID.randomUUID().toString().replace("-", "")+ext;
//                log.info("fileName : "+fileName);
                noticeFile.setHfFilename(fileName);
                // S3에 저장 할 파일주소
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                String filePath = "/toppos-notice-file/" + date.format(new Date());
//                log.info("filePath : "+AWSBUCKETURL+filePath+"/");
                noticeFile.setHfPath(AWSBUCKETURL+filePath+"/");
                awss3Service.nomalFileUpload(multipartFile, fileName, filePath);
                noticeFile.setInsert_id(login_id);
                noticeFile.setInsertDateTime(LocalDateTime.now());
                noticeFileList.add(noticeFile);
            }
//            log.info("tagNoticeFileList : "+tagNoticeFileList);
            noticeFileRepository.saveAll(noticeFileList);
        }else{
            log.info("첨부파일이 존재하지 않습니다");
        }

        data.put("id",saveNotice.getHnId());

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  공지사항 게시판 - 글삭제
    @Transactional
    public ResponseEntity<Map<String, Object>> noticeDelete(Long hnId) {
        log.info("noticeDelete 호출");

        AjaxResponse res = new AjaxResponse();

        Optional<Notice> optionalNotice = noticeRepository.findById(hnId);
        if(optionalNotice.isPresent()){
//            log.info("optionalNotice : "+optionalNotice.get().getHnId());

            List<NoticeFile> noticeFileList = noticeFileRepository.findByNoticeFileDelete(optionalNotice.get().getHnId());
//            log.info("tagNoticeFileList : "+tagNoticeFileList);

            for(NoticeFile noticeFile : noticeFileList){
                // AWS 파일 삭제
                String insertDate =noticeFile.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String path = "/toppos-notice-file/"+insertDate;
//                log.info("path : "+path);
                String filename = noticeFile.getHfFilename();
//                log.info("filename : "+filename);
                awss3Service.deleteObject(path,filename);
//                log.info("AWS삭제성공");
            }

            noticeFileRepository.deleteAll(noticeFileList);
            noticeRepository.delete(optionalNotice.get());
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "삭제 할 "+ResponseErrorCode.TP022.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
        }

        return ResponseEntity.ok(res.success());
    }


}
