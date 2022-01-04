package com.broadwave.toppos.User.Addprocess;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2022-01-03
 * Time :
 * Remark : 상용구 추가&수정&삭제 AddprocessSet
 */
@Setter
public class AddprocessSet {

    // 추가 행 리스트
    private ArrayList<AddprocessMapperDto> add;

    // 삭제 행 리스트
    private ArrayList<AddprocessMapperDto> update;

    // 삭제 행 리스트
    private ArrayList<AddprocessMapperDto> delete;

    public ArrayList<AddprocessMapperDto> getUpdate() {
        return update;
    }

    public ArrayList<AddprocessMapperDto> getAdd() {
        return add;
    }

    public ArrayList<AddprocessMapperDto> getDelete() {
        return delete;
    }
}
