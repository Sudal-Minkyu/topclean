package com.broadwave.toppos.common;

import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : Toppos Ajax 호출용 응답클래스
 */
public class AjaxResponse {

    Map<String, Object> res;

    public AjaxResponse(){
        this.res = new HashMap<>();

    }

    // Pages 형태의 조회 API 반환 함수
    // 리스트를 출력해야한다는 상황에 해당 함수사용
    public Map<String, Object> ResponseEntityPage(Page pages ){
        res.clear();
        res.put("status",200);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "SUCCESS");
        res.put("err_code", "");
        res.put("err_msg", "");

        if(pages.getTotalElements()> 0 ){
            res.put("datalist",pages.getContent());
            res.put("total_page",pages.getTotalPages());
            res.put("current_page",pages.getNumber() + 1);
            res.put("total_rows",pages.getTotalElements());
            res.put("current_rows",pages.getNumberOfElements());
        }else{
            res.put("total_page",pages.getTotalPages());
            res.put("current_page",pages.getNumber() + 1);
            res.put("total_rows",pages.getTotalElements());
            res.put("current_rows",pages.getNumberOfElements());
        }
        return this.res;
    }

    // 프론트에게 데이터를 보내야하면 해당 함수를 사용
    public Map<String, Object> dataSendSuccess(HashMap<String,Object> sendData) {
        res.put("sendData",sendData);
        res.put("status",200);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "SUCCESS");
        res.put("err_code", "");
        res.put("err_msg", "");
        return this.res;
    }

    // 보낼 데이터없고 호출에 성공할 경우
    public Map<String, Object> success() {
        res.put("status",200);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "SUCCESS");
        res.put("err_code", "");
        res.put("err_msg", "");
        return this.res;
    }

    // 보낼데이터가 없고 호출이 실패할시 해당함수 사용
    public Map<String, Object> fail(String err_code,String err_msg,String err_code2,String err_msg2) {
        res.clear();
        res.put("status",500);
        res.put("timestamp", new Timestamp(System.currentTimeMillis()));
        res.put("message", "Error");
        res.put("err_code", err_code);
        res.put("err_msg", err_msg);
        res.put("err_code2", err_code2);
        res.put("err_msg2", err_msg2);
        return this.res;
    }

}
