package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountRepository;
import com.broadwave.toppos.Account.AcountDtos.AccountHeadHeaderDto;
import com.broadwave.toppos.Account.AcountDtos.AccountHeadInfoDto;
import com.broadwave.toppos.Head.Branch.BranchDtos.head.BranchSearchInfoDto;
import com.broadwave.toppos.Head.Branch.BranchRepository;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseSearchInfoDto;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2022-05-10
 * Time :
 * Remark : Toppos 본사 정보관련 서비스
 */
@Slf4j
@Service
public class HeadInfoService {

    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    @Autowired
    public HeadInfoService(ModelMapper modelMapper, TokenProvider tokenProvider, PasswordEncoder passwordEncoder,
                           AccountRepository accountRepository, BranchRepository branchRepository, FranchiseRepository franchiseRepository){
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    // 현재 로그인한 본사 정보 가져오기
    public ResponseEntity<Map<String, Object>> headHeaderData(HttpServletRequest request) {

        log.info("headHeaderData 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        AccountHeadHeaderDto accountHeadHeaderDto =  accountRepository.findByHeadHeaderInfo(login_id);
        data.put("accountHeaderData", accountHeadHeaderDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사 나의정보관리 수정 API
    @javax.transaction.Transactional
    public ResponseEntity<Map<String, Object>> headMyInfoSave(String userEmail, String userTel, String nowPassword, String newPassword, String checkPassword, HttpServletRequest request) {
        log.info("headMyInfoSave 호출");

        log.info("userEmail : "+userEmail);
        log.info("userTel : "+userTel);
        log.info("nowPassword : "+nowPassword);
        log.info("newPassword : "+newPassword);
        log.info("checkPassword : "+checkPassword);

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        Optional<Account> optionalAccount = accountRepository.findByUserid(login_id);

        //수정일때
        if(!optionalAccount.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "나의 "+ResponseErrorCode.TP005.getDesc(), "문자", "고객센터에 문의해주세요."));
        }else{
            if(!userEmail.equals("")) {
                optionalAccount.get().setUseremail(userEmail);
            }
            if(!userTel.equals("")){
                optionalAccount.get().setUsertel(userTel);
            }

            if(!nowPassword.equals("") && !newPassword.equals("") && !checkPassword.equals("") ) {

                //현재암호비교
                if (!passwordEncoder.matches(nowPassword,optionalAccount.get().getPassword())){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP020.getCode(), "현재 "+ResponseErrorCode.TP020.getDesc(), null, null));
                }

                if(!newPassword.equals(checkPassword)){
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP021.getCode(), ResponseErrorCode.TP021.getDesc(), null, null));
                }
                optionalAccount.get().setPassword(passwordEncoder.encode(checkPassword));
            }

            Account accountSave =  accountRepository.save(optionalAccount.get());
            log.info("사용자정보(패스워드)수정 성공 아이디 : " + accountSave.getUserid() +"'" );
        }


        return ResponseEntity.ok(res.success());
    }

    // 현재 본사의 정보 호출하기
    public ResponseEntity<Map<String, Object>> headMyInfo(HttpServletRequest request) {
        log.info("headMyInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        AccountHeadInfoDto headInfoDto =  accountRepository.findByHeadInfo(login_id);
        data.put("headInfoDto",headInfoDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 지사리스트, 지사소속된 가맹점 리스트 호출 API
    public ResponseEntity<Map<String, Object>> headBrFrInfoList() {
        log.info("headBrFrInfoList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<BranchSearchInfoDto> branchListDto = branchRepository.findByBranchSearchInfo();
        List<FranchiseSearchInfoDto> franchiseListDto = franchiseRepository.findByFranchiseSearchInfo();

        data.put("branchList",branchListDto);
        data.put("franchiseList",franchiseListDto);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

}
