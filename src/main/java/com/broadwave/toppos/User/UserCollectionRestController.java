package com.broadwave.toppos.User;

import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseNameInfoDto;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailCloseListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.user.RequestDetailMobileListDto;
import com.broadwave.toppos.User.UserService.ReceiptService;
import com.broadwave.toppos.common.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final HeadService headService;
    private final ReceiptService receiptService;

    @Autowired
    public UserCollectionRestController(HeadService headService, ReceiptService receiptService) {
        this.headService = headService;
        this.receiptService = receiptService;
    }

    // 가맹점명 호출
    @GetMapping("/info")
    public ResponseEntity<Map<String,Object>> collectInfo(@RequestParam("frCode")String frCode) {

        log.info("collectInfo 호출성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        FranchiseNameInfoDto franchiseNameInfoDto = headService.findByFranchiseNameInfo(frCode);

        if(franchiseNameInfoDto != null){
            data.put("frName",franchiseNameInfoDto.getFrName());
        }else{
            data.put("frName",null);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 수거처리
    @GetMapping("/process")
    public ResponseEntity<Map<String,Object>> collectProcess(@RequestParam("frCode")String frCode) {

        log.info("collectProcess 호출성공");

        LocalDateTime nowTime = LocalDateTime.now();
        log.info("현재시간 : "+nowTime);
        log.info("가맹코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();
        // 수기마감 페이지에 보여줄 리스트 호출
        List<RequestDetailMobileListDto> requestDetailMobileListDtos = receiptService.findByRequestDetailMobileCloseList(frCode);
        log.info("requestDetailMobileListDtos : "+requestDetailMobileListDtos);
        List<Long> fdIdList = new ArrayList<>();
        for(RequestDetailMobileListDto requestDetailMobileListDto : requestDetailMobileListDtos){
            fdIdList.add(requestDetailMobileListDto.getFdId());
        }
        log.info("fdIdList : "+fdIdList);
//        List<InspeotYnDto> inspeotYnDtos = inspeotRepositoryCustom.findByInspeotStateList(fdIdList,"1");

        return ResponseEntity.ok(res.success());
    }










}
