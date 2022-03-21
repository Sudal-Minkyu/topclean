package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.TagGallery.TagGallery;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck.TagGalleryCheckListDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck.TagGalleryCheckRepository;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryDetailDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryListDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryDtos.TagGalleryMapperDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryFile.TagGalleryFile;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryFile.TagGalleryFileListDto;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryFile.TagGalleryFileRepository;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-03-15
 * Time :
 * Remark : Toppos 지사 - NEW 택분실게시판 서비스
 */
@Slf4j
@Service
public class TagGalleryService {

    @Value("${toppos.aws.s3.bucket.url}")
    private String AWSBUCKETURL;

    private final TokenProvider tokenProvider;

    private final AWSS3Service awss3Service;

    private final TagGalleryRepository tagGalleryRepository;
    private final TagGalleryFileRepository tagGalleryFileRepository;
    private final TagGalleryCheckRepository tagGalleryCheckRepository;

    @Autowired
    public TagGalleryService(TokenProvider tokenProvider, AWSS3Service awss3Service,
                             TagGalleryRepository tagGalleryRepository,
                             TagGalleryCheckRepository tagGalleryCheckRepository,
                             TagGalleryFileRepository tagGalleryFileRepository){
        this.tokenProvider = tokenProvider;
        this.awss3Service = awss3Service;
        this.tagGalleryRepository = tagGalleryRepository;
        this.tagGalleryCheckRepository = tagGalleryCheckRepository;
        this.tagGalleryFileRepository = tagGalleryFileRepository;
    }

    //  NEW 택분실게시판 - 등록&수정
    @Transactional
    public ResponseEntity<Map<String, Object>> tagGallerySave(TagGalleryMapperDto tagGalleryMapperDto, HttpServletRequest request) throws IOException {
        log.info("tagGallerySave 호출");

        log.info("tagGalleryMapperDto : "+tagGalleryMapperDto);
        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        TagGallery tagGallery = new TagGallery();
        TagGallery saveTagGallery;
        if(tagGalleryMapperDto.getBtId() != null){
            Optional<TagGallery> optionalTagGallery = tagGalleryRepository.findById(tagGalleryMapperDto.getBtId());
            if(optionalTagGallery.isPresent()){
                log.info("수정입니다.");
                optionalTagGallery.get().setBtBrandName(tagGalleryMapperDto.getBtBrandName());
                optionalTagGallery.get().setBtInputDate(tagGalleryMapperDto.getBtInputDate());
                optionalTagGallery.get().setBtMaterial(tagGalleryMapperDto.getBtMaterial());
                optionalTagGallery.get().setBtRemark(tagGalleryMapperDto.getBtRemark());
                optionalTagGallery.get().setModify_id(login_id);
                optionalTagGallery.get().setModifyDateTime(LocalDateTime.now());

                if(tagGalleryMapperDto.getDeletePhotoList() != null) {
                    // AWS 파일 삭제
                    List<TagGalleryFile> tagGalleryFileList = tagGalleryFileRepository.findByTagGalleryFileDeleteList(tagGalleryMapperDto.getDeletePhotoList());
                    for(TagGalleryFile tagGalleryFile : tagGalleryFileList){
                        String insertDate =tagGalleryFile.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        String path = "/toppos-manager-tagGallery/"+insertDate;
//                        log.info("path : "+path);
                        String filename = tagGalleryFile.getBfFilename();
//                        log.info("filename : "+filename);
                        awss3Service.deleteObject(path,filename);
                    }
                    tagGalleryFileRepository.tagGalleryFileListDelete(tagGalleryMapperDto.getDeletePhotoList());
                }

                saveTagGallery = tagGalleryRepository.save(optionalTagGallery.get());
            }else{
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "해당 글은 "+ResponseErrorCode.TP030.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
            }
        }else{
            log.info("신규입니다.");
            tagGallery.setBrCode(brCode);
            tagGallery.setBtBrandName(tagGalleryMapperDto.getBtBrandName());
            tagGallery.setBtInputDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            tagGallery.setBtInputDate(tagGalleryMapperDto.getBtInputDate());
            tagGallery.setBtMaterial(tagGalleryMapperDto.getBtMaterial());
            tagGallery.setBtRemark(tagGalleryMapperDto.getBtRemark());
            tagGallery.setBrCloseYn("N");
            tagGallery.setInsert_id(login_id);
            tagGallery.setInsertDateTime(LocalDateTime.now());
            saveTagGallery = tagGalleryRepository.save(tagGallery);
        }

        List<TagGalleryFile> tagGalleryFileList = new ArrayList<>();
        // AWS 파일저장
        if(tagGalleryMapperDto.getAddPhotoList() != null){
            TagGalleryFile tagGalleryFile;

//            log.info("multipartFileList.size() : "+tagGalleryMapperDto.getMultipartFileList().size());
            for(MultipartFile multipartFile : tagGalleryMapperDto.getAddPhotoList()){

                tagGalleryFile = new TagGalleryFile();
                tagGalleryFile.setBtId(saveTagGallery);

                // 파일 오리지널 Name(이미지일경우 제외)
//                String originalFilename = Normalizer.normalize(Objects.requireNonNull(multipartFile.getOriginalFilename()), Normalizer.Form.NFC);
//                log.info("originalFilename : "+originalFilename);
//                tagGalleryFile.setBfOriginalFilename(originalFilename);

                // 파일 Size
                long fileSize = multipartFile.getSize();
//                log.info("fileSize : "+fileSize);
                tagGalleryFile.setBfVolume(fileSize);

                // 확장자 (이미지일경우 제외)
//                String ext;
//                ext = '.'+originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//                log.info("ext : "+ext);

                // 파일 중복명 처리
                String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
//                log.info("fileName : "+fileName);
                tagGalleryFile.setBfFilename(fileName);

                // S3에 저장 할 파일주소
                SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                String filePath = "/toppos-manager-tagGallery/" + date.format(new Date());
//                log.info("filePath : "+AWSBUCKETURL+filePath+"/");
                tagGalleryFile.setBfPath(AWSBUCKETURL+filePath+"/");
                awss3Service.imageFileUpload(multipartFile, fileName, filePath);

                tagGalleryFile.setInsert_id(login_id);
                tagGalleryFile.setInsertDateTime(LocalDateTime.now());

                tagGalleryFileList.add(tagGalleryFile);
            }

//            log.info("tagGalleryFileList : "+tagGalleryFileList);
            tagGalleryFileRepository.saveAll(tagGalleryFileList);
        }else{
            log.info("첨부파일이 존재하지 않습니다");
        }
        return ResponseEntity.ok(res.success());
    }

    //  NEW 택분실게시판 - 리스트 호출
    public ResponseEntity<Map<String, Object>> tagGalleryList(String searchString, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("tagGalleryList 호출");

//        log.info("searchString : "+searchString);
//        log.info("filterFromDt : "+filterFromDt);
//        log.info("filterToDt : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<HashMap<String,Object>> tagGalleryListData = new ArrayList<>();
        HashMap<String,Object> tagGalleryInfo;

        List<TagGalleryListDto> tagGalleryListDtos = tagGalleryRepository.findByTagGalleryList(searchString, filterFromDt, filterToDt, brCode);
//        log.info("tagGalleryListDtos : "+tagGalleryListDtos);

        for(TagGalleryListDto tagGalleryListDto : tagGalleryListDtos){
            tagGalleryInfo = new HashMap<>();

            tagGalleryInfo.put("insertDateTime", tagGalleryListDto.getInsertDateTime());
            tagGalleryInfo.put("btId", tagGalleryListDto.getBtId());
            tagGalleryInfo.put("btBrandName", tagGalleryListDto.getBtBrandName());
            tagGalleryInfo.put("btInputDate", tagGalleryListDto.getBtInputDate());
            tagGalleryInfo.put("btMaterial", tagGalleryListDto.getBtMaterial());
            tagGalleryInfo.put("btRemark", tagGalleryListDto.getBtRemark());
            tagGalleryInfo.put("tagGalleryCheckFranchise", tagGalleryListDto.getFrName());

            List<TagGalleryFileListDto> tagGalleryFileListDto = tagGalleryFileRepository.findByTagGalleryFileList(Long.parseLong(String.valueOf(tagGalleryListDto.getBtId())), 3);
            tagGalleryInfo.put("bfPathFilename", tagGalleryFileListDto);

            tagGalleryListData.add(tagGalleryInfo);
        }
        data.put("tagGalleryList",tagGalleryListData);


        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  NEW 택분실게시판 - 상세보기 호출
    public ResponseEntity<Map<String, Object>> tagGalleryDetail(Long btId, HttpServletRequest request) {
        log.info("tagGalleryDetail 호출");

//        log.info("btId : "+btId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        TagGalleryDetailDto tagGalleryDetailDto = tagGalleryRepository.findByTagGalleryDetail(btId, brCode);
//        log.info("tagGalleryDetailDto : "+tagGalleryDetailDto);
        data.put("tagGallery",tagGalleryDetailDto);
        List<TagGalleryFileListDto> tagGalleryFileListDtos = tagGalleryFileRepository.findByTagGalleryFileList(btId, 6);
//        log.info("tagGalleryFileListDtos : "+tagGalleryFileListDtos);
        data.put("tagGalleryFileList", tagGalleryFileListDtos);
        List<TagGalleryCheckListDto> tagGalleryCheckListDtos = tagGalleryCheckRepository.findByTagGalleryCheckList(btId);
//        log.info("tagGalleryCheckListDtos : "+tagGalleryCheckListDtos);
        data.put("tagGalleryCheckList", tagGalleryCheckListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


//    //  택분실게시판 - 글삭제
//    public ResponseEntity<Map<String, Object>> lostGalleryDelete(Long htId) {
//        log.info("lostGalleryDelete 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        Optional<TagGallery> optionalTagGallery = tagGalleryRepository.findById(htId);
//        if(optionalTagGallery.isPresent()){
////            log.info("optionalTagGallery : "+optionalTagGallery.get().getHtId());
//
//            List<TagGalleryComment> tagGalleryCommentList = tagGalleryCommentRepository.findByTagGalleryCommentDelete(optionalTagGallery.get().getHtId());
////            log.info("tagGalleryCommentList : "+tagGalleryCommentList);
//
//            List<TagGalleryFile> tagGalleryFileList = tagGalleryFileRepository.findByTagGalleryFileDelete(optionalTagGallery.get().getHtId());
////            log.info("tagGalleryFileList : "+tagGalleryFileList);
//
//            for(TagGalleryFile tagGalleryFile : tagGalleryFileList){
//                // AWS 파일 삭제
//                String insertDate =tagGalleryFile.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//                String path = "/toppos-manager-tagGallery/"+insertDate;
////                log.info("path : "+path);
//                String filename = tagGalleryFile.getHfFilename();
////                log.info("filename : "+filename);
//                awss3Service.deleteObject(path,filename);
////                log.info("AWS삭제성공");
//            }
//
//            tagGalleryCommentRepository.deleteAll(tagGalleryCommentList);
//            tagGalleryFileRepository.deleteAll(tagGalleryFileList);
//            tagGalleryRepository.delete(optionalTagGallery.get());
//        }else{
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "삭제 할 "+ResponseErrorCode.TP022.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
//        }
//
//        return ResponseEntity.ok(res.success());
//    }
//
//    //  택분실게시판 - 리스트호출 테이블
//    public ResponseEntity<Map<String, Object>> lostGalleryList(String searchString, LocalDateTime filterFromDt, LocalDateTime filterToDt, Pageable pageable, HttpServletRequest request, String type) {
//        log.info("lostGalleryList 호출성공");
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String frbrCode;
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//        if(type.equals("1")){
//            frbrCode = (String) claims.get("brCode"); // 현재 지사 코드
//            log.info("현재 접속한 지사 코드 : "+frbrCode);
//        }else{
//            frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
//            log.info("현재 접속한 소속된 지사 코드 : "+frbrCode);
//        }
//
//        // 검색조건
////        log.info("searchString : "+searchString);
////        log.info("filterFromDt : "+filterFromDt);
////        log.info("filterToDt : "+filterToDt);
//        Page<TagGalleryListDto> tagGalleryListDtoPage = tagGalleryRepository.findByTagGalleryList(searchString, filterFromDt, filterToDt, frbrCode, pageable);
//        return ResponseEntity.ok(res.ResponseEntityPage(tagGalleryListDtoPage,type));
//    }
//
//    //  택분실게시판 - 글보기
//    public ResponseEntity<Map<String, Object>> lostGalleryView(Long htId, HttpServletRequest request, String type) {
//
//        log.info("tagGalleryView 호출성공");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//
//        String code;
//        if(type.equals("1")){
//            code = (String) claims.get("brCode"); // 현재 지사 코드
//        }else{
//            code = (String) claims.get("frbrCode"); // 소속된 지사 코드
//        }
//        log.info("소속된 지사 코드 : "+code);
//
//        // 검색조건
////        log.info("htId : "+htId);
//        TagGalleryViewDto tagGalleryViewDto = tagGalleryRepository.findByTagGalleryView(htId, code);
//
//        List<TagGalleryFileListDto> tagGalleryFileListDtos = tagGalleryFileRepository.findByTagGalleryFileList(htId);
//
//        HashMap<String,Object> tagGalleryViewInfo = new HashMap<>();
//        if(tagGalleryViewDto != null){
//            if(!tagGalleryViewDto.getBrCode().equals(code)){
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP026.getCode(), "해당 글의 "+ ResponseErrorCode.TP026.getDesc(), null, null));
//            }
//            tagGalleryViewInfo.put("htId", tagGalleryViewDto.getHtId());
//            tagGalleryViewInfo.put("isWritter", type); // 1이면 지사, 2이면 가맹점
//            tagGalleryViewInfo.put("subject", tagGalleryViewDto.getSubject());
//            tagGalleryViewInfo.put("content", tagGalleryViewDto.getContent());
//            tagGalleryViewInfo.put("name", tagGalleryViewDto.getName());
//            tagGalleryViewInfo.put("insertDateTime", tagGalleryViewDto.getInsertDateTime());
//            tagGalleryViewInfo.put("fileList", tagGalleryFileListDtos);
//
//            TagGalleryViewSubDto tagGalleryViewPreDto = tagGalleryRepository.findByTagGalleryPreView(tagGalleryViewDto.getHtId(), code);
//            if(tagGalleryViewPreDto != null){
//                tagGalleryViewInfo.put("prevId", tagGalleryViewPreDto.getSubId());
//                tagGalleryViewInfo.put("prevSubject", tagGalleryViewPreDto.getSubSubject());
//                tagGalleryViewInfo.put("prevInsertDateTime", tagGalleryViewPreDto.getSubInsertDateTime());
//            }else{
//                tagGalleryViewInfo.put("prevId", "");
//                tagGalleryViewInfo.put("prevSubject", "이전 글은 존재하지 않습니다.");
//                tagGalleryViewInfo.put("prevInsertDateTime", "");
//            }
//            TagGalleryViewSubDto tagGalleryViewNextDto = tagGalleryRepository.findByTagGalleryNextView(tagGalleryViewDto.getHtId(), code);
//            if(tagGalleryViewNextDto != null){
//                tagGalleryViewInfo.put("nextId", tagGalleryViewNextDto.getSubId());
//                tagGalleryViewInfo.put("nextSubject", tagGalleryViewNextDto.getSubSubject());
//                tagGalleryViewInfo.put("nextvInsertDateTime", tagGalleryViewNextDto.getSubInsertDateTime());
//            }else{
//                tagGalleryViewInfo.put("nextId", "");
//                tagGalleryViewInfo.put("nextSubject", "다음 글은 존재하지 않습니다.");
//                tagGalleryViewInfo.put("nextvInsertDateTime", "");
//            }
//        }
//
//        data.put("tagGalleryViewDto",tagGalleryViewInfo);
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }
//
//    //  택분실게시판 - 댓글 리스트 호출
//    public ResponseEntity<Map<String, Object>> lostGalleryCommentList(Long htId, HttpServletRequest request) {
//        log.info("lostGalleryCommentList 호출성공");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String login_id = claims.getSubject(); // 현재 아이디
//
//        // 검색조건
////        log.info(htId+" 의 댓글리스트 호출");
//        List<TagGalleryCommentListDto> tagGalleryCommentList = tagGalleryCommentRepository.findByTagGalleryCommentList(htId,login_id);
//        data.put("commentListDto",tagGalleryCommentList);
//
//        return ResponseEntity.ok(res.dataSendSuccess(data));
//    }
//
//    //  택분실게시판 - 댓글 작성 and 수정
//    public ResponseEntity<Map<String, Object>> lostGalleryCommentSave(Long hcId, Long htId, String type, String comment, Long preId, HttpServletRequest request) {
//        log.info("lostGalleryCommentSave 호출");
//
//        AjaxResponse res = new AjaxResponse();
//
//        // 클레임데이터 가져오기
//        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
//        String login_id = claims.getSubject(); // 현재 아이디
//        log.info("현재 접속한 아이디 : "+login_id);
//
//        if(hcId != null){
//            Optional<TagGalleryComment> optionalTagGalleryComment = tagGalleryCommentRepository.findById(hcId);
//            if(optionalTagGalleryComment.isPresent()){
//                log.info("수정 댓글입니다.");
//                optionalTagGalleryComment.get().setHcComment(comment);
//                optionalTagGalleryComment.get().setModify_id(login_id);
//                optionalTagGalleryComment.get().setModifyDateTime(LocalDateTime.now());
//                tagGalleryCommentRepository.save(optionalTagGalleryComment.get());
//            }
//        }else{
//            log.info("신규 댓글입니다.");
//            TagGalleryComment tagGalleryComment = new TagGalleryComment();
//            tagGalleryComment.setHtId(htId);
//            tagGalleryComment.setHcComment(comment);
//            tagGalleryComment.setHcType(type);
//            if(type.equals("2")){
//                tagGalleryComment.setHcPreId(preId);
//            }
//            tagGalleryComment.setInsert_id(login_id);
//            tagGalleryComment.setInsertDateTime(LocalDateTime.now());
//            tagGalleryCommentRepository.save(tagGalleryComment);
//        }
//
//        return ResponseEntity.ok(res.success());
//    }

}
