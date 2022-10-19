package com.broadwave.toppos.Error;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
public class ErrorRestController {

    private final ErrorService errorService;

    @Autowired
    public ErrorRestController(ErrorService errorService) {
        this.errorService = errorService;
    }

    // 에러메세지 저장 API
    @PostMapping("/errorSave")
    @ApiOperation(value = "에러메세지 저장" , notes = "에러메세지를 저장한다")
    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization", value="JWT Token",required = true,dataType="string",paramType = "header")})
    public ResponseEntity<Map<String,Object>> errorSave(@ModelAttribute ErrorMapperDto errorMapperDto, HttpServletRequest request) {
        return errorService.errorSave(errorMapperDto, request);
    }





}
