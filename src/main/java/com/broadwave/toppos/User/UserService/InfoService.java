package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AcountDtos.AccountPasswordDto;
import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.FranchiseUserDto;
import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Addprocess.*;
import com.broadwave.toppos.User.Addprocess.AddprocessDtos.AddprocessDto;
import com.broadwave.toppos.User.Addprocess.AddprocessDtos.AddprocessMapperDto;
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
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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
    private final AccountService accountService;
    private final HeadService headService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final AddprocessRepository addprocessRepository;

    @Autowired
    public InfoService(ModelMapper modelMapper, TokenProvider tokenProvider, AccountService accountService, HeadService headService, UserService userService,
                       PasswordEncoder passwordEncoder, AddprocessRepository addprocessRepository){
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.accountService = accountService;
        this.headService = headService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.addprocessRepository = addprocessRepository;
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

        FranchiseUserDto franchisInfoDto = headService.findByFranchiseUserInfo(frCode);
        data.put("franchisInfoDto",franchisInfoDto);

        // 수선 항목 리스트 데이터 가져오기
        List<AddprocessDto> repairListData = userService.findByAddProcessDtoList(frCode, "1");
        log.info("repairListData : "+repairListData);
        log.info("repairListData 사이즈 : "+repairListData.size());
        data.put("repairListData",repairListData);

        // 추가요금 항목 리스트 데이터 가져오기
        List<AddprocessDto> addAmountData = userService.findByAddProcessDtoList(frCode, "2");
        log.info("addAmountData : "+addAmountData);
        log.info("addAmountData 사이즈 : "+addAmountData.size());
        data.put("addAmountData",addAmountData);

        // 추가요금 항목 리스트 데이터 가져오기
        List<AddprocessDto> keyWordData = userService.findByAddProcessDtoList(frCode, "3");
        log.info("keyWordData : "+keyWordData);
        log.info("keyWordData 사이즈 : "+keyWordData.size());
        data.put("keyWordData",keyWordData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 나의정보 수정 API
    public ResponseEntity<Map<String,Object>> franchiseMyInfoSave(FranchiseUserDto franchiseUserDto, HttpServletRequest request) {
        log.info("franchiseMyInfoSave 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        Optional<Franchise> optionalFranohise = headService.findByFrCode(frCode);
        if(optionalFranohise.isPresent()){
            Franchise franchise = modelMapper.map(optionalFranohise.get(), Franchise.class);

//            franchise.setFrBusinessNo(franchiseUserDto.getFrBusinessNo());
            franchise.setFrRpreName(franchiseUserDto.getFrRpreName());
            franchise.setFrTelNo(franchiseUserDto.getFrTelNo());
//            franchise.setFrTagNo(franchiseUserDto.getFrTagNo());
//            franchise.setFrLastTagno(franchiseUserDto.getFrTagNo() + optionalFranohise.get().getFrLastTagno().substring(3, 7));
//            franchise.setFrEstimateDuration(franchiseUserDto.getFrEstimateDuration());
            franchise.setFrPostNo(franchiseUserDto.getFrPostNo());
            franchise.setFrAddress(franchiseUserDto.getFrAddress());
            franchise.setFrAddressDetail(franchiseUserDto.getFrAddressDetail());
            franchise.setFrMultiscreenYn(franchiseUserDto.getFrMultiscreenYn());
            franchise.setFrDepositAmount(franchiseUserDto.getFrDepositAmount());
            franchise.setFrRentalAmount(franchiseUserDto.getFrRentalAmount());

            franchise.setModify_id(login_id);
            franchise.setModifyDateTime(LocalDateTime.now());

            Franchise franchiseSave =  headService.franchise(franchise);
            log.info("가맹점 마이페이지 수정 성공 Frcode : '" + franchiseSave.getFrCode() + "'");
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "나의 "+ResponseErrorCode.TP005.getDesc(), "문자", "고객센터에 문의해주세요."));
        }

        return ResponseEntity.ok(res.success());
    }

    // 가맹점 비밀번호 수정 API
    @Transactional
    public ResponseEntity<Map<String, Object>> franchisePassword(AccountPasswordDto accountPasswordDto, HttpServletRequest request) {
        log.info("franchisePassword 호출");

        AjaxResponse res = new AjaxResponse();

        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        Optional<Account> optionalAccount = accountService.findByUserid(login_id);

        //수정일때
        if(!optionalAccount.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "나의 "+ResponseErrorCode.TP005.getDesc(), "문자", "고객센터에 문의해주세요."));
        }else{
            //현재암호비교
            if (!passwordEncoder.matches(accountPasswordDto.getOldpassword(),optionalAccount.get().getPassword())){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP020.getCode(), "현재 "+ResponseErrorCode.TP020.getDesc(), null, null));
            }
            if( !accountPasswordDto.getNewpassword().equals(accountPasswordDto.getPasswordconfirm()) ){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP021.getCode(), ResponseErrorCode.TP021.getDesc(), null, null));
            }
            Account account;
            account = modelMapper.map(optionalAccount.get(),Account.class);
            account.setPassword(accountPasswordDto.getPasswordconfirm());
            account.setModify_id(login_id);
            account.setModifyDateTime(LocalDateTime.now());

            Account accountSave =  accountService.save(account);

            log.info("사용자정보(패스워드)수정 성공 아이디 : " + accountSave.getUserid() +"'" );
        }

        return ResponseEntity.ok(res.success());
    }

    // 수선항목,추가항목,상용구 - 저장&수정&삭제
    public ResponseEntity<Map<String, Object>> franchiseAddProcess(AddprocessSet addprocessSet, HttpServletRequest request) {
        log.info("franchiseAddProcess 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        Optional<Franchise> optionalFranchise = headService.findByFrCode(frCode);

        List<Addprocess> saveAddProcessList = new ArrayList<>();
        if(!optionalFranchise.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "가맹점 "+ResponseErrorCode.TP005.getDesc(), null, null));
        }else{

            String baType = addprocessSet.getBaType(); // 타입
            if(baType == null){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "저장 유형 타입"+ResponseErrorCode.TP022.getDesc(), "문자", "baType : "+baType));
            }else{
                ArrayList<AddprocessMapperDto> addProcessList = addprocessSet.getList(); // 추가 리스트 얻기

                // 저장하기전에 삭제처리하기
                List<Addprocess> deleteAddprocessList = userService.findByAddProcessList(frCode, baType);
                if(deleteAddprocessList.size() != 0){
                    addprocessRepository.deleteAll(deleteAddprocessList);
                }

                // 저장하기
                for(AddprocessMapperDto addprocessMapperDto : addProcessList){
                    Addprocess addprocess = modelMapper.map(addprocessMapperDto,Addprocess.class);
                    addprocess.setBaType(baType);
                    addprocess.setFrId(optionalFranchise.get());
                    addprocess.setFrCode(optionalFranchise.get().getFrCode());
                    addprocess.setInsert_id(login_id);
                    addprocess.setInsertDateTime(LocalDateTime.now());
                    saveAddProcessList.add(addprocess);
                }

                if(saveAddProcessList.size() != 0){
                    addprocessRepository.saveAll(saveAddProcessList);
                }
            }

        }

        return ResponseEntity.ok(res.success());
    }

    // 택번호 변경시 비밀번호 입력후 확인 API
    public ResponseEntity<Map<String, Object>> franchiseCheck(String password, HttpServletRequest request) {
        log.info("franchiseCheck 호출");

        AjaxResponse res = new AjaxResponse();

        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        Optional<Account> optionalAccount = accountService.findByUserid(login_id);

        //수정일때
        if(!optionalAccount.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "나의 "+ResponseErrorCode.TP005.getDesc(), "문자", "고객센터에 문의해주세요."));
        }else{
            //현재암호비교
            if (!passwordEncoder.matches(password,optionalAccount.get().getPassword())){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP020.getCode(), ResponseErrorCode.TP020.getDesc(), null, null));
            }
        }
        return ResponseEntity.ok(res.success());
    }


}
