package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Find.FindListDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.Find.FindRepository;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 상품정렬 서비스
 */
@Slf4j
@Service
public class FindService {

    private final TokenProvider tokenProvider;
    private final FindRepository findRepository;
    @Autowired
    public FindService(TokenProvider tokenProvider, FindRepository findRepository){
        this.tokenProvider = tokenProvider;
        this.findRepository = findRepository;
    }

    // 지사 물건찾기 리스트 호출
    public ResponseEntity<Map<String, Object>> objectFind(Long franchiseId, String filterFromDt, String filterToDt, String ffState,HttpServletRequest request) {
        log.info("objectFind 호출");

        log.info("franchiseId : "+franchiseId);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);
        log.info("ffState : "+ffState);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String brCode = (String) claims.get("brCode"); // 현재 지사 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 지사 코드 : "+brCode);

        List<FindListDto> findListDtos = findRepository.findByFindList(franchiseId, brCode, filterFromDt, filterToDt, ffState);
        log.info("findListDtos : "+findListDtos);

        data.put("gridListData",findListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 물건찾기 할 리스트 호출
    public ResponseEntity<Map<String, Object>> franchiseObjectFind(Long bcId, String filterFromDt, String filterToDt, HttpServletRequest request) {
        log.info("franchiseObjectFind 호출");

        log.info("bcId : "+bcId);
        log.info("filterFromDt : "+filterFromDt);
        log.info("filterToDt : "+filterToDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        String frCode = (String) claims.get("frCode"); // 현재 가맹점 코드
        String frbrCode = (String) claims.get("frbrCode"); // 소속된 지사 코드
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("접속한 가맹점 코드 : "+frCode);
        log.info("현재 소속된 지사 코드 : "+frbrCode);

//        data.put("gridListData",findListDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
