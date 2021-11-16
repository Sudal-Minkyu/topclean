package com.broadwave.toppos.common;

/**
 * @author Minkyu
 * Date : 2021-10-07
 * Time :
 * Remark : 에러 응답코드
 */
public enum ResponseErrorCode {
    TP001("TP001", "이미 존재하는 아이디입니다."),
    TP002("TP002", "아이디와 비밀번호를 확인해주세요."),
    TP003("TP003", "중복되는 코드가 존재합니다."),
    TP004("TP004", "아이디를 입력해주세요."),
    TP005("TP005", "정보가 존재하지 않습니다."),
    TP006("TP006", "새로고침이후에 다시 등록해주세요."),
    TP007("TP007", "권한이 없습니다."),
    TP008("TP008", "가맹점 전용기능 입니다.")
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
