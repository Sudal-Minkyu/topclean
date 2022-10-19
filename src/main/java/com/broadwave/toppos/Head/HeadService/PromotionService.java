package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Branch.BranchDtos.head.BranchEventListDto;
import com.broadwave.toppos.Head.Branch.BranchRepository;
import com.broadwave.toppos.Head.Franchise.FranchiseDtos.head.FranchiseEventListDto;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupEventListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepository;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSEventListDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSRepository;
import com.broadwave.toppos.Head.Item.Group.C.ItemDtos.ItemEventListDto;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepository;
import com.broadwave.toppos.Head.Promotion.Promotion;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionDto;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionListDto;
import com.broadwave.toppos.Head.Promotion.PromotionDtos.PromotionSetDto;
import com.broadwave.toppos.Head.Promotion.PromotionFr.PromotionFr;
import com.broadwave.toppos.Head.Promotion.PromotionFr.PromotionFrDto;
import com.broadwave.toppos.Head.Promotion.PromotionFr.PromotionFrRepository;
import com.broadwave.toppos.Head.Promotion.PromotionItem.PromotionItem;
import com.broadwave.toppos.Head.Promotion.PromotionItem.PromotionItemDto;
import com.broadwave.toppos.Head.Promotion.PromotionItem.PromotionItemRepository;
import com.broadwave.toppos.Head.Promotion.PromotionRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.ResponseErrorCode;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2022-07-28
 * Time :
 * Remark : Toppos 행사관련 서비스
 */
@Slf4j
@Service
public class PromotionService {

    private final TokenProvider tokenProvider;

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    private final ItemGroupRepository itemGroupRepository;
    private final ItemGroupSRepository itemGroupSRepository;
    private final ItemRepository itemRepository;

    private final ModelMapper modelMapper;

    private final PromotionRepository promotionRepository;
    private final PromotionFrRepository promotionFrRepository;
    private final PromotionItemRepository promotionItemRepository;

    @Autowired
    public PromotionService(TokenProvider tokenProvider, BranchRepository branchRepository, FranchiseRepository franchiseRepository,
                            ItemGroupRepository itemGroupRepository, ItemGroupSRepository itemGroupSRepository, ItemRepository itemRepository,
                            ModelMapper modelMapper,
                            PromotionRepository promotionRepository, PromotionFrRepository promotionFrRepository, PromotionItemRepository promotionItemRepository){
        this.tokenProvider = tokenProvider;
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.itemGroupSRepository = itemGroupSRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.promotionRepository = promotionRepository;
        this.promotionFrRepository = promotionFrRepository;
        this.promotionItemRepository = promotionItemRepository;
    }

    // 본사 지사와 소속된 가맹점 리스트를 호출한다. (계약기간 사이에서 한정)
    public ResponseEntity<Map<String, Object>> headContractList() {
        log.info("headContractList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 현재 날짜 기준
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<HashMap<String,Object>> eventListData = new ArrayList<>();
        HashMap<String,Object> eventBranchInfoData;
        List<HashMap<String,Object>> eventSubListData;
        HashMap<String,Object> eventFranchInfoData;

        List<BranchEventListDto> branchEventListDtos = branchRepository.findByEventList(nowDate);
        for(BranchEventListDto branchEventListDto : branchEventListDtos){
            eventBranchInfoData = new HashMap<>();
            eventSubListData = new ArrayList<>(); // SubList 초기화

            eventBranchInfoData.put("brName", branchEventListDto.getBrName());

            List<FranchiseEventListDto> franchiseEventListDtos = franchiseRepository.findByEventList(branchEventListDto.getBranchId(), nowDate);
            for(FranchiseEventListDto franchiseEventListDto : franchiseEventListDtos){
                eventFranchInfoData = new HashMap<>();
                eventFranchInfoData.put("franchiseId", franchiseEventListDto.getFranchiseId());
                eventFranchInfoData.put("frCode", franchiseEventListDto.getFrCode());
                eventFranchInfoData.put("frName", franchiseEventListDto.getFrName());
                eventFranchInfoData.put("brName", branchEventListDto.getBrName());
                eventSubListData.add(eventFranchInfoData);
            }
            eventBranchInfoData.put("children", eventSubListData);

            eventListData.add(eventBranchInfoData);
        }

        data.put("gridListData", eventListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 등록된 상품 리스트를 호출한다.
    public ResponseEntity<Map<String, Object>> headItemList() {
        log.info("headItemList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 대분류 데이터
        List<HashMap<String,Object>> itemAListData = new ArrayList<>();
        HashMap<String,Object> eventAItemInfoData;

        // 중분류 데이터
        List<HashMap<String,Object>> itemBListData;
        HashMap<String,Object> eventBItemInfoData;

        // 소분류 데이터(상품)
        List<HashMap<String,Object>> itemCListData;
        HashMap<String,Object> eventCItemInfoData;

        List<ItemGroupEventListDto> itemGroupEventListDtos = itemGroupRepository.findByItemGroupEventList();
        // 대분류 리스트 호출
        for(ItemGroupEventListDto itemGroupEventListDto : itemGroupEventListDtos){
            eventAItemInfoData = new HashMap<>();
            itemBListData = new ArrayList<>(); // 중분류 리스트 초기화
            eventAItemInfoData.put("bgName", itemGroupEventListDto.getBgName());

            // 중분류 리스트 호출
            List<ItemGroupSEventListDto> itemGroupSEventListDtos = itemGroupSRepository.findByItemGroupSEventList(itemGroupEventListDto.getBgItemGroupcode());
            for(ItemGroupSEventListDto itemGroupSEventListDto : itemGroupSEventListDtos){
                eventBItemInfoData = new HashMap<>();
                itemCListData = new ArrayList<>(); // 소분류 리스트 초기화
                eventBItemInfoData.put("bsName", itemGroupSEventListDto.getBsName());
                // 소분류 리스트 호출(상품)
                List<ItemEventListDto> itemEventListDtos = itemRepository.findByItemEventList(itemGroupEventListDto.getBgItemGroupcode(), itemGroupSEventListDto.getBsItemGroupcodeS());
                for(ItemEventListDto itemEventListDto : itemEventListDtos) {
                    eventCItemInfoData = new HashMap<>();
                    eventCItemInfoData.put("bgName", itemGroupEventListDto.getBgName());
                    eventCItemInfoData.put("bsName", itemGroupSEventListDto.getBsName());
                    eventCItemInfoData.put("biName", itemEventListDto.getBiName());
                    eventCItemInfoData.put("biItemcode", itemEventListDto.getBiItemcode());
                    itemCListData.add(eventCItemInfoData);
                }
                eventBItemInfoData.put("children", itemCListData);
                itemBListData.add(eventBItemInfoData);
            }
            eventAItemInfoData.put("children", itemBListData);
            itemAListData.add(eventAItemInfoData);
        }

        data.put("gridListData", itemAListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 행사 등록 및 삭제
    @Transactional
    public ResponseEntity<Map<String, Object>> headPromotionSave(PromotionSetDto promotionSetDto, String hpInputType, HttpServletRequest request) {
        log.info("headPromotionSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("promotionSetDto : "+promotionSetDto);

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);

        String insertType;
        if(hpInputType.equals("HR")){
            insertType = "HR";
        }else{
            insertType = (String) claims.get("brCode"); // 현재 지사의 코드(2자리) 가져오기
        }
        log.info("입력 hpInputType : "+insertType);


        String type; // 저장/업데이트 타입
        // 행사 마스터 테이블
        Promotion promotion = modelMapper.map(promotionSetDto.getHbPromotion(), Promotion.class);
        if(promotion.getHpId() != 0){
            log.info("수정저장");

            type = "update";
            // 수정
            Optional<Promotion> optionalPromotion = promotionRepository.findById(promotion.getHpId());
            if(!optionalPromotion.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP030.getCode(), "행사 데이터가 "+ResponseErrorCode.TP030.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
            }else{
                promotion.setHpYyyymmdd(optionalPromotion.get().getHpYyyymmdd());
                promotion.setHpInputType(optionalPromotion.get().getHpInputType());
                promotion.setInsert_id(optionalPromotion.get().getInsert_id());
                promotion.setInsertDateTime(optionalPromotion.get().getInsertDateTime());
                promotion.setModify_id(login_id);
                promotion.setModifyDateTime(LocalDateTime.now());
            }
        }else{
            // 현재 날짜 기준
            String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.info("신규저장");

            type = "new";
            // 신규저장
            promotion.setHpId(null);
            promotion.setHpInputType(insertType);
            promotion.setHpYyyymmdd(nowDate);
            promotion.setInsert_id(login_id);
            promotion.setInsertDateTime(LocalDateTime.now());
        }
//        log.info("저장되는 promotion : "+promotion);

        // 행사 가맹점 테이블
        PromotionFr promotionFr;
        List<PromotionFr> promotionFrList = new ArrayList<>();
        List<Long> franchiseIdList = new ArrayList<>();
        for(PromotionFrDto promotionFrDto : promotionSetDto.getHbPromotionFranchise()){
            if(type.equals("update")){
                franchiseIdList.add(promotionFrDto.getFranchiseId());
            }
            promotionFr = modelMapper.map(promotionFrDto, PromotionFr.class);
            promotionFrList.add(promotionFr);
        }

        // 행사 상품 테이블
        PromotionItem promotionItem;
        List<PromotionItem> promotionItemList = new ArrayList<>();
        List<String> biItemcodeList = new ArrayList<>();
        List<Double> discountList = new ArrayList<>();
        for(PromotionItemDto promotionItemDto : promotionSetDto.getHbPromotionItem()){
            if(type.equals("update")){
                biItemcodeList.add(promotionItemDto.getBiItemcode());
                discountList.add(promotionItemDto.getHiDiscountRt());
            }
            promotionItem = modelMapper.map(promotionItemDto, PromotionItem.class);
            promotionItemList.add(promotionItem);
        }

        boolean result = promotionSaveFun(promotion, promotionFrList, promotionItemList, type, franchiseIdList, biItemcodeList, discountList, login_id);
        log.info("결과 : "+result);

        if(!result){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP031.getCode(), "행사 등록을"+ResponseErrorCode.TP031.getDesc(), ResponseErrorCode.TP027.getCode(), ResponseErrorCode.TP027.getDesc()));
        }

//        data.put("gridListData", itemAListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 행사관련 저장 및 수정로직
    @Transactional
    public boolean promotionSaveFun(Promotion promotion, List<PromotionFr> promotionFrList, List<PromotionItem> promotionItemList, String type, List<Long> franchiseIdList,
                                    List<String> biItemcodeList, List<Double> discountList, String login_id){
        try{
            Promotion promotionSave = promotionRepository.save(promotion);
            if(type.equals("update")){
                // 수정하는데 이미 존재하는 값은 삭제처리
                promotionFrRepository.findByPromotionFrDelete(promotionSave.getHpId(), franchiseIdList);
                promotionItemRepository.findByPromotionItemDelete(promotionSave.getHpId(), biItemcodeList);

                // 가맹점 존재하는 값 수정처리(modify)
                promotionFrRepository.findByPromotionFrUpdate(promotionSave.getHpId(), login_id, franchiseIdList);
//                // 상품 존재하는 값 수정처리(modify, discount)
//                for(int i=0; i<biItemcodeList.size(); i++){
//                    promotionItemRepository.findByPromotionItemUpdate(promotionSave.getHpId(), login_id, biItemcodeList.get(i), discountList.get(i));
//                }

                // 추가된 항목 저장(가맹점, 상품)
                List<PromotionFr> removePromotionFrList = new ArrayList<>();
                for(PromotionFr promotionFr : promotionFrList){
                    Optional<PromotionFr> optionalPromotionFr = promotionFrRepository.findByPromotionFr(promotionSave.getHpId(), promotionFr.getFranchiseId());
                    if(!optionalPromotionFr.isPresent()){
                        promotionFr.setHpId(promotionSave.getHpId());
                        promotionFr.setInsert_id(login_id);
                        promotionFr.setInsertDateTime(LocalDateTime.now());
                    }else{
                        removePromotionFrList.add(promotionFr);
                    }
                }
                promotionFrList.removeAll(removePromotionFrList);
                promotionFrRepository.saveAll(promotionFrList);

//                List<PromotionItem> removePromotionItemList = new ArrayList<>();
                // 상품 존재하는 값 수정처리(modify, discount)
                for(PromotionItem promotionItem : promotionItemList){
                    Optional<PromotionItem> optionalPromotionItem = promotionItemRepository.findByPromotionItem(promotionSave.getHpId(), promotionItem.getBiItemcode());
                    if(!optionalPromotionItem.isPresent()){
                        promotionItem.setHpId(promotionSave.getHpId());
                        promotionItem.setInsert_id(login_id);
                        promotionItem.setInsertDateTime(LocalDateTime.now());
                    }else{
                        promotionItem.setHpId(optionalPromotionItem.get().getHpId());
                        promotionItem.setInsert_id(optionalPromotionItem.get().getInsert_id());
                        promotionItem.setInsertDateTime(optionalPromotionItem.get().getInsertDateTime());
                        promotionItem.setModify_id(login_id);
                        promotionItem.setModifyDateTime(LocalDateTime.now());
                    }
                }
//                promotionItemList.removeAll(removePromotionItemList);
                promotionItemRepository.saveAll(promotionItemList);

            }else{
                for(PromotionFr promotionFr : promotionFrList){
                    promotionFr.setHpId(promotionSave.getHpId());
                    promotionFr.setInsert_id(login_id);
                    promotionFr.setInsertDateTime(LocalDateTime.now());
                }
                promotionFrRepository.saveAll(promotionFrList);
                for(PromotionItem promotionItem : promotionItemList){
                    promotionItem.setHpId(promotionSave.getHpId());
                    promotionItem.setInsert_id(login_id);
                    promotionItem.setInsertDateTime(LocalDateTime.now());
                }
                promotionItemRepository.saveAll(promotionItemList);
            }

            return true;
        }catch (Exception e){
            log.info("에러발생 : "+e);
            return false;
        }
    }

    // 본사 행사 조회
    public ResponseEntity<Map<String, Object>> headPromotionList(Long branchId, Long franchiseId, String filterDt, String hpName, String hpStatus) {
        log.info("headPromotionList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("branchId : "+branchId); // 0 이면 전체, 숫자가 올 경우 해당 지사를 의미
        log.info("franchiseId : "+franchiseId); // 0 이면 전체, 숫자가 올 경우 해당 가맹점을 의미
        log.info("filterDt : "+filterDt); // 조회기간
        log.info("hpName : "+hpName); // 행사명 - 문자가 포함되어 있으면 검색됨
        log.info("hpStatus : "+hpStatus); // '00' 전체, '01' 진행, '02' 종료

        List<PromotionDto> promotionDtos = promotionRepository.findByPromotion(branchId, franchiseId, filterDt, hpName, hpStatus);
        data.put("gridListData", promotionDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 본사 행사의 세부정보 조회
    public ResponseEntity<Map<String, Object>> headPromotionSub(Long hpId) {
        log.info("headPromotionSub 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("hpId : "+hpId);

        List<PromotionFrDto> promotionFrDtos = promotionFrRepository.findByPromotionFrList(hpId);
        List<PromotionItemDto> promotionItemDtos = promotionItemRepository.findByPromotionItemList(hpId);

        data.put("hbPromotionFranchise", promotionFrDtos);
        data.put("hbPromotionItem", promotionItemDtos);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 가맹점 행사 데이터 조회
    public List<PromotionListDto> findByPromotionList(String frCode, int dayOfWeek, String nowDate) {
        return promotionRepository.findByPromotionList(frCode, dayOfWeek, nowDate);
    }
}
