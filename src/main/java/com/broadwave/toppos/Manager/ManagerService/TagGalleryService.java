package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.TagGallery.TagGallery;
import com.broadwave.toppos.Manager.TagGallery.TagGalleryCheck.TagGalleryCheck;
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
    public ResponseEntity<Map<String, Object>> tagGalleryList(String searchString, String filterFromDt, String filterToDt, HttpServletRequest request, String type) {
        log.info("tagGalleryList 호출");

//        log.info("searchString : "+searchString);
//        log.info("filterFromDt : "+filterFromDt);
//        log.info("filterToDt : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode;
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        if(type.equals("1")){
            brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
            log.info("현재 접속한 지사 코드 : "+brCode);
        }else{
            brCode = (String) claims.get("frbrCode"); // 현재 소속된 지사의 코드(2자리) 가져오기
            log.info("현재 소속된 지사 코드 : "+brCode);
        }

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
            tagGalleryInfo.put("brCloseYn", tagGalleryListDto.getBrCloseYn());

            List<TagGalleryFileListDto> tagGalleryFileListDto = tagGalleryFileRepository.findByTagGalleryFileList(Long.parseLong(String.valueOf(tagGalleryListDto.getBtId())), 3);
            tagGalleryInfo.put("bfPathFilename", tagGalleryFileListDto);

            tagGalleryListData.add(tagGalleryInfo);
        }
        data.put("tagGalleryList",tagGalleryListData);


        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  NEW 택분실게시판 - 상세보기 호출
    public ResponseEntity<Map<String, Object>> tagGalleryDetail(Long btId, HttpServletRequest request, String type) {
        log.info("tagGalleryDetail 호출");

//        log.info("btId : "+btId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode;
        String frCode = null;
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        if(type.equals("1")){
            brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
            log.info("현재 접속한 지사 코드 : "+brCode);
        }else{
            brCode = (String) claims.get("frbrCode"); // 현재 소속된 지사의 코드(2자리) 가져오기
            frCode = (String) claims.get("frCode"); // 현재 소속된 지사의 코드(2자리) 가져오기
            log.info("현재 로그인한 가맹점 코드 : "+frCode);
            log.info("현재 소속된 지사 코드 : "+brCode);
        }

        TagGalleryDetailDto tagGalleryDetailDto = tagGalleryRepository.findByTagGalleryDetail(btId, brCode);
//        log.info("tagGalleryDetailDto : "+tagGalleryDetailDto);
        data.put("tagGallery",tagGalleryDetailDto);
        List<TagGalleryFileListDto> tagGalleryFileListDtos = tagGalleryFileRepository.findByTagGalleryFileList(btId, 6);
//        log.info("tagGalleryFileListDtos : "+tagGalleryFileListDtos);
        data.put("tagGalleryFileList", tagGalleryFileListDtos);
        List<TagGalleryCheckListDto> tagGalleryCheckListDtos = tagGalleryCheckRepository.findByTagGalleryCheckList(btId);
//        log.info("tagGalleryCheckListDtos : "+tagGalleryCheckListDtos);
        data.put("tagGalleryCheckList", tagGalleryCheckListDtos);

        int frCompleteCheck = 0;
        if(type.equals("2")){
            for(int i=0; i<tagGalleryCheckListDtos.size(); i++){
                if(tagGalleryCheckListDtos.get(i).getFrCode().equals(frCode)){
                    frCompleteCheck = 1;
                    if(tagGalleryCheckListDtos.get(i).getBrCompleteYn().equals("Y")){
                        frCompleteCheck = 2;
                    }
                }
            }
            data.put("frCompleteCheck", frCompleteCheck);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    //  NEW 택분실게시판 - 삭제
    public ResponseEntity<Map<String, Object>> tagGalleryDelete(Long btId, HttpServletRequest request) {
        log.info("tagGalleryDelete 호출");

        log.info("btId : "+btId);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        // AWS 파일 삭제
        List<TagGalleryFile> tagGalleryFileList = tagGalleryFileRepository.findByTagGalleryFile(btId);
        for(TagGalleryFile tagGalleryFile : tagGalleryFileList){
            String insertDate =tagGalleryFile.getInsertDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String path = "/toppos-manager-tagGallery/"+insertDate;
            String filename = tagGalleryFile.getBfFilename();
            awss3Service.deleteObject(path,filename);
        }
        tagGalleryFileRepository.deleteAll(tagGalleryFileList);

        List<TagGalleryCheck> tagGalleryCheckList = tagGalleryCheckRepository.findByTagGalleryCheck(btId);
        tagGalleryCheckRepository.deleteAll(tagGalleryCheckList);

        TagGallery tagGallery = tagGalleryRepository.findByTagGallery(btId);
        tagGalleryRepository.delete(tagGallery);

        return ResponseEntity.ok(res.success());
    }

    //  NEW 택분실게시판 - 해당 게시물을 종료하는 호출
    public ResponseEntity<Map<String, Object>> tagGalleryEnd(Long btId, HttpServletRequest request) {
        log.info("tagGalleryEnd 호출");

        log.info("btId : "+btId);

        AjaxResponse res = new AjaxResponse();
        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        TagGallery tagGallery = tagGalleryRepository.findByTagGallery(btId);
        if(tagGallery != null){
            tagGallery.setBrCloseYn("Y");
            tagGallery.setModify_id(login_id);
            tagGallery.setModifyDateTime(LocalDateTime.now());
            tagGalleryRepository.save(tagGallery);

            return ResponseEntity.ok(res.success());
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "해당 게시물이 "+ResponseErrorCode.TP030.getDesc(), null,null));
        }
    }

    //  NEW 택분실게시판 - 가맹점 체크 and 완료 호출
    public ResponseEntity<Map<String, Object>> tagGalleryCheck(Long btId, String type, HttpServletRequest request) {
        log.info("tagGalleryCheck 호출");

        log.info("btId : "+btId);
        log.info("type : "+type);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 소속된 지사의 코드(2자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 현재 소속된 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 소속된 지사 코드 : "+frbrCode);

        TagGalleryCheck tagGalleryCheck = tagGalleryCheckRepository.findByTagGalleryFrCode(btId, frCode);
        TagGallery tagGallery = tagGalleryRepository.findByTagGallery(btId);
//        log.info("tagGallery : "+tagGallery);

        // type = 1 이면 체크or해제, 2 이면 최종확인완료
        if(tagGalleryCheck != null){
            if(type.equals("1")){
                tagGalleryCheckRepository.delete(tagGalleryCheck);

                return ResponseEntity.ok(res.success());
            }else{
                tagGalleryCheck.setBrCompleteYn("Y");
                tagGalleryCheck.setBrCompleteDt(LocalDateTime.now());
                tagGalleryCheck.setModify_id(login_id);
                tagGalleryCheck.setModifyDateTime(LocalDateTime.now());
            }
        }else{
            tagGalleryCheck = new TagGalleryCheck();

            tagGalleryCheck.setBtId(tagGallery);
            tagGalleryCheck.setFrCode(frCode);
            if(type.equals("1")){
                tagGalleryCheck.setBrCompleteYn("N");
            }else{
                tagGalleryCheck.setBrCompleteYn("Y");
                tagGalleryCheck.setBrCompleteDt(LocalDateTime.now());
            }
            tagGalleryCheck.setInsert_id(login_id);
            tagGalleryCheck.setInsertDateTime(LocalDateTime.now());
        }
        tagGalleryCheckRepository.save(tagGalleryCheck);

        return ResponseEntity.ok(res.success());
    }





}
