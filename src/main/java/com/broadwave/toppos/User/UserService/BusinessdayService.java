package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotDtos.InspeotYnDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot.InspeotRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetail;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepository;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouceForce;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.force.InhouseRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-01-25
 * Time :
 * Remark : Toppos 가맹점 일일영업일보 서비스
 */
@Slf4j
@Service
public class BusinessdayService {

    private final TokenProvider tokenProvider;

    private final InhouseRepository inhouseRepository;
    private final InspeotRepositoryCustom inspeotRepositoryCustom;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    @Autowired
    public BusinessdayService(TokenProvider tokenProvider,
                              InhouseRepository inhouseRepository, InspeotRepositoryCustom inspeotRepositoryCustom,
                              RequestDetailRepository requestDetailRepository, RequestDetailRepositoryCustom requestDetailRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.inhouseRepository = inhouseRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.inspeotRepositoryCustom = inspeotRepositoryCustom;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    //  일일영업일보 - 리스트호출 테이블
    public ResponseEntity<Map<String, Object>> businessdayList(String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("businessdayList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);
        log.info("소속된 지사 코드 : "+frbrCode);







        return ResponseEntity.ok(res.dataSendSuccess(data));

    }







}
