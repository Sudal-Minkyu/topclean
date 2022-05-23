package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupCodeAndNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.outsourcingPrice.OutsourcingPrice;
import com.broadwave.toppos.Manager.outsourcingPrice.OutsourcingPriceRepository;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListInputDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceSearchDto;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : DongA
 * Date : 2022-05-13
 * Time :
 * Remark : 지사 외주가격 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OutsourcingPriceService {

    private final OutsourcingPriceRepository outsourcingPriceRepository;

    private final ItemGroupRepository itemGroupRepository;

    private final TokenProvider tokenProvider;

//    public void outsourcingInsert(){
//        outsourcingPriceRepository.save();
//    }

    // 외주가격에 쓰일 대분류 이름과 코드 호출
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> groupcodeAndNameList(HttpServletRequest request) {
        log.info("groupcodeAndNameList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<ItemGroup> groupcodeAndName = itemGroupRepository.findGroupcodeAndName();

        // 엔터티 -> DTO 변환
        List<ItemGroupCodeAndNameListDto> itemCodeAndNameDtos = groupcodeAndName.stream()
                .map(g -> new ItemGroupCodeAndNameListDto(g.getBgItemGroupcode(), g.getBgName()))
                .collect(Collectors.toList());

        data.put("bgItemListData", itemCodeAndNameDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 외주가격 리스트 호출
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> outsourcingPriceList(OutsourcingPriceListInputDto outsourcingPriceListInputDto, HttpServletRequest request) {
        log.info("outsourcingPriceList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 토큰에서 지사코드 추출
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode");

        // DB에서 리스트 가져오기
        List<OutsourcingPriceListDto> byOutsourcingPriceList = outsourcingPriceRepository.findByOutsourcingPriceList(
                outsourcingPriceListInputDto.getBiItemcode(),
                outsourcingPriceListInputDto.getBiName(),
                outsourcingPriceListInputDto.getBpOutsourcingYn(),
                brCode
        );

        data.put("gridListData", byOutsourcingPriceList);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 외주가격 리스트들 저장
    public ResponseEntity<Map<String, Object>> outsourcingPriceSave(List<OutsourcingPriceListDto> outsourcingPriceListDtos, HttpServletRequest request) {
        log.info("outsourcingPriceSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 토큰에서 지사코드 추출
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String brCode = (String) claims.get("brCode");


        List<OutsourcingPrice> itemcodesAndBrCodes = outsourcingPriceRepository.findAllItemcodeAndBrCode();
        List<OutsourcingPrice> allOutsourcingPrice = new ArrayList<>();

        System.out.println(itemcodesAndBrCodes);
        System.out.println(outsourcingPriceListDtos);


        System.out.println("4444444444");
        for (OutsourcingPrice itemcodeAndBrCode : itemcodesAndBrCodes) {
            System.out.println("33333333");
            for (OutsourcingPriceListDto outsourcingPriceListDto : outsourcingPriceListDtos) {
                System.out.println("222222222");
                if (outsourcingPriceListDto.getBiItemcode().equals(itemcodeAndBrCode.getBiItemcode()) && brCode.equals(itemcodeAndBrCode.getBrCode())) {
                    System.out.println("1111111111");
                    itemcodeAndBrCode.setBpOutsourcingPrice(outsourcingPriceListDto.getBpOutsourcingPrice());
                    itemcodeAndBrCode.setBpOutsourcingYn(outsourcingPriceListDto.getBpOutsourcingYn());
                    itemcodeAndBrCode.setBpRemark(outsourcingPriceListDto.getBpRemark());
                } else {
                    System.out.println("0000000000");
                    OutsourcingPrice outsourcingPrice = new OutsourcingPrice();
                    outsourcingPrice
                            .builder()
                            .biItemcode(outsourcingPriceListDto.getBiItemcode())
                            .brCode(brCode)
                            .bpOutsourcingPrice(outsourcingPriceListDto.getBpOutsourcingPrice())
                            .bpOutsourcingYn(outsourcingPriceListDto.getBpOutsourcingYn())
                            .bpRemark(outsourcingPriceListDto.getBpRemark())
                            .build();
                    allOutsourcingPrice.add(outsourcingPrice);
                }
            }
        }
        List<OutsourcingPrice> outsourcingPrices = outsourcingPriceRepository.saveAll(allOutsourcingPrice);

        data.put("gridListData", outsourcingPrices);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


}
