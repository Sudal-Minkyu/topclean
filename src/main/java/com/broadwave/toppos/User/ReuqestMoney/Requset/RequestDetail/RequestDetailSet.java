package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.RequestDetailDtos.RequestDetailMapperDto;
import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDtos.RequestMapperDto;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수세부 Set
 */
@Data
public class RequestDetailSet {

    // etc 데이터
    private RequestMapperDto etc;

    // 수정 행 리스트
    private ArrayList<RequestDetailMapperDto> update;

    // 추가 행 리스트
    private ArrayList<RequestDetailMapperDto> add;

    // 삭제 행 리스트
    private ArrayList<RequestDetailMapperDto> delete;

}
