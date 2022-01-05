package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountPasswordDto;
import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Head.Franohise.FranchisUserDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Addprocess.*;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailRepositoryCustom;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailSearchDto;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-01-05
 * Time :
 * Remark : Toppos 가맹점 통합조회 서비스
 */
@Slf4j
@Service
public class InspectService {

    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final HeadService headService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final AddprocessRepository addprocessRepository;
    private final RequestDetailRepositoryCustom requestDetailRepositoryCustom;

    @Autowired
    public InspectService(ModelMapper modelMapper, TokenProvider tokenProvider, AccountService accountService, HeadService headService, UserService userService,
                          RequestDetailRepositoryCustom requestDetailRepositoryCustom, PasswordEncoder passwordEncoder, AddprocessRepository addprocessRepository){
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.accountService = accountService;
        this.headService = headService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.addprocessRepository = addprocessRepository;
        this.requestDetailRepositoryCustom = requestDetailRepositoryCustom;
    }

    //  통합조회용 - 접수세부 테이블
    @PostMapping("franchiseRequestDetailSearch")
    public ResponseEntity<Map<String, Object>> franchiseRequestDetailSearch(Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt, HttpServletRequest request) {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<RequestDetailSearchDto> requestDetailSearchDtoList = requestDetailSearch(frCode, bcId, searchTag, filterCondition, filterFromDt, filterToDt);

        data.put("gridListData",requestDetailSearchDtoList);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    private List<RequestDetailSearchDto> requestDetailSearch(String frCode, Long bcId, String searchTag, String filterCondition, String filterFromDt, String filterToDt) {
        return requestDetailRepositoryCustom.requestDetailSearch(frCode, bcId, searchTag, filterCondition, filterFromDt, filterToDt);
    }













}
