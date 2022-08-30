package com.broadwave.toppos.Head.HeadService;

import com.broadwave.toppos.Head.Franchise.Franchise;
import com.broadwave.toppos.Head.Franchise.FranchiseRepository;
import com.broadwave.toppos.Head.Item.Group.C.Item;
import com.broadwave.toppos.Head.Item.Group.C.ItemRepository;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceDto;
import com.broadwave.toppos.Head.Item.ItemDtos.ItemPriceListDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePrice;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.FranchisePriceDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.FranchisePriceListDto;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.FranchisePriceSet;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceDtos.ItemPriceSet;
import com.broadwave.toppos.Head.Item.Price.FranchisePrice.FranchisePriceRepository;
import com.broadwave.toppos.Head.Item.Price.ItemPrice;
import com.broadwave.toppos.Head.Item.Price.ItemPriceNotList;
import com.broadwave.toppos.Head.Item.Price.ItemPriceRepository;
import com.broadwave.toppos.common.AjaxResponse;
import com.broadwave.toppos.common.CommonUtils;
import com.broadwave.toppos.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class ItemPriceService {

    private final ModelMapper modelMapper;

    private final FranchiseRepository franchiseRepository;
    private final ItemRepository itemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final FranchisePriceRepository franchisePriceRepository;

    @Autowired
    public ItemPriceService(ModelMapper modelMapper, FranchiseRepository franchiseRepository, ItemRepository itemRepository, ItemPriceRepository itemPriceRepository,
                            FranchisePriceRepository franchisePriceRepository){
        this.modelMapper = modelMapper;
        this.franchiseRepository = franchiseRepository;
        this.itemRepository = itemRepository;
        this.itemPriceRepository = itemPriceRepository;
        this.franchisePriceRepository = franchisePriceRepository;
    }

    // 상품 가격 리스트 호출
    public ResponseEntity<Map<String, Object>> findByItemPriceList(String bgName, String biItemcode, String biName, String setDt) {
        log.info("itemPriceList 호출");

        log.info("가격적용일 : " + setDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> itemPriceListData = new ArrayList<>();
        HashMap<String, Object> itemPriceInfo;

        List<ItemPriceListDto> itemPriceListDtos = itemPriceRepository.findByItemPriceList(bgName, biItemcode, biName, setDt);
//        log.info("itemPriceListDtos : "+itemPriceListDtos.size());
        for (ItemPriceListDto itemPriceListDto : itemPriceListDtos) {

            itemPriceInfo = new HashMap<>();

            itemPriceInfo.put("biItemcode", itemPriceListDto.getBiItemcode());
            itemPriceInfo.put("bgName", itemPriceListDto.getBgName());
            itemPriceInfo.put("bsName", itemPriceListDto.getBsName());
            itemPriceInfo.put("biName", itemPriceListDto.getBiName());
            itemPriceInfo.put("setDt", itemPriceListDto.getSetDt());
            itemPriceInfo.put("closeDt", itemPriceListDto.getCloseDt());

            itemPriceInfo.put("bpBasePrice", itemPriceListDto.getBpBasePrice());
            itemPriceInfo.put("bpAddPrice", itemPriceListDto.getBpAddPrice());

            itemPriceInfo.put("bpPriceA", itemPriceListDto.getBpPriceA());
            itemPriceInfo.put("bpPriceB", itemPriceListDto.getBpPriceB());
            itemPriceInfo.put("bpPriceC", itemPriceListDto.getBpPriceC());
            itemPriceInfo.put("bpPriceD", itemPriceListDto.getBpPriceD());
            itemPriceInfo.put("bpPriceE", itemPriceListDto.getBpPriceE());

            itemPriceInfo.put("bpRemark", itemPriceListDto.getBiRemark());

            itemPriceListData.add(itemPriceInfo);
        }

//        log.info("상품 가격 리스트 : "+itemPriceListData);
//        log.info("상품 가격 리스트 사이즈 : "+itemPriceListData.size());
        data.put("gridListData", itemPriceListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 가격페이지 업데이트 및 수정 호출 API
    @Transactional
    public ResponseEntity<Map<String, Object>> itemPriceUpdate(ItemPriceSet itemPriceSet, HttpServletRequest request) {

        log.info("itemPriceUpdate 호출");

        AjaxResponse res = new AjaxResponse();

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 접속한 아이디 : " + login_id);

        ArrayList<ItemPriceDto> addList = itemPriceSet.getAdd(); // 추가 리스트 얻기
        ArrayList<ItemPriceDto> updateList = itemPriceSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<ItemPriceDto> deleteList = itemPriceSet.getDelete(); // 제거 리스트 얻기
//        log.info("추가 리스트 : "+addList);
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        List<ItemPrice> itemPriceList = new ArrayList<>();

        // 상품가격 추가 시작
        if (addList.size() != 0) {
            for (ItemPriceDto itemPriceDto : addList) {
//                log.info("추가 할 품목 상품코드 : " + itemPriceDto.getBiItemcode());
                ItemPrice itemPrice = new ItemPrice();

                itemPrice.setBiItemcode(itemPriceDto.getBiItemcode());
                itemPrice.setSetDt(itemPriceDto.getSetDt());
                itemPrice.setCloseDt(itemPriceDto.getCloseDt());

                itemPrice.setBpBasePrice(itemPriceDto.getBpBasePrice());
                itemPrice.setBpAddPrice(itemPriceDto.getBpAddPrice());
                itemPrice.setBpPriceA(itemPriceDto.getBpPriceA());
                itemPrice.setBpPriceB(itemPriceDto.getBpPriceB());
                itemPrice.setBpPriceC(itemPriceDto.getBpPriceC());
                itemPrice.setBpPriceD(itemPriceDto.getBpPriceD());
                itemPrice.setBpPriceE(itemPriceDto.getBpPriceE());
                itemPrice.setBpRemark(itemPriceDto.getBpRemark());

                itemPrice.setInsert_id(login_id);
                itemPrice.setInsertDateTime(LocalDateTime.now());
//                log.info("itemPrice : " + itemPrice);
                itemPriceList.add(itemPrice);
            }
        }
        log.info("추가 itemPriceList : " +itemPriceList);
        if (itemPriceList.size() != 0) {
            itemPriceRepository.saveAll(itemPriceList);
            itemPriceList.clear();
        }

        // 상품가격 수정 시작
        if (updateList.size() != 0) {
            for (ItemPriceDto itemPriceDto : updateList) {
                Optional<ItemPrice> optionalItemPrice = itemPriceRepository.findByItemPriceOptional(itemPriceDto.getBiItemcode(), itemPriceDto.getSetDt(), itemPriceDto.getCloseDt());
                if (!optionalItemPrice.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 품목" + ResponseErrorCode.TP005.getDesc(), "문자", "상품코드 : " + itemPriceDto.getBiItemcode()));
                } else {
//                    log.info("수정 할 품목 상품코드 : " + optionalItemPrice.get().getBiItemcode());
                    ItemPrice itemPrice = new ItemPrice();

                    itemPrice.setBiItemcode(optionalItemPrice.get().getBiItemcode());
                    itemPrice.setSetDt(optionalItemPrice.get().getSetDt());
                    itemPrice.setCloseDt(optionalItemPrice.get().getCloseDt());

                    itemPrice.setBpBasePrice(itemPriceDto.getBpBasePrice());
                    itemPrice.setBpAddPrice(itemPriceDto.getBpAddPrice());
                    itemPrice.setBpPriceA(itemPriceDto.getBpPriceA());
                    itemPrice.setBpPriceB(itemPriceDto.getBpPriceB());
                    itemPrice.setBpPriceC(itemPriceDto.getBpPriceC());
                    itemPrice.setBpPriceD(itemPriceDto.getBpPriceD());
                    itemPrice.setBpPriceE(itemPriceDto.getBpPriceE());
                    itemPrice.setBpRemark(itemPriceDto.getBpRemark());

                    itemPrice.setInsert_id(optionalItemPrice.get().getInsert_id());
                    itemPrice.setInsertDateTime(optionalItemPrice.get().getInsertDateTime());
                    itemPrice.setModify_id(login_id);
                    itemPrice.setModifyDateTime(LocalDateTime.now());

//                    log.info("itemPrice : " + itemPrice);
                    itemPriceList.add(itemPrice);
                }
            }
        }
//        log.info("수정 itemPriceList : " +itemPriceList);
        if (itemPriceList.size() != 0) {
            itemPriceRepository.saveAll(itemPriceList);
            itemPriceList.clear();
        }

        // 상품가격 삭제로직 실행
        if (deleteList.size() != 0) {
            for (ItemPriceDto itemPriceDto : deleteList) {
                Optional<ItemPrice> optionalItemPrice = itemPriceRepository.findByItemPriceOptional(itemPriceDto.getBiItemcode(), itemPriceDto.getSetDt(), itemPriceDto.getCloseDt());
                if (optionalItemPrice.isPresent()) {
                    log.info("삭제할 상품소재 코드 : " + optionalItemPrice.get().getBiItemcode());
                    itemPriceList.add(optionalItemPrice.get());
                } else {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 " + ResponseErrorCode.TP005.getDesc(), "문자", "상품코드 : " + itemPriceDto.getBiItemcode()));
                }
            }
        }
//        log.info("삭제 itemPriceList : " +itemPriceList);
        if (itemPriceList.size() != 0) {
            itemPriceRepository.deleteAll(itemPriceList);
            itemPriceList.clear();
        }

        return ResponseEntity.ok(res.success());
    }

    // 상품그룹 가격페이지 호출 API
    public ResponseEntity<Map<String, Object>> itemPrice(MultipartFile priceUpload, String setDt, HttpServletRequest request) throws IOException, ParseException {
        log.info("itemPrice 호출");

        AjaxResponse res = new AjaxResponse();
        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 로그인한 아이디 : " + login_id);

        String extension = FilenameUtils.getExtension(priceUpload.getOriginalFilename());
//        log.info("확장자 : " + extension);

        // 확장자가 엑셀이 맞는지 확인
        Workbook workbook;
        assert extension != null;
        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(priceUpload.getInputStream());  // -> .xlsx
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(priceUpload.getInputStream());  // -> .xls
        } else {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP012.getCode(), ResponseErrorCode.TP012.getDesc(), null, null));
        }

        log.info("시작일 : " + setDt);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date setDate = formatter.parse(setDt);
        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(setDate);
        cal.add(Calendar.DATE, -1); // 하루전으로 셋팅
        String closeDate = formatter.format(cal.getTime());
        log.info("종료임 : " + closeDate);

        String setDtReplace = setDt.replaceAll("-", "");
        String closeDtReplace = closeDate.replaceAll("-", "");
        log.info("setDtReplace : " + setDtReplace);

        Sheet worksheet = workbook.getSheetAt(0); // 첫번째 시트
        try {
            Row rowCheck = worksheet.getRow(0);
            Object cellDataCheck = rowCheck.getCell(0);
//            log.info("cellDataCheck : " + cellDataCheck.toString());
            if (!cellDataCheck.toString().equals("상품코드")) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP013.getCode(), ResponseErrorCode.TP013.getDesc(), null, null));
            }
        } catch (NullPointerException e) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP013.getCode(), ResponseErrorCode.TP013.getDesc(), null, null));
        }


        int numOfRows = worksheet.getPhysicalNumberOfRows();
        log.info("데이터 총 길이 : " + numOfRows);

        ArrayList<ItemPrice> itemPriceSaveArrayList = new ArrayList<>();
        ArrayList<ItemPrice> itemPriceUpdateArrayList = new ArrayList<>();

        ArrayList<Object> excelList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            ItemPrice itemPrice = new ItemPrice();
            for (int j = 0; j < 12; j++) {
                Row row = worksheet.getRow(i);
                Cell cellData = row.getCell(j);
                if(cellData != null){
                    CellType ct = cellData.getCellType();
                    if (ct == CellType.BLANK) {
                        excelList.add("");
                    } else {
                        excelList.add(getStringValue(cellData));
                    }
                }else{
                    excelList.add("");
                }
            }

            log.info(i + "번째 excelList : " + excelList);

            Optional<Item> optionalItem = itemRepository.findByBiItemcode(excelList.get(0).toString());
            if (!optionalItem.isPresent()) {
                errorList.add(i + "번째 행의 코드가 존재하지 않습니다.");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), i+"번쨰 상품"+ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : "+excelList.get(0).toString()));
            } else {
                log.info(i + "번째 준비");
                ItemPriceDto priceDto = itemPriceRepository.findByItemPrice(excelList.get(0).toString(), setDtReplace);
                if (priceDto != null) {
                    errorList.add(i + "번째 행의 중복되는 적용일자가 존재합니다.");
//                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP015.getCode(), i+"번째 행 "+ResponseErrorCode.TP015.getDesc(), "문자", "상품코드 : "+excelList.get(0).toString()));
                } else {
                    log.info(i + "번째 excelList 시작");
                    ItemPriceDto itemPriceDto = itemPriceRepository.findByItemPrice(excelList.get(0).toString(), "99991231");
                    log.info(i + "번째 itemPriceDto : " + itemPriceDto);
                    if (itemPriceDto != null) {
                        itemPrice = modelMapper.map(itemPriceDto, ItemPrice.class);
                        itemPrice.setModify_id(login_id);
                        itemPrice.setModifyDateTime(LocalDateTime.now());
                        itemPrice.setCloseDt(closeDtReplace);

                        itemPriceUpdateArrayList.add(itemPrice);
                        itemPrice = new ItemPrice();
                    }
                    itemPrice.setBiItemcode(excelList.get(0).toString());
                    itemPrice.setSetDt(setDtReplace);
                    itemPrice.setCloseDt("99991231");

                    try {
                        itemPrice.setBpBasePrice(Integer.parseInt((String) excelList.get(3)));
                        itemPrice.setBpAddPrice(Integer.parseInt((String) excelList.get(5)));
                        itemPrice.setBpPriceA(Integer.parseInt((String) excelList.get(6)));
                        itemPrice.setBpPriceB(Integer.parseInt((String) excelList.get(7)));
                        itemPrice.setBpPriceC(Integer.parseInt((String) excelList.get(8)));
                        itemPrice.setBpPriceD(Integer.parseInt((String) excelList.get(9)));
                        itemPrice.setBpPriceE(Integer.parseInt((String) excelList.get(10)));
                    } catch (NumberFormatException e) {
                        log.info("문자가 들어가있음 : " + e);
                        errorList.add(i + "번째 행 금액에 문자가 들어갔습니다. 숫자만 입력해주세요.");
//                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP016.getCode(), i+"번째 행 "+ResponseErrorCode.TP016.getDesc(), "문자", "상품코드 : "+excelList.get(0).toString()));
                    }

                    itemPrice.setBpRemark(excelList.get(11).toString());

                    itemPrice.setInsert_id(login_id);
                    itemPrice.setInsertDateTime(LocalDateTime.now());
                    log.info(i + "번째 itemPrice : " + itemPrice);

                    itemPriceSaveArrayList.add(itemPrice);
                }
                excelList.clear();
            }
        }

        log.info("수정 데이터리스트 itemPriceUpdateArrayList : "+itemPriceUpdateArrayList);
        log.info("신규 데이터리스트 itemPriceSaveArrayList : "+itemPriceSaveArrayList);

        HashMap<String, Object> data = new HashMap<>();
        if (errorList.size() == 0) {
            itemPriceRepository.saveAll(itemPriceUpdateArrayList);
            itemPriceRepository.saveAll(itemPriceSaveArrayList);
            data.put("errorListData", null);
        } else {
            data.put("errorListData", errorList);
        }

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 엑셀 : cell의 데이터를 String 또는 Int형으로 변경
    public static String getStringValue(Cell cell) {
        String rtnValue;
        try {
            rtnValue = cell.getStringCellValue();
        } catch (IllegalStateException e) {
            rtnValue = Integer.toString((int) cell.getNumericCellValue());
        }
        return rtnValue;
    }

    // 가맹점 특정상품가격 호출 API
    public ResponseEntity<Map<String, Object>> franchisePrice(FranchisePriceSet franchisePriceSet, HttpServletRequest request) {

        log.info("franchisePrice 호출");

        AjaxResponse res = new AjaxResponse();

        String login_id = CommonUtils.getCurrentuser(request);
        log.info("현재 접속한 아이디 : " + login_id);

        ArrayList<FranchisePriceDto> addList = franchisePriceSet.getAdd(); // 추가 리스트 얻기
        ArrayList<FranchisePriceDto> updateList = franchisePriceSet.getUpdate(); // 수정 리스트 얻기
        ArrayList<FranchisePriceDto> deleteList = franchisePriceSet.getDelete(); // 제거 리스트 얻기

//        log.info("추가 리스트 : "+addList);
//        log.info("수정 리스트 : "+updateList);
//        log.info("삭제 리스트 : "+deleteList);

        List<FranchisePrice> franchisePriceList = new ArrayList<>();
        // 특정가격 적용품목 저장 시작.
        if (addList.size() != 0) {
            Optional<Franchise> optionalFranchise = franchiseRepository.findByFrCode(addList.get(0).getFrCode());
            if (!optionalFranchise.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "가맹점 " + ResponseErrorCode.TP009.getDesc(), "문자", "가맹점코드 : " + addList.get(0).getFrCode()));
            } else {
                for (FranchisePriceDto franchisePriceDto : addList) {
                    Optional<Item> optionalItem = itemRepository.findByBiItemcode(franchisePriceDto.getBiItemcode());
                    if (!optionalItem.isPresent()) {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.TP009.getCode(), "상품" + ResponseErrorCode.TP009.getDesc(), "문자", "상품코드 : " + franchisePriceDto.getBiItemcode()));
                    } else {
                        Optional<FranchisePrice> optionalFranchisePrice = franchisePriceRepository.findByFranchisePrice(franchisePriceDto.getBiItemcode(), franchisePriceDto.getFrCode());
                        if (optionalFranchisePrice.isPresent()) {
                            log.info("이미 존재하는 특정가격 적용품목");
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.TP014.getCode(), ResponseErrorCode.TP014.getDesc()+" 상품코드입니다.", "문자", "상품코드 : " + franchisePriceDto.getBiItemcode()));
                        } else {
                            log.info("특정가격 적용품목 신규생성");
                            FranchisePrice franchisePrice = new FranchisePrice();
                            franchisePrice.setBiItemcode(franchisePriceDto.getBiItemcode());
                            franchisePrice.setFrCode(franchisePriceDto.getFrCode());

                            franchisePrice.setBfPrice(franchisePriceDto.getBfPrice());
                            franchisePrice.setBfRemark(franchisePriceDto.getBfRemark());
                            franchisePrice.setInsert_id(login_id);
                            franchisePrice.setInsertDateTime(LocalDateTime.now());

                            franchisePriceList.add(franchisePrice);
                        }
                    }
                }
            }
        }

//        log.info("저장 franchisePriceList : " +franchisePriceList);
        if (franchisePriceList.size() != 0) {
            franchisePriceRepository.saveAll(franchisePriceList);
            franchisePriceList.clear();
        }

        // 특정가격 적용품목 수정 시작.
        if (updateList.size() != 0) {
            for (FranchisePriceDto franchisePriceDto : updateList) {
                Optional<FranchisePrice> optionalFranchisePrice = franchisePriceRepository.findByFranchisePrice(franchisePriceDto.getBiItemcode(), franchisePriceDto.getFrCode());
                if (!optionalFranchisePrice.isPresent()) {
                    log.info("존재하지 않은 상품소재 코드 : " + franchisePriceDto.getBiItemcode());
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "수정 할 품목" + ResponseErrorCode.TP005.getDesc(), "문자", "상품코드 : " + franchisePriceDto.getBiItemcode()));
                } else {
                    log.info("수정 할 품목 상품코드 : " + optionalFranchisePrice.get().getBiItemcode());
                    FranchisePrice franchisePrice = new FranchisePrice();

                    franchisePrice.setBiItemcode(optionalFranchisePrice.get().getBiItemcode());
                    franchisePrice.setFrCode(optionalFranchisePrice.get().getFrCode());

                    franchisePrice.setBfPrice(franchisePriceDto.getBfPrice());
                    franchisePrice.setBfRemark(franchisePriceDto.getBfRemark());

                    franchisePrice.setInsert_id(optionalFranchisePrice.get().getInsert_id());
                    franchisePrice.setInsertDateTime(optionalFranchisePrice.get().getInsertDateTime());
                    franchisePrice.setModify_id(login_id);
                    franchisePrice.setModifyDateTime(LocalDateTime.now());

//                    log.info("franchisePrice : " + franchisePrice);
                    franchisePriceList.add(franchisePrice);
                }
            }
        }

//        log.info("수정 franchisePriceList : " +franchisePriceList);
        if (franchisePriceList.size() != 0) {
            franchisePriceRepository.saveAll(franchisePriceList);
            franchisePriceList.clear();
        }

        // 특정가격 적용품목 삭제로직 실행
        if (deleteList.size() != 0) {
            for (FranchisePriceDto franchisePriceDto : deleteList) {
                Optional<FranchisePrice> optionalFranchisePrice = franchisePriceRepository.findByFranchisePrice(franchisePriceDto.getBiItemcode(), franchisePriceDto.getFrCode());
                if (optionalFranchisePrice.isPresent()) {
//                    log.info("삭제할 상품소재 코드 : "+optionalFranchisePrice.get().getBiItemcode());
                    franchisePriceList.add(optionalFranchisePrice.get());
                } else {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.TP005.getCode(), "삭제 할 " + ResponseErrorCode.TP005.getDesc(), "문자", "가맹점코드 : " + franchisePriceDto.getFrCode() + ", " + "상품코드 : " + franchisePriceDto.getBiItemcode()));
                }
            }
        }

        if (franchisePriceList.size() != 0) {
            franchisePriceRepository.saveAll(franchisePriceList);
            franchisePriceList.clear();
        }

        return ResponseEntity.ok(res.success());
    }

    // 가맹점 특정상품가격 리스트 호출 API
    public ResponseEntity<Map<String, Object>> franchisePriceList(String frCode) {
        log.info("franchisePriceList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<HashMap<String, Object>> franchisePriceListData = new ArrayList<>();
        HashMap<String, Object> franchisePriceInfo;

//        log.info("frCode : "+frCode);

        List<FranchisePriceListDto> franchisePriceListDtos = franchisePriceRepository.findByFranchisePriceList(frCode);
//        log.info("franchisePriceListDtos : "+franchisePriceListDtos);
        for (FranchisePriceListDto franchisePriceListDto : franchisePriceListDtos) {

            franchisePriceInfo = new HashMap<>();

            franchisePriceInfo.put("biItemcode", franchisePriceListDto.getBiItemcode());
            franchisePriceInfo.put("bgName", franchisePriceListDto.getBgName());
            franchisePriceInfo.put("bsName", franchisePriceListDto.getBsName());
            franchisePriceInfo.put("biName", franchisePriceListDto.getBiName());
            franchisePriceInfo.put("bfPrice", franchisePriceListDto.getBfPrice());
            franchisePriceInfo.put("bfRemark", franchisePriceListDto.getBfRemark());

            franchisePriceListData.add(franchisePriceInfo);
        }

//        log.info("가맹점 특정가격 적용품목 리스트 : "+franchisePriceListData);
        data.put("gridListData", franchisePriceListData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 상품그룹 가격페이지 등록되지 않은 상품 조회리스트 호출 API
    public ResponseEntity<Map<String, Object>> itemPriceNotList(String bgName, String biItemcode, String biName, String setDt) {
        log.info("itemPriceNotList 호출");

        log.info("bgName : "+bgName);
        log.info("biItemcode : "+biItemcode);
        log.info("biName : "+biName);
        log.info("setDt : "+setDt);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemPriceNotList>  itemPriceNotLists = itemPriceRepository.findByItemPricedNotList(bgName, biItemcode, biName, setDt);
        data.put("gridListData", itemPriceNotLists);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


}
