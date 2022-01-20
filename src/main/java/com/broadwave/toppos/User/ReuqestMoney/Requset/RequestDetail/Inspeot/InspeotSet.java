package com.broadwave.toppos.User.ReuqestMoney.Requset.RequestDetail.Inspeot;

import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Minkyu
 * Date : 2022-01-14
 * Time :
 * Remark : Toppos 가맹점 검품 Set
 */
@Setter
public class InspeotSet {

    // 수정 행 리스트
    private ArrayList<InspeotDto> list;

    public ArrayList<InspeotDto> getList() {
        return list;
    }
}