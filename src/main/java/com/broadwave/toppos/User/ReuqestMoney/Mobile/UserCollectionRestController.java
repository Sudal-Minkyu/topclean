package com.broadwave.toppos.User.ReuqestMoney.Mobile;

import com.broadwave.toppos.User.UserService.QrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-03-04
 * Time :
 * Remark : 비로그인 모바일 수거처리 RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/collection")
public class UserCollectionRestController {

    private final QrService qrService;

    @Autowired
    public UserCollectionRestController(QrService qrService) {
        this.qrService = qrService;
    }

    // 가맹점명 호출
    @GetMapping("/info")
    public ResponseEntity<Map<String,Object>> collectInfo(@RequestParam("frCode")String frCode) {
        return qrService.collectInfo(frCode);
    }

    // 수거처리
    @GetMapping("/process")
    public ResponseEntity<Map<String,Object>> collectProcess(@RequestParam("frCode")String frCode) {
        return qrService.collectProcess(frCode);
    }










}
