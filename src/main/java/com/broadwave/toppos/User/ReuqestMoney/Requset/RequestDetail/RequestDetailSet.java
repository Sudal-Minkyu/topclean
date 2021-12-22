package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail;

import com.broadwave.toppos.User.ReuqestMoney.Requset.RequestMapperDto;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2021-12-13
 * Time :
 * Remark : Toppos 가맹점 접수세부 Set
 */
@Setter
public class RequestDetailSet {
    // etc 데이터
    private RequestMapperDto etc;

    // 수정 행 리스트
    private ArrayList<RequestDetailDto> update;

    // 추가 행 리스트
    private ArrayList<RequestDetailDto> add;

    // 삭제 행 리스트
    private ArrayList<RequestDetailDto> delete;

    public RequestMapperDto getEtc() {
        return etc;
    }

    public ArrayList<RequestDetailDto> getUpdate() {
        return update;
    }

    public ArrayList<RequestDetailDto> getAdd() {
        return add;
    }

    public ArrayList<RequestDetailDto> getDelete() {
        return delete;
    }

}
