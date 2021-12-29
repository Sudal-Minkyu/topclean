package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.AccountPasswordDto;
import com.broadwave.toppos.Head.Franohise.FranchisUserDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 나의정보 서비스
 */
@Slf4j
@Service
public class InfoService {

    private final ModelMapper modelMapper;
    private final HeadService headService;
    private final TokenProvider tokenProvider;

    @Autowired
    public InfoService(ModelMapper modelMapper, TokenProvider tokenProvider, HeadService headService){
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.headService = headService;
    }

    // 현재 가맹점의 정보 호출하기
    public ResponseEntity<Map<String, Object>> myInfo(HttpServletRequest request) {
        log.info("myInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        FranchisUserDto franchisInfoDto = headService.findByFranchiseUserInfo(frCode);
        data.put("franchisInfoDto",franchisInfoDto);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 나의정보 수정 API
    public ResponseEntity<Map<String,Object>> franchiseMyInfoSave(FranchisUserDto franchisUserDto, HttpServletRequest request) {
        log.info("franchiseMyInfoSave 호출");

        AjaxResponse res = new AjaxResponse();

        Franchise franchise = modelMapper.map(franchisUserDto, Franchise.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Franchise> optionalFranohise = headService.findByFrCode(franchisUserDto.getFrCode());
        if(optionalFranohise.isPresent()){
            franchise.setId(optionalFranohise.get().getId());

            franchise.setFrRefCode(optionalFranohise.get().getFrRefCode());
            franchise.setFrContractState(optionalFranohise.get().getFrContractState());
            franchise.setFrPriceGrade(optionalFranohise.get().getFrPriceGrade());
            franchise.setFrRemark(optionalFranohise.get().getFrRemark());

            franchise.setBrId(optionalFranohise.get().getBrId());
            franchise.setBrCode(optionalFranohise.get().getBrCode());
            franchise.setBrAssignState(optionalFranohise.get().getBrAssignState());

            if(franchisUserDto.getFrTagNo() == null || franchisUserDto.getFrTagNo().equals("")) {
                franchise.setFrTagNo(franchisUserDto.getFrCode());
                franchise.setFrLastTagno(franchisUserDto.getFrCode()+"0000");
            }else{
                if(optionalFranohise.get().getFrLastTagno() != null){
                    franchise.setFrLastTagno(franchisUserDto.getFrTagNo() + optionalFranohise.get().getFrLastTagno().substring(3, 7));
                }else{
                    franchise.setFrLastTagno(franchisUserDto.getFrTagNo() + "0000");
                }
            }

            franchise.setModify_id(login_id);
            franchise.setModifyDateTime(LocalDateTime.now());
            franchise.setInsert_id(optionalFranohise.get().getInsert_id());
            franchise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "나의 "+ResponseErrorCode.TP005.getDesc(), "문자", "고객센터에 문의해주세요."));
        }

        Franchise franchiseSave =  headService.franchise(franchise);
        log.info("가맹점 마이페이지 수정 성공 Frcode : '" + franchiseSave.getFrCode() + "'");
        return ResponseEntity.ok(res.success());
    }

    // 가맹점 비밀번호 수정 API
    public ResponseEntity<Map<String, Object>> franchisePassword(AccountPasswordDto accountPasswordDto, HttpServletRequest request) {
        log.info("franchisePassword 호출");

        AjaxResponse res = new AjaxResponse();
        log.info("accountPasswordDto : "+accountPasswordDto);

        return ResponseEntity.ok(res.success());
    }
}
