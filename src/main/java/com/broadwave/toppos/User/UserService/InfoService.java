package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Account.Account;
import com.broadwave.toppos.Account.AccountPasswordDto;
import com.broadwave.toppos.Account.AccountService;
import com.broadwave.toppos.Head.Franohise.FranchisUserDto;
import com.broadwave.toppos.Head.Franohise.Franchise;
import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.Addprocess.*;
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

        FranchisUserDto franchisInfoDto = headService.findByFranchiseUserInfo(frCode);
        data.put("franchisInfoDto",franchisInfoDto);

        // 수선 항목 리스트 데이터 가져오기
        List<AddprocessDto> repairListData = userService.findByAddProcessList(frCode, "1");
        log.info("repairListData : "+repairListData);
        log.info("repairListData 사이즈 : "+repairListData.size());
        data.put("repairListData",repairListData);

        // 추가요금 항목 리스트 데이터 가져오기
        List<AddprocessDto> addAmountData = userService.findByAddProcessList(frCode, "2");
        log.info("addAmountData : "+addAmountData);
        log.info("addAmountData 사이즈 : "+addAmountData.size());
        data.put("addAmountData",addAmountData);

        // 추가요금 항목 리스트 데이터 가져오기
        List<AddprocessDto> keyWordData = userService.findByAddProcessList(frCode, "3");
        log.info("keyWordData : "+keyWordData);
        log.info("keyWordData 사이즈 : "+keyWordData.size());
        data.put("keyWordData",keyWordData);

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
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP020.getCode(), ResponseErrorCode.TP020.getDesc(), null, null));
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

    // 수선항목,추가항목,상용구 - 저장&삭제
    public ResponseEntity<Map<String, Object>> franchiseAddProcess(AddprocessSet addprocessSet, HttpServletRequest request) {

        log.info("itemGroupA 호출");
        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        Optional<Franchise> optionalFranchise = headService.findByFrCode(frCode);

        List<Addprocess> addprocessList = new ArrayList<>();
        if(!optionalFranchise.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "가맹점 "+ResponseErrorCode.TP005.getDesc(), null, null));
        }else{

            ArrayList<AddprocessMapperDto> addList = addprocessSet.getAdd(); // 추가 리스트 얻기
            ArrayList<AddprocessMapperDto> deleteList = addprocessSet.getDelete(); // 제거 리스트 얻기

            // 저장 시작.
            for (AddprocessMapperDto addprocessMapperDto : addList) {
                Addprocess addprocess = modelMapper.map(addprocessMapperDto, Addprocess.class);
                addprocess.setFrId(optionalFranchise.get());
                addprocess.setFrCode(optionalFranchise.get().getFrCode());
                addprocess.setInsert_id(login_id);
                addprocess.setInsertDateTime(LocalDateTime.now());
                addprocessList.add(addprocess);
            }

            if(addprocessList.size() != 0){
                addprocessRepository.saveAll(addprocessList);
                addprocessList.clear();
            }

            // 삭제로직 실행 : 데이터베이스에 해당 데이터가 존재하지 않으면 리턴처리한다.
            for (AddprocessMapperDto addprocessMapperDto : deleteList) {
                Optional<Addprocess> optionalAddprocess = userService.findByAddProcess(addprocessMapperDto.getBaType(),addprocessMapperDto.getBaName());
                if (!optionalAddprocess.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP022.getCode(), "삭제 할 " + ResponseErrorCode.TP022.getDesc(), "문자", "다시 시도해주세요. 삭제할 명칭 : " + addprocessMapperDto.getBaName()));
                }else {
                    addprocessList.add(optionalAddprocess.get());
                }
            }

            if(addprocessList.size() != 0){
                addprocessRepository.deleteAll(addprocessList);
                addprocessList.clear();
            }
        }

        return ResponseEntity.ok(res.success());
    }



}
