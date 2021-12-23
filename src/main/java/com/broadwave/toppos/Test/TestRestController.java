package com.broadwave.toppos.Test;

import com.broadwave.toppos.Test.Girdtest.GridTestDto;
import com.broadwave.toppos.Test.Girdtest.GridTestRepositoryCustom;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

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

    private final GridTestRepositoryCustom gridTestRepositoryCustom;

    @Autowired
    public TestRestController(GridTestRepositoryCustom gridTestRepositoryCustom) {
        this.gridTestRepositoryCustom = gridTestRepositoryCustom;
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
    public ResponseEntity<Map<String,Object>> photoTest(MultipartHttpServletRequest multi){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("테스트 준비완료");

        //파일저장
        Iterator<String> files = multi.getFileNames();
        log.info("files : "+files);
        String uploadFile = files.next();
        log.info("uploadFile : "+uploadFile);
        MultipartFile mFile = multi.getFile(uploadFile);
        log.info("mFile : "+mFile);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }














}
