package com.broadwave.toppos.common;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 에러 응답코드
 */
public enum ResponseErrorCode {
//    E001("E001", "이미 존재하는 아이디입니다. 입력된데이터의 코드(아이디)를 확인하시기바랍니다."),
   ;
    private String code;
    private String desc;

    ResponseErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
