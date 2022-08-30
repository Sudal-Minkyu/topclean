package com.broadwave.toppos.Manager.ManagerService;

import com.broadwave.toppos.Head.Item.Group.A.ItemGroup;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.ItemGroupCodeAndNameListDto;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupRepository;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.Manager.outsourcingPrice.OutsourcingPrice;
import com.broadwave.toppos.Manager.outsourcingPrice.OutsourcingPriceRepository;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListDto;
import com.broadwave.toppos.Manager.outsourcingPrice.outsourcingPriceDtos.OutsourcingPriceListInputDto;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
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
@Transactional(rollbackFor = Exception.class)
public class OutsourcingPriceService {

    private final OutsourcingPriceRepository outsourcingPriceRepository;

    private final ItemGroupRepository itemGroupRepository;

    private final TokenProvider tokenProvider;

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
        String brCode = (String) claims.get("brCode"); // brCode 추출
        String loginId = claims.getSubject(); // 로그인 아이디 추출

        List<OutsourcingPrice> outsourcingPrices = new ArrayList<>();

        for (OutsourcingPriceListDto outsourcingPriceListDto : outsourcingPriceListDtos) {
            OutsourcingPrice findOutsourcingPrice = outsourcingPriceRepository.findByOutsourcingPriceAll(outsourcingPriceListDto.getBiItemcode(), brCode);

            if (findOutsourcingPrice != null) {
                // update
                findOutsourcingPrice.setBpOutsourcingPrice(outsourcingPriceListDto.getBpOutsourcingPrice());
                findOutsourcingPrice.setBpOutsourcingYn(outsourcingPriceListDto.getBpOutsourcingYn());
                findOutsourcingPrice.setBpRemark(outsourcingPriceListDto.getBpRemark());
                findOutsourcingPrice.setModify_id(loginId);
                findOutsourcingPrice.setModifyDate(LocalDateTime.now());
            } else {
                // insert
                OutsourcingPrice saveOutsourcingPrice = new OutsourcingPrice();
                saveOutsourcingPrice.setBiItemcode(outsourcingPriceListDto.getBiItemcode());
                saveOutsourcingPrice.setBrCode(brCode);
                saveOutsourcingPrice.setBpOutsourcingPrice(outsourcingPriceListDto.getBpOutsourcingPrice());
                saveOutsourcingPrice.setBpOutsourcingYn(outsourcingPriceListDto.getBpOutsourcingYn());
                saveOutsourcingPrice.setBpRemark(outsourcingPriceListDto.getBpRemark());
                saveOutsourcingPrice.setInsert_id(loginId);
                saveOutsourcingPrice.setModify_id(loginId);
                saveOutsourcingPrice.setInsertDate(LocalDateTime.now());
                saveOutsourcingPrice.setModifyDate(LocalDateTime.now());
                outsourcingPrices.add(saveOutsourcingPrice);
            }
        }
        outsourcingPriceRepository.saveAll(outsourcingPrices); // 한번에 저장

        data.put("gridListData", "Success");
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }


}
