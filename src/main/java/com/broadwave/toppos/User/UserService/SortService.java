package com.broadwave.toppos.User.UserService;

import com.broadwave.toppos.Head.HeadService;
import com.broadwave.toppos.Head.Item.Group.A.UserItemGroupSortDto;
import com.broadwave.toppos.Jwt.token.TokenProvider;
import com.broadwave.toppos.User.GroupSort.*;
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

    @Autowired
    public SortService(TokenProvider tokenProvider, HeadService headService,
                       GroupSortRepository groupSortRepository, GroupSortRepositoryCustom groupSortRepositoryCustom){
        this.tokenProvider = tokenProvider;
        this.headService = headService;
        this.groupSortRepository = groupSortRepository;
        this.groupSortRepositoryCustom = groupSortRepositoryCustom;
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
        data.put("userItemGroupSortData",userItemGroupSortData);

        return ResponseEntity.ok(res.dataSendSuccess(data));
    }

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
        List<GroupSortUpdateDto> groupSortUpdateDtos = findByGroupSortList(frCode);
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

    // 현재 가맹점의 대분류상품 정렬 리스트
    public List<GroupSortUpdateDto> findByGroupSortList(String frCode) {
        return groupSortRepositoryCustom.findByGroupSortList(frCode);
    }

}
