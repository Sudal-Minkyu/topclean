package com.broadwave.toppos.Mobile;

import com.broadwave.toppos.User.UserService.InspectService;
import com.broadwave.toppos.User.UserService.ReceiptService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-05-02
 * Time :
 * Remark : Toppos Mobile RestController -> unAuth 미인증(API 시큐리티 없음) 호출, Auth 인증 호출
 */
@Slf4j
@RestController
@RequestMapping("/api/mobile")
public class MobileRestController {

    private final ReceiptService receiptService; // 가맹점 접수페이지 전용 서비스
    private final InspectService inspectService; // 통합조회 서비스

    @Autowired
    public MobileRestController(ReceiptService receiptService, InspectService inspectService) {
        this.receiptService = receiptService;
        this.inspectService = inspectService;
    }

    // 모바일 접수페이지 모바일 영수증 출력
    @GetMapping("unAuth/requestPaymentMobilePaper")
    public ResponseEntity<Map<String,Object>> requestPaymentMobilePaper(@RequestParam(value="frNo", defaultValue="") String frNo, @RequestParam(value="frId", defaultValue="") Long frId){
        return receiptService.requestPaymentPaper(null, frNo, frId);
    }

    // 모바일 가맹검품 조회 - 확인품 정보 요청
    @GetMapping("unAuth/franchiseInspectionMobileInfo")
    public ResponseEntity<Map<String,Object>> franchiseInspectionInfo(@RequestParam(value="fiId", defaultValue="") Long fiId){
        return inspectService.inspectionInfo(fiId, "0", null);
    }

    // 모바일  통합조회용 - 검품 고객 수락/거부
    @PostMapping("unAuth/franchiseInspectionYn")
    public ResponseEntity<Map<String,Object>> franchiseInspectionYn(@RequestParam(value="fiId", defaultValue="") Long fiId,
                                                                    @RequestParam(value="type", defaultValue="") String type,
                                                                    @RequestParam(value="fiAddAmt", defaultValue="") Integer fiAddAmt){
        return inspectService.franchiseInspectionYn(fiId, type, fiAddAmt, null);
    }
}
