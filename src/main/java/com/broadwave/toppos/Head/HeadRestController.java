package com.broadwave.toppos.Head;

import com.broadwave.toppos.Head.Branoh.Branoh;
import com.broadwave.toppos.Head.Branoh.BranohListDto;
import com.broadwave.toppos.Head.Branoh.BranohMapperDto;
import com.broadwave.toppos.Head.Franohise.Franohise;
import com.broadwave.toppos.Head.Franohise.FranohiseListDto;
import com.broadwave.toppos.Head.Franohise.FranohiseMapperDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2021-11-08
 * Time :
 * Remark : Toppos Head RestController
 */
@Slf4j
@RestController
@RequestMapping("/api/head") //  ( 권한 : 어드민, 본사일반 )
public class HeadRestController {

    private final HeadService headService;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    @Autowired
    public HeadRestController(TokenProvider tokenProvider, ModelMapper modelMapper, HeadService headService) {
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
        this.headService = headService;
    }

    // 지사 등록 API
    @PostMapping("branohSave")
    public ResponseEntity<Map<String,Object>> branohSave(@ModelAttribute BranohMapperDto branohMapperDto, HttpServletRequest request){

        log.info("지사등록");

        AjaxResponse res = new AjaxResponse();

        Branoh branoh = modelMapper.map(branohMapperDto, Branoh.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Branoh> optionalBranoh  =  headService.findByBrCode(branohMapperDto.getBrCode());
        if( optionalBranoh.isPresent()){
//            log.info("널이 아닙니다 : 업데이트");
            branoh.setId(optionalBranoh.get().getId());

            branoh.setModify_id(login_id);
            branoh.setModifyDateTime(LocalDateTime.now());
            branoh.setInsert_id(optionalBranoh.get().getInsert_id());
            branoh.setInsertDateTime(optionalBranoh.get().getInsertDateTime());
        }else{
//            log.info("널입니다. : 신규생성");
            branoh.setInsert_id(login_id);
            branoh.setInsertDateTime(LocalDateTime.now());
        }

        Branoh branohSave =  headService.branohSave(branoh);
        log.info("지사 저장 성공 : id '" + branohSave.getBrCode() + "'");
        return ResponseEntity.ok(res.success());

    }

    // 가맹점 등록 API
    @PostMapping("franohiseSave")
    public ResponseEntity<Map<String,Object>> franohiseSave(@ModelAttribute FranohiseMapperDto franohiseMapperDto, HttpServletRequest request){

        log.info("가맹점등록");

        AjaxResponse res = new AjaxResponse();

        Franohise franohise = modelMapper.map(franohiseMapperDto, Franohise.class);

        String login_id = CommonUtils.getCurrentuser(request);
//        log.info("현재 사용자 아이디 : "+login_id);

        Optional<Franohise> optionalFranohise  =  headService.findByFrCode(franohiseMapperDto.getFrCode());
        if( optionalFranohise.isPresent()){
//            log.info("널이 아닙니다 : 업데이트");

            franohise.setId(optionalFranohise.get().getId());

            franohise.setBrId(optionalFranohise.get().getBrId());
            franohise.setBrCode(optionalFranohise.get().getBrCode());
            franohise.setBrAssignState(optionalFranohise.get().getBrAssignState());

            franohise.setModify_id(login_id);
            franohise.setModifyDateTime(LocalDateTime.now());
            franohise.setInsert_id(optionalFranohise.get().getInsert_id());
            franohise.setInsertDateTime(optionalFranohise.get().getInsertDateTime());
        }else{
//            log.info("널입니다. : 신규생성");

            franohise.setBrId(null);
            franohise.setBrCode(null);
            franohise.setBrAssignState("01");
            franohise.setInsert_id(login_id);
            franohise.setInsertDateTime(LocalDateTime.now());
        }

        Franohise franohiseSave =  headService.franohiseSave(franohise);
        log.info("가맹점 저장 성공 : id '" + franohiseSave.getFrCode() + "'");
        return ResponseEntity.ok(res.success());
    }

    // 지사 리스트 API
    @GetMapping("branohList")
    public ResponseEntity<Map<String,Object>> branohList(){
        log.info("branohList 호출");

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> branohListData = new ArrayList<>();
        HashMap<String,Object> branohsetInfo;

        List<BranohListDto> branohListDtos = headService.findByBranohList();
//        log.info("branohListDtos : "+branohListDtos);

        for (BranohListDto branoh: branohListDtos) {

            branohsetInfo = new HashMap<>();

            branohsetInfo.put("brCode", branoh.getBrCode());
            branohsetInfo.put("brName", branoh.getBrName());
            branohsetInfo.put("brContractDt", branoh.getBrContractDt());
            branohsetInfo.put("brContractFromDt", branoh.getBrContractFromDt());
            branohsetInfo.put("brContractToDt", branoh.getBrContractToDt());
            branohsetInfo.put("brContractState", branoh.getBrContractState());
            branohsetInfo.put("brCarculateRateHq", branoh.getBrCarculateRateHq());
            branohsetInfo.put("brCarculateRateBr", branoh.getBrCarculateRateBr());
            branohsetInfo.put("brCarculateRateFr", branoh.getBrCarculateRateFr());
            branohsetInfo.put("brRemark", branoh.getBrRemark());
            branohListData.add(branohsetInfo);

        }

        log.info("지사 리스트 : "+branohListData);
        data.put("gridListData",branohListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 리스트 API
    @GetMapping("franohiseList")
    public ResponseEntity<Map<String,Object>> franohiseList(@RequestParam(value="brAssignState", defaultValue="") String brAssignState,
                                                                                        @RequestParam(value="frName", defaultValue="") String frName){
        log.info("franohiseList 호출");

        AjaxResponse res = new AjaxResponse();

        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String,Object>> franohiseListData = new ArrayList<>();
        HashMap<String,Object> franohisetInfo;

        List<FranohiseListDto> franohiseListDtos = headService.findByFranohiseList(brAssignState, frName);
//        log.info("franohiseListDtos : "+franohiseListDtos);

        for (FranohiseListDto franohise : franohiseListDtos) {

            franohisetInfo = new HashMap<>();

            franohisetInfo.put("frCode", franohise.getFrCode());
            franohisetInfo.put("frName", franohise.getFrName());
            franohisetInfo.put("frContractDt", franohise.getFrContractDt());
            franohisetInfo.put("frContractFromDt", franohise.getFrContractFromDt());
            franohisetInfo.put("frContractToDt", franohise.getFrContractToDt());
            franohisetInfo.put("frContractState", franohise.getFrContractState());
            franohisetInfo.put("frRemark", franohise.getFrRemark());
            franohisetInfo.put("brName", franohise.getBrName());
            franohiseListData.add(franohisetInfo);

        }

        log.info("가맹점리스트 : "+franohiseListData);
        data.put("gridListData",franohiseListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }













    // 가맹점코드 중복확인 API
    @GetMapping("franohiseOverlap")
    public ResponseEntity<Map<String,Object>> franohiseOverlap(@RequestParam(value="frCode", defaultValue="") String frCode){

        log.info("가맹점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        Optional<Franohise> franohiseOptional =  headService.findByFrCode(frCode);
        if(franohiseOptional.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(),null,null));
        }else{
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }


    // 지점코드 중복확인 API
    @GetMapping("branohOverlap")
    public ResponseEntity<Map<String,Object>> branohOverlap(@RequestParam(value="brCode", defaultValue="") String brCode){

        log.info("지점 코드 중복확인");
        AjaxResponse res = new AjaxResponse();

        Optional<Branoh> branohOptional =  headService.findByBrCode(brCode);
        if(branohOptional.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP003.getCode(), ResponseErrorCode.TP003.getDesc(),null,null));
        }else{
            log.info("중복확인 완료");
        }
        return ResponseEntity.ok(res.success());
    }


}
