package com.broadwave.toppos.Error;

import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2022-03-24
 * Time :
 * Remark : Toppos ErrorRestController
 */
@Slf4j
@RestController
@RequestMapping("/api/error") //  ( 권한 : 없음 )
public class ErrorService {

    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;
    private final ErrorRepository errorRepository;

    @Autowired
    public ErrorService(TokenProvider tokenProvider, ModelMapper modelMapper, ErrorRepository errorRepository) {
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;
        this.errorRepository = errorRepository;
    }

    // 에러메세지 저장 API
    public ResponseEntity<Map<String, Object>> errorSave(ErrorMapperDto errorMapperDto, HttpServletRequest request) {
        AjaxResponse res = new AjaxResponse();

        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject();
        log.info("현재 접속한 아이디 : "+login_id);

        Error error = modelMapper.map(errorMapperDto,Error.class);
        error.setInsert_id(login_id);
        error.setInsertDateTime(LocalDateTime.now());

        errorRepository.save(error);
        return ResponseEntity.ok(res.success());
    }

}
