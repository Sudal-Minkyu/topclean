package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Head.HeadService.HeadService;
import com.broadwave.toppos.Head.Item.Group.A.ItemGroupDtos.UserItemGroupSortDto;
import com.broadwave.toppos.Head.Item.Group.B.ItemGroupSDtos.ItemGroupSUserListDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.GroupSort.*;
import com.broadwave.toppos.User.GroupSort.GroupSortDtos.GroupSortUpdateDto;
import com.broadwave.toppos.User.ItemSort.*;
import com.broadwave.toppos.User.ItemSort.ItemSortDtos.ItemSortListDto;
import com.broadwave.toppos.User.ItemSort.ItemSortDtos.ItemSortUpdateDto;
import com.broadwave.toppos.common.AjaxResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-12-28
 * Time :
 * Remark : Toppos 가맹점 상품정렬 서비스
 */
@Slf4j
@Service
public class SortService {

    private final HeadService headService;
    private final TokenProvider tokenProvider;

    private final GroupSortRepository groupSortRepository;
    private final GroupSortRepositoryCustom groupSortRepositoryCustom;

    private final ItemSortRepository itemSortRepository;
    private final ItemSortRepositoryCustom itemSortRepositoryCustom;

    @Autowired
    public SortService(TokenProvider tokenProvider, HeadService headService,
                       GroupSortRepository groupSortRepository, GroupSortRepositoryCustom groupSortRepositoryCustom,
                       ItemSortRepository itemSortRepository, ItemSortRepositoryCustom itemSortRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.headService = headService;
        this.groupSortRepository = groupSortRepository;
        this.groupSortRepositoryCustom = groupSortRepositoryCustom;
        this.itemSortRepository = itemSortRepository;
        this.itemSortRepositoryCustom = itemSortRepositoryCustom;
    }

    // 현재 가맹점의 대분류상품 정렬 리스트
    public ResponseEntity<Map<String,Object>> findByGroupSort(HttpServletRequest request) {
        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 현재 가맹점의 대분류 리스트 가져오기 + 가맹점이 등록한 대분류 순서 테이블 leftjoin
        List<UserItemGroupSortDto> userItemGroupSortData = headService.findByUserItemGroupSortDtoList(frCode);
        data.put("gridListData",userItemGroupSortData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 현재 가맹점의 대분류 순서 업데이트
    @Transactional
    public ResponseEntity<Map<String, Object>> findByGroupSortUpdate(GroupSortSet groupSortSet, HttpServletRequest request) {
        log.info("franchiseItemGroupUpdate 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

//        log.info("groupSortSet.getList() : "+groupSortSet.getList());

        AjaxResponse res = new AjaxResponse();

        List<String> bgItemGroupcodeList = new ArrayList<>();
        // 현재 가맹점의 대분류상품 GroupSortUpdateDto 호출
        List<GroupSortUpdateDto> groupSortUpdateDtos = groupSortRepositoryCustom.findByGroupSortList(frCode);
        for(GroupSortUpdateDto groupSortUpdateDto : groupSortUpdateDtos){
            bgItemGroupcodeList.add(groupSortUpdateDto.getBgItemGroupcode());
        }

        List<GroupSort> groupSortList = new ArrayList<>();
//        log.info("bgItemGroupcodeList : "+bgItemGroupcodeList);
        GroupSort groupSort;
        for(int i=0; i<groupSortSet.getList().size(); i++){
            groupSort = new GroupSort();
            groupSort.setFrCode(frCode);
            groupSort.setBgItemGroupcode(groupSortSet.getList().get(i).getBgItemGroupcode());
            groupSort.setBgFavoriteYn(groupSortSet.getList().get(i).getBgFavoriteYn());
            groupSort.setBgSort(groupSortSet.getList().get(i).getBgSort());

            if(bgItemGroupcodeList.contains(groupSortSet.getList().get(i).getBgItemGroupcode())){
                groupSort.setInsert_id(groupSortUpdateDtos.get(0).getInsert_id());
                groupSort.setInsertDateTime(groupSortUpdateDtos.get(0).getInsertDateTime());
                groupSort.setModify_id(login_id);
                groupSort.setModifyDateTime(LocalDateTime.now());
            }else{
                groupSort.setInsert_id(login_id);
                groupSort.setInsertDateTime(LocalDateTime.now());
            }

            groupSortList.add(groupSort);
        }

//        log.info("groupSortList : "+groupSortList);
        groupSortRepository.saveAll(groupSortList);

        return ResponseEntity.ok(res.success());
    }

    // 현재 가맹점의 중분류 리스트 가져오기
    public ResponseEntity<Map<String, Object>> franchiseItemSortList(String filterCode, String filterName) {
        log.info("franchiseItemSortList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        List<ItemGroupSUserListDto> itemGroupSListData = headService.findByItemGroupSUserList(filterCode, filterName);
        data.put("gridListData",itemGroupSListData);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 현재 가맹점의 상품순서 리스트 가져오기
    public ResponseEntity<Map<String, Object>> franchiseItemList(String biItemMgroup, HttpServletRequest request, String nowDate) {
        log.info("franchiseItemList 호출");

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("frCode : "+frCode);
//        log.info("biItemMgroup : "+biItemMgroup);
//        log.info("nowDate : "+nowDate);
        List<ItemSortListDto> itemSortListDtos = itemSortRepositoryCustom.itemSortList(frCode, biItemMgroup, nowDate);
        data.put("gridListData",itemSortListDtos);
        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

    // 현재 가맹점의 상품순서 업데이트
    @Transactional
    public ResponseEntity<Map<String, Object>> findByItemSortUpdate(ItemSortSet itemSortSet, HttpServletRequest request) {
        log.info("findByItemSortUpdate 호출");

        AjaxResponse res = new AjaxResponse();

        // 클레임데이터 가져오기
        Claims claims = tokenProvider.parseClaims(request.getHeader("Authorization"));
        String frCode = (String) claims.get("frCode"); // 현재 가맹점의 코드(3자리) 가져오기
        String login_id = claims.getSubject(); // 현재 아이디
        log.info("현재 접속한 아이디 : "+login_id);
        log.info("현재 접속한 가맹점 코드 : "+frCode);

        List<String> biItemcodeList = new ArrayList<>();
        // 현재 가맹점의 대분류상품 GroupSortUpdateDto 호출
        String biItemMgroup = itemSortSet.getList().get(0).getBiItemcode().substring(0,4);
        List<ItemSortUpdateDto> itemSortUpdateDtos = itemSortRepositoryCustom.findByItemSortList(biItemMgroup,frCode);
        for(ItemSortUpdateDto itemSortUpdateDto : itemSortUpdateDtos){
            biItemcodeList.add(itemSortUpdateDto.getBiItemcode());
        }

//        log.info("biItemcodeList : "+biItemcodeList);
        List<ItemSort> itemSortList = new ArrayList<>();
        ItemSort itemSort;
        for(int i=0; i<itemSortSet.getList().size(); i++){
            itemSort = new ItemSort();
            itemSort.setFrCode(frCode);
            itemSort.setBiItemcode(itemSortSet.getList().get(i).getBiItemcode());
            itemSort.setBfSort(itemSortSet.getList().get(i).getBfSort());
            itemSort.setBiItemMgroup(itemSortSet.getList().get(i).getBiItemcode().substring(0,4));

            if(biItemcodeList.contains(itemSortSet.getList().get(i).getBiItemcode())){
                itemSort.setInsert_id(itemSortUpdateDtos.get(0).getInsert_id());
                itemSort.setInsertDateTime(itemSortUpdateDtos.get(0).getInsertDateTime());
                itemSort.setModify_id(login_id);
                itemSort.setModifyDateTime(LocalDateTime.now());
            }else{
                itemSort.setInsert_id(login_id);
                itemSort.setInsertDateTime(LocalDateTime.now());
            }

            itemSortList.add(itemSort);
        }

//        log.info("itemSortList : "+itemSortList);
        itemSortRepository.saveAll(itemSortList);

        return ResponseEntity.ok(res.success());
    }


}
