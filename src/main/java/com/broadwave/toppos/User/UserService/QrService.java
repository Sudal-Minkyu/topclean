package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseNameInfoDto;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrClose;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos.QrCloseCountListDto;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseDtos.QrCloseCountSubListDto;
import com.broadwave.toppos.User.ReuqestMoney.Mobile.QrClose.QrCloseRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-07-12
 * Time :
 * Remark : Toppos Qr 관련 서비스
 */
@Slf4j
@Service
public class QrService {

    private final TokenProvider tokenProvider;
    private final HeadService headService;
    private final ReceiptStateService receiptStateService;
    private final QrCloseRepository qrCloseRepository;

    @Autowired
    public QrService(TokenProvider tokenProvider, HeadService headService, ReceiptStateService receiptStateService, QrCloseRepository qrCloseRepository){
        this.tokenProvider = tokenProvider;
        this.headService = headService;
        this.receiptStateService = receiptStateService;
        this.qrCloseRepository = qrCloseRepository;
    }

    // 가맹점명 호출
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
    public ResponseEntity<Map<String,Object>> collectProcess(@RequestParam("frCode")String frCode) {

        log.info("collectProcess 호출성공");

        LocalDateTime nowDate = LocalDateTime.now();
        log.info("현재날짜 : "+nowDate);
        log.info("가맹코드 : "+frCode);

        AjaxResponse res = new AjaxResponse();

        // 수기마감 페이지에 보여줄 리스트 호출
        int requestDetailMobileListDtos = receiptStateService.findByRequestDetailMobileCloseList(frCode);

        QrClose qrClose = new QrClose();
        qrClose.setFrCode(frCode);
        qrClose.setFqCloseCnt(requestDetailMobileListDtos);
        qrClose.setInsertYyyymmdd(nowDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        qrClose.setInsertDt(nowDate);
        qrCloseRepository.save(qrClose);

        return ResponseEntity.ok(res.success());
    }

    // QR 건수 조회 왼쪽리스트 호출 API
    public ResponseEntity<Map<String, Object>> branchQrCloseCntList(String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("branchQrCloseCntList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<QrCloseCountListDto> qrCloseCountListDtos =  qrCloseRepository.findByQrClostCntList(brCode, filterFromDt, filterToDt);
        data.put("gridListData",qrCloseCountListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // QR 건수 조회 오른쪽리스트 호출 API
    public ResponseEntity<Map<String, Object>> branchQrCloseCntSubList(String insertYyyymmdd, HttpServletRequest request) {
        log.info("branchQrCloseCntSubList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("insertYyyymmdd : "+insertYyyymmdd);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<QrCloseCountSubListDto> qrCloseCountSubListDtos =  qrCloseRepository.findByQrClostCntSubList(brCode, insertYyyymmdd);
        data.put("gridListData",qrCloseCountSubListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


}
