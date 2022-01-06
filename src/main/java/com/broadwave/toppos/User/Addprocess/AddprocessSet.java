package com.broadwave.toppos.User.Addprocess;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2022-01-03
 * Time :
 * Remark : 상용구&수선항목&추가항목 AddprocessSet
 */
@Setter
public class AddprocessSet {

    private String baType; // 1: 수선항목, 2:추가요금항목, 3:상용구항목
    private ArrayList<AddprocessMapperDto> list;   // 행 리스트

    public String getBaType() {
        return baType;
    }

    public ArrayList<AddprocessMapperDto> getList() {
        return list;
    }
}
