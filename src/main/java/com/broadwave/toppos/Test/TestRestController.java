package com.broadwave.toppos.Test;

import com.broadwave.toppos.Aws.AWSS3Service;
import com.broadwave.toppos.Test.Girdtest.GridTestDto;
import com.broadwave.toppos.Test.Girdtest.GridTestRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-11-03
 * Time :
 * Remark : 테스트 RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestRestController {

    private final AWSS3Service awss3Service;
    private final GridTestRepositoryCustom gridTestRepositoryCustom;

    @Autowired
    public TestRestController(GridTestRepositoryCustom gridTestRepositoryCustom, AWSS3Service awss3Service) {
        this.gridTestRepositoryCustom = gridTestRepositoryCustom;
        this.awss3Service = awss3Service;
    }

    // 그리드 리스트 호출 테스트
    @GetMapping("grid/list")
    public ResponseEntity<Map<String,Object>> gridList(){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> gridListData = new ArrayList<>();
        HashMap<String,Object> girdInfo;

        List<GridTestDto> gridTestDto = gridTestRepositoryCustom.findByAllList();

        for (GridTestDto gird : gridTestDto) {

            girdInfo = new HashMap<>();

            girdInfo.put("testName", gird.getTestName());
            girdInfo.put("testOld", gird.getTestOld());
            girdInfo.put("testGender", gird.getTestGender());
            girdInfo.put("testMoney", gird.getTestMoney());
            gridListData.add(girdInfo);

        }

        log.info("그리드 테스트 리스트 : "+gridListData);
        data.put("gridListData",gridListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 사진테스트 api
    @PostMapping("photoTest")
    public ResponseEntity<Map<String,Object>> photoTest(MultipartHttpServletRequest multi) throws IOException {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("테스트 준비완료");

        //파일저장
        Iterator<String> files = multi.getFileNames();
        String uploadFile = files.next();
        MultipartFile mFile = multi.getFile(uploadFile);
        log.info("mFile : "+mFile);

        assert mFile != null;
        if(!mFile.isEmpty()) {
            // 파일 중복명 처리
            String genId = UUID.randomUUID().toString().replace("-", "");;
            log.info("genId : "+genId);

            // S3에 저장 할 파일주소
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
            String filePath = "/toppos-franchise/" + date.format(new Date());
            log.info("filePath : "+filePath);
            String storedFileName = genId + ".png";
            log.info("storedFileName : "+storedFileName);
            String aws_firle_url = awss3Service.uploadObject(mFile, storedFileName, filePath);
            log.info("aws_firle_url : "+aws_firle_url);
            data.put("fileUrl",aws_firle_url);
        }else{
            log.info("사진파일을 못불러왔습니다.");
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

//    private String getExtension(String fileName) {
//        int dotPosition = fileName.lastIndexOf('.');
//
//        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
//            return fileName.substring(dotPosition + 1);
//        } else {
//            return "";
//        }
//    }

    //  택분실게시판 - 리스트호출 테이블(테스트)
    @PostMapping("/lostNoticeList")
    public ResponseEntity<Map<String,Object>> lostNoticeList(
            @RequestParam("searchType")String searchType,
            @RequestParam("searchString")String searchString,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "someField") Pageable pageable) {

        log.info("lostNoticeList 테스트 호출성공");

//        AjaxResponse res = new AjaxResponse();
////        HashMap<String, Object> data = new HashMap<>();
//        Page<TagNoticeListDto> tagNoticeListDtoPage = tagNoticeRepositoryCustom.findByTagNoticeList(searchType, searchString, pageable);

//        data.put("tagNoticeListDtoPage",tagNoticeListDtoPage);
//        return ResponseEntity.ok(res.ResponseEntityPage(tagNoticeListDtoPage));
        return null;
    }










}
